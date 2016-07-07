package gdp.gdpv1;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class WaterGraph extends AppCompatActivity{
    DatabaseHelper myDb;
    LineChart lineChart;
    Button button;
    TextView displaydate;
    String dateday;

    Calendar c= Calendar.getInstance();

    int cdate,cmonth,cyear;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.water_graph);
        myDb = new DatabaseHelper(this);

        lineChart = (LineChart) findViewById(R.id.chart);

        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDescription(" ");
        lineChart.setGridBackgroundColor(Color.WHITE);

        button = (Button) findViewById(R.id.button);
        displaydate=(TextView)findViewById(R.id.textView2);
        CustomMarkerView mv = new CustomMarkerView (this ,R.layout.custom_marker_view_layout);
        lineChart.setMarkerView(mv);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
        String formattedDate = df.format(c.getTime());
        displaydate.setText(formattedDate);
        myDb.setString(formattedDate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(WaterGraph.this,d1,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        if(myDb.WaterData().size()!=0) {
            addData();
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
    }

    DatePickerDialog.OnDateSetListener d1=new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cdate=dayOfMonth;
            cmonth=monthOfYear+1;
            cyear=year-2000;
            if(cdate < 10){

                dateday  = "0" + cdate ;
            }
            else{
                dateday= String.valueOf(cdate);
            }
            String sdf = new DateFormatSymbols().getShortMonths()[monthOfYear];
            String day = dateday + "-" + sdf + "-" + cyear;
            displaydate.setText(day);
            myDb.setString(day);
            if(myDb.WaterData().size()!=0) {
                addData();
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }
            if(myDb.WaterData().size()==0){
                lineChart.notifyDataSetChanged();
                lineChart.clear();
            }
        }
    };


    private void addData() {
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < myDb.WaterData().size(); i++) {
            yVals.add(new Entry(myDb.WaterData().get(i), i));
        }
        LineDataSet dataset = new LineDataSet(yVals, "Water");
        ArrayList<String> xVals = new ArrayList<String>();
        for (int j = 0; j < myDb.queryXData().size(); j++) {
            xVals.add(myDb.queryXData().get(j));
        }
        LineData data = new LineData(xVals, dataset);
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);
        data.setDrawValues(false);
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
        lineChart.animateXY(3000, 3000);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false); // no axis labels
        yAxis.setTextSize(13f); // set the textsize
        yAxis.setAxisMaxValue(1024f); // the axis maximum is 100
        yAxis.setAxisMinValue(0f);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setLabelCount(10, true);


        Legend l = lineChart.getLegend();
        l.setEnabled(false);
        lineChart.setVisibleXRangeMaximum(20);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}