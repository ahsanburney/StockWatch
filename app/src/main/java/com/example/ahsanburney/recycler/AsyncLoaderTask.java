package com.example.ahsanburney.recycler;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class AsyncLoaderTask extends AsyncTask<String, Void, String> {


    private MainActivity mainActivity;
    private String companyName ="";


    String json_url = "http://finance.google.com/finance/info?client=ig&q=";

    public AsyncLoaderTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {

        Uri dataUri = Uri.parse(json_url+params[1]);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse+params[0]+params[1]);
        companyName = params[0];
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

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }


    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> companyList = new ArrayList<>();
        try {
            JSONArray jObjMain = new JSONArray(s);
             int count = jObjMain.length();


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCompany = (JSONObject) jObjMain.get(i);
                String cSymbol = jCompany.getString("t");
                double cPrice = Double.parseDouble(jCompany.getString("l"));
                double cChange = Double.parseDouble(jCompany.getString("c"));
                double cPercent = Double.parseDouble(jCompany.getString("cp"));

                companyList.add(
                        new Stock("burney",cSymbol,cPrice,cChange,cPercent));
            }
            return companyList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null)
         {
            ArrayList<Stock> companyList = parseJSON(s.substring(3));
            companyList.get(0).setCompany_name(companyName);
            mainActivity.updateStoke(companyList);
        }
    }

}










