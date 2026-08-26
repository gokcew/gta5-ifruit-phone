package com.gta5phone.ifruit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {

    private TextView tvStatusTime, tvMoney;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer clockTimer;

    // Simulated player balance
    private long playerBalance = 2_587_430L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_home);

        tvStatusTime = findViewById(R.id.tvStatusTime);
        tvMoney = findViewById(R.id.tvMoney);

        tvMoney.setText("$" + String.format("%,d", playerBalance));

        startClock();
    }

    private void startClock() {
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    tvStatusTime.setText(fmt.format(cal.getTime()));
                });
            }
        }, 0, 1000);
    }

    // ===== APP LAUNCH METHODS (called from XML onClick) =====

    public void openContacts(View v) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    public void openMessages(View v) {
        // Messages shows same as contacts in GTA (call history / text)
        startActivity(new Intent(this, ContactsActivity.class));
    }

    public void openLifeInvader(View v) {
        startActivity(new Intent(this, LifeInvaderActivity.class));
    }

    public void openMazeBank(View v) {
        startActivity(new Intent(this, MazeBankActivity.class));
    }

    public void openEyeFind(View v) {
        startActivity(new Intent(this, EyeFindActivity.class));
    }

    public void openRadio(View v) {
        startActivity(new Intent(this, RadioActivity.class));
    }

    public void openCamera(View v) {
        // Camera - shows a placeholder in GTA style
        startActivity(new Intent(this, EyeFindActivity.class));
    }

    public void openSettings(View v) {
        startActivity(new Intent(this, SettingsAppActivity.class));
    }

    public void openDialer(View v) {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        // Back from home → go back to lock screen
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clockTimer != null) clockTimer.cancel();
    }
}
