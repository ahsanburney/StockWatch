package com.example.ahsanburney.recycler;

import java.io.Serializable;

public class Stock{

    private String company_name;
    private String company_symbol;
    private Double l;
    private Double c;
    private Double cp;

    public Stock(String company_name, String company_symbol, double cPrice, double cChange, double cPercent) {
        this.company_name = company_name;
        this.company_symbol = company_symbol;
       this.l = cPrice;
        this.c = cChange;
        this.cp = cPercent;

    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_symbol() {
        return company_symbol;
    }

  public Double getL() {
        return l;
    }

    public Double getC() {
        return c;
    }

    public Double getCp() {
        return cp;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setCompany_symbol(String company_symbol) {
        this.company_symbol = company_symbol;
    }

  public void setL(Double l) {
        this.l = l;
    }

    public void setC(Double c) {
        this.c = c;
    }

    public void setCp(Double cp) {
        this.cp = cp;
    }
}