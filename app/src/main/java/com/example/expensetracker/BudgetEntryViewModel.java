package com.example.expensetracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
 * BudgetEntryViewModel provides data from the BudgetEntryRepository to the UI. The view model
 * is used in order to insure that the data survives configuration changes.
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public class BudgetEntryViewModel extends AndroidViewModel {

    // Private member variables for referencing the repository
    // and to store the list of budget entries
    private BudgetEntryRepository mRepository;
    private LiveData<List<BudgetEntry>> mAllBudgetEntries;

    // constructor to get reference and budget entry list from BudgetEntryRepository
    public BudgetEntryViewModel (Application application) {
        super(application);
        mRepository = new BudgetEntryRepository(application);
        mAllBudgetEntries = mRepository.getAllBudgetEntries();
    }

    // Wrapper method to return cached words as live data
    LiveData<List<BudgetEntry>> getAllBudgetEntries() {
        return mAllBudgetEntries;
    }

    // Wrapper method for the insert method
    public void insert (BudgetEntry budgetEntry) {
        mRepository.insert(budgetEntry);
    }

    // Wrapper method for the deleteAll method
    public void deleteAll() {
        mRepository.deleteAll();
    }

    // Wrapper method for the deleteBudgetEntry method
    public void deleteBudgetEntry (BudgetEntry budgetEntry) {
        mRepository.deleteBudgetEntry(budgetEntry);
    }
}
