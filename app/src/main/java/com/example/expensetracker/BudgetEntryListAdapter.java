package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

/*
 * BudgetEntryListAdapter manages the Views used in the RecyclerView. It sets data for the views
 * as they appear in the RecyclerView.
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public class BudgetEntryListAdapter extends RecyclerView.Adapter<BudgetEntryListAdapter.BudgetEntryViewHolder> {

    private final LayoutInflater mInflator;
    private List<BudgetEntry> mBudgetEntries;
    private Context mContext;

    /*
     * Default constructor. *Data is set using a setter method.
     */
    BudgetEntryListAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        mContext = context;
    }

    /*
     * Creates new views using the recyclerview_item layout
     */
    @NonNull
    @Override
    public BudgetEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.recyclerview_item, parent, false);
        return new BudgetEntryViewHolder(itemView);
    }

    /*
     * Sets the content for the views using data from mBudgetEntries
     */
    @Override
    public void onBindViewHolder(@NonNull BudgetEntryListAdapter.BudgetEntryViewHolder holder, int position) {
        if (mBudgetEntries != null) {
            // Gets current budgetEntry and fills the name and value views with the appropriate data
            BudgetEntry current = mBudgetEntries.get(position);
            holder.budgetEntryItemNameView.setText(current.getEntryName());

            // Format double to currency
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            holder.budgetEntryItemValueView.setText(nf.format(current.getEntryValue()));

            // If the current budget entry is an expense budgetEntryItemValue text will be red
            // otherwise it will be green
            if (current.getIsExpense()) {
                holder.budgetEntryItemValueView.setTextColor(ContextCompat.getColor(mContext,
                        R.color.colorRedAccent));
            } else {
                holder.budgetEntryItemValueView.setTextColor(ContextCompat.getColor(mContext,
                        R.color.colorGreenAccent));
            }

            // Color codes the current entry depending on its frequency
            switch (current.getEntryFrequency()) {
                case BudgetEntry.DAILY: // Coloured orange
                    holder.budgetEntryItemFrequncyView.setBackgroundColor(ContextCompat.getColor(
                            mContext, R.color.dailyColor));
                    break;
                case BudgetEntry.WEEKLY: // Coloured blue
                    holder.budgetEntryItemFrequncyView.setBackgroundColor(ContextCompat.getColor(
                            mContext, R.color.weeklyColor));
                    break;
                case BudgetEntry.MONTHLY: // Coloured Yellow
                    holder.budgetEntryItemFrequncyView.setBackgroundColor(ContextCompat.getColor(
                            mContext, R.color.monthlyColor));
                    break;
                case BudgetEntry.YEARLY: // Coloured Purple
                    holder.budgetEntryItemFrequncyView.setBackgroundColor(ContextCompat.getColor(
                            mContext, R.color.yearlyColor));
                    break;
                case BudgetEntry.ONE_TIME: // Coloured Pink
                    holder.budgetEntryItemFrequncyView.setBackgroundColor(ContextCompat.getColor(
                            mContext, R.color.oneTimeColor));
                    break;
                default:
                    // DO nothing
                    break;
            }
        } else {
            // if data is not ready
            holder.budgetEntryItemNameView.setText(R.string.no_budget_entry);
        }
    }

    /*
     * Method used to set data used by the adapter. Accepts a list of Budget Entries.
     */
    void setBudgetEntries(List<BudgetEntry> budgetEntries) {
        mBudgetEntries = budgetEntries;
        notifyDataSetChanged();
    }

    /*
     * Method to get the BudgetEntry at a given position
     */
    public BudgetEntry getBudgetEntryAtPosition(int position) {
        return mBudgetEntries.get(position);
    }

    /*
     * Method to return the number of budget entries
     */
    @Override
    public int getItemCount() {
        // Returns the number of budget entries
        if (mBudgetEntries != null)
            return mBudgetEntries.size();
        else return 0;
    }

    /*
     * Inner BudgetEntryViewHolder class to get the different view components form the
     * recyclerview_item layout
     *
     * Author: Shivam Sood
     * Date: 2020-06-20
     */
    static class BudgetEntryViewHolder extends RecyclerView.ViewHolder {
        // Variables to hold the different TextViews part of the recyclerview item
        private final TextView budgetEntryItemNameView;
        private final TextView budgetEntryItemValueView;
        private final TextView budgetEntryItemFrequncyView;

        // Gets the the different TextViews
        private BudgetEntryViewHolder(View itemView) {
            super(itemView);
            budgetEntryItemNameView = itemView.findViewById(R.id.textView_name);
            budgetEntryItemValueView = itemView.findViewById(R.id.textView_value);
            budgetEntryItemFrequncyView = itemView.findViewById(R.id.textView_frequency);
        }
    }
}
