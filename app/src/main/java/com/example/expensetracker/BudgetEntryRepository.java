package com.example.expensetracker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
 * BudgetEntryRepository class is used to handle data operations. Accessing the
 * BudgetEntryRoomDatabase to get data from the local database.
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

public class BudgetEntryRepository {
    // Member variables for DAO and list of words
    private BudgetEntryDao mBudgetEntryDao;
    private LiveData<List<BudgetEntry>> mAllBudgetEntries;

    // Constructor gets database and initializes member variables
    BudgetEntryRepository(Application application) {
        BudgetEntryRoomDatabase db = BudgetEntryRoomDatabase
                .getDatabase(application);
        mBudgetEntryDao = db.budgetEntryDao();
        mAllBudgetEntries = mBudgetEntryDao.getAllBudgetEntries();
    }

    // Wrapper method to return cached words as live data
    LiveData<List<BudgetEntry>> getAllBudgetEntries() {
        return mAllBudgetEntries;
    }

    // method to invoke insert async task
    public void insert (BudgetEntry budgetEntry) {
        new insertAsyncTask(mBudgetEntryDao).execute(budgetEntry);
    }

    // method to invoke deleteAll async task
    public void deleteAll() {
        new deleteAllEntriesAsyncTask(mBudgetEntryDao).execute();
    }

    // method to invoke the deleteBudgetEntry async task
    public void deleteBudgetEntry(BudgetEntry budgetEntry) {
        new deleteWordAsyncTask(mBudgetEntryDao).execute(budgetEntry);
    }

    /*
     * insertAsyncTask class is used to insert a new budget entry using a separate thread
     *
     * Author: Shivam Sood
     * Date: 2020-06-20
     */
    private static class insertAsyncTask extends AsyncTask<BudgetEntry,
            Void, Void> {

        private BudgetEntryDao mAsyncTaskDao;

        insertAsyncTask(BudgetEntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final BudgetEntry... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /*
     * deleteAllEntriesAsyncTask class is used to delete all Budget entries using a separate
     * thread
     *
     * Author: Shivam Sood
     * Date: 2020-06-20
     */
    private static class deleteAllEntriesAsyncTask extends AsyncTask<
            Void, Void, Void> {

        private BudgetEntryDao mAsyncTaskDao;

        deleteAllEntriesAsyncTask(BudgetEntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /*
     * deleteWordAsyncTask class is used to delete a specific budget entry using a separate
     * thread
     *
     * Author: Shivam Sood
     * Date: 2020-06-20
     */
    private static class deleteWordAsyncTask extends AsyncTask<BudgetEntry,
            Void, Void> {
        private BudgetEntryDao mAsyncTaskDao;

        deleteWordAsyncTask(BudgetEntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(BudgetEntry... params) {
            mAsyncTaskDao.deleteBudgetEntry(params[0]);
            return null;
        }
    }
}
