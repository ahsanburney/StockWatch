package com.example.ahsanburney.recycler;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.RecyclerViewHolder> {
    ArrayList<Stock> arrayList = new ArrayList<>();
    private MainActivity mainAct;

    public StockAdapter(ArrayList<Stock> arrayList, MainActivity ma) {
        this.arrayList = arrayList;
        mainAct=ma;
    }

    public StockAdapter(Context c, ArrayList<Double> stocks) {

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_row, parent, false);
        view.setOnClickListener(mainAct);
        view.setOnLongClickListener(mainAct);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        String color;
        String triangle;
        Stock stock = arrayList.get(position);
        if (stock.getCp() >= 0) {
            color = "#05c622";
            triangle = "\u25B2";
        } else {
            triangle = "\u25BC";
            color = "#d80808";
        }
        holder.CompanySymbol.setTextColor(Color.parseColor(color));
        holder.CompanyName.setTextColor(Color.parseColor(color));
        holder.c.setTextColor(Color.parseColor(color));
        holder.cp.setTextColor(Color.parseColor(color));
        holder.l.setTextColor(Color.parseColor(color));

        holder.CompanyName.setText(stock.getCompany_name());
        holder.CompanySymbol.setText(stock.getCompany_symbol());
        holder.l.setText(Double.toString(stock.getL()));
        holder.c.setText(triangle + " " + Double.toString(stock.getC()));
        holder.cp.setText("("+Double.toString(stock.getCp())+"%)");

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView CompanyName, CompanySymbol, l, c, cp;

        RecyclerViewHolder(View view) {
            super(view);
            CompanyName = (TextView) view.findViewById(R.id.company_name);
            CompanySymbol = (TextView) view.findViewById(R.id.company_symbol);
            l = (TextView) view.findViewById(R.id.l);
            c = (TextView) view.findViewById(R.id.c);
            cp = (TextView) view.findViewById(R.id.cp);
        }
    }
}
