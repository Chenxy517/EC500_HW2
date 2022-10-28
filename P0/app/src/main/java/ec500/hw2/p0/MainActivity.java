package ec500.hw2.p0;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private TextView ms_msg;
    private String location_message;

    private Handler handler = new Handler(new Handler.Callback(){

        @Override
        public boolean handleMessage(Message msg) {
            if ( msg.what == 0x001 ) {
                ms_msg.setText(location_message);
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
        public void onStatusChanged(String provider, int status, Bundle extras) {

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

            // 当GPS LocationProvider可用时，更新定位
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
        ms_msg = (TextView) findViewById(R.id.ms_msg);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationUpdate();
    }

    public void onResume() {
        super.onResume();
        locationUpdate();
    }

    public void onPause() {
        super.onPause();
        lm.removeUpdates(mLocationListener);
    }

    private void updateShow(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Location: \n");
            sb.append("Longitude: " + location.getLongitude() + "\n");
            sb.append("Latitude: " + location.getLatitude() + "\n");
            sb.append("Speed: " + location.getSpeed() + "\n");


            location_message = sb.toString();
        } else location_message = "";

        handler.sendEmptyMessage(0x001);
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


        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
    }

}