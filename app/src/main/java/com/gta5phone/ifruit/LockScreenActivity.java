package com.gta5phone.ifruit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LockScreenActivity extends AppCompatActivity {

    private TextView tvTime, tvDate, tvStatusTime;
    private FrameLayout btnUnlock;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Timer clockTimer;
    private GestureDetectorCompat gestureDetector;

    // GTA Online character names for missed call (random)
    private final String[] callers = {
        "Lester Crest", "Ron Jakowski", "Trevor Philips",
        "Martin Madrazo", "Agent 14", "Simeon Yetarian"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fullscreen - hide status bar like GTA phone
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_lock_screen);

        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        tvStatusTime = findViewById(R.id.tvStatusTime);
        btnUnlock = findViewById(R.id.btnUnlock);

        // Random missed caller
        TextView tvCaller = findViewById(R.id.tvMissedCaller);
        int idx = (int)(Math.random() * callers.length);
        tvCaller.setText(callers[idx]);

        // Start clock
        startClock();

        // Gesture detector for swipe up to unlock
        gestureDetector = new GestureDetectorCompat(this,
            new GestureDetector.SimpleOnGestureListener() {
                private static final int SWIPE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY = 100;

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                                       float velocityX, float velocityY) {
                    if (e1 == null || e2 == null) return false;
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    // Swipe UP or swipe RIGHT to unlock
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD &&
                            Math.abs(velocityX) > SWIPE_VELOCITY) {
                            if (diffX > 0) {
                                unlock();
                                return true;
                            }
                        }
                    } else {
                        if (Math.abs(diffY) > SWIPE_THRESHOLD &&
                            Math.abs(velocityY) > SWIPE_VELOCITY) {
                            if (diffY < 0) {
                                unlock();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });

        // Also allow tap on unlock button
        btnUnlock.setOnClickListener(v -> unlock());

        // Fade in animation
        View root = findViewById(R.id.lockRoot);
        root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void unlock() {
        // GTA-style flash then go to home
        View root = findViewById(R.id.lockRoot);
        root.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction(() -> {
                Intent intent = new Intent(LockScreenActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            })
            .start();
    }

    private void startClock() {
        clockTimer = new Timer();
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> updateClock());
            }
        }, 0, 1000);
    }

    private void updateClock() {
        Calendar cal = Calendar.getInstance();
        // Big clock
        SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        tvTime.setText(timeFmt.format(cal.getTime()));
        // Status bar small time
        SimpleDateFormat shortFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
        tvStatusTime.setText(shortFmt.format(cal.getTime()));
        // Date
        SimpleDateFormat dateFmt = new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH);
        tvDate.setText(dateFmt.format(cal.getTime()).toUpperCase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clockTimer != null) {
            clockTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        // GTA phone: back button from lock screen does nothing
        // (mimics the in-game phone behavior)
    }
}
