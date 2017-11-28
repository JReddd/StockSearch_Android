package com.jitong.stocksearch;

import java.util.Comparator;

/**
 * Created by jitong on 11/27/17.
 */

public class StockInFav implements Comparable<StockInFav> {

    private String symbol;
    private double price;
    private double change;
    private double changePercent;

    public StockInFav(String symbol, double price, double change, double changePercent) {
        super();
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public String getSymbol() {
        return symbol;

    }

    public double getPrice() {
        return price;
    }

    public double getChange() {
        return change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    @Override
    public int compareTo(StockInFav o) {
        return 0;
    }

    public static Comparator<StockInFav> SymbolComparator = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            String symbol1 = o1.getSymbol().toUpperCase();
            String symbol2 = o2.getSymbol().toUpperCase();

            //ascending order
            return symbol1.compareTo(symbol2);

            //descending order
            //return symbol2.compareTo(symbol1);
        }
    };

    public static Comparator<StockInFav> SymbolComparatorNeg = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            String symbol1 = o1.getSymbol().toUpperCase();
            String symbol2 = o2.getSymbol().toUpperCase();

            //ascending order
            //return symbol1.compareTo(symbol2);

            //descending order
            return symbol2.compareTo(symbol1);
        }
    };

    public static Comparator<StockInFav> PriceComparator = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            double price1 = o1.getPrice();
            double price2 = o2.getPrice();

            //ascending order
            if (price1 < price2) return -1;
            if (price1 > price2) return 1;
            return 0;

            //descending order

        }
    };

    public static Comparator<StockInFav> PriceComparatorNeg = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            double price1 = o1.getPrice();
            double price2 = o2.getPrice();

            //descending order
            if (price1 < price2) return 1;
            if (price1 > price2) return -1;
            return 0;



        }
    };

    public static Comparator<StockInFav> ChangeComparator = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            double price1 = o1.getChange();
            double price2 = o2.getChange();

            //ascending order
            if (price1 < price2) return -1;
            if (price1 > price2) return 1;
            return 0;

            //descending order

        }
    };

    public static Comparator<StockInFav> ChangeComparatorNeg = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            double price1 = o1.getChange();
            double price2 = o2.getChange();

            //ascending order
            if (price1 < price2) return 1;
            if (price1 > price2) return -1;
            return 0;

            //descending order

        }
    };

    public static Comparator<StockInFav> ChangePercentComparator = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            double price1 = o1.getChangePercent();
            double price2 = o2.getChangePercent();

            //ascending order
            if (price1 < price2) return -1;
            if (price1 > price2) return 1;
            return 0;

            //descending order

        }
    };

    public static Comparator<StockInFav> ChangePercentComparatorNeg = new Comparator<StockInFav>() {
        @Override
        public int compare(StockInFav o1, StockInFav o2) {
            double price1 = o1.getChangePercent();
            double price2 = o2.getChangePercent();

            //ascending order
            if (price1 < price2) return 1;
            if (price1 > price2) return -1;
            return 0;

            //descending order

        }
    };


}
