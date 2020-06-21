package com.example.expensetracker;

import android.widget.ProgressBar;

/*
 * Interface containing methods used for updating graphs made with progress bars
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public interface GraphUpdater {

    // Constant used to for the delay between progress bar updates (x% every 30ms).
    int PROGRESS_CYCLE_TIME = 30;

    // Method to get percentage
    double getPercent(double percentOf, double total);

    // Method to update progress bar graph
    void updateGraph(ProgressBar graph, int percentage);
}
