package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * The BudgetEntry object is used to describe an expense or income transaction.
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

@Entity(tableName = "budgetEntry_table")
public class BudgetEntry {

    // Constants to describe entry frequency
    static final int DAILY = 0;
    static final int WEEKLY = 1;
    static final int MONTHLY = 2;
    static final int YEARLY = 3;
    static final int ONE_TIME = 4;

    // Constants used for conversions
    static final int DAYS_IN_YEAR = 365;
    static final int WEEKS_IN_YEAR = 52;
    static final int MONTHS_IN_YEAR = 12;

    // Constants used to describe entry expense type
    static final int RENT_EXPENSE = 0;
    static final int FOOD_EXPENSE = 1;
    static final int BILLS_EXPENSE = 2;
    static final int SHOPPING_EXPENSE = 3;
    static final int OTHER_EXPENSE = 4;
    static final int NONE = -1;

    @NonNull
    @ColumnInfo(name = "entry_name")
    private String mEntryName;
    @ColumnInfo(name = "entry_type")
    private boolean mIsExpense;
    @ColumnInfo(name = "entry_value")
    private double mEntryValue;
    @ColumnInfo(name = "entry_frequency")
    private int mEntryFrequency;
    @ColumnInfo(name = "entry_expenseType")
    private int mEntryExpenseType;
    @ColumnInfo(name = "entry_id")
    @PrimaryKey(autoGenerate = true)
    private int entryKey;

    //Constructor to set default values
    public BudgetEntry(@NonNull String entryName, boolean isExpense, double entryValue,
                       int entryFrequency, int entryExpenseType) {
        this.mEntryName = entryName;
        this.mIsExpense = isExpense;
        this.mEntryValue = entryValue;
        this.mEntryFrequency = entryFrequency;
        this.mEntryExpenseType = entryExpenseType;
    }

    // Getters and setters for the different private variables
    public String getEntryName() {
        return this.mEntryName;
    }

    public boolean getIsExpense() {
        return this.mIsExpense;
    }

    public double getEntryValue() {
        return this.mEntryValue;
    }

    public int getEntryFrequency() {
        return this.mEntryFrequency;
    }

    public int getEntryExpenseType() {
        return this.mEntryExpenseType;
    }

    public int getEntryKey() {
        return this.entryKey;
    }

    public void setEntryKey(int key) {
        this.entryKey = key;
    }
}
