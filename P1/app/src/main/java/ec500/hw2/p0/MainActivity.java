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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import java.math.BigDecimal;
import java.math.MathContext;

import ec500.hw2.p0.database.GPSDatabase;
import ec500.hw2.p0.model.Speed;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private TextView txtLocation, txtSpeed;
//    private String strLocation, strSpeed;
    private Button btnChangeSize, btnHelp, btnPause, btnTest, btnReset;
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


    private TextView txtLongitudeValue, txtLongitude;
    private TextView txtLatitudeValue, txtLatitude;
    private TextView txtAltitudeValue, txtAltitude;
    private TextView txtDistanceValue, txtDistance, txtDistanceUnit;
    private TextView txtTimeValue, txtTimeShow, txtTimeUnit, txtRunningTime;
    private TextView txtSpeedValue, txtSpeedShow, txtSpeedUnit;
    private TextView txtSpeedMinValue, txtSpeedMinShow, txtSpeedMinUnit;
    private TextView txtSpeedMaxValue, txtSpeedMaxShow, txtSpeedMaxUnit;
    private String strLongitude, strLatitude, strAltitude, strSpeedMax, strSpeedMin, strSpeedUnit;
    private String strTimeValue, strTimeUnit;
    private String strRunningTime;
    private String strDistanceValue, strDistanceUnit;
    private String strSpeedValue;

    // Define the Significant Filter:
    private static final int FRACTION_CONSTRAINT = 3;
    private static final int GPS_CONSTRAINT = 4;

    // String filter, Java regex:
    private final String regex = "^\\-?(\\d{0,8}|\\d{0,5}\\.\\d{0,3})$";

    private static GPSDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = Room.databaseBuilder(this, GPSDatabase.class, "GPS_db").allowMainThreadQueries().build();

//        txtLocation = (TextView) findViewById(R.id.txtLocation);
//        txtSpeed = (TextView) findViewById(R.id.txtSpeed);

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


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationUpdate();

        // Change font size
        btnChangeSize = (Button) findViewById(R.id.btnChangeSize);
        edtFontSize = (EditText) findViewById(R.id.edtFontSize);
        btnChangeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
//                if(edtFontSize.getText().toString().matches("[0-9]+")) {
//                    setFontSize(txtLocation, Float.parseFloat(edtFontSize.getText().toString()));
//                    setFontSize(txtSpeed, Float.parseFloat(edtFontSize.getText().toString()));
//                }
                if(edtFontSize.getText().toString().matches("[0-9]+")) {
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
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        makeToast("Thank you, See you next time! ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        makeToast("Welcome back ^_^");
    }

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
//                Unit_distance = Distance[0]
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//        Unit_distance = DistanceUnit.getSelectedItem().toString();
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
//        Unit_Time = TimeUnit.getSelectedItem().toString();
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
//        Unit_Speed = SpeedUnit.getSelectedItem().toString();
    }


    // Handle messages
    private Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message msg) {
            if ( msg.what == 0x001 ) {
                if (valCurrentSpeed < 10.0) {
//                    txtSpeed.setTextColor(Color.BLACK);
                    txtSpeedValue.setTextColor(Color.BLACK);
                }
                else if (valCurrentSpeed < 20.0){
//                    txtSpeed.setTextColor(Color.GREEN);
                    txtSpeedValue.setTextColor(Color.GREEN);
                }
                else if (valCurrentSpeed < 30.0){
//                    txtSpeed.setTextColor(Color.BLUE);
                    txtSpeedValue.setTextColor(Color.BLUE);
                }
                else if (valCurrentSpeed < 50.0){
//                    txtSpeed.setTextColor(Color.CYAN);
                    txtSpeedValue.setTextColor(Color.CYAN);
                }
                else{
//                    txtSpeed.setTextColor(Color.RED);
                    txtSpeedValue.setTextColor(Color.RED);
                }
//                txtSpeed.setText(strSpeed);
//                txtLocation.setText(strLocation);
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
        else
        {   // No need to round up, return the original value to expedite running of the program.
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

        // If is not in the test mode:
        if(!isTest) {
            curTime = System.nanoTime();
            if (location != null) {
//                StringBuilder sb_loc = new StringBuilder();
//                StringBuilder sb_speed = new StringBuilder();

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

                stringBuilderLongitude.append(significant_fraction(location.getLongitude(), GPS_CONSTRAINT));
                stringBuilderLatitude.append(significant_fraction(location.getLatitude(), GPS_CONSTRAINT));
                stringBuilderAltitude.append(significant_fraction(location.getAltitude(), GPS_CONSTRAINT));

                valCurrentSpeed = 3.6 * location.getSpeed();
//                sb_loc.append("Longitude: " + significant_fraction(location.getLongitude(), GPS_CONSTRAINT) + "\n");
//                sb_loc.append("Latitude: " + significant_fraction(location.getLatitude(), GPS_CONSTRAINT) + "\n");
//                sb_loc.append("Altitude: " + significant_fraction(location.getAltitude(), GPS_CONSTRAINT) + "\n");
                if(!(preLatitude == location.getLatitude() && preLongitude == location.getLongitude())){
                    valCurrentTime = (curTime - preTime) / 1E9 + valCurrentTime;
                }
                preLatitude = location.getLatitude();
                preLongitude = location.getLongitude();
                valCurrentDistance = valCurrentDistance + location.getSpeed() * (curTime - preTime) / 1E9;

                preTime = curTime;
                if(isReset){
                    preTime = startTime;
                    valCurrentTime = 0;
                    valCurrentDistance = 0;
                    valCurrentMaxSpeed = 0;
                    valCurrentMinSpeed = Double.MAX_VALUE;
                    for (Speed speedItem : database.speedDao().getAll()) {
                        database.speedDao().delete(speedItem);
                    }

                    isReset = false;
                }

//                // Unit Transfer
//                sb_loc.append(time_unit_transfer(valCurrentTime, Unit_Time));
//                sb_loc.append(distance_unit_transfer(valCurrentDistance, Unit_distance));
//                sb_speed.append(speed_unit_transfer(location.getSpeed(), Unit_Speed));

                // Unit Transfer New
                stringBuilderSpeedValue.append(speed_unit_transfer_value(location.getSpeed(),Unit_Speed));
                stringBuilderSpeedUnit.append(speed_unit_transfer_unit(Unit_Speed));
                stringBuilderTimeValue.append(time_unit_transfer_value(valCurrentTime,Unit_Time));
                stringBuilderTimeUnit.append(time_unit_transfer_unit(Unit_Time));
                stringBuilderDistanceValue.append(distance_unit_transfer_value(valCurrentDistance,Unit_distance));
                stringBuilderDistanceUnit.append(distance_unit_transfer_unit(Unit_distance));
                stringBuildertxtRunningTime.append("App running Time: ").append((curTime - runningTime) / 1E9).append("s");

                String[] s_max = new String[1];
                s_max[0] = "max";
                if (database.speedDao().loadAllByIds(s_max).size() > 0) {
                    valCurrentMaxSpeed = database.speedDao().loadAllByIds(s_max).get(0).val;

                    if (location.getSpeed() > valCurrentMaxSpeed) {
                        valCurrentMaxSpeed = location.getSpeed();
                        Speed speed = new Speed();
                        speed.val = valCurrentMaxSpeed;
                        speed.id = "max";
                        database.speedDao().delete(speed);
                        database.speedDao().insertAll(speed);
                    }

                    stringBuilderSpeedMax.append(significant_fraction(valCurrentMaxSpeed, FRACTION_CONSTRAINT));
//                    sb_speed.append("Max Speed: " + significant_fraction(valCurrentMaxSpeed, FRACTION_CONSTRAINT) + "\n");
                } else {
                    Speed speed = new Speed();
                    speed.val = valCurrentMaxSpeed;
                    speed.id = "max";
                    database.speedDao().delete(speed);
                    database.speedDao().insertAll(speed);
//                    sb_speed.append("Max Speed: 0.0\n");
                    stringBuilderSpeedMax.append("0.0");
                }

                String[] s_min = new String[1];
                s_min[0] = "min";
                if (database.speedDao().loadAllByIds(s_min).size() > 0) {
                    valCurrentMinSpeed = database.speedDao().loadAllByIds(s_min).get(0).val;

                    if (location.getSpeed() < valCurrentMinSpeed) {
                        valCurrentMinSpeed = location.getSpeed();
                        Speed speed = new Speed();
                        speed.val = valCurrentMinSpeed;
                        speed.id = "min";
                        database.speedDao().delete(speed);
                        database.speedDao().insertAll(speed);
                    }

                    stringBuilderSpeedMin.append(significant_fraction(valCurrentMinSpeed, FRACTION_CONSTRAINT));
//                    sb_speed.append("Min Speed: " + significant_fraction(valCurrentMinSpeed, FRACTION_CONSTRAINT) + "\n");
                } else {
                    Speed speed = new Speed();
                    speed.val = valCurrentMinSpeed;
                    speed.id = "min";
                    database.speedDao().delete(speed);
                    database.speedDao().insertAll(speed);
//                    sb_speed.append("Min Speed: 0.0\n");
                    stringBuilderSpeedMin.append("0.0");
                }

//                strLocation = sb_loc.toString();
//                strSpeed = sb_speed.toString();

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
//                strLocation = "";
//                strSpeed = "";

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
        else {
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
                Sim_valCurrentTime = (curTime - preTime) / 1E9 + valCurrentTime;
                Sim_valCurrentDistance = valCurrentDistance + Sim_valCurrentSpeed * (curTime - preTime) / 1E9;

                preTime = curTime;
                if(isReset){
                    preTime = startTime;
                    Sim_valCurrentTime = 0;
                    Sim_valCurrentDistance = 0;
                    valCurrentMaxSpeed = 0;
                    valCurrentMinSpeed = Double.MAX_VALUE;
                    for (Speed speedItem : database.speedDao().getAll()) {
                        database.speedDao().delete(speedItem);
                    }

                    isReset = false;
                }

                // Unit Transfer New
                stringBuilderSpeedValue.append(speed_unit_transfer_value(Sim_valCurrentSpeed,Unit_Speed));
                stringBuilderSpeedUnit.append(speed_unit_transfer_unit(Unit_Speed));
                stringBuilderTimeValue.append(time_unit_transfer_value(Sim_valCurrentTime,Unit_Time));
                stringBuilderTimeUnit.append(time_unit_transfer_unit(Unit_Time));
                stringBuilderDistanceValue.append(distance_unit_transfer_value(Sim_valCurrentDistance,Unit_distance));
                stringBuilderDistanceUnit.append(distance_unit_transfer_unit(Unit_distance));
                stringBuildertxtRunningTime.append("App running Time: ").append((curTime - runningTime) / 1E9).append("s");

                String[] s_max = new String[1];
                s_max[0] = "max";
                if (database.speedDao().loadAllByIds(s_max).size() > 0) {
                    valCurrentMaxSpeed = database.speedDao().loadAllByIds(s_max).get(0).val;

                    if (location.getSpeed() > valCurrentMaxSpeed) {
                        valCurrentMaxSpeed = location.getSpeed();
                        Speed speed = new Speed();
                        speed.val = valCurrentMaxSpeed;
                        speed.id = "max";
                        database.speedDao().delete(speed);
                        database.speedDao().insertAll(speed);
                    }

                    stringBuilderSpeedMax.append(significant_fraction(valCurrentMaxSpeed, FRACTION_CONSTRAINT));
//                    sb_speed.append("Max Speed: " + significant_fraction(valCurrentMaxSpeed, FRACTION_CONSTRAINT) + "\n");
                } else {
                    Speed speed = new Speed();
                    speed.val = valCurrentMaxSpeed;
                    speed.id = "max";
                    database.speedDao().delete(speed);
                    database.speedDao().insertAll(speed);
//                    sb_speed.append("Max Speed: 0.0\n");
                    stringBuilderSpeedMax.append("0.0");
                }

                String[] s_min = new String[1];
                s_min[0] = "min";
                if (database.speedDao().loadAllByIds(s_min).size() > 0) {
                    valCurrentMinSpeed = database.speedDao().loadAllByIds(s_min).get(0).val;

                    if (location.getSpeed() < valCurrentMinSpeed) {
                        valCurrentMinSpeed = location.getSpeed();
                        Speed speed = new Speed();
                        speed.val = valCurrentMinSpeed;
                        speed.id = "min";
                        database.speedDao().delete(speed);
                        database.speedDao().insertAll(speed);
                    }

                    stringBuilderSpeedMin.append(significant_fraction(valCurrentMinSpeed, FRACTION_CONSTRAINT));
//                    sb_speed.append("Min Speed: " + significant_fraction(valCurrentMinSpeed, FRACTION_CONSTRAINT) + "\n");
                } else {
                    Speed speed = new Speed();
                    speed.val = valCurrentMinSpeed;
                    speed.id = "min";
                    database.speedDao().delete(speed);
                    database.speedDao().insertAll(speed);
//                    sb_speed.append("Min Speed: 0.0\n");
                    stringBuilderSpeedMin.append("0.0");
                }

//                strLocation = sb_loc.toString();
//                strSpeed = sb_speed.toString();

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
//                strLocation = "";
//                strSpeed = "";

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
        if ( checkCallingOrSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
