package ec500.hw2.p0;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private TextView txtLocation, txtSpeed;
    private String strLocation, strSpeed;
    private Button btnChangeSize, btnHelp, btnPause, btnUnit, btnTest;
    private EditText edtFontSize;
    private boolean isTest = false;
    private boolean isMeterPerSecond = true;
    private double valCurrentSpeed = 0.0;
    private boolean isPause = false;
    private double valLatitude = 0.0;
    private double valLongitude = 0.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationUpdate();

        // Change font size
        btnChangeSize = (Button) findViewById(R.id.btnChangeSize);
        edtFontSize = (EditText) findViewById(R.id.edtFontSize);
        btnChangeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(edtFontSize.getText().toString().matches("[0-9]+")) {
                    setFontSize(txtLocation, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeed, Float.parseFloat(edtFontSize.getText().toString()));
                }
            }
        });

        // help information page
        helpful_click();

        // test app under synthetic source
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isTest = !isTest;
            }
        });

        // pause display
        btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPause){
                    onPause();
                    Toast.makeText(MainActivity.this, "Pause the location updates", Toast.LENGTH_SHORT).show();
                    isPause = true;
                }
                else{
                    onResume();
                    Toast.makeText(MainActivity.this, "Resume the location updates", Toast.LENGTH_SHORT).show();
                    isPause = false;
                }
            }
        });

        // change speed unit
        btnUnit = (Button) findViewById(R.id.btnUnit);
        btnUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isMeterPerSecond = !isMeterPerSecond;
            }
        });

        makeToast("Welcome to the My - GPS! ");
    }

    // handle message
    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            if ( msg.what == 0x001 ) {
                if (valCurrentSpeed < 10.0) {
                    txtSpeed.setTextColor(Color.BLACK);
                }
                else if (valCurrentSpeed < 20.0){
                    txtSpeed.setTextColor(Color.GREEN);
                }
                else if (valCurrentSpeed < 30.0){
                    txtSpeed.setTextColor(Color.BLUE);
                }
                else if (valCurrentSpeed < 50.0){
                    txtSpeed.setTextColor(Color.CYAN);
                }
                else{
                    txtSpeed.setTextColor(Color.RED);
                }
                txtSpeed.setText(strSpeed);
                txtLocation.setText(strLocation);
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

            updateShow(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateShow(null);
        }
    };

    public void onResume() {
        super.onResume();
        locationUpdate();
    }

    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(mLocationListener);
    }

    public String unit_transfer(double speed){
        speed = 3.6 * speed;
        if (isMeterPerSecond){
            return "Speed: " + speed + "kph \n";
        }
        else{
            speed = 0.621371192 * speed;
            return "Speed: " + speed + "mph \n";
        }
    }

    private void updateShow(Location location) {
        if(!isTest) {
            if (location != null) {
                StringBuilder sb_loc = new StringBuilder();
                StringBuilder sb_speed = new StringBuilder();
                valCurrentSpeed = 3.6 * location.getSpeed();
                sb_loc.append("Longitude: " + location.getLongitude() + "\n");
                sb_loc.append("Latitude: " + location.getLatitude() + "\n");
                sb_speed.append(unit_transfer(location.getSpeed()));

                strLocation = sb_loc.toString();
                strSpeed = sb_speed.toString();
            } else {
                strLocation = "";
                strSpeed = "";
            }
        }
        else{
            StringBuilder sb_loc = new StringBuilder();
            strSpeed = distance().toString();
            sb_loc.append("Longitude: " + valLatitude + "\n");
            sb_loc.append("Latitude: " + valLongitude + "\n");
            strLocation = sb_loc.toString();
        }

        handler.sendEmptyMessage(0x001);
    }

    private StringBuilder distance() {
        double r_earth = 6378.0;
        double dy = 0;
        double dx = 16.098 / 36000;
        double new_latitude = valLatitude + (dy / r_earth) * (180 / Math.PI);
        double new_longitude = valLongitude + (dx / r_earth) * (180 / Math.PI) / Math.cos(valLatitude * Math.PI/180);
        valCurrentSpeed = 16.098 / 3.6;
        double speed = valCurrentSpeed;
        StringBuilder sb = new StringBuilder();
//        sb.append("Longitude: " + new_longitude + "\n");
//        sb.append("Latitude: " + new_latitude + "\n");
        sb.append(unit_transfer(speed));
        valLatitude = new_latitude;
        valLongitude = new_longitude;
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

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        updateShow(location);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
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
        btnHelp = (Button) findViewById(R.id.btnHelp);

        btnHelp.setOnClickListener(new View.OnClickListener() {
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
