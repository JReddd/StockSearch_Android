package com.jitong.stocksearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jitong on 11/20/17.
 */

public class AutoCompleteAdapter extends ArrayAdapter implements Filterable {

    private ArrayList<String> data;
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(getContext());
    private final String url = "http://stocksearch-env.us-west-1.elasticbeanstalk.com/?input=";

    public AutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return data.get(position);
    }


    @NonNull
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults results = new FilterResults();
                final boolean[] isDone = {false};
                if (constraint != null) {

                    JsonObjectRequest jsObjRequest = new JsonObjectRequest
                            (Request.Method.GET, url + constraint.toString(), null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject obj) {
                                    try {
                                        JSONArray terms = obj.getJSONArray("auto");
                                        Log.i("auto", String.valueOf(terms));
                                        ArrayList<String> suggestions = new ArrayList<>();
                                        for (int ind = 0; ind < terms.length(); ind++) {
                                            String term = terms.getString(ind);
                                            JSONObject objAuto = new JSONObject(term);
                                            String symbolAutoComplete = objAuto.getString("Symbol");
                                            String nameAutoComplete = objAuto.getString("Name");
                                            String exchangeAutoComplete = objAuto.getString("Exchange");
                                            suggestions.add(symbolAutoComplete + " - " + nameAutoComplete + " (" + exchangeAutoComplete + ") ");
                                        }

                                        data = suggestions;
                                        results.values = data;
                                        results.count = data.size();
                                        isDone[0] = true;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        isDone[0] = true;
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO Auto-generated method stub
                                    isDone[0] = true;
                                    Log.i("auto error", String.valueOf(error));

                                }
                            });
                    // Add the request to the RequestQueue.
                    queue.add(jsObjRequest);

                } else {

                    isDone[0] = true;

                }
                while (!isDone[0]) {
                    //wait the service request
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else notifyDataSetInvalidated();
            }
        };
    }
}