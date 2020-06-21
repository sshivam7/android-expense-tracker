package com.example.expensetracker;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class BudgetEntryRoomDatabase_Impl extends BudgetEntryRoomDatabase {
  private volatile BudgetEntryDao _budgetEntryDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `budgetEntry_table` (`entry_name` TEXT NOT NULL, `entry_type` INTEGER NOT NULL, `entry_value` REAL NOT NULL, `entry_frequency` INTEGER NOT NULL, `entry_expenseType` INTEGER NOT NULL, `entry_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"d21c271b7ae4a918cca5ee7ae3752516\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `budgetEntry_table`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsBudgetEntryTable = new HashMap<String, TableInfo.Column>(6);
        _columnsBudgetEntryTable.put("entry_name", new TableInfo.Column("entry_name", "TEXT", true, 0));
        _columnsBudgetEntryTable.put("entry_type", new TableInfo.Column("entry_type", "INTEGER", true, 0));
        _columnsBudgetEntryTable.put("entry_value", new TableInfo.Column("entry_value", "REAL", true, 0));
        _columnsBudgetEntryTable.put("entry_frequency", new TableInfo.Column("entry_frequency", "INTEGER", true, 0));
        _columnsBudgetEntryTable.put("entry_expenseType", new TableInfo.Column("entry_expenseType", "INTEGER", true, 0));
        _columnsBudgetEntryTable.put("entry_id", new TableInfo.Column("entry_id", "INTEGER", true, 1));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgetEntryTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBudgetEntryTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBudgetEntryTable = new TableInfo("budgetEntry_table", _columnsBudgetEntryTable, _foreignKeysBudgetEntryTable, _indicesBudgetEntryTable);
        final TableInfo _existingBudgetEntryTable = TableInfo.read(_db, "budgetEntry_table");
        if (! _infoBudgetEntryTable.equals(_existingBudgetEntryTable)) {
          throw new IllegalStateException("Migration didn't properly handle budgetEntry_table(com.example.expensetracker.BudgetEntry).\n"
                  + " Expected:\n" + _infoBudgetEntryTable + "\n"
                  + " Found:\n" + _existingBudgetEntryTable);
        }
      }
    }, "d21c271b7ae4a918cca5ee7ae3752516", "4fba38e00eeb905c6c48be1ae70f98b7");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "budgetEntry_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `budgetEntry_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public BudgetEntryDao budgetEntryDao() {
    if (_budgetEntryDao != null) {
      return _budgetEntryDao;
    } else {
      synchronized(this) {
        if(_budgetEntryDao == null) {
          _budgetEntryDao = new BudgetEntryDao_Impl(this);
        }
        return _budgetEntryDao;
      }
    }
  }
}
