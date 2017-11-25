package com.jitong.stocksearch;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class CurrentFragment extends Fragment {

    private View rootView;

    public CurrentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null){

            rootView = inflater.inflate(R.layout.fragment_current, container, false);
            final ArrayList<String> stockTable = new ArrayList<>();
            final String[] keyTable = {
                    "Stock Symbol",
                    "Last Price",
                    "Change",
                    "Timestamp",
                    "Open",
                    "Close",
                    "Day's Range",
                    "Volume"
            };
            final Integer upId = R.drawable.up;
            final Integer downId = R.drawable.down;

            final ProgressBar progressBar = rootView.findViewById(R.id.stockDetailsProgressBar);

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String symbol = getActivity().getIntent().getStringExtra("symbol");
            String url = "http://stocksearch-env.us-west-1.elasticbeanstalk.com/?stock_symbolTable=" + symbol.substring(0, symbol.indexOf("-"));
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                progressBar.setVisibility(View.GONE);

                                String symbolTable = response.getString("Stock Ticker Symbol");
                                String priceTable = response.getString("Last Price");
                                String changeTable = response.getString("Change");
                                String timeStampTable = response.getString("TimeStamp");
                                String openTable = response.getString("Open");
                                String closeTable = response.getString("Close");
                                String rangeTable = response.getString("Days Range");
                                String volumeTable = response.getString("Volume");
                                String changePercentTable = response.getString("Change Percent");
                                String change = changeTable + " (" + changePercentTable + ") ";
                                stockTable.add(symbolTable);
                                stockTable.add(priceTable);
                                stockTable.add(change);
                                stockTable.add(timeStampTable);
                                stockTable.add(openTable);
                                stockTable.add(closeTable);
                                stockTable.add(rangeTable);
                                stockTable.add(volumeTable);

                                ListView stockDetails = rootView.findViewById(R.id.stockDetailsListView);
                                stockDetails.setVisibility(View.VISIBLE);
                                ListVIewAdapter arrayAdapter;

                                if (Objects.equals(changeTable.substring(0,1), "-")){
                                    arrayAdapter = new ListVIewAdapter(getActivity(),keyTable, stockTable, downId);
                                } else {
                                    arrayAdapter = new ListVIewAdapter(getActivity(),keyTable, stockTable, upId);
                                }

                                stockDetails.setAdapter(arrayAdapter);

                            }catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.i("error", String.valueOf(error));

                        }
                    });

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    4000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsObjRequest);

        }

        return rootView;
    }

}
