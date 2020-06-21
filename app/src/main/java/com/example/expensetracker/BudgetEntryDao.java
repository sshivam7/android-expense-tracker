package com.example.expensetracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/*
 * BudgetEntryDao contains the methods used to access the Room Database
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

@Dao
public interface BudgetEntryDao {
    // Method to insert new Budget Entry
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(BudgetEntry budgetEntry);

    // Method to delete a specific budget entry
    @Delete
    void deleteBudgetEntry(BudgetEntry budgetEntry);

    // Method to delete all budget entries
    @Query("DELETE FROM budgetEntry_table")
    void deleteAll();

    // Method to get a single budget entry
    @Query("SELECT * from budgetEntry_table LIMIT 1")
    BudgetEntry[] getAnyBudgetEntry();

    // Methof to get all budget entries
    @Query("SELECT * from budgetEntry_table ORDER BY entry_frequency ASC")
    LiveData<List<BudgetEntry>> getAllBudgetEntries();

}
