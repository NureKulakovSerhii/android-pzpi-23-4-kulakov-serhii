package com.example.pt3;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Handler handler = new Handler(Looper.getMainLooper());
        Button startHandlerButton = findViewById(R.id.startHandlerButton);
        startHandlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.handlerMessageTextView);
                        textView.setText("Обробник виконано після затримки");
                    }
                }, 2000);
            }
        });
        Handler new_handler = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.backgroundTextView);
                        textView.setText("Оновлено з фонового потоку");
                    }
                });
            }
        }).start();


        Handler message_handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                TextView handlerMessage = findViewById(R.id.handlerMessage);
                handlerMessage.setText("Повідомлення отримано: " + msg.what);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = message_handler.obtainMessage();
                msg.what = 1;
                message_handler.sendMessage(msg);
            }
        }).start();

        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper());

        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}