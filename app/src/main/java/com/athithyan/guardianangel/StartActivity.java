package com.athithyan.guardianangel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity implements SensorEventListener {

    private static final int SHAKE_THRESHOLD = 600;
    private static final String FORMAT = "%02d:%02d:%02d";
    boolean startButtonFlag;
    TextView text1;
    int seconds, minutes;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageView img = (ImageView) findViewById(R.id.startButton);
        TextView txt = (TextView) findViewById(R.id.startText);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        startButtonFlag = sharedPref.getBoolean(getString(R.string.start_button), true);
        if (!startButtonFlag) {
            img.setImageResource(R.drawable.stop);
            txt.setText("Guardian Angel is on now, Stay cool, He is watching over you!");
        } else {
            img.setImageResource(R.drawable.start);
            txt.setText("Guardian Angel is resting now, please switch him on to Guard you!");
        }
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    public void callSettings(MenuItem menuItem) {
        setContentView(R.layout.settings);
        setDocText(findViewById(R.id.editable_doctor_number));
        setFamText(findViewById(R.id.editable_family_number));
        setPolText(findViewById(R.id.editable_police_number));
    }

    public void callDetonateScreen(MenuItem menuItem) {
        TextView detonateCountDown = (TextView) findViewById(R.id.countdown_time);
        setContentView(R.layout.detonate_screen);

        text1 = (TextView) findViewById(R.id.countdown_time);

        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                text1.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                text1.setText("done!");
                callPolice(findViewById(R.id.callPoliceButton));
                sendLocationAsMessage();
            }
        }.start();

    }

    public void callDetonateScreen() {
        TextView detonateCountDown = (TextView) findViewById(R.id.countdown_time);
        setContentView(R.layout.detonate_screen);

        text1 = (TextView) findViewById(R.id.countdown_time);

        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                text1.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                text1.setText("done!");
                callPolice(findViewById(R.id.callPoliceButton));
                sendLocationAsMessage();
            }
        }.start();

    }


    public void callHome(MenuItem menuItem) {

        setContentView(R.layout.activity_start);
        ImageView img = (ImageView) findViewById(R.id.startButton);
        TextView txt = (TextView) findViewById(R.id.startText);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        startButtonFlag = sharedPref.getBoolean(getString(R.string.start_button), true);
        if (!startButtonFlag) {
            img.setImageResource(R.drawable.stop);
            txt.setText("Guardian Angel is on now, Stay cool, He is watching over you!");
        } else {
            img.setImageResource(R.drawable.start);
            txt.setText("Guardian Angel is resting now, please switch him on to Guard you!");
        }

    }

    public void onClickStartButton(View view) {
        ImageView img = (ImageView) findViewById(R.id.startButton);
        TextView txt = (TextView) findViewById(R.id.startText);
        if (startButtonFlag) {
            img.setImageResource(R.drawable.stop);
            startButtonFlag = false;
            txt.setText("Guardian Angel is on now, Stay cool, He is watching over you!");
        } else {
            img.setImageResource(R.drawable.start);
            startButtonFlag = true;
            txt.setText("Guardian Angel is resting now, please switch him on to Guard you!");
        }
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.start_button), startButtonFlag);
        editor.commit();
    }


    public void callPolice(View view) {
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.police_number), "100");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public void callDoctor(View view) {
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.doctor_number), "102");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phone, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public void callFamily(View view) {
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPref.getString(getString(R.string.family_number), "0000000000");

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "+91" + phone, null));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public void iAmSafe(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void submitFamilyNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.editable_family_number);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.family_number), (editText.getText().toString()));
        editor.commit();
        setFamText(editText);
    }

    public void submitPoliceNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.editable_police_number);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.police_number), (editText.getText().toString()));
        editor.commit();
        setPolText(editText);
    }

    public void submitDoctorNumber(View view) {
        EditText editText = (EditText) findViewById(R.id.editable_doctor_number);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.doctor_number), (editText.getText().toString()));
        editor.commit();
        setDocText(editText);
    }

    public void setDocText(View view) {
        EditText editText = (EditText) findViewById(R.id.editable_doctor_number);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String num = sharedPref.getString(getString(R.string.doctor_number), "102");
        editText.setText(num);
    }

    public void setPolText(View view) {
        EditText editText = (EditText) findViewById(R.id.editable_police_number);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String num = sharedPref.getString(getString(R.string.police_number), "100");
        editText.setText(num);
    }

    public void setFamText(View view) {
        EditText editText = (EditText) findViewById(R.id.editable_family_number);
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String num = sharedPref.getString(getString(R.string.family_number), "0000000000");
        editText.setText(num);
    }

    public String retLoc() {
        GPSTracker gpsTracker = new GPSTracker(this);
        String stringLongitude = "";
        String stringLatitude = "";
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            stringLatitude = String.valueOf(12.910254);

            stringLongitude = String.valueOf(80.2229224);

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
        return "http://maps.google.com/maps?saddr=" + stringLatitude + "," + stringLongitude;
    }

    public void sendLocationAsMessage() {
        SharedPreferences sharedPref = StartActivity.this.getPreferences(Context.MODE_PRIVATE);
        String famNum = sharedPref.getString(getString(R.string.family_number), "0000000000");

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(famNum, null, retLoc(), null, null);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                    callDetonateScreen();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}

