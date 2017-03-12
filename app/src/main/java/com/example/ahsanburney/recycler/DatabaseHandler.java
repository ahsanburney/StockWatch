package com.example.ahsanburney.recycler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StockAppDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHandler";
    public static final String TABLE_NAME = "StockWatchTable";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + StockContract.stockEntry.TABLE_NAME + " (" +
                    StockContract.stockEntry.company_symbol + " TEXT not null unique," +
                    StockContract.stockEntry.company_name + " TEXT not null)";

    private static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + StockContract.stockEntry.TABLE_NAME + ";";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseHandler: DataBase Created...");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Making New DB");
        db.execSQL(SQL_CREATE_TABLE);
        Log.d(TAG, "DatabaseHandler: Table is Created ...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        Log.d(TAG, "DatabaseHandler: DataBase Updated ...");

    }

    public void putInformation(String company_name,String company_symbol,SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(StockContract.stockEntry.company_symbol, company_symbol);
        values.put(StockContract.stockEntry.company_name, company_name);

        long key = db.insert(StockContract.stockEntry.TABLE_NAME, null,values);
        Log.d(TAG, "addCountry: " + key);
        Log.d(TAG, "Database operations: one row inserted" + key);

    }

    public void deleteCountry(String symbol, SQLiteDatabase db) {
        Log.d(TAG, "deleteCountry: " + symbol+"  "+StockContract.stockEntry.company_symbol);
        int cnt = db.delete(TABLE_NAME, StockContract.stockEntry.company_name + " = ?", new String[]{symbol});
        //int cnt = db.delete(StockContract.stockEntry.TABLE_NAME, StockContract.stockEntry.company_symbol + " = ?", new String[]{symbol});
        Log.d(TAG, "deleteCountry: " + cnt);

    }

    public Cursor getInformation(SQLiteDatabase db) {
        String[] projections = { StockContract.stockEntry.company_symbol,StockContract.stockEntry.company_name};
        Cursor cursor = db.query(StockContract.stockEntry.TABLE_NAME, projections, null, null, null, null, null);
        return cursor;
    }

}
