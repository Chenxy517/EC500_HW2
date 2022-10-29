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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private TextView ms_msg;
    private String location_message;
    private Button changeSize, help_btn, pause_btn;
    private EditText fontSize;

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
        ms_msg = (TextView) findViewById(R.id.ms_msg);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationUpdate();

        // Change font size
        changeSize = (Button) findViewById(R.id.changeSize);
        fontSize = (EditText) findViewById(R.id.fontSize);
        changeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                setFontSize(ms_msg, Float.parseFloat(fontSize.getText().toString()));
            }
        });

        // pop out help information
        help_btn = (Button) findViewById(R.id.help);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View popupView = MainActivity.this.getLayoutInflater().inflate(R.layout.popupwindow, null);

                TextView helpText = (TextView) popupView.findViewById(R.id.helpText);

                PopupWindow window = new PopupWindow(popupView, 400, 600);
                window.setAnimationStyle(R.style.popup_window_anim);
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                window.setFocusable(true);
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(help_btn, 0, 20);
            }
        });

        // pause display(TODO: function incomplete)
        pause_btn = (Button) findViewById(R.id.pause);
        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
            for(int i=0; i<vChildCount; i++)
            {
                View v1 = ((ViewGroup) v).getChildAt(i);
                setFontSize(v1, fontSizeValue);
            }
        }
    }

}