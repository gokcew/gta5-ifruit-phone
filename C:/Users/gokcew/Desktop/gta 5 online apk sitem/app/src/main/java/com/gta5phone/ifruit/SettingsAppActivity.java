package com.gta5phone.ifruit;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsAppActivity extends AppCompatActivity {

    private boolean dndOn = false;
    private boolean wantedOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_settings_app);

        TextView toggleDND    = findViewById(R.id.toggleDND);
        TextView toggleWanted = findViewById(R.id.toggleWanted);

        toggleDND.setOnClickListener(v -> {
            dndOn = !dndOn;
            toggleDND.setText(dndOn ? "ON" : "OFF");
            toggleDND.setBackgroundResource(dndOn ? R.drawable.toggle_on : R.drawable.toggle_off);
            toggleDND.setTextColor(dndOn
                ? getResources().getColor(R.color.gta_bg_dark, null)
                : getResources().getColor(R.color.gta_grey, null));
        });

        toggleWanted.setOnClickListener(v -> {
            wantedOn = !wantedOn;
            toggleWanted.setText(wantedOn ? "ON" : "OFF");
            toggleWanted.setBackgroundResource(wantedOn ? R.drawable.toggle_on : R.drawable.toggle_off);
            toggleWanted.setTextColor(wantedOn
                ? getResources().getColor(R.color.gta_bg_dark, null)
                : getResources().getColor(R.color.gta_grey, null));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
