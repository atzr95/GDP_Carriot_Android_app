package gdp.gdpv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pubnub.api.*;

public class SettingsActivity extends AppCompatActivity{

    Button on;
    Button off;
    Pubnub pubnub;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);
        pubnub = new Pubnub("pub-c-439acacb-bf51-4baf-934a-efb42f18151a","sub-c-19e82eb8-2ae1-11e6-bfbc-02ee2ddab7fe");

        on.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Callback callback = new Callback() {
                                          public void successCallback(String channel, Object response) {
                                              System.out.println(response.toString());
                                          }
                                          public void errorCallback(String channel, PubnubError error) {
                                              System.out.println(error.toString());
                                          }
                                      };
                                      pubnub.publish("my_channel", "ON" , callback);
                                  }
                              });

        off.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Callback callback = new Callback() {
                                           public void successCallback(String channel, Object response) {
                                               System.out.println(response.toString());
                                           }
                                           public void errorCallback(String channel, PubnubError error) {
                                               System.out.println(error.toString());
                                           }
                                       };
                                       pubnub.publish("my_channel", "OFF" , callback);
                                   }
                               });
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