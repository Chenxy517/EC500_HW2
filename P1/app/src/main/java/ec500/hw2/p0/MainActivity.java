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
import androidx.room.Room;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ViewFlipper;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.math.BigDecimal;
import java.math.MathContext;

import ec500.hw2.p0.database.GPSDatabase;
import ec500.hw2.p0.model.Loc;

public class MainActivity extends AppCompatActivity {

    private ViewFlipper flipper;
    private LocationManager locationManager;
    private TextView txtLocation, txtSpeed;

//    private String strLocation, strSpeed;
    private Button btnChangeSize, btnHelp, btnPause, btnTest, btnReset, btn_previous, btn_next;
    private static Spinner DistanceUnit;
    private static Spinner TimeUnit;
    private static Spinner SpeedUnit;
    private EditText edtFontSize;
    private boolean isTest = false;
    private boolean isReset = false;
    private boolean isMeterPerSecond = true;
    private double valCurrentSpeed = 0.0;
    private double Sim_valCurrentSpeed = 0.0;
    private double valCurrentTime = 0.0;
    private double Sim_valCurrentTime = 0.0;
    private double valCurrentDistance = 0.0;
    private double Sim_valCurrentDistance = 0.0;
    private boolean isPause = false;
    private double valLatitude = 0.0;
    private double preLatitude = 0.0;
    private double valLongitude = 0.0;
    private double preLongitude = 0.0;
    private double valCurrentMaxSpeed = 0.0;
    private double valCurrentMinSpeed = Double.MAX_VALUE;
    private static int Unit_distance, Unit_Time, Unit_Speed;
    private int count_Speed_OnItemSelectedListener = 0;
    private int count_Distance_OnItemSelectedListener = 0;
    private int count_Time_OnItemSelectedListener = 0;
    private long startTime = System.nanoTime();
    private long runningTime = startTime;
    private long curTime = 0;
    private long preTime = startTime;

    // Use for High Score View only:
    private double valVarianceSpeed = 0.0;
    private double valGlobalMinSpeed = Double.MAX_VALUE;
    private double valGlobalMaxSpeed = 0.0;
    private double valHighestAltitude = 0.0;
    private double valVarianceAltitude = 0.0;
    private double valLongestDistance = 0.0;
    private double valLongestTime = 0.0;

    // Textview in Main view.
    private TextView txtLongitudeValue, txtLongitude;
    private TextView txtLatitudeValue, txtLatitude;
    private TextView txtAltitudeValue, txtAltitude;
    private TextView txtDistanceValue, txtDistance, txtDistanceUnit;
    private TextView txtTimeValue, txtTimeShow, txtTimeUnit, txtRunningTime;
    private TextView txtSpeedValue, txtSpeedShow, txtSpeedUnit;
    private TextView txtSpeedMinValue, txtSpeedMinShow, txtSpeedMinUnit;
    private TextView txtSpeedMaxValue, txtSpeedMaxShow, txtSpeedMaxUnit;

    // Textview in High_Score view.
    private TextView titleHC_speedData, titleHC_Max, titleHC_Min, titleHC_Altitude, titleHC_Distance, titleHC_Time;
    private TextView txtHC_speedData, txtHC_variationSpeed, txtHC_maxSpeed, txtHC_minSpeed;
    private TextView txtHC_Altitude, txtHC_variationAltitude, txtHC_Distance, txtHC_Time;

    // For value shown in Textview() in Main Layout
    private String strLongitude, strLatitude, strAltitude, strSpeedMax, strSpeedMin, strSpeedUnit;
    private String strTimeValue, strTimeUnit;
    private String strRunningTime;
    private String strDistanceValue, strDistanceUnit;
    private String strSpeedValue;

    // For value shown in Textview() in High Score Layout:
    private String strHighestSpeed, strVariantSpeed, strGlobalSpeedMax, strGlobalSpeedMin;
    private String strHighestAltitude, strVariantAltitude, strLongestDistance, strLongestTime;

    // Define the Significant Filter:
    private static final int FRACTION_CONSTRAINT = 3;
    private static final int GPS_CONSTRAINT = 4;

    // A database to store all the temporary variables that used to record the Value of matrices. (Could be able to "RESET")
    private static GPSDatabase database;
    // A database to store all the global variables that used to record the Highest Value of matrices.
    private static GPSDatabase globalDatabase;


    public void Initialize() {
        // Set Value for the TextView in Main:
        // Longitude
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtLongitudeValue = (TextView) findViewById(R.id.txtLongitudeValue);

        // Latitude
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLatitudeValue = (TextView) findViewById(R.id.txtLatitudeValue);

        // Altitude
        txtAltitude = (TextView) findViewById(R.id.txtAltitude);
        txtAltitudeValue = (TextView) findViewById(R.id.txtAltitudeValue);

        // Distance
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDistanceValue = (TextView) findViewById(R.id.txtDistanceValue);
        txtDistanceUnit = (TextView) findViewById(R.id.txtDistanceUnitShow);

        // Time
        txtTimeShow = (TextView) findViewById(R.id.txtTime);
        txtTimeValue = (TextView) findViewById(R.id.txtTimeValue);
        txtTimeUnit = (TextView) findViewById(R.id.txtTimeUnitShow);

        // Program RunningTime:
        txtRunningTime = (TextView) findViewById(R.id.txtRunningTime);

        // Speed
        txtSpeedShow = (TextView) findViewById(R.id.txtSpeedShow);
        txtSpeedValue = (TextView) findViewById(R.id.txtSpeedValue);
        txtSpeedUnit = (TextView) findViewById(R.id.txtSpeedUnitShow);

        // Min Speed
        txtSpeedMinShow = (TextView) findViewById(R.id.txtSpeedMin);
        txtSpeedMinValue = (TextView) findViewById(R.id.txtSpeedMinValue);
        txtSpeedMinUnit = (TextView) findViewById(R.id.txtSpeedMinUnitShow);

        // Max Speed
        txtSpeedMaxShow = (TextView) findViewById(R.id.txtSpeedMax);
        txtSpeedMaxValue = (TextView) findViewById(R.id.txtSpeedMaxValue);
        txtSpeedMaxUnit = (TextView) findViewById(R.id.txtSpeedMaxUnitShow);

        /**
         * --------  High_Score View Here: ---------------
         */

        // Title:
        titleHC_speedData = (TextView) findViewById(R.id.titleHC_speedData);
        titleHC_Max = (TextView) findViewById(R.id.titleHC_Max);
        titleHC_Min = (TextView) findViewById(R.id.titleHC_Min);
        titleHC_Altitude = (TextView) findViewById(R.id.titleHC_Altitude);
        titleHC_Distance = (TextView) findViewById(R.id.titleHC_Distance);
        titleHC_Time = (TextView) findViewById(R.id.titleHC_Time);

        // Value:
        txtHC_speedData = (TextView) findViewById(R.id.txtHC_speedData);
        txtHC_variationSpeed = (TextView) findViewById(R.id.txtHC_variationSpeed);
        txtHC_maxSpeed = (TextView) findViewById(R.id.txtHC_maxSpeed);
        txtHC_minSpeed = (TextView) findViewById(R.id.txtHC_minSpeed);
        txtHC_Altitude = (TextView) findViewById(R.id.txtHC_Altitude);
        txtHC_variationAltitude = (TextView) findViewById(R.id.txtHC_variationAltitude);
        txtHC_Distance = (TextView) findViewById(R.id.txtHC_Distance);
        txtHC_Time = (TextView) findViewById(R.id.txtHC_Time);

        txtSpeedValue.setText("N/A");
        txtSpeedUnit.setText("N/A");
        txtLongitudeValue.setText("N/A");
        txtLatitudeValue.setText("N/A");
        txtAltitudeValue.setText("N/A");
        txtDistanceValue.setText("N/A");
        txtDistanceUnit.setText("N/A");
        txtTimeValue.setText("N/A");
        txtTimeUnit.setText("N/A");
        txtSpeedMaxValue.setText("N/A");
        txtSpeedMinValue.setText("N/A");
        txtSpeedMinUnit.setText("N/A");
        txtSpeedMaxUnit.setText("N/A");
        txtRunningTime.setText("N/A");

        // Set Value for the TextView in High Score:
        txtHC_speedData.setText("N/A");
//                txtHC_speedData.setTextColor(Color.BLUE);
        txtHC_variationSpeed.setText("N/A");
        txtHC_variationSpeed.setTextColor(Color.BLUE);
        txtHC_maxSpeed.setText("N/A");
        txtHC_maxSpeed.setTextColor(Color.RED);
        txtHC_minSpeed.setText("N/A");
        txtHC_minSpeed.setTextColor(Color.GREEN);
        txtHC_Altitude.setText("N/A");
        txtHC_Altitude.setTextColor(Color.BLUE);
        txtHC_variationAltitude.setText("N/A");
        txtHC_variationAltitude.setTextColor(Color.BLUE);
        txtHC_Distance.setText("N/A");
        txtHC_Distance.setTextColor(Color.BLUE);
        txtHC_Time.setText("N/A");
        txtHC_Time.setTextColor(Color.BLUE);

        for (Loc locItem : database.locDao().getAll()) {
            database.locDao().delete(locItem);
        }
        for (Loc locItem : globalDatabase.locDao().getAll()) {
            database.locDao().delete(locItem);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flipper = findViewById(R.id.view_flipper);
        btn_previous = findViewById(R.id.prev_button);
        btn_next = findViewById(R.id.next_button);

        btn_nextClick();
        btn_previousClick();

        database = Room.databaseBuilder(this, GPSDatabase.class, "GPS_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        globalDatabase = Room.databaseBuilder(this, GPSDatabase.class, "GPS_global_db").allowMainThreadQueries().build();

        // Longitude
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtLongitudeValue = (TextView) findViewById(R.id.txtLongitudeValue);

        // Latitude
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLatitudeValue = (TextView) findViewById(R.id.txtLatitudeValue);

        // Altitude
        txtAltitude = (TextView) findViewById(R.id.txtAltitude);
        txtAltitudeValue = (TextView) findViewById(R.id.txtAltitudeValue);

        // Distance
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtDistanceValue = (TextView) findViewById(R.id.txtDistanceValue);
        txtDistanceUnit = (TextView) findViewById(R.id.txtDistanceUnitShow);

        // Time
        txtTimeShow = (TextView) findViewById(R.id.txtTime);
        txtTimeValue = (TextView) findViewById(R.id.txtTimeValue);
        txtTimeUnit = (TextView) findViewById(R.id.txtTimeUnitShow);

        // Program RunningTime:
        txtRunningTime = (TextView) findViewById(R.id.txtRunningTime);

        // Speed
        txtSpeedShow = (TextView) findViewById(R.id.txtSpeedShow);
        txtSpeedValue = (TextView) findViewById(R.id.txtSpeedValue);
        txtSpeedUnit = (TextView) findViewById(R.id.txtSpeedUnitShow);

        // Min Speed
        txtSpeedMinShow = (TextView) findViewById(R.id.txtSpeedMin);
        txtSpeedMinValue = (TextView) findViewById(R.id.txtSpeedMinValue);
        txtSpeedMinUnit = (TextView) findViewById(R.id.txtSpeedMinUnitShow);

        // Max Speed
        txtSpeedMaxShow = (TextView) findViewById(R.id.txtSpeedMax);
        txtSpeedMaxValue = (TextView) findViewById(R.id.txtSpeedMaxValue);
        txtSpeedMaxUnit = (TextView) findViewById(R.id.txtSpeedMaxUnitShow);

        /**
         * --------  High_Score View Here: ---------------
         */

        // Title:
        titleHC_speedData = findViewById(R.id.titleHC_speedData);
        titleHC_Max = findViewById(R.id.titleHC_Max);
        titleHC_Min = findViewById(R.id.titleHC_Min);
        titleHC_Altitude = findViewById(R.id.titleHC_Altitude);
        titleHC_Distance = findViewById(R.id.titleHC_Distance);
        titleHC_Time = findViewById(R.id.titleHC_Time);

        // Value:
        txtHC_speedData = findViewById(R.id.txtHC_speedData);
        txtHC_variationSpeed = findViewById(R.id.txtHC_variationSpeed);
        txtHC_maxSpeed = findViewById(R.id.txtHC_maxSpeed);
        txtHC_minSpeed = findViewById(R.id.txtHC_minSpeed);
        txtHC_Altitude = findViewById(R.id.txtHC_Altitude);
        txtHC_variationAltitude =  findViewById(R.id.txtHC_variationAltitude);
        txtHC_Distance = findViewById(R.id.txtHC_Distance);
        txtHC_Time = findViewById(R.id.txtHC_Time);


        // Location Object.
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

                    // Set Font Size for Main view:
                    setFontSize(txtLongitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLongitudeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLatitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtLatitudeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtAltitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtAltitudeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtDistance, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtDistanceValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtDistanceUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtTimeShow, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtTimeValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtTimeUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedShow, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedMinShow, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedMinValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedMinUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedMaxShow, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedMaxValue, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtSpeedMaxUnit, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtRunningTime, Float.parseFloat(edtFontSize.getText().toString()));

                    // Set Font size for High_Score View:
                    // Title:
                    setFontSize(titleHC_speedData, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(titleHC_Max, Float.parseFloat(edtFontSize.getText().toString()) - 2);
                    setFontSize(titleHC_Min, Float.parseFloat(edtFontSize.getText().toString()) - 2);
                    setFontSize(titleHC_Altitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(titleHC_Distance, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(titleHC_Time, Float.parseFloat(edtFontSize.getText().toString()));

                    // Corresponding Value:
                    setFontSize(txtHC_speedData, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtHC_variationSpeed, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_maxSpeed, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_minSpeed, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_Altitude, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtHC_variationAltitude, Float.parseFloat(edtFontSize.getText().toString()) - 3);
                    setFontSize(txtHC_Distance, Float.parseFloat(edtFontSize.getText().toString()));
                    setFontSize(txtHC_Time, Float.parseFloat(edtFontSize.getText().toString()));
                }
            }
        });

        // Instruction page of the APP.
        helpful_click();

        // Test app under synthetic source
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isTest = !isTest;
            }
        });

        // Reset accumulated distance and time
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isReset = true;
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
                    preTime = System.nanoTime();
                    onResume();
                    Toast.makeText(MainActivity.this, "Resume the location updates", Toast.LENGTH_SHORT).show();
                    isPause = false;
                }
            }
        });

        DistanceSpinner();

        TimeSpinner();

        SpeedSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Initialize();
        makeToast("Welcome to My Altimeter! ");
    }

    @Override
    public void onResume() {
        super.onResume();
        locationUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(mLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        makeToast("My Altimeter is still recording... Resume in any time ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Initialize();
        locationManager.removeUpdates(mLocationListener);
        makeToast("Thank you, See you next time! ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("Welcome back ^_^");
    }

    public void btn_nextClick(){
        btn_next.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // Set the in and out animation of View flipper.
                        flipper.setInAnimation(MainActivity.this,
                                R.anim.slide_right);
                        flipper.setOutAnimation(MainActivity.this,
                                R.anim.slide_left);

                        // It shows next view..
                        flipper.showNext();
                    }
                });
    }

    public void btn_previousClick() {
        btn_previous.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        // Set the in and out animation of View flipper.
                        flipper.setInAnimation(MainActivity.this,
                                android.R.anim.slide_in_left);
                        flipper.setOutAnimation(MainActivity.this,
                                android.R.anim.slide_out_right);

                        // It shows previous view.
                        flipper.showPrevious();
                    }
                });
    }


    /**
     * ---------- All Spinner Views in the Layout - activity_main -----------
     */

    public void DistanceSpinner(){
        // change Distance unit
        DistanceUnit = (Spinner)findViewById(R.id.optionDistanceUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_Distance = ArrayAdapter.createFromResource(this,
                R.array.distance_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_Distance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        DistanceUnit.setAdapter(adapter_Distance);
        final String[] Distance = new String[1];
        DistanceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count_Distance_OnItemSelectedListener == 1) {
                    Distance[0] = parent.getItemAtPosition(position).toString();
                    Unit_distance = position;
                }
                else{
                    count_Distance_OnItemSelectedListener += 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void TimeSpinner(){
        // change Time unit
        TimeUnit = (Spinner)findViewById(R.id.optionTimeUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_Time = ArrayAdapter.createFromResource(this,
                R.array.time_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_Time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        TimeUnit.setAdapter(adapter_Time);
        final String[] Time = new String[1];
        TimeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count_Time_OnItemSelectedListener == 1) {
                    Time[0] = parent.getItemAtPosition(position).toString();
                    Unit_Time = position;
                }
                else{
                    count_Time_OnItemSelectedListener += 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void SpeedSpinner(){
        // change Speed unit
        SpeedUnit = (Spinner)findViewById(R.id.optionSpeedUnit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_Speed = ArrayAdapter.createFromResource(this,
                R.array.speed_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_Speed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        SpeedUnit.setAdapter(adapter_Speed);
        final String[] Speed = new String[1];
        SpeedUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (count_Speed_OnItemSelectedListener == 1) {
                    Speed[0] = parent.getItemAtPosition(position).toString();
                    Unit_Speed = position;
                }
                else{
                    count_Speed_OnItemSelectedListener += 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    // Handle messages, TODO: Add more code explaination at here:
    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            if ( msg.what == 0x001 ) {
                if (valCurrentSpeed < 10.0) {
                    txtSpeedValue.setTextColor(Color.BLACK);
                }
                else if (valCurrentSpeed < 20.0){
                    txtSpeedValue.setTextColor(Color.GREEN);
                }
                else if (valCurrentSpeed < 30.0){
                    txtSpeedValue.setTextColor(Color.BLUE);
                }
                else if (valCurrentSpeed < 50.0){
                    txtSpeedValue.setTextColor(Color.CYAN);
                }
                else{
                    txtSpeedValue.setTextColor(Color.RED);
                }

                // Set Value for the TextView in Main:
                txtSpeedValue.setText(strSpeedValue);
                txtSpeedUnit.setText(strSpeedUnit);
                txtLongitudeValue.setText(strLongitude);
                txtLatitudeValue.setText(strLatitude);
                txtAltitudeValue.setText(strAltitude);
                txtDistanceValue.setText(strDistanceValue);
                txtDistanceUnit.setText(strDistanceUnit);
                txtTimeValue.setText(strTimeValue);
                txtTimeUnit.setText(strTimeUnit);
                txtSpeedMaxValue.setText(strSpeedMax);
                txtSpeedMinValue.setText(strSpeedMin);
                txtSpeedMinUnit.setText(strSpeedUnit);
                txtSpeedMaxUnit.setText(strSpeedUnit);
                txtRunningTime.setText(strRunningTime);

                // Set Value for the TextView in High Score:
                String text_speedData = strSpeedValue + " /" + strSpeedUnit;
                txtHC_speedData.setText(text_speedData);
                txtHC_speedData.setTextColor(Color.BLACK);
                txtHC_variationSpeed.setText(strVariantSpeed);
                txtHC_variationSpeed.setTextColor(Color.BLUE);
                txtHC_maxSpeed.setText(strGlobalSpeedMax);
                txtHC_maxSpeed.setTextColor(Color.RED);
                txtHC_minSpeed.setText(strGlobalSpeedMin);
                txtHC_minSpeed.setTextColor(Color.GREEN);
                txtHC_Altitude.setText(strHighestAltitude);
                txtHC_Altitude.setTextColor(Color.MAGENTA);
                txtHC_variationAltitude.setText(strVariantAltitude);
                txtHC_variationAltitude.setTextColor(Color.BLUE);
                txtHC_Distance.setText(strLongestDistance);
                txtHC_Distance.setTextColor(Color.MAGENTA);
                txtHC_Time.setText(strLongestTime);
                txtHC_Time.setTextColor(Color.MAGENTA);

            }

            return false;
        }
    });


    // Update variable within the class of Location.
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


    public double speed_unit_transfer_value(double speed, int Unit_Speed){
        switch (Unit_Speed) {
            case 0:
                return significant_fraction(speed, FRACTION_CONSTRAINT);
            case 1:
                return significant_fraction(3.6 * speed, FRACTION_CONSTRAINT);
            case 2:
                return significant_fraction(2.2369362921 * speed, FRACTION_CONSTRAINT);
            default:
                return significant_fraction(2.2369362921 / 60 * speed, FRACTION_CONSTRAINT);
        }
    }

    public String speed_unit_transfer_unit(int Unit_Speed){
        switch (Unit_Speed) {
            case 0:
                return "Mps";
            case 1:
                return "Kph";
            case 2:
                return "Mph";
            default:
                return "Mpm";
        }
    }

    public double distance_unit_transfer_value(double distance, int Unit_Distance){
        switch (Unit_Distance) {
            case 0:
                return significant_fraction(distance, FRACTION_CONSTRAINT) ;
            case 1:
                return significant_fraction(distance / 1000, FRACTION_CONSTRAINT);
            case 2:
                return significant_fraction(distance * 0.000621371192, FRACTION_CONSTRAINT);
            default:
                return significant_fraction(distance * 3.2808399, FRACTION_CONSTRAINT);
        }
    }

    public String distance_unit_transfer_unit(int Unit_Distance){
        switch (Unit_Distance) {
            case 0:
                return "m";
            case 1:
                return "km";
            case 2:
                return "miles";
            default:
                return "feet";
        }
    }

    public Double time_unit_transfer_value(double time, int Unit_Time){
        switch (Unit_Time) {
            case 0:
                return significant_fraction(time, FRACTION_CONSTRAINT);
            case 1:
                return significant_fraction(time / 60, FRACTION_CONSTRAINT);
            case 2:
                return significant_fraction(time / 3600, FRACTION_CONSTRAINT);
            default:
                return significant_fraction(time / 3600 / 24, FRACTION_CONSTRAINT);
        }
    }

    public String time_unit_transfer_unit(int Unit_Time){
        switch (Unit_Time) {
            case 0:
                return "s";
            case 1:
                return "mins";
            case 2:
                return "hrs";
            default:
                return "days";
        }
    }

    /**
     * Given a string, and check if it matches the regular expression of
     * @param var: Input decimal number that needed to be rounded up.
     * @param decimal_places : Number of decimal place to round up
     * @return A rounded value in certain significant figures.
     */
    private synchronized double significant_fraction(double var, int decimal_places) {

        String str_var = Double.toString(Math.abs(var));
        int decimal_place = str_var.length() - str_var.indexOf('.') - 1;

        // If variables' decimal places length is larger than our expectation:
        if (decimal_place > decimal_places)
        {
            int integer_part = (int)var;
            BigDecimal big_var = new BigDecimal(var - integer_part);
            big_var = big_var.round(new MathContext(decimal_places));
            return big_var.doubleValue() + integer_part;
        }
        // Else: No need to round up, return the original value to expedite running of the program.
        else
        {
            return var;
        }

    }


    /**
     * Synchronize update the MainActivity by the update of Location variables.
     * Synchronized used: Wait until responses from Local Database update finishing, and then update
     * @param location: The data class object of the Location, consists of a latitude, longitude, timestamp, accuracy,
     *                and other information such as bearing, altitude and velocity
     */
    private synchronized void updateShow(Location location) {

        // Not test mode, get real-time data.
        if(!isTest) {
            curTime = System.nanoTime();

            if (location != null) {

                StringBuilder stringBuilderLongitude = new StringBuilder();
                StringBuilder stringBuilderLatitude = new StringBuilder();
                StringBuilder stringBuilderAltitude = new StringBuilder();

                stringBuilderLongitude.append(significant_fraction(location.getLongitude(), GPS_CONSTRAINT));
                stringBuilderLatitude.append(significant_fraction(location.getLatitude(), GPS_CONSTRAINT));
                stringBuilderAltitude.append(significant_fraction(location.getAltitude(), GPS_CONSTRAINT));

                // String Builder for calculate values of matrices in Main View:
                StringBuilder stringBuilderSpeedValue = new StringBuilder();
                StringBuilder stringBuilderSpeedUnit = new StringBuilder();
                StringBuilder stringBuilderSpeedMin = new StringBuilder();
                StringBuilder stringBuilderSpeedMax = new StringBuilder();
                StringBuilder stringBuilderTimeValue = new StringBuilder();
                StringBuilder stringBuilderTimeUnit = new StringBuilder();
                StringBuilder stringBuilderDistanceValue = new StringBuilder();
                StringBuilder stringBuilderDistanceUnit = new StringBuilder();
                StringBuilder stringBuildertxtRunningTime = new StringBuilder();

                // String Builder for calculate values of matrices in HighScore View:
                StringBuilder stringBuilderHighestSpeed = new StringBuilder();
                StringBuilder stringBuilderVarianceSpeed = new StringBuilder();
                StringBuilder stringBuilderGlobalMaxSpeed = new StringBuilder();
                StringBuilder stringBuilderGlobalMinSpeed = new StringBuilder();
                StringBuilder stringBuilderHighestAltitude = new StringBuilder();
                StringBuilder stringBuilderVarianceAltitude = new StringBuilder();
                StringBuilder stringBuilderLongestDistance = new StringBuilder();
                StringBuilder stringBuilderLongestTime = new StringBuilder();

                valCurrentSpeed = 3.6 * location.getSpeed();

                if(!(preLatitude == location.getLatitude() && preLongitude == location.getLongitude())){
                    valCurrentTime = (curTime - preTime) / 1E9 + valCurrentTime;
                }
                preLatitude = location.getLatitude();
                preLongitude = location.getLongitude();
                valCurrentDistance = valCurrentDistance + location.getSpeed() * (curTime - preTime) / 1E9;

                // ------  Functionalities of RESET:  ------
                preTime = curTime;
                if(isReset){
                    preTime = System.nanoTime();
                    valCurrentTime = 0;
                    valCurrentDistance = 0;
                    valCurrentMaxSpeed = 0;
                    valCurrentMinSpeed = Double.MAX_VALUE;
                    for (Loc locItem : database.locDao().getAll()) {
                        database.locDao().delete(locItem);
                    }

                    isReset = false;
                }


                // Append transferred unit value into the TextView and showing:
                stringBuilderSpeedValue.append(speed_unit_transfer_value(location.getSpeed(), Unit_Speed));
                stringBuilderSpeedUnit.append(speed_unit_transfer_unit(Unit_Speed));
                stringBuilderTimeValue.append(time_unit_transfer_value(valCurrentTime,Unit_Time));
                stringBuilderTimeUnit.append(time_unit_transfer_unit(Unit_Time));
                stringBuilderDistanceValue.append(distance_unit_transfer_value(valCurrentDistance,Unit_distance));
                stringBuilderDistanceUnit.append(distance_unit_transfer_unit(Unit_distance));
                stringBuildertxtRunningTime.append("App running Time: ").append((curTime - runningTime) / 1E9).append("s");


                // ---------  Current database update: --------
                String[] s_max = new String[1];
                s_max[0] = "max_speed";
                if (database.locDao().loadAllByIds(s_max).size() > 0) {
                    valCurrentMaxSpeed = database.locDao().loadAllByIds(s_max).get(0).speed;

                    if (location.getSpeed() > valCurrentMaxSpeed) {
                        valCurrentMaxSpeed = location.getSpeed();
                        Loc loc = new Loc();
                        loc.speed = valCurrentMaxSpeed;
                        loc.id = "max_speed";
                        database.locDao().delete(loc);
                        database.locDao().insertAll(loc);
                    }

                    stringBuilderSpeedMax.append(significant_fraction(valCurrentMaxSpeed, FRACTION_CONSTRAINT));
                } else {
                    Loc loc = new Loc();
                    loc.speed = valCurrentMaxSpeed;
                    loc.id = "max_speed";
                    database.locDao().delete(loc);
                    database.locDao().insertAll(loc);
                }

                String[] s_min = new String[1];
                s_min[0] = "min_speed";
                if (database.locDao().loadAllByIds(s_min).size() > 0) {
                    valCurrentMinSpeed = database.locDao().loadAllByIds(s_min).get(0).speed;

                    if (location.getSpeed() < valCurrentMinSpeed) {
                        valCurrentMinSpeed = location.getSpeed();
                        Loc loc = new Loc();
                        loc.speed = valCurrentMinSpeed;
                        loc.id = "min_speed";
                        database.locDao().delete(loc);
                        database.locDao().insertAll(loc);
                    }
                    stringBuilderSpeedMin.append(significant_fraction(valCurrentMinSpeed, FRACTION_CONSTRAINT));
                } else {
                    Loc loc = new Loc();
                    loc.speed = valCurrentMinSpeed;
                    loc.id = "min_speed";
                    database.locDao().delete(loc);
                    database.locDao().insertAll(loc);
                    stringBuilderSpeedMin.append("0.min_speed");

                }


                // ------ Global Database and High Score View update: -------
                // TODO: Unfinished Backend: - VarianceAltitude...
                updateHighScoreData(location, stringBuilderVarianceSpeed,
                                    stringBuilderGlobalMaxSpeed,
                                    stringBuilderGlobalMinSpeed,
                                    stringBuilderHighestAltitude,
                                    stringBuilderVarianceAltitude,
                                    stringBuilderLongestDistance,
                                    stringBuilderLongestTime);

                /* ------------------------------------------------------------------------------------------------------*/
                // ---- Main View: ----

                // GPS information:
                strLongitude = stringBuilderLongitude.toString();
                strLatitude = stringBuilderLatitude.toString();
                strAltitude = stringBuilderAltitude.toString();

                // Speed information:
                strSpeedValue = stringBuilderSpeedValue.toString();
                strSpeedUnit = stringBuilderSpeedUnit.toString();
                strSpeedMin = stringBuilderSpeedMin.toString();
                strSpeedMax = stringBuilderSpeedMax.toString();

                // Time information:
                strTimeValue = stringBuilderTimeValue.toString();
                strTimeUnit = stringBuilderTimeUnit.toString();

                // Distance information:
                strDistanceValue = stringBuilderDistanceValue.toString();
                strDistanceUnit = stringBuilderDistanceUnit.toString();

                // Running time:
                strRunningTime = stringBuildertxtRunningTime.toString();


            } else {
                // If Location is null, cannot grab information from Location (etc. "Non Fine authority of GPS").
                strLongitude = "";
                strLatitude = "";
                strAltitude = "";
                strSpeedValue = "";
                strSpeedMax = "";
                strSpeedMin = "";
                strSpeedUnit = "";
                strTimeValue = "";
                strTimeUnit = "";
                strDistanceValue = "";
                strDistanceUnit = "";

                // Highest Scores:
                strHighestSpeed = "";
                strVariantSpeed = "";
                strGlobalSpeedMax = "";
                strGlobalSpeedMin = "";
                strHighestAltitude = "";
                strVariantAltitude = "";
                strLongestDistance = "";
                strLongestTime = "";

                strRunningTime = "App running Time: " + ((System.nanoTime() - runningTime) / 1E9) + "s";
            }
        }
        else {
            // ----------  Testing mode: ---------
            simulation_distance();
            curTime = System.nanoTime();
            if (location != null) {

                StringBuilder stringBuilderLongitude = new StringBuilder();
                StringBuilder stringBuilderLatitude = new StringBuilder();
                StringBuilder stringBuilderAltitude = new StringBuilder();
                StringBuilder stringBuilderSpeedValue = new StringBuilder();
                StringBuilder stringBuilderSpeedUnit = new StringBuilder();
                StringBuilder stringBuilderSpeedMin = new StringBuilder();
                StringBuilder stringBuilderSpeedMax = new StringBuilder();
                StringBuilder stringBuilderTimeValue = new StringBuilder();
                StringBuilder stringBuilderTimeUnit = new StringBuilder();
                StringBuilder stringBuilderDistanceValue = new StringBuilder();
                StringBuilder stringBuilderDistanceUnit = new StringBuilder();
                StringBuilder stringBuildertxtRunningTime = new StringBuilder();

                stringBuilderLongitude.append(significant_fraction(valLongitude, GPS_CONSTRAINT));
                stringBuilderLatitude.append(significant_fraction(valLatitude, GPS_CONSTRAINT));
                stringBuilderAltitude.append(significant_fraction(curTime/1E20, GPS_CONSTRAINT));
                Sim_valCurrentTime = (curTime - preTime) / 1E9 + Sim_valCurrentTime;
                Sim_valCurrentDistance = Sim_valCurrentDistance + Sim_valCurrentSpeed * (curTime - preTime) / 1E9;

                valCurrentSpeed = 3.6 * location.getSpeed();
                if(!(preLatitude == location.getLatitude() && preLongitude == location.getLongitude())){
                    valCurrentTime = (curTime - preTime) / 1E9 + valCurrentTime;
                }
                preLatitude = location.getLatitude();
                preLongitude = location.getLongitude();
                valCurrentDistance = valCurrentDistance + location.getSpeed() * (curTime - preTime) / 1E9;

                preTime = curTime;
                if(isReset){
                    preTime = System.nanoTime();
                    Sim_valCurrentTime = 0;
                    Sim_valCurrentDistance = 0;
                    valCurrentMaxSpeed = 0;
                    valCurrentMinSpeed = Double.MAX_VALUE;
                    for (Loc locItem : database.locDao().getAll()) {
                        database.locDao().delete(locItem);
                    }

                    isReset = false;
                }

                // Unit Transfer
                stringBuilderSpeedValue.append(speed_unit_transfer_value(Sim_valCurrentSpeed,Unit_Speed));
                stringBuilderSpeedUnit.append(speed_unit_transfer_unit(Unit_Speed));
                stringBuilderTimeValue.append(time_unit_transfer_value(Sim_valCurrentTime,Unit_Time));
                stringBuilderTimeUnit.append(time_unit_transfer_unit(Unit_Time));
                stringBuilderDistanceValue.append(distance_unit_transfer_value(Sim_valCurrentDistance,Unit_distance));
                stringBuilderDistanceUnit.append(distance_unit_transfer_unit(Unit_distance));
                stringBuildertxtRunningTime.append("App running Time: ").append((curTime - runningTime) / 1E9).append("s");

                String[] s_max = new String[1];
                s_max[0] = "max_speed";
                if (database.locDao().loadAllByIds(s_max).size() > 0) {
                    valCurrentMaxSpeed = database.locDao().loadAllByIds(s_max).get(0).speed;

                    if (location.getSpeed() > valCurrentMaxSpeed) {
                        valCurrentMaxSpeed = location.getSpeed();
                        Loc loc = new Loc();
                        loc.speed = valCurrentMaxSpeed;
                        loc.id = "max_speed";
                        database.locDao().delete(loc);
                        database.locDao().insertAll(loc);
                    }
                    stringBuilderSpeedMax.append(significant_fraction(valCurrentMaxSpeed, FRACTION_CONSTRAINT));

                } else {
                    Loc loc = new Loc();
                    loc.speed = valCurrentMaxSpeed;
                    loc.id = "max_speed";
                    database.locDao().delete(loc);
                    database.locDao().insertAll(loc);
                    stringBuilderSpeedMax.append("0.0");
                }

                String[] s_min = new String[1];
                s_min[0] = "min_speed";
                if (database.locDao().loadAllByIds(s_min).size() > 0) {
                    valCurrentMinSpeed = database.locDao().loadAllByIds(s_min).get(0).speed;

                    if (location.getSpeed() < valCurrentMinSpeed) {
                        valCurrentMinSpeed = location.getSpeed();
                        Loc loc = new Loc();
                        loc.speed = valCurrentMinSpeed;
                        loc.id = "min_speed";
                        database.locDao().delete(loc);
                        database.locDao().insertAll(loc);
                    }
                    stringBuilderSpeedMin.append(significant_fraction(valCurrentMinSpeed, FRACTION_CONSTRAINT));
                } else {
                    Loc loc = new Loc();
                    loc.speed = valCurrentMinSpeed;
                    loc.id = "min_speed";
                    database.locDao().delete(loc);
                    database.locDao().insertAll(loc);
                    stringBuilderSpeedMin.append("0.min_speed");
                }

                strLongitude = stringBuilderLongitude.toString();
                strLatitude = stringBuilderLatitude.toString();
                strAltitude = stringBuilderAltitude.toString();
                strSpeedValue = stringBuilderSpeedValue.toString();
                strSpeedUnit = stringBuilderSpeedUnit.toString();
                strSpeedMin = stringBuilderSpeedMin.toString();
                strSpeedMax = stringBuilderSpeedMax.toString();
                strTimeValue = stringBuilderTimeValue.toString();
                strTimeUnit = stringBuilderTimeUnit.toString();
                strDistanceValue = stringBuilderDistanceValue.toString();
                strDistanceUnit = stringBuilderDistanceUnit.toString();
                strRunningTime = stringBuildertxtRunningTime.toString();

            } else {

                strLongitude = "";
                strLatitude = "";
                strAltitude = "";
                strSpeedValue = "";
                strSpeedMax = "";
                strSpeedMin = "";
                strSpeedUnit = "";
                strTimeValue = "";
                strTimeUnit = "";
                strDistanceValue = "";
                strDistanceUnit = "";
                strRunningTime = "App running Time: " + ((System.nanoTime() - runningTime) / 1E9) + "s";
            }
        }

        handler.sendEmptyMessage(0x001);
    }

    /**
     * Using Dom to Record all extreme values of matrices from the User
     * @param location: Location Object,
     * @param stringBuilderGlobalMaxSpeed:
     * @param stringBuilderGlobalMinSpeed:
     * High Score View Back-end:
     *      @Speed:     MaximumSpeed, MinimumSpeed Reached; variation of speed;
     *      @Altitude:  LongestAltitude Reached; variation of Longitude;
     *      @Distance:  LongestDistance Traveled;
     *      @Time:      GlobalLongestTime Traveled;
     */

    private synchronized void updateHighScoreData(Location location, StringBuilder stringBuilderVarianceSpeed,
                                     StringBuilder stringBuilderGlobalMaxSpeed,
                                     StringBuilder stringBuilderGlobalMinSpeed,
                                     StringBuilder stringBuilderHighestAltitude,
                                     StringBuilder stringBuilderVarianceAltitude,
                                     StringBuilder stringBuilderLongestDistance,
                                     StringBuilder stringBuilderLongestTime)  {

        // ---------- Update Variance Speed ----------
        String[] variant_s = new String[1];
        variant_s[0] = "variant_speed";
        if (globalDatabase.locDao().loadAllByIds(variant_s).size() > 0) {
            valVarianceSpeed = globalDatabase.locDao().loadAllByIds(variant_s).get(0).speed;

            // If Current Speed is greater than the last time stamp's speed, Print the Differences and store the current Speed.
            if (location.getSpeed() > valVarianceSpeed) {
                double difference = location.getSpeed() - valVarianceSpeed;
                Loc loc = new Loc();
                loc.speed = location.getSpeed();
                loc.id = "variant_speed";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
                stringBuilderVarianceSpeed.append("+ ");
                stringBuilderVarianceSpeed.append(
                        speed_unit_transfer_value(
                                significant_fraction(difference, FRACTION_CONSTRAINT), Unit_Speed)
                );
                stringBuilderVarianceSpeed.append(speed_unit_transfer_unit(Unit_Speed));
            }
            // Else: Current Speed is smaller than the last time stamp's speed, Print the Differences and store the current Speed.
            else {
                double difference = valVarianceSpeed - location.getSpeed();
                Loc loc = new Loc();
                loc.speed = location.getSpeed();
                loc.id = "variant_speed";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
                stringBuilderVarianceSpeed.append("- ");
                stringBuilderVarianceSpeed.append(
                        speed_unit_transfer_value(
                                significant_fraction(difference, FRACTION_CONSTRAINT), Unit_Speed)
                );
                stringBuilderVarianceSpeed.append(speed_unit_transfer_unit(Unit_Speed));
            }

        } else {
            Loc loc = new Loc();
            loc.speed = location.getSpeed();
            loc.id = "variant_speed";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderVarianceSpeed.append(speed_unit_transfer_value(location.getSpeed(), Unit_Speed));
            stringBuilderVarianceSpeed.append(speed_unit_transfer_unit(Unit_Speed));
        }

        strVariantSpeed = stringBuilderVarianceSpeed.toString();


        // --------- Update Global Maximum Speed; ----------
        String[] global_sMax = new String[1];
        global_sMax[0] = "g_max";

        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_sMax).size() > 0) {
            valGlobalMaxSpeed = globalDatabase.locDao().loadAllByIds(global_sMax).get(0).speed;

            if (location.getSpeed() >= valGlobalMaxSpeed) {
                valGlobalMaxSpeed = location.getSpeed();
                Loc loc = new Loc();
                loc.speed = valGlobalMaxSpeed;
                loc.id = "g_max";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }

            stringBuilderGlobalMaxSpeed.append(
                    speed_unit_transfer_value(
                            significant_fraction(valGlobalMaxSpeed, FRACTION_CONSTRAINT), Unit_Speed)
            );
            stringBuilderGlobalMaxSpeed.append(speed_unit_transfer_unit(Unit_Speed));

        } else {
            // Initiate this entity
            Loc loc = new Loc();
            loc.speed = valGlobalMaxSpeed;
            loc.id = "g_max";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderGlobalMaxSpeed.append("0.0");
            stringBuilderGlobalMaxSpeed.append(speed_unit_transfer_unit(Unit_Speed));
        }

        strGlobalSpeedMax = stringBuilderGlobalMaxSpeed.toString();


        // Update Global Minimum Speed;
        String[] global_sMin = new String[1];
        global_sMin[0] = "g_min";
        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_sMin).size() > 0) {
            valGlobalMinSpeed = globalDatabase.locDao().loadAllByIds(global_sMin).get(0).speed;

            if (location.getSpeed() < valGlobalMinSpeed) {
                valGlobalMinSpeed = location.getSpeed();
                Loc loc = new Loc();
                loc.speed = valGlobalMinSpeed;
                loc.id = "g_min";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }

            stringBuilderGlobalMinSpeed.append(
                    speed_unit_transfer_value(
                            significant_fraction(valGlobalMinSpeed, FRACTION_CONSTRAINT), Unit_Speed)
            );
            stringBuilderGlobalMinSpeed.append(speed_unit_transfer_unit(Unit_Speed));

        } else {
            // Initiate this entity
            Loc loc = new Loc();
            loc.speed = valGlobalMinSpeed;
            loc.id = "g_min";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderGlobalMinSpeed.append(speed_unit_transfer_value(0.0, Unit_Speed));
            stringBuilderGlobalMinSpeed.append(speed_unit_transfer_unit(Unit_Speed));
        }

        strGlobalSpeedMin = stringBuilderGlobalMinSpeed.toString();


        // --------- Update Global Highest Altitude; ----------
        String[] global_HighAL = new String[1];
        global_HighAL[0] = "g_maxAltitude";

        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_HighAL).size() > 0) {
            valHighestAltitude = globalDatabase.locDao().loadAllByIds(global_HighAL).get(0).speed;

            if (location.getAltitude() >= valHighestAltitude) {
                valGlobalMaxSpeed = location.getAltitude();
                Loc speed = new Loc();
                speed.speed = valHighestAltitude;
                speed.id = "g_maxAltitude";
                globalDatabase.locDao().delete(speed);
                globalDatabase.locDao().insertAll(speed);
            }

            stringBuilderHighestAltitude.append(
                    distance_unit_transfer_value(
                            significant_fraction(valHighestAltitude, FRACTION_CONSTRAINT), Unit_distance)
            );
            stringBuilderHighestAltitude.append(distance_unit_transfer_unit(Unit_distance));

        } else {
            Loc loc = new Loc();
            loc.speed = valHighestAltitude;
            loc.id = "g_maxAltitude";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderHighestAltitude.append(distance_unit_transfer_value(0.0, Unit_distance));
            stringBuilderHighestAltitude.append(distance_unit_transfer_unit(Unit_distance));
        }

        strHighestAltitude = stringBuilderHighestAltitude.toString();


        // --------- Update Global Longest Distance traveled since last reset pressed ----------
        String[] global_longD = new String[1];
        global_longD[0] = "g_longDistance";

        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_longD).size() > 0) {
            valLongestDistance = globalDatabase.locDao().loadAllByIds(global_longD).get(0).speed;

            // Record from Temporary Database, If the Longest Distance smaller than the record since Last Traveled before Reset.
            if (valCurrentDistance >= valLongestDistance) {
                valLongestDistance = valCurrentDistance;
                Loc loc = new Loc();
                loc.speed = valLongestDistance;
                loc.id = "g_longDistance";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }

            stringBuilderLongestDistance.append(
                    distance_unit_transfer_value(
                            significant_fraction(valLongestDistance, FRACTION_CONSTRAINT), Unit_distance)
            );
            stringBuilderLongestDistance.append(distance_unit_transfer_unit(Unit_distance));

        } else {
            Loc loc = new Loc();
            loc.speed = valLongestDistance;
            loc.id = "g_longDistance";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderLongestDistance.append(distance_unit_transfer_value(0.0, Unit_distance));
            stringBuilderLongestDistance.append(distance_unit_transfer_unit(Unit_distance));
        }

        strLongestDistance = stringBuilderLongestDistance.toString();


        // --------- Update Global Longest Time traveled Since Last Reset pressed; ----------
        String[] global_longT = new String[1];
        global_longT[0] = "g_longTime";

        // If existed this entity in Global-Database
        if (globalDatabase.locDao().loadAllByIds(global_longT).size() > 0) {
            valLongestTime = globalDatabase.locDao().loadAllByIds(global_longT).get(0).speed;

            // Record from Temporary Database, If the Longest time smaller than the record since Last Traveled before Reset.
            if (valCurrentTime >= valLongestTime) {
                valLongestTime = valCurrentTime;
                Loc loc = new Loc();
                loc.speed = valLongestTime;
                loc.id = "g_longTime";
                globalDatabase.locDao().delete(loc);
                globalDatabase.locDao().insertAll(loc);
            }

            stringBuilderLongestTime.append(
                    time_unit_transfer_value(
                            significant_fraction(valLongestTime, FRACTION_CONSTRAINT), Unit_Time)
            );
            stringBuilderLongestTime.append(time_unit_transfer_unit(Unit_Time));

        } else {
            Loc loc = new Loc();
            loc.speed = valLongestTime;
            loc.id = "g_longTime";
            globalDatabase.locDao().delete(loc);
            globalDatabase.locDao().insertAll(loc);
            stringBuilderLongestTime.append(time_unit_transfer_value(0.0, Unit_Time));
            stringBuilderLongestTime.append(time_unit_transfer_unit(Unit_Time));
        }

        strLongestTime = stringBuilderLongestTime.toString();

    }


    //--------------------------------------------------------

    private void simulation_distance() {
        double r_earth = 6378.0;
        double dy = 0;
        double dx = 16.098 / 36000;
        double new_latitude = valLatitude + (dy / r_earth) * (180 / Math.PI);
        double new_longitude = valLongitude + (dx / r_earth) * (180 / Math.PI) / Math.cos(valLatitude * Math.PI/180);
        Sim_valCurrentSpeed = 16.098 / 3.6;
        double speed = valCurrentSpeed;
        StringBuilder sb = new StringBuilder();
        valLatitude = new_latitude;
        valLongitude = new_longitude;
    }

    public void locationUpdate() {

        // check GPS authority from User and App..
        if (checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            makeToast("You should enable GPS to get full functionalities work !");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
            return;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager. GPS_PROVIDER);

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