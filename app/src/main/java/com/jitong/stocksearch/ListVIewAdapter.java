package com.jitong.stocksearch;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jitong on 11/23/17.
 */

public class ListVIewAdapter extends ArrayAdapter {

    private final Activity context;
    private final String[] keyTable;
    private final ArrayList<String> valueTable;

    public ListVIewAdapter(@NonNull Activity context, String[] keyTable, ArrayList<String> valueTable) {
        super(context, R.layout.list_single, keyTable);
        this.context = context;
        this.keyTable = keyTable;
        this.valueTable = valueTable;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView keyTableTextView = rowView.findViewById(R.id.keyTableTextView);
        TextView valueTableTextView = rowView.findViewById(R.id.valueTableTextView);

        keyTableTextView.setText(keyTable[position]);
        valueTableTextView.setText(valueTable.get(position));

        //ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }

}




