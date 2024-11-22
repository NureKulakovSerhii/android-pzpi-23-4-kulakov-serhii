package com.example.pt42;

import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope, magnetometer, activeSensor;
    private TextView textX, textY, textZ;
    private Button buttonAccelrometer, buttonGyroscope, buttonMagnetometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textX = findViewById(R.id.textX);
        textY = findViewById(R.id.textY);
        textZ = findViewById(R.id.textZ);
        buttonAccelrometer = findViewById(R.id.buttonAccelerometer);
        buttonGyroscope = findViewById(R.id.buttonGyroscope);
        buttonMagnetometer = findViewById(R.id.buttonMagnetometer);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        buttonAccelrometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveSensor(accelerometer);
            }
        });

        buttonGyroscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveSensor(gyroscope);
            }
        });

        buttonMagnetometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActiveSensor(magnetometer);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        textX.setText("X: " + x);
        textY.setText("Y: " + y);
        textZ.setText("Z: " + z);
    }

    private void setActiveSensor(Sensor sensor) {
        sensorManager.unregisterListener(this);
        activeSensor = sensor;
        if (activeSensor != null) {
            sensorManager.registerListener(this, activeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}