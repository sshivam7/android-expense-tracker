package com.example.expensetracker;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

/*
 * The main activity holds the BottomNavigation and is responsible for loading
 * the different fragments. It is also responsible for delivering notifications.
 *
 * Corresponding layout: activity_main.xml
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private BudgetEntryViewModel mBudgetEntryViewModel;
    private NotificationManager mNotificationManager;

    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    /*
     * Implementation for itemSelectedListener for bottom navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Launch fragment for selected bottomNavigation button
                    switch (item.getItemId()) {
                        case R.id.botNav_transactions:
                            toolbar.setTitle("Transactions");
                            launchFragment(new TransactionFragment());
                            return true;
                        case R.id.botNav_overview:
                            toolbar.setTitle("Overview");
                            launchFragment(new OverviewFragment());
                            return true;
                        case R.id.botNav_details:
                            toolbar.setTitle("Details");
                            launchFragment(new DetailsFragment());
                            return true;
                    }
                    return false;
                }
            };

    /*
     * Method handles loading the starting state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        // Get bottom navigation view and add itemSelectedListener
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // load the store fragment by default
        toolbar.setTitle("OverView");
        launchFragment(new OverviewFragment());

        // Get view model
        mBudgetEntryViewModel = ViewModelProviders.of(this).get(BudgetEntryViewModel.class);

        // Save default value for preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Get notification preference value
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        boolean notificationPref = sharedPreferences.getBoolean
                (SettingsActivity.NOTIFICATION_PREF_SWITCH, false);

        // Intent for alarm receiver
        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // initialize the AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to start at 6pm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);

        // Create the notification Channel
        createNotificationChannel();

        // If notification preference is turned on set a repeating alarm to ring at 6pm everyday
        if (notificationPref) {
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, notifyPendingIntent);
            }
        } else {
            // if the alarm is off cancel the notification
            mNotificationManager.cancelAll();

            if (alarmManager != null) {
                alarmManager.cancel(notifyPendingIntent);
            }
        }
    }

    /*
     * Method to create Notification channel (available in oreo and above)
     */
    public void createNotificationChannel() {
        // Create notification manager object
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Check build version (channels are only available in oreo and higher)
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create the notification channel
            NotificationChannel nChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    getString(R.string.notification_title), NotificationManager.IMPORTANCE_HIGH);

            nChannel.enableLights(true);
            nChannel.setLightColor(Color.WHITE);
            nChannel.enableVibration(true);
            nChannel.setDescription(getString(R.string.notification_description));

            mNotificationManager.createNotificationChannel(nChannel);
        }
    }

    /*
     * Method to load a fragment onto the screen
     */
    private void launchFragment(Fragment fragment) {
        // launch fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment).addToBackStack(null).commit();
    }

    /*
     * Method to create options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu to add items to the actionbar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Method to check which menu item was selected and act accordingly
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        // If the settings icon is selected launch the settings activity
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.clear_data) {
            // display toast for confirmation
            Toast.makeText(this, R.string.clear_data_toast,
                    Toast.LENGTH_SHORT).show();

            // Delete all data
            mBudgetEntryViewModel.deleteAll();
            return true;
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
