package com.example.expensetracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/*
 * BudgetEntryRoomDatabase class is used to create the Room database. Queries are handled through
 * a separate Dao class
 *
 * Author: Shivam Sood
 * Date: 2020-06-20
 */

// Marks class as a room database
@Database(entities = {BudgetEntry.class}, version = 4, exportSchema = false)
public abstract class BudgetEntryRoomDatabase extends RoomDatabase {

    // Defining the DAO that works with the database
    public abstract BudgetEntryDao budgetEntryDao();

    // Using singleton design pattern to avoid having multiple instances
    // of the database open at the same time
    private static BudgetEntryRoomDatabase INSTANCE;

    public static BudgetEntryRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BudgetEntryRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BudgetEntryRoomDatabase.class, "budgetEntry_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


