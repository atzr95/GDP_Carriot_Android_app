package gdp.gdpv1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener
{
    //declare variables
    List<Data> listData;
    String apikey;

    @BindView(R.id.toolbar_header_view)
    protected HeaderView toolbarHeaderView;

    @BindView(R.id.float_header_view)
    protected HeaderView floatHeaderView;

    @BindView(R.id.appbar)
    protected AppBarLayout appBarLayout;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.scrollableView)
    protected RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isHideToolbarView = false;

    DatabaseHelper myDb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        apikey = "2237b4e3033138120baa05d325c62a93188a1c674220dc5cbc7e28385a8fedb1";
        new getStreamsTask().execute();
        initUi();
        initializeData();
        Adapter adapter = new Adapter(listData);
        recyclerView.setAdapter(adapter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.dark_grey);

        //pull down refresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isNetworkAvailable()== true)
                {
                    new getStreamsTask().execute();
                }
                if(isNetworkAvailable()==false){
                    Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {     //check internet
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeData() {      //create list for data
        listData = new ArrayList<Data>();
        Cursor cursor = myDb.getData();
        if (cursor.getCount() == 0) {
            listData.add(new Data("Temperature", "NA"));
            listData.add(new Data("Humidity", "NA"));
            listData.add(new Data("Soil Moisture", "NA"));
            listData.add(new Data("Light Intensity", "NA"));
            listData.add(new Data("Wind Speed", "NA"));
            listData.add(new Data("Water Voltage", "NA"));
        } else {
            cursor.moveToPosition(cursor.getCount() - 1);
            listData.add(new Data("Temperature", cursor.getString(2) + "°C"));
            listData.add(new Data("Humidity", cursor.getString(3) + "%"));
            listData.add(new Data("Soil Moisture", cursor.getString(4) + "mV"));
            listData.add(new Data("Light Intensity", cursor.getString(5)));
            listData.add(new Data("Wind Speed", cursor.getString(6)+"km/h"));
            listData.add(new Data("Water Level", cursor.getString(7) + "mV"));
        }
    }

    private void initUi() {         //startUI
        appBarLayout.addOnOffsetChangedListener(this);
        Cursor cursor = myDb.getData();
        if (cursor.getCount() == 0) {
            toolbarHeaderView.bindTo("Plant Environment Control System", "Last updated at " + "NA");
            floatHeaderView.bindTo("Plant Environment Control System", "Last updated at " + "NA");
        } else {
            cursor.moveToPosition(cursor.getCount() - 1);
            toolbarHeaderView.bindTo("Plant Environment Control System", "Last updated at " + cursor.getString(0)+" "+
                    cursor.getString(1));
            floatHeaderView.bindTo("Plant Environment Control System", "Last updated at " + cursor.getString(0)+" "+
                    cursor.getString(1));
        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        if (percentage == 1f && isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            toolbarHeaderView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    //set data from SQLite database to the tiles
    private class getStreamsTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            return getData();
        }

        protected void onPostExecute(String result) {
            try {
                Cursor cursor = myDb.getData();
                cursor.moveToPosition(cursor.getCount() - 1);
                Data temperature = new Data("Temperature", cursor.getString(2) + "°C");
                Data humidity = new Data("Humidity", cursor.getString(3) + "%");
                Data soil = new Data("Soil Moisture", cursor.getString(4) + "mV");
                Data light = new Data("Light Intensity", cursor.getString(5));
                Data wind_speed = new Data("Wind Speed", cursor.getString(6)+"km/h");
                Data water = new Data("Water Level", cursor.getString(7) + "mV");
                listData.set(0, temperature);
                listData.set(1, humidity);
                listData.set(2, soil);
                listData.set(3, light);
                listData.set(4, wind_speed);
                listData.set(5, water);
                toolbarHeaderView.bindTo("Plant Environment Control System", "Last updated at " + cursor.getString(0)+" "+
                        cursor.getString(1));
                floatHeaderView.bindTo("Plant Environment Control System", "Last updated at " + cursor.getString(0)+" "+
                        cursor.getString(1));
            Adapter adapter = new Adapter(listData);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
        }

        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    protected String getData() {     //acquire data from Carriots
            String decodedString = "";
            String returnMsg = "";
            String request = "https://api.carriots.com/streams/?_t=str&device=defaultDevice@GroupDP.GroupDP&owner=GroupDP";
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(request);
                connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("carriots.apikey", apikey);
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((decodedString = in.readLine()) != null) {
                    returnMsg += decodedString;
                }
                in.close();
                JSONObject json = new JSONObject(returnMsg);
                JSONArray results = json.getJSONArray("result");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject d = results.getJSONObject(i);
                    Long date = d.getLong("at");
                    Date t = new Date(date * 1000L);
                    SimpleDateFormat formattertime = new SimpleDateFormat("hh:mm a");
                    SimpleDateFormat formatterdate = new SimpleDateFormat("dd-MMM-yy");
                    String time = formattertime.format(t);
                    String day=formatterdate.format(t);

                    JSONObject data = d.getJSONObject("data");
                    String humdata = data.getString("hum");
                    String tempdata = data.getString("temp");
                    String soildata = data.getString("soil");
                    String lightdata = data.getString("light");
                    String winddata = data.getString("wind");
                    String waterdata = data.getString("water");
                    myDb.addData(day,time, tempdata, humdata, soildata, lightdata, winddata, waterdata);

                }
                return returnMsg;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        return returnMsg;
    }
    }
}

