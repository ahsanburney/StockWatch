package com.example.ahsanburney.recycler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private StockAdapter mAdapter;
    DatabaseHandler databaseHandler,dbh;
    SQLiteDatabase sqLiteDatabase,db1;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Stock> arrayList = new ArrayList<>();
    private SwipeRefreshLayout swiper;
    private int iflag=0;


    public MainActivity() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        databaseHandler = new DatabaseHandler(this);
        sqLiteDatabase = databaseHandler.getReadableDatabase();
        dbh = new DatabaseHandler(this);
        db1 = databaseHandler.getReadableDatabase();

        arrayList.clear();
        mAdapter = new StockAdapter(arrayList, this);
        recyclerView.setAdapter(mAdapter);

        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });


        Cursor cursor = databaseHandler.getInformation(sqLiteDatabase);
        Log.d(TAG, "Stock name: Edit text Value..." + cursor);

        if (network() == 1) {
            if (cursor.moveToFirst()) {
                do {
                    AsyncLoaderTask asyncLoaderTask = new AsyncLoaderTask(this);
                    iflag++;
                    asyncLoaderTask.execute(cursor.getString(0), cursor.getString(1));


                } while (cursor.moveToNext());
            }

        }
    }

    private void doRefresh() {
        if (network() == 1) {
            arrayList.clear();

            Cursor cursor = databaseHandler.getInformation(sqLiteDatabase);

            if (cursor.moveToFirst()) {
                do {
                    AsyncLoaderTask asyncLoaderTask = new AsyncLoaderTask(this);
                    iflag++;
                    asyncLoaderTask.execute(cursor.getString(0), cursor.getString(1));

                } while (cursor.moveToNext());
            }

            swiper.setRefreshing(false);
        }
    }


    public int network() {

        ConnectivityManager connect =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connect.getActiveNetworkInfo();
        if (!(network != null && network.isConnectedOrConnecting())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Stocks Cannot be Added without a Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();
            return 0;
        } else {
            return 1;
        }
    }

    public void updateStoke(ArrayList<Stock> cList) {
        Log.d(TAG, "updateDate ahsan " + cList);
        cList.get(0).getCompany_symbol();
        cList.get(0).getCompany_name();
        if(iflag==0) {
            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            SQLiteDatabase db = databaseHandler.getWritableDatabase();
            databaseHandler.putInformation(cList.get(0).getCompany_symbol(), cList.get(0).getCompany_name(), db);
        }
        else
            iflag=iflag-1;
            int flag = 1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getCompany_symbol().equals(cList.get(0).getCompany_symbol())) {
                flag = 0;
                break;
            }

        }
        if (flag == 0) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Duplicate Stock")
                    .setMessage("Stock symbol"+" " + cList.get(0).getCompany_symbol() +" "+ "is already displayed")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } else {
            arrayList.addAll(cList);
        }

        Collections.sort(arrayList, nameComparator);
        mAdapter.notifyDataSetChanged();
    }

    public static Comparator<Stock> nameComparator = new Comparator<Stock>() {

        @Override
        public int compare(Stock companyDetail, Stock t1) {

            return companyDetail.getCompany_symbol().compareTo(t1.getCompany_symbol());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_new_note:

                if (network() == 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Stock Selection,                          Please enter a Stock symbol");
                    //Enter Stock Symbol
                    final EditText input = new EditText(this);
                    input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                    final AsyncLoaderTask alt = new AsyncLoaderTask(this);
                    //Sending Value to Asynctask-2
                    final AsyncLoaderTask_2 alt2 = new AsyncLoaderTask_2(this);
                    builder.setView(input);

                    AlertDialog.Builder builder1 = builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            String cityName = input.getText().toString().trim().toUpperCase().replaceAll(", ", ",");
                            alt2.execute(cityName);
                            Log.d(TAG, "Stock name: Edit text Value..." + cityName);

                            dialog.dismiss();

                        }


                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                    return true;}

                    default:
                        return super.onOptionsItemSelected(item);
                }
        }

    @Override
    public void onClick(View v) {
        if (network() == 1){
            String uRL = "http://www.marketwatch.com/investing/stock/";
            int pos = recyclerView.getChildLayoutPosition(v);
            Stock stock = arrayList.get(pos);
            uRL += stock.getCompany_symbol();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(uRL));
            startActivity(i);
    }

    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        clickPos(pos);
        return true;
    }

    public void clickPos(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG, "onActivityResult: yes  ");
                databaseHandler.deleteCountry(arrayList.get(pos).getCompany_symbol(),sqLiteDatabase);
                arrayList.remove(pos);
                recyclerView.setAdapter(mAdapter);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        Stock stock = arrayList.get(pos);
        builder.setTitle("Delete Stock");
        builder.setMessage("Delete Stock Symbol "+stock.getCompany_symbol()+"?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


