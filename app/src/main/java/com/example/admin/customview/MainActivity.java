package com.example.admin.customview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.customview.view.CustomView;

public class MainActivity extends AppCompatActivity {
    private CustomView customView;
    private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customView = (CustomView) findViewById(R.id.customView);
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0 ; i < 100 ; i++) {
                        progress = i;
                        sleep(1000);
                        handler.obtainMessage().sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            customView.setProgress(progress);
        }
    };
}
