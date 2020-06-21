package com.example.expensetracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/*
 * SettingsActivity loads the SettingsFragment
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public class SettingsActivity extends AppCompatActivity {

    // Tag for notification switch
    public static final String NOTIFICATION_PREF_SWITCH = "notification_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Loads SettingsFragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
