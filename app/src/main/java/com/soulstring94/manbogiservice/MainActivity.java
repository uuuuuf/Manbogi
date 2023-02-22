package com.soulstring94.manbogiservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop;
    TextView txtStep;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("com.soulstring94.stepcounter.STEP_COUNT".equals(intent.getAction())) {
                int stepCount = intent.getIntExtra("count", 0);
                txtStep.setText(String.valueOf(stepCount));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMain();

        IntentFilter filter = new IntentFilter("com.soulstring94.stepcounter.STEP_COUNT");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);

        Intent serviceIntent = new Intent(this, StepCounterService.class);

        View.OnClickListener clickListener = view -> {
            switch (view.getId()) {
                case R.id.btnStart:
                    startService(serviceIntent);
                    break;
                case R.id.btnStop:
                    stopService(serviceIntent);
                    txtStep.setText("0");
                    break;
            }
        };

        btnStart.setOnClickListener(clickListener);
        btnStop.setOnClickListener(clickListener);
    }

    private void initMain() {
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        txtStep = findViewById(R.id.txtStep);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}