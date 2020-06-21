package com.example.expensetracker;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.InvalidationTracker.Observer;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class BudgetEntryDao_Impl implements BudgetEntryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBudgetEntry;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBudgetEntry;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BudgetEntryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetEntry = new EntityInsertionAdapter<BudgetEntry>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `budgetEntry_table`(`entry_name`,`entry_type`,`entry_value`,`entry_frequency`,`entry_expenseType`,`entry_id`) VALUES (?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BudgetEntry value) {
        if (value.getEntryName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getEntryName());
        }
        final int _tmp;
        _tmp = value.getIsExpense() ? 1 : 0;
        stmt.bindLong(2, _tmp);
        stmt.bindDouble(3, value.getEntryValue());
        stmt.bindLong(4, value.getEntryFrequency());
        stmt.bindLong(5, value.getEntryExpenseType());
        stmt.bindLong(6, value.getEntryKey());
      }
    };
    this.__deletionAdapterOfBudgetEntry = new EntityDeletionOrUpdateAdapter<BudgetEntry>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `budgetEntry_table` WHERE `entry_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BudgetEntry value) {
        stmt.bindLong(1, value.getEntryKey());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM budgetEntry_table";
        return _query;
      }
    };
  }

  @Override
  public void insert(BudgetEntry budgetEntry) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBudgetEntry.insert(budgetEntry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteBudgetEntry(BudgetEntry budgetEntry) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfBudgetEntry.handle(budgetEntry);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public BudgetEntry[] getAnyBudgetEntry() {
    final String _sql = "SELECT * from budgetEntry_table LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfMEntryName = _cursor.getColumnIndexOrThrow("entry_name");
      final int _cursorIndexOfMIsExpense = _cursor.getColumnIndexOrThrow("entry_type");
      final int _cursorIndexOfMEntryValue = _cursor.getColumnIndexOrThrow("entry_value");
      final int _cursorIndexOfMEntryFrequency = _cursor.getColumnIndexOrThrow("entry_frequency");
      final int _cursorIndexOfMEntryExpenseType = _cursor.getColumnIndexOrThrow("entry_expenseType");
      final int _cursorIndexOfEntryKey = _cursor.getColumnIndexOrThrow("entry_id");
      final BudgetEntry[] _result = new BudgetEntry[_cursor.getCount()];
      int _index = 0;
      while(_cursor.moveToNext()) {
        final BudgetEntry _item;
        final String _tmpMEntryName;
        _tmpMEntryName = _cursor.getString(_cursorIndexOfMEntryName);
        final boolean _tmpMIsExpense;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfMIsExpense);
        _tmpMIsExpense = _tmp != 0;
        final double _tmpMEntryValue;
        _tmpMEntryValue = _cursor.getDouble(_cursorIndexOfMEntryValue);
        final int _tmpMEntryFrequency;
        _tmpMEntryFrequency = _cursor.getInt(_cursorIndexOfMEntryFrequency);
        final int _tmpMEntryExpenseType;
        _tmpMEntryExpenseType = _cursor.getInt(_cursorIndexOfMEntryExpenseType);
        _item = new BudgetEntry(_tmpMEntryName,_tmpMIsExpense,_tmpMEntryValue,_tmpMEntryFrequency,_tmpMEntryExpenseType);
        final int _tmpEntryKey;
        _tmpEntryKey = _cursor.getInt(_cursorIndexOfEntryKey);
        _item.setEntryKey(_tmpEntryKey);
        _result[_index] = _item;
        _index ++;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<BudgetEntry>> getAllBudgetEntries() {
    final String _sql = "SELECT * from budgetEntry_table ORDER BY entry_frequency ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<BudgetEntry>>(__db.getQueryExecutor()) {
      private Observer _observer;

      @Override
      protected List<BudgetEntry> compute() {
        if (_observer == null) {
          _observer = new Observer("budgetEntry_table") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfMEntryName = _cursor.getColumnIndexOrThrow("entry_name");
          final int _cursorIndexOfMIsExpense = _cursor.getColumnIndexOrThrow("entry_type");
          final int _cursorIndexOfMEntryValue = _cursor.getColumnIndexOrThrow("entry_value");
          final int _cursorIndexOfMEntryFrequency = _cursor.getColumnIndexOrThrow("entry_frequency");
          final int _cursorIndexOfMEntryExpenseType = _cursor.getColumnIndexOrThrow("entry_expenseType");
          final int _cursorIndexOfEntryKey = _cursor.getColumnIndexOrThrow("entry_id");
          final List<BudgetEntry> _result = new ArrayList<BudgetEntry>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BudgetEntry _item;
            final String _tmpMEntryName;
            _tmpMEntryName = _cursor.getString(_cursorIndexOfMEntryName);
            final boolean _tmpMIsExpense;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfMIsExpense);
            _tmpMIsExpense = _tmp != 0;
            final double _tmpMEntryValue;
            _tmpMEntryValue = _cursor.getDouble(_cursorIndexOfMEntryValue);
            final int _tmpMEntryFrequency;
            _tmpMEntryFrequency = _cursor.getInt(_cursorIndexOfMEntryFrequency);
            final int _tmpMEntryExpenseType;
            _tmpMEntryExpenseType = _cursor.getInt(_cursorIndexOfMEntryExpenseType);
            _item = new BudgetEntry(_tmpMEntryName,_tmpMIsExpense,_tmpMEntryValue,_tmpMEntryFrequency,_tmpMEntryExpenseType);
            final int _tmpEntryKey;
            _tmpEntryKey = _cursor.getInt(_cursorIndexOfEntryKey);
            _item.setEntryKey(_tmpEntryKey);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    }.getLiveData();
  }
}
