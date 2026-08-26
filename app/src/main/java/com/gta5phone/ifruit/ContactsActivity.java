package com.gta5phone.ifruit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactsActivity extends AppCompatActivity {

    // Full GTA Online contacts list
    private static final String[][] CONTACTS = {
        // {Name, Number, Initial, GroupColor}
        {"Lester Crest",       "(555) 372-3767", "L", "#A3FF47"},
        {"Ron Jakowski",       "(555) 455-0399", "R", "#00B4FF"},
        {"Trevor Philips",     "(555) 279-5733", "T", "#FF6B00"},
        {"Martin Madrazo",     "(555) 845-2647", "M", "#FF2020"},
        {"Agent 14",           "(555) 814-9305", "A", "#888888"},
        {"Simeon Yetarian",    "(555) 845-3688", "S", "#FFD700"},
        {"Gerald",             "(555) 845-7829", "G", "#A3FF47"},
        {"Lamar Davis",        "(555) 845-3382", "L", "#FF6B00"},
        {"Brucie Kibbutz",     "(555) 845-5500", "B", "#00B4FF"},
        {"Merryweather",       "(555) 845-9528", "M", "#888888"},
        {"Mors Mutual Ins.",   "(555) 845-9473", "M", "#1565C0"},
        {"Pegasus",            "(555) 845-0912", "P", "#A3FF47"},
        {"Rockford Hills PD",  "(555) 845-6800", "P", "#1565C0"},
        {"LS Customs",         "(555) 845-2040", "L", "#FF6B00"},
        {"Maze Bank",          "(555) 845-2345", "M", "#1565C0"},
        {"Ammunation",         "(555) 845-9400", "A", "#FF2020"},
        {"SecuroServ",         "(555) 845-7711", "S", "#FFD700"},
        {"VIP/CEO",            "(555) 845-2020", "V", "#A3FF47"},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_contacts);

        // Update contact count
        TextView tvCount = findViewById(R.id.tvContactCount);
        tvCount.setText(CONTACTS.length + " contacts");

        LinearLayout contactsList = findViewById(R.id.contactsList);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Current letter group header tracker
        char lastLetter = 0;

        for (String[] contact : CONTACTS) {
            String name = contact[0];
            String number = contact[1];
            String initial = contact[2];
            String color = contact[3];

            // Add alphabet group header if letter changed
            char firstLetter = name.charAt(0);
            if (firstLetter != lastLetter) {
                lastLetter = firstLetter;
                View header = inflater.inflate(R.layout.item_contact_header, contactsList, false);
                ((TextView) header.findViewById(R.id.tvHeader)).setText(String.valueOf(firstLetter));
                contactsList.addView(header);
            }

            // Add contact row
            View row = inflater.inflate(R.layout.item_contact, contactsList, false);
            TextView tvInitial = row.findViewById(R.id.tvInitial);
            TextView tvName = row.findViewById(R.id.tvName);
            TextView tvNumber = row.findViewById(R.id.tvNumber);
            TextView btnCall = row.findViewById(R.id.btnCallContact);

            tvInitial.setText(initial);
            tvInitial.setTextColor(Color.parseColor(color));
            tvName.setText(name);
            tvNumber.setText(number);

            final String contactName = name;
            final String contactNumber = number;

            // Row tap → call screen
            row.setOnClickListener(v -> openCallScreen(contactName, contactNumber));
            btnCall.setOnClickListener(v -> openCallScreen(contactName, contactNumber));

            // Add divider
            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(Color.parseColor("#1AFFFFFF"));

            contactsList.addView(row);
            contactsList.addView(divider);
        }
    }

    private void openCallScreen(String name, String number) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra("contact_name", name);
        intent.putExtra("contact_number", number);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
