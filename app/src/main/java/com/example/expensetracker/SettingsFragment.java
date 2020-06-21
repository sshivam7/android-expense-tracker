package com.example.expensetracker;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

/*
 * Settings fragment adds preferences (notification preference) through the prefernces file
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // loads preference xml
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
