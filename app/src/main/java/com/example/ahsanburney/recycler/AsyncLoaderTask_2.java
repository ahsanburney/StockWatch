package com.example.ahsanburney.recycler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.ahsanburney.recycler.StockContract.stockEntry.company_name;
import static com.example.ahsanburney.recycler.StockContract.stockEntry.company_symbol;


/**
 * @author Ahsan Burney
 * @version 1.0
 */

public class AsyncLoaderTask_2 extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;
    private JSONArray jsonarray = new JSONArray();

    private final String dataURL = "http://stocksearchapi.com/api/?";
    private final String yourAPIKey = "43c30be222eb8b48f8d562f23b00e189bc42b0e9";
    private static final String TAG = "AsyncCountryLoader_2";
    private String symbol;
    public AsyncLoaderTask_2(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(final String s) {
        Log.d(TAG, "doInBackground1111: " + s);

        if(s==null) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage("Data for Stock Symbol");
        builder.setTitle("Symbol Not Found : " + symbol);
        AlertDialog dialog = builder.create();
        dialog.show();
        return;
    }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this.mainActivity);
        builder1.setTitle("Make a selection");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.mainActivity, android.R.layout.simple_list_item_1);

        int count=0;
        while (count<jsonarray.length()) {
            JSONObject JO = null;
            try {
                JO = jsonarray.getJSONObject(count);
                arrayAdapter.add((JO.getString("company_name")+"-"+JO.getString("company_symbol")));

                count++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

         AlertDialog.Builder builder = builder1.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strsymbol = arrayAdapter.getItem(which).split("-")[0];
                String strcompany = arrayAdapter.getItem(which).split("-")[1];
                AsyncLoaderTask asyncLoaderTask = new AsyncLoaderTask(mainActivity);

                AlertDialog.Builder builderInner = new AlertDialog.Builder(mainActivity);
                asyncLoaderTask.execute(strsymbol,strcompany);

                builderInner.show();
            }
        });
        builder1.show();
    }




    @Override
    protected String doInBackground(String... params) {

        Uri.Builder buildURL = Uri.parse(dataURL).buildUpon();

        symbol = params[0];
        buildURL.appendQueryParameter("api_key", yourAPIKey);
        buildURL.appendQueryParameter("search_text", params[0]);
        String urlToUse = buildURL.build().toString();

        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        parseJSON(sb.toString().trim());

        return sb.toString();
    }

    private void parseJSON(String s) {

        try {

            jsonarray = new JSONArray(s);

            int count=0;
            while (count<jsonarray.length()) {
                JSONObject JO = jsonarray.getJSONObject(count);
                count++;

                String company_name=JO.getString("company_name");
                String company_symbol=JO.getString("company_symbol");

                Log.d("Details", "company_name =" + company_name + "company_symbol = " + company_symbol);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

