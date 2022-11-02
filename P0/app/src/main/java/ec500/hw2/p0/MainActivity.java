package ec500.hw2.p0;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private TextView loc_msg, speed_msg;
    private String loc_message, speed_message;
    private Button changeSize, help_btn, pause_btn, unit_btn, test_btn;
    private EditText fontSize;
    private boolean is_test = false;
    private boolean is_meter_per_second = true;
    private double cur_speed = 0.0;
    private boolean pauseStatus = false;
    private double latitude = 0.0;
    private double longitude = 0.0;

    // handle message
    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            if ( msg.what == 0x001 ) {
                if (cur_speed < 10.0) {
                    speed_msg.setTextColor(Color.BLACK);
                }
                else if (cur_speed < 20.0){
                    speed_msg.setTextColor(Color.GREEN);
                }
                else if (cur_speed < 30.0){
                    speed_msg.setTextColor(Color.BLUE);
                }
                else if (cur_speed < 50.0){
                    speed_msg.setTextColor(Color.CYAN);
                }
                else{
                    speed_msg.setTextColor(Color.RED);
                }
                speed_msg.setText(speed_message);
                loc_msg.setText(loc_message);
            }

            return false;
        }
    });

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // update message when location changes
            updateShow(location);
        }

        @Override
        public void onProviderEnabled(String provider) {

            // check GPS authority
            if ( checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, "Turn on GPS", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 0);
                return;
            }

            updateShow(lm.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateShow(null);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loc_msg = (TextView) findViewById(R.id.loc_msg);
        speed_msg = (TextView) findViewById(R.id.speed_msg);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationUpdate();

        // Change font size
        changeSize = (Button) findViewById(R.id.changeSize);
        fontSize = (EditText) findViewById(R.id.fontSize);
        changeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(fontSize.getText().toString().matches("[0-9]+")) {
                    setFontSize(loc_msg, Float.parseFloat(fontSize.getText().toString()));
                    setFontSize(speed_msg, Float.parseFloat(fontSize.getText().toString()));
                }
            }
        });

        // help information page
        helpful_click();

        // test app under synthetic source
        test_btn = (Button) findViewById(R.id.btnTest);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                is_test = !is_test;
            }
        });

        // pause display
        pause_btn = (Button) findViewById(R.id.pause);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pauseStatus){
                    onPause();
                    Toast.makeText(MainActivity.this, "Pause the location updates", Toast.LENGTH_SHORT).show();
                    pauseStatus = true;
                }
                else{
                    onResume();
                    Toast.makeText(MainActivity.this, "Resume the location updates", Toast.LENGTH_SHORT).show();
                    pauseStatus = false;
                }
            }
        });

        // change speed unit
        unit_btn = (Button) findViewById(R.id.unit);
        unit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                is_meter_per_second = !is_meter_per_second;
            }
        });

        makeToast("Welcome to the My - GPS! ");
    }



    public void onResume() {
        super.onResume();
        locationUpdate();
    }

    public void onPause() {
        super.onPause();
        lm.removeUpdates(mLocationListener);
    }



    public String unit_transfer(double speed){
        speed = 3.6 * speed;
        if (is_meter_per_second){
            return "Speed: " + speed + "kph \n";
        }
        else{
            speed = 0.621371192 * speed;
            return "Speed: " + speed + "mph \n";
        }
    }

    private void updateShow(Location location) {
        if(!is_test) {
            if (location != null) {
                StringBuilder sb_loc = new StringBuilder();
                StringBuilder sb_speed = new StringBuilder();
                cur_speed = 3.6 * location.getSpeed();
                sb_loc.append("Longitude: " + location.getLongitude() + "\n");
                sb_loc.append("Latitude: " + location.getLatitude() + "\n");
                sb_speed.append(unit_transfer(location.getSpeed()));

                loc_message = sb_loc.toString();
                speed_message = sb_speed.toString();
            } else {
                loc_message = "";
                speed_message = "";
            }
        }
        else{
            StringBuilder sb_loc = new StringBuilder();
            speed_message = distance().toString();
            sb_loc.append("Longitude: " + latitude + "\n");
            sb_loc.append("Latitude: " + longitude + "\n");
            loc_message = sb_loc.toString();
        }

        handler.sendEmptyMessage(0x001);
    }

    private StringBuilder distance() {
        double r_earth = 6378.0;
        double dy = 0;
        double dx = 16.098 / 36000;
        double new_latitude = latitude  + (dy / r_earth) * (180 / Math.PI);
        double new_longitude = longitude + (dx / r_earth) * (180 / Math.PI) / Math.cos(latitude * Math.PI/180);
        cur_speed = 16.098 / 3.6;
        double speed = cur_speed;
        StringBuilder sb = new StringBuilder();
//        sb.append("Longitude: " + new_longitude + "\n");
//        sb.append("Latitude: " + new_latitude + "\n");
        sb.append(unit_transfer(speed));
        latitude = new_latitude;
        longitude = new_longitude;
        return sb;
    }

    public void locationUpdate() {

        // check GPS authority
        if ( checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this, "Turn on GPS", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        updateShow(location);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
    }


    public void setFontSize(View v, float fontSizeValue) {
        if(v instanceof TextView)
        {
            ((TextView) v).setTextSize(fontSizeValue);
        }
        else if(v instanceof EditText)
        {
            ((EditText) v).setTextSize(fontSizeValue);
        }
        else if(v instanceof Button)
        {
            ((Button) v).setTextSize(fontSizeValue);
        }
        else
        {
            int vChildCount = ((ViewGroup) v).getChildCount();
            for(int i = 0; i < vChildCount; i++)
            {
                View v1 = ((ViewGroup) v).getChildAt(i);
                setFontSize(v1, fontSizeValue);
            }
        }
    }

    public void helpful_click() {
        // help Button.
        help_btn = (Button) findViewById(R.id.help);

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent();
                // Jump to Help Activity:
                intent.setClass(getApplicationContext(), HelpActivity.class);
                startActivity(intent);

            }
        });

    }

    /**
     * Show a Toast of the given string
     *
     * @param str The string to show in the Toast
     */
    public void makeToast(String str) {
        runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_LONG).show());
    }

}
