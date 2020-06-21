package com.example.expensetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.List;


/*
 * OverviewFragment shows an overview of total expenses and revenue on a yearly basis. The
 * fragment also displays how expenses are being used by percentage.
 *
 * Corresponding Layout: fragment_overview.xml
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */
public class OverviewFragment extends Fragment {

    private boolean expenseBtnLastClick = false;
    private LinearLayout percentInfo;

    public OverviewFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_overview, container, false);

        percentInfo = view.findViewById(R.id.expense_percent_info);
        percentInfo.setVisibility(View.GONE);

        // Get view model
        BudgetEntryViewModel mBudgetEntryViewModel = ViewModelProviders.of(requireActivity())
                .get(BudgetEntryViewModel.class);

        // Observer for live data
        mBudgetEntryViewModel.getAllBudgetEntries().observe(getViewLifecycleOwner(),
                new Observer<List<BudgetEntry>>() {
            @Override
            public void onChanged(List<BudgetEntry> budgetEntries) {
                new updateAsyncTask(view, getContext()).execute(budgetEntries);
            }
        });

        // Gets expense summary button and sets onClickListener
        Button btnPercentInfoExpand = view.findViewById(R.id.expense_summary_button);
        btnPercentInfoExpand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // if the button was already clicked turn the percentInfo layout invisible
                // (the layout used to display how expenses are divided)
                if (expenseBtnLastClick) {
                    percentInfo.setVisibility(View.GONE);
                } else { // Otherwise make the layout visible
                    percentInfo.setVisibility(View.VISIBLE);
                }
                // Change the state of the boolean variable
                expenseBtnLastClick = !expenseBtnLastClick;
            }
        });

        return view;
    }

    /*
     * updateAsyncTask class is responsible for loading the data and updating the contents of
     * the fragment
     *
     * Author: Shivam Sood
     * Date: 2020-06-20
     */
    private static class updateAsyncTask extends AsyncTask<List<BudgetEntry>,
            Void, Void> implements GraphUpdater {

        private WeakReference<View> mView;
        private WeakReference<Context> mContextRef;
        private double totalExpenses, totalRevenue, totalProfit;
        private double rentPercent, foodPercent, billsPercent, shoppingPercent, otherPercent;

        // Constructor to get View
        private updateAsyncTask(View view, Context context) {
            this.mView = new WeakReference<>(view);
            this.mContextRef = new WeakReference<>(context);
        }

        /*
         * Method to get and calculate data on a separate thread
         */
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<BudgetEntry>... lists) {
            // Reset variables to 0
            totalRevenue = 0;
            totalExpenses = 0;
            totalProfit = 0;
            if (!lists[0].isEmpty()) {
                // loop through array and get entry value
                for (int i = 0; i < lists[0].size(); i++) {
                    double entryValue = calculateYearlyEntryValue(lists[0].get(i));

                    // if the budget entry is an expense add it to total expenses
                    // otherwise add it to total revenue
                    if (lists[0].get(i).getIsExpense()) {
                        totalExpenses += entryValue;
                    } else {
                        totalRevenue += entryValue;
                    }
                }
                calculateExpenseTypePercent(lists[0]);
            }
            return null;
        }

        /*
         * Method to update the fragment after the doInBackground method has finished
         * obtaining the data
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            totalProfit = totalRevenue - totalExpenses;

            // Get text views for total expense and total revenue
            TextView totalExpenseView = mView.get().findViewById(R.id.total_expense_textView);
            TextView totalRevenueView = mView.get().findViewById(R.id.total_revenue_textView);

            // Format to currency standard
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            // Format to percent standard
            NumberFormat pf = NumberFormat.getPercentInstance();

            // set total revenue and expense values for the TextViews
            totalExpenseView.setText(String.format(mContextRef.get()
                    .getString(R.string.expenses_total_text), nf.format(totalExpenses)));
            totalRevenueView.setText(String.format(mContextRef.get()
                    .getString(R.string.revenue_total_text), nf.format(totalRevenue)));

            // Get text view from center of pie chart and set text to totalProfit
            TextView profitTextView = mView.get().findViewById(R.id.profit_textview);
            profitTextView.setText(nf.format(totalProfit));

            // Get pieChart progress bar and update it
            ProgressBar pieChart = mView.get().findViewById(R.id.revenue_progressbar);
            updateGraph(pieChart, (int) (getPercent(totalRevenue, totalRevenue + totalExpenses)
                    * 100));

            // Set expense summary values
            TextView txtRentPercent = mView.get().findViewById(R.id.rent_percentage);
            txtRentPercent.setText(String.format(mContextRef.get()
                    .getString(R.string.rent_expense_percent), pf.format(rentPercent)));

            TextView txtFoodPercent = mView.get().findViewById(R.id.food_percentage);
            txtFoodPercent.setText(String.format(mContextRef.get()
                    .getString(R.string.food_expense_percent), pf.format(foodPercent)));

            TextView txtBillsPercent = mView.get().findViewById(R.id.bills_percentage);
            txtBillsPercent.setText(String.format(mContextRef.get()
                    .getString(R.string.bills_expense_percent), pf.format(billsPercent)));

            TextView txtShoppingPercent = mView.get().findViewById(R.id.shopping_percentage);
            txtShoppingPercent.setText(String.format(mContextRef.get()
                    .getString(R.string.shopping_expense_percent), pf.format(shoppingPercent)));

            TextView txtOtherPercent = mView.get().findViewById(R.id.other_percentage);
            txtOtherPercent.setText(String.format(mContextRef.get()
                    .getString(R.string.other_expense_percent), pf.format(otherPercent)));

        }

        // Method to get percentage
        @Override
        public double getPercent(double percentOf, double total) {
            return (percentOf / total);
        }

        // Method to update pieChart graph and create graph animation
        @Override
        public void updateGraph(final ProgressBar graph, final int percentage) {
            // Convert percentage into time value
            int time = percentage * (PROGRESS_CYCLE_TIME);

            // Create a new count down timer for graph animation
            new CountDownTimer(time, PROGRESS_CYCLE_TIME) {
                int counter = 0;

                /*
                 * Method to update the progress by 1 every time the PROGRESS_CYCLE_TIME is reached
                 */
                @Override
                public void onTick(long millisUntilFinished) {
                    counter++;
                    graph.setProgress(counter);
                }

                /*
                 * Method to run on finish
                 */
                @Override
                public void onFinish() {
                    counter = 0;
                    // Update graph to make up for possible skipped frames
                    graph.setProgress(percentage);
                }
            }.start();
        }

        /*
         * Method to get percentage values of the different expense types
         */
        private void calculateExpenseTypePercent(List<BudgetEntry> entries) {
            // Variables to hold expense totals for the different expense types
            double totalRentExpense = 0, totalFoodExpense = 0, totalBillsExpense = 0,
                    totalShoppingExpense = 0, totalOtherExpense = 0;

            // Loop through entries and add to respective totals
            for (int i = 0; i < entries.size(); i++) {
                switch (entries.get(i).getEntryExpenseType()) {
                    case BudgetEntry.RENT_EXPENSE:
                        totalRentExpense += calculateYearlyEntryValue(entries.get(i));
                        break;
                    case BudgetEntry.FOOD_EXPENSE:
                        totalFoodExpense += calculateYearlyEntryValue(entries.get(i));
                        break;
                    case BudgetEntry.BILLS_EXPENSE:
                        totalBillsExpense += calculateYearlyEntryValue(entries.get(i));
                        break;
                    case BudgetEntry.SHOPPING_EXPENSE:
                        totalShoppingExpense += calculateYearlyEntryValue(entries.get(i));
                        break;
                    case BudgetEntry.OTHER_EXPENSE:
                        totalOtherExpense += calculateYearlyEntryValue(entries.get(i));
                        break;
                    default:
                        // Do nothing
                        break;
                }
            }

            // Get percentage values from the expense type totals
            rentPercent = getPercent(totalRentExpense, totalExpenses);
            foodPercent = getPercent(totalFoodExpense, totalExpenses);
            billsPercent = getPercent(totalBillsExpense, totalExpenses);
            shoppingPercent = getPercent(totalShoppingExpense, totalExpenses);
            otherPercent = getPercent(totalOtherExpense, totalExpenses);
        }

        // Calculate the value of the budget entry for the entire year
        private double calculateYearlyEntryValue(BudgetEntry entry) {
            // Get entry value
            double value = entry.getEntryValue();

            switch (entry.getEntryFrequency()) {
                case BudgetEntry.DAILY:
                    // if the entry is of frequency daily multiply by the number of days
                    // in the year to calculate the yearly cost
                    value *= BudgetEntry.DAYS_IN_YEAR;
                    break;
                case BudgetEntry.WEEKLY:
                    // if entry is of frequency weekly multiple by number of weeks in year
                    value *= BudgetEntry.WEEKS_IN_YEAR;
                    break;
                case BudgetEntry.MONTHLY:
                    // if entry is of type monthly multiple by number of months in year
                    value *= BudgetEntry.MONTHS_IN_YEAR;
                    break;
                default:
                    // Do nothing. BudgetEntry.YEARLY and BudgetEntry.ONE_TIME do not result in a
                    // change and so they were not included
                    break;
            }

            // return final value
            return value;
        }
    }
}

