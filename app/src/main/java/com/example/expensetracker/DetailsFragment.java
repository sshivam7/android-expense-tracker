package com.example.expensetracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.List;


/*
 * DetailsFragment shows expense and revenue graphs categorized by entry frequency.
 *
 * Corresponding Layout: fragment_details.xml
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */
public class DetailsFragment extends Fragment {

    private static double[] expenses = {0, 0, 0, 0, 0}, revenue = {0, 0, 0, 0, 0};

    public DetailsFragment() {
        // Required empty public constructor
    }

    /*
     * Method runs on fragment creation and is responsible for setting up the default state of the
     * fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Get view model
        BudgetEntryViewModel mBudgetEntryViewModel = ViewModelProviders.of(requireActivity())
                .get(BudgetEntryViewModel.class);

        // Observer for live data
        mBudgetEntryViewModel.getAllBudgetEntries().observe(getViewLifecycleOwner(),
                new Observer<List<BudgetEntry>>() {
            @Override
            public void onChanged(List<BudgetEntry> budgetEntries) {
                new updateChartsAsyncTask(view).execute(budgetEntries);
            }
        });


        return view;
    }

    /*
     * updateChartsAsyncTask class is responsible for loading the data and updating the contents of
     * the fragment
     *
     * Author: Shivam Sood
     * Date: 2020-06-20
     */
    private static class updateChartsAsyncTask extends AsyncTask<List<BudgetEntry>,
            Void, Void> implements GraphUpdater {

        private WeakReference<View> mView;

        // Constructor to get view
        private updateChartsAsyncTask(View view) {
            this.mView = new WeakReference<>(view);
        }

        /*
         * Method to categorize data by entry frequency and store it in the appropriate array.
         * Run on another thread.
         */
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<BudgetEntry>... lists) {
            for (int i = 0; i < 5; i++) {
                expenses[i] = 0;
                revenue[i] = 0;
            }

            if (!lists[0].isEmpty()) {
                // loop through array and get entry value
                for (int i = 0; i < lists[0].size(); i++) {
                    categorizeEntryValue(lists[0].get(i));
                }
            }
            return null;
        }

        /*
         * Method to update the contents of the fragment after the data has been categorized in
         * the doInBackground method
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Get currency format
            NumberFormat nf = NumberFormat.getCurrencyInstance();

            // *** Get progress bars and text Views *** \\

            ProgressBar[] expenseBar = new ProgressBar[5];
            ProgressBar[] revenueBar = new ProgressBar[5];

            TextView[] expenseTextView = new TextView[5];
            TextView[] revenueTextView = new TextView[5];

            // Get daily expense and revenue progress bars
            expenseBar[BudgetEntry.DAILY] = mView.get().findViewById(R.id.daily_expense_bar);
            revenueBar[BudgetEntry.DAILY] = mView.get().findViewById(R.id.daily_revenue_bar);

            // Get daily expense and revenue TextViews
            expenseTextView[BudgetEntry.DAILY] = mView.get().findViewById(R.id.daily_expense_text);
            revenueTextView[BudgetEntry.DAILY] = mView.get().findViewById(R.id.daily_revenue_text);

            // Get weekly expense and revenue progress bars
            expenseBar[BudgetEntry.WEEKLY] = mView.get().findViewById(R.id.weekly_expense_bar);
            revenueBar[BudgetEntry.WEEKLY] = mView.get().findViewById(R.id.weekly_revenue_bar);

            // Get weekly expense and revenue TextViews
            expenseTextView[BudgetEntry.WEEKLY] = mView.get().findViewById(R.id.weekly_expense_text);
            revenueTextView[BudgetEntry.WEEKLY] = mView.get().findViewById(R.id.weekly_revenue_text);

            // Get monthly expense and revenue progress bars
            expenseBar[BudgetEntry.MONTHLY] = mView.get().findViewById(R.id.monthly_expense_bar);
            revenueBar[BudgetEntry.MONTHLY] = mView.get().findViewById(R.id.monthly_revenue_bar);

            // Get monthly expense and revenue TextViews
            expenseTextView[BudgetEntry.MONTHLY] = mView.get().findViewById(R.id.monthly_expense_text);
            revenueTextView[BudgetEntry.MONTHLY] = mView.get().findViewById(R.id.monthly_revenue_text);

            // Get yearly expense and revenue progress bars
            expenseBar[BudgetEntry.YEARLY] = mView.get().findViewById(R.id.yearly_expense_bar);
            revenueBar[BudgetEntry.YEARLY] = mView.get().findViewById(R.id.yearly_revenue_bar);

            // Get monthly expense and revenue TextViews
            expenseTextView[BudgetEntry.YEARLY] = mView.get().findViewById(R.id.yearly_expense_text);
            revenueTextView[BudgetEntry.YEARLY] = mView.get().findViewById(R.id.yearly_revenue_text);

            // Get one time expense and revenue progress bars
            expenseBar[BudgetEntry.ONE_TIME] = mView.get().findViewById(R.id.oneTime_expense_bar);
            revenueBar[BudgetEntry.ONE_TIME] = mView.get().findViewById(R.id.oneTime_revenue_bar);

            // Get monthly expense and revenue TextViews
            expenseTextView[BudgetEntry.ONE_TIME] = mView.get().findViewById(R.id.oneTime_expense_text);
            revenueTextView[BudgetEntry.ONE_TIME] = mView.get().findViewById(R.id.oneTime_revenue_text);

            // **** Update charts and text views **** \\

            // expense daily chart
            updateGraph(expenseBar[BudgetEntry.DAILY], (int) getPercent(expenses[BudgetEntry.DAILY],
                    expenses[BudgetEntry.DAILY] + revenue[BudgetEntry.DAILY]));
            // revenue daily chart
            updateGraph(revenueBar[BudgetEntry.DAILY], (int) getPercent(revenue[BudgetEntry.DAILY],
                    expenses[BudgetEntry.DAILY] + revenue[BudgetEntry.DAILY]));

            // expense and revenue daily TextView
            expenseTextView[BudgetEntry.DAILY].setText(nf.format(expenses[BudgetEntry.DAILY]));
            revenueTextView[BudgetEntry.DAILY].setText(nf.format(revenue[BudgetEntry.DAILY]));

            // expense weekly chart
            updateGraph(expenseBar[BudgetEntry.WEEKLY], (int) getPercent(expenses[BudgetEntry.WEEKLY],
                    expenses[BudgetEntry.WEEKLY] + revenue[BudgetEntry.WEEKLY]));
            // revenue weekly chart
            updateGraph(revenueBar[BudgetEntry.WEEKLY], (int) getPercent(revenue[BudgetEntry.WEEKLY],
                    expenses[BudgetEntry.WEEKLY] + revenue[BudgetEntry.WEEKLY]));

            // expense and revenue weekly TextView
            expenseTextView[BudgetEntry.WEEKLY].setText(nf.format(expenses[BudgetEntry.WEEKLY]));
            revenueTextView[BudgetEntry.WEEKLY].setText(nf.format(revenue[BudgetEntry.WEEKLY]));

            // expense monthly chart
            updateGraph(expenseBar[BudgetEntry.MONTHLY], (int) getPercent(expenses[BudgetEntry.MONTHLY],
                    expenses[BudgetEntry.MONTHLY] + revenue[BudgetEntry.MONTHLY]));
            // revenue monthly chart
            updateGraph(revenueBar[BudgetEntry.MONTHLY], (int) getPercent(revenue[BudgetEntry.MONTHLY],
                    expenses[BudgetEntry.MONTHLY] + revenue[BudgetEntry.MONTHLY]));

            // expense and revenue monthly TextView
            expenseTextView[BudgetEntry.MONTHLY].setText(nf.format(expenses[BudgetEntry.MONTHLY]));
            revenueTextView[BudgetEntry.MONTHLY].setText(nf.format(revenue[BudgetEntry.MONTHLY]));

            // expense yearly chart
            updateGraph(expenseBar[BudgetEntry.YEARLY], (int) getPercent(expenses[BudgetEntry.YEARLY],
                    expenses[BudgetEntry.YEARLY] + revenue[BudgetEntry.YEARLY]));
            // revenue yearly chart
            updateGraph(revenueBar[BudgetEntry.YEARLY], (int) getPercent(revenue[BudgetEntry.YEARLY],
                    expenses[BudgetEntry.YEARLY] + revenue[BudgetEntry.YEARLY]));

            // expense and revenue yearly TextView
            expenseTextView[BudgetEntry.YEARLY].setText(nf.format(expenses[BudgetEntry.YEARLY]));
            revenueTextView[BudgetEntry.YEARLY].setText(nf.format(revenue[BudgetEntry.YEARLY]));

            // expense one time chart
            updateGraph(expenseBar[BudgetEntry.ONE_TIME], (int) getPercent(expenses[BudgetEntry.ONE_TIME],
                    expenses[BudgetEntry.ONE_TIME] + revenue[BudgetEntry.ONE_TIME]));
            // revenue one time chart
            updateGraph(revenueBar[BudgetEntry.ONE_TIME], (int) getPercent(revenue[BudgetEntry.ONE_TIME],
                    expenses[BudgetEntry.ONE_TIME] + revenue[BudgetEntry.ONE_TIME]));

            // expense and revenue one time TextView
            expenseTextView[BudgetEntry.ONE_TIME].setText(nf.format(expenses[BudgetEntry.ONE_TIME]));
            revenueTextView[BudgetEntry.ONE_TIME].setText(nf.format(revenue[BudgetEntry.ONE_TIME]));
        }

        /*
         * Method to categorize entry values by frequency
         */
        private void categorizeEntryValue(BudgetEntry entry) {
            // Add the entry to the corresponding array with the corresponding index depending
            // on the entryFrequency
            if (entry.getIsExpense()) {
                expenses[entry.getEntryFrequency()] += entry.getEntryValue();
            } else {
                revenue[entry.getEntryFrequency()] += entry.getEntryValue();
            }
        }

        // Method to get percentage
        @Override
        public double getPercent(double percentOf, double total) {
            return (int) ((percentOf / total) * 100);
        }

        // Method to update the graphs
        @Override
        public void updateGraph(ProgressBar graph, int percentage) {
            graph.setProgress(percentage);
        }

    }
}
