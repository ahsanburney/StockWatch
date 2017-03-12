package com.example.ahsanburney.recycler;

/**
 * @author Ahsan Burney
 * @version 1.0
 */

public class StockContract {

    public StockContract(){}

    public static class stockEntry {

        // DB Table Name
        public static final String TABLE_NAME = "StockWatchTable";

        public static final String company_name = "company_name";
        public static final String company_symbol = "company_symbol";
        public static final String l = "price";
        public static final String c = "price_change";
        public static final String cp = "percentage";


    }


}
