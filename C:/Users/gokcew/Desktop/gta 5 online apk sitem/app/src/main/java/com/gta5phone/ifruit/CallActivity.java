package com.gta5phone.ifruit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class CallActivity extends AppCompatActivity {

    private TextView tvCallStatus, tvCallTimer, tvCallName, tvCallNumber, tvCallInitial;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int callSeconds = 0;
    private boolean isConnected = false;
    private Runnable timerRunnable;

    // GTA-style call pickup delay (2-4 seconds)
    private static final int PICKUP_DELAY_MS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_call);

        String name = getIntent().getStringExtra("contact_name");
        String number = getIntent().getStringExtra("contact_number");
        if (name == null) name = "Unknown";
        if (number == null) number = "---";

        tvCallName = findViewById(R.id.tvCallName);
        tvCallNumber = findViewById(R.id.tvCallNumber);
        tvCallInitial = findViewById(R.id.tvCallInitial);
        tvCallStatus = findViewById(R.id.tvCallStatus);
        tvCallTimer = findViewById(R.id.tvCallTimer);
        TextView endBtn = findViewById(R.id.endCallBtn);

        tvCallName.setText(name);
        tvCallNumber.setText(number);
        tvCallInitial.setText(String.valueOf(name.charAt(0)).toUpperCase());

        // Simulate "calling..." then "connected" after delay
        handler.postDelayed(() -> {
            tvCallStatus.setText("CONNECTED");
            tvCallTimer.setVisibility(View.VISIBLE);
            isConnected = true;
            startTimer();
        }, PICKUP_DELAY_MS);

        endBtn.setOnClickListener(v -> endCall());
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                callSeconds++;
                int min = callSeconds / 60;
                int sec = callSeconds % 60;
                tvCallTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timerRunnable);
    }

    private void endCall() {
        if (timerRunnable != null) handler.removeCallbacks(timerRunnable);
        handler.removeCallbacksAndMessages(null);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        endCall();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerRunnable != null) handler.removeCallbacks(timerRunnable);
        handler.removeCallbacksAndMessages(null);
    }
}
