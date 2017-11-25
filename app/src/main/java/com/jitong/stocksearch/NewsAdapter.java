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
 * Created by jitong on 11/25/17.
 */

public class NewsAdapter extends ArrayAdapter {

    private final Activity context;
    private final ArrayList<String> newsTitle;
    private final ArrayList<String> newsAuthor;
    private final ArrayList<String> newsDate;

    public NewsAdapter(@NonNull Activity context, ArrayList<String> newsTitle, ArrayList<String> newsAuthor, ArrayList<String> newsDate) {
        super(context, R.layout.list_single,newsTitle);
        this.context = context;
        this.newsTitle = newsTitle;
        this.newsAuthor = newsAuthor;
        this.newsDate = newsDate;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_news, null, true);
        TextView newsAuthorTextView = rowView.findViewById(R.id.newsAuthorTextView);
        TextView newsTitleTextView = rowView.findViewById(R.id.newsTitleTextView);
        TextView newsDateTextView = rowView.findViewById(R.id.newsDateTextView);

        newsAuthorTextView.setText(newsAuthor.get(position));
        newsTitleTextView.setText(newsTitle.get(position));
        newsDateTextView.setText(newsDate.get(position));

        return rowView;
    }

}
