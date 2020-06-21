package com.example.expensetracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/*
 * AboutActivity displays information about the app. (Information text stored in strings.xml)
 *
 * Corresponding Layout: activity_about.xml
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
