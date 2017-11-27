package com.jitong.stocksearch;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jitong on 11/26/17.
 */

public class FavoriteAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> symbolFav;
    private final ArrayList<String> priceFav;
    private final ArrayList<String> changeFav;

    public FavoriteAdapter(@NonNull Activity context, ArrayList<String> symbolFav, ArrayList<String> priceFav, ArrayList<String> changeFav) {
        super(context, R.layout.list_single, symbolFav);
        this.context = context;
        this.symbolFav = symbolFav;
        this.priceFav = priceFav;
        this.changeFav = changeFav;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_favorite, null, true);
        TextView symbolFavTextView = rowView.findViewById(R.id.symbolFavTextView);
        TextView priceFavTextView = rowView.findViewById(R.id.priceFavTextView);
        TextView changeFavTextView = rowView.findViewById(R.id.changeFavTextView);

        symbolFavTextView.setText(symbolFav.get(position));
        priceFavTextView.setText(priceFav.get(position));
        changeFavTextView.setText(changeFav.get(position));

        if (changeFav.get(position).substring(0,1).equals("-")){

        }

        return rowView;
    }

}
