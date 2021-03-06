package com.example.poojagupta.stopwatch06;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poojagupta.stopwatch06.BoundService.MyBinder;

public class MainActivity extends AppCompatActivity {

    // declare variables
    Button startServiceBtn;
    Button stopServiceBtn;
    Button startBtn;
    Button stopBtn;
    Button resetBtn;
    TextView timer;
    Boolean boundServiceActive = false;
    ServiceConnection serviceConnection;
    BoundService boundService;
    Boolean timerStarted = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialise variables
        startServiceBtn = (Button) findViewById(R.id.startServiceBtn);
        stopServiceBtn = (Button) findViewById(R.id.stopServiceBtn);
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        timer = (TextView) findViewById(R.id.timer);
        final Handler handler = new Handler();

        // define service connection
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                try {
                    boundServiceActive = true;
                    MyBinder myBinder = (MyBinder) iBinder;
                    boundService = myBinder.getService();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                boundServiceActive = false;
            }
        };

        // start BoundService on START SERVICES button click
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    intent = new Intent(getApplicationContext(), BoundService.class);
                    if (BoundService.running) {
                        Toast.makeText(getApplicationContext(), "Service Started already", Toast.LENGTH_SHORT).show();

                    } else {
                        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // start timer on START button click
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timerStarted = true;

                if (boundServiceActive) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (timerStarted) {
                                    timer.setText(boundService.getTime());
                                    handler.postDelayed(this, 0);
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Start the services first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // stop timer on STOP button click and remove the handler
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boundServiceActive) {
                    try {
                        timerStarted = false;
                        handler.removeCallbacksAndMessages(null);
                        boundService.stopTimer();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Start the services first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // reset timer on RESET button click and remove the handler
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boundServiceActive) {
                    try {
                        timerStarted = false;
                        handler.removeCallbacksAndMessages(null);
                        timer.setText("00:00:000");
                        boundService.resetTimer();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Start the services first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // stop service on STOP SERVICES button click and remove the handler if attached
        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (boundServiceActive) {
                    try {
                        timerStarted = false;
                        handler.removeCallbacksAndMessages(null);
                        // reset the timer when service is stopped
                        resetBtn.performClick();
                        unbindService(serviceConnection);
                        boundServiceActive = false;
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}
