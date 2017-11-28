package com.jitong.stocksearch;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jitong on 11/26/17.
 */

public class FavoriteAdapter extends ArrayAdapter {

    private final Activity context;

    private final ArrayList<StockInFav> stockInFavArrayList;

    public FavoriteAdapter(@NonNull Activity context, ArrayList<StockInFav> stockInFavArrayList) {
        super(context, R.layout.list_single,stockInFavArrayList);
        this.context = context;
        this.stockInFavArrayList = stockInFavArrayList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_favorite, null, true);
        TextView symbolFavTextView = rowView.findViewById(R.id.symbolFavTextView);
        TextView priceFavTextView = rowView.findViewById(R.id.priceFavTextView);
        TextView changeFavTextView = rowView.findViewById(R.id.changeFavTextView);

        symbolFavTextView.setText(stockInFavArrayList.get(position).getSymbol());
        String priceFav = String.valueOf(stockInFavArrayList.get(position).getPrice());
        priceFavTextView.setText(priceFav);
        String changeFav = stockInFavArrayList.get(position).getChange() + " (" + stockInFavArrayList.get(position).getChangePercent() + "%) ";
        changeFavTextView.setText(changeFav);

        if (stockInFavArrayList.get(position).getChange() < 0){
            changeFavTextView.setTextColor(Color.RED);
        }else {
            changeFavTextView.setTextColor(Color.GREEN);
        }

        return rowView;
    }

}
