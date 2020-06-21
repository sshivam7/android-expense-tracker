package com.example.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/*
 * TransactionFragment holds the different budget entries and allows the user to view, delete,
 * and add new entries.
 *
 * Corresponding Layout: fragment_details.xml
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */
public class TransactionFragment extends Fragment {

    private BudgetEntryViewModel mBudgetEntryViewModel;
    private static final int NEW_BUDGET_ENTRY_ACTIVITY_REQUEST_CODE = 1;

    public TransactionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        // Add RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.transaction_recyclerview);
        final BudgetEntryListAdapter adapter = new BudgetEntryListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get view model
        mBudgetEntryViewModel = ViewModelProviders.of(requireActivity()).
                get(BudgetEntryViewModel.class);

        // Observer for live data
        mBudgetEntryViewModel.getAllBudgetEntries().observe(getViewLifecycleOwner(),
                new Observer<List<BudgetEntry>>() {
                    @Override
                    public void onChanged(List<BudgetEntry> budgetEntries) {
                        // Update the cached copy of the words in the adapter
                        adapter.setBudgetEntries(budgetEntries);
                    }
                });


        // Adds floating action button used to add new budget entries
        final FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_playlist_add_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewBudgetEntryActivity.class);
                startActivityForResult(intent, NEW_BUDGET_ENTRY_ACTIVITY_REQUEST_CODE);
            }
        });

        final RelativeLayout legendLayout = view.findViewById(R.id.legend_layout);

        // Hides floating action button when the recycle view is scrolled
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    // Hide the legend if scrolled
                    legendLayout.setVisibility(View.GONE);
                    fab.hide();
                } else if (dy < 0) {
                    fab.show();
                }

                // Show the legend if scrolled to the top of the RecyclerView
                if (!recyclerView.canScrollVertically(-1)) {
                    legendLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Functionality to swipe to delete items
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        // Do Nothing
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        // Get the budget entry item to delete
                        int position = viewHolder.getAdapterPosition();
                        BudgetEntry myBudgetEntry = adapter.getBudgetEntryAtPosition(position);

                        // Delete the budget entry
                        mBudgetEntryViewModel.deleteBudgetEntry(myBudgetEntry);
                    }
                });
        helper.attachToRecyclerView(recyclerView);
        return view;
    }

    /*
     * Runs on result from NewBudgetEntryActivity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_BUDGET_ENTRY_ACTIVITY_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            // Get data from NewBudgetEntryActivity
            String name = data.getStringExtra(NewBudgetEntryActivity.EXTRA_NAME);
            boolean isExpense = data.getBooleanExtra(NewBudgetEntryActivity.EXTRA_ISEXPENSE, false);
            double value = data.getDoubleExtra(NewBudgetEntryActivity.EXTRA_VALUE, 0);
            int frequency = data.getIntExtra(NewBudgetEntryActivity.EXTRA_FREQUENCY, 0);
            int expenseType = data.getIntExtra(NewBudgetEntryActivity.EXTRA_EXPENSE_TYPE, -1);

            // Create a new budget entry and add it to the RecyclerView
            BudgetEntry entry = new BudgetEntry(name, isExpense, value, frequency, expenseType);
            mBudgetEntryViewModel.insert(entry);
        } else {
            Toast.makeText(getActivity(), R.string.add_error,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
