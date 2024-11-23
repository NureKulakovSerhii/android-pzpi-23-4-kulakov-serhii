package com.example.practtask32;

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
                        TextView textView = findViewById(R.id.messageHandler1);
                        textView.setText("Текст змінився після натискання кнопки");
                    }
                }, 2000);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = findViewById(R.id.messageHandler2);
                        textView.setText("Оновлено з фонового потоку");
                    }
                });
            }
        }).start();
        Handler handler3 = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                TextView textView = findViewById(R.id.messageHandler3);
                textView.setText("Повідомлення надіслано!: " + msg.what);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = handler3.obtainMessage();
                msg.what = 1;  // Код повідомлення
                handler3.sendMessage(msg);
            }
        }).start();
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        Handler backgroundHandler = new Handler(handlerThread.getLooper());
        Handler mainHandler = new Handler(Looper.getMainLooper());

        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i <=5; i ++)
                {
                    try{
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    final int iteration = i;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView messageHandler4 = findViewById(R.id.messageHandler4);
                            messageHandler4.setText("Ітерація: " + iteration);
                        }
                    });
                }
            }
        });
    }
}