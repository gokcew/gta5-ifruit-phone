package com.gta5phone.ifruit;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RadioActivity extends AppCompatActivity {

    // GTA Online Radio Stations
    private static final String[][] STATIONS = {
        {"Los Santos Rock Radio",   "107.7 FM", "📻", "Don't Stop Me Now - Queen"},
        {"Non-Stop-Pop FM",         "100.7 FM", "🎵", "Pop Muzik - M"},
        {"Radio Los Santos",        "106.3 FM", "🎤", "Hood Gone Love It - Jay Rock"},
        {"Blaine County Radio",     "99.3 FM",  "🤠", "Pickup Man - Joe Diffie"},
        {"West Coast Classics",     "103.5 FM", "🏆", "Regulate - Warren G"},
        {"Soulwax FM",              "105.1 FM", "🎧", "Bounce - Soulwax"},
        {"East Los FM",             "104.5 FM", "🌶", "Mi Gente - J Balvin"},
        {"Rebel Radio",             "98.5 FM",  "🤘", "Still Alive - J. Tex"},
        {"Vinewood Boulevard Radio","98.3 FM",  "🎸", "Undead Nightmare - Kreayshawn"},
        {"The Lab",                 "101.3 FM", "🥼", "Fresh Prince (Beat) - Wavves"},
        {"FlyLo FM",                "101.8 FM", "🌌", "Between Friends - Flying Lotus"},
        {"The Blue Ark",            "103.0 FM", "⚓", "Three Little Birds - Bob Marley"},
        {"Self Radio",              "96.7 FM",  "📀", "Custom station"},
        {"Lowdown 91.1",            "91.1 FM",  "🎺", "Fantastic Voyage - Coolio"},
        {"Space 103.2",             "103.2 FM", "🚀", "Play That Funky Music - Wild Cherry"},
    };

    private int currentStation = 0;
    private boolean isPlaying = true;

    private TextView tvStationName, tvTrackName, tvFrequency, tvStationArt, btnPlayPause;
    private Handler progressHandler = new Handler(Looper.getMainLooper());
    private View progressFill;
    private int progressWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_radio);

        tvStationName = findViewById(R.id.tvStationName);
        tvTrackName   = findViewById(R.id.tvTrackName);
        tvFrequency   = findViewById(R.id.tvFrequency);
        tvStationArt  = findViewById(R.id.tvStationArt);
        btnPlayPause  = findViewById(R.id.btnPlayPause);
        progressFill  = findViewById(R.id.progressFill);

        updateNowPlaying();
        buildStationList();

        btnPlayPause.setOnClickListener(v -> togglePlay());
        findViewById(R.id.btnNext).setOnClickListener(v -> nextStation());
    }

    private void updateNowPlaying() {
        String[] s = STATIONS[currentStation];
        tvStationName.setText(s[0]);
        tvFrequency.setText(s[1]);
        tvStationArt.setText(s[2]);
        tvTrackName.setText(s[3]);
    }

    private void togglePlay() {
        isPlaying = !isPlaying;
        btnPlayPause.setText(isPlaying ? "⏸" : "▶");
    }

    private void nextStation() {
        currentStation = (currentStation + 1) % STATIONS.length;
        updateNowPlaying();
        buildStationList(); // refresh selection
    }

    private void buildStationList() {
        LinearLayout list = findViewById(R.id.stationsList);
        list.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < STATIONS.length; i++) {
            final int idx = i;
            String[] s = STATIONS[i];

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(56));
            row.setLayoutParams(lp);
            row.setPadding(dpToPx(16), 0, dpToPx(16), 0);
            row.setClickable(true);
            row.setFocusable(true);

            if (i == currentStation) {
                row.setBackgroundColor(Color.parseColor("#15A3FF47"));
            }

            // Station emoji
            TextView tvEmoji = new TextView(this);
            tvEmoji.setText(s[2]);
            tvEmoji.setTextSize(20);
            tvEmoji.setWidth(dpToPx(40));
            tvEmoji.setGravity(android.view.Gravity.CENTER);

            // Name + freq
            LinearLayout info = new LinearLayout(this);
            info.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams infoLp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            info.setLayoutParams(infoLp);
            info.setPadding(dpToPx(12), 0, 0, 0);

            TextView name = new TextView(this);
            name.setText(s[0]);
            name.setTextColor(i == currentStation ? Color.parseColor("#A3FF47") : Color.WHITE);
            name.setTextSize(14);
            name.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView freq = new TextView(this);
            freq.setText(s[1]);
            freq.setTextColor(Color.parseColor("#555555"));
            freq.setTextSize(11);

            info.addView(name);
            info.addView(freq);

            // Now playing indicator
            if (i == currentStation && isPlaying) {
                TextView indicator = new TextView(this);
                indicator.setText("▶");
                indicator.setTextColor(Color.parseColor("#A3FF47"));
                indicator.setTextSize(14);
                row.addView(tvEmoji);
                row.addView(info);
                row.addView(indicator);
            } else {
                row.addView(tvEmoji);
                row.addView(info);
            }

            row.setOnClickListener(v -> {
                currentStation = idx;
                isPlaying = true;
                btnPlayPause.setText("⏸");
                updateNowPlaying();
                buildStationList();
            });

            list.addView(row);

            // Divider
            View div = new View(this);
            div.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
            div.setBackgroundColor(Color.parseColor("#1AFFFFFF"));
            list.addView(div);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int)(dp * density);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
