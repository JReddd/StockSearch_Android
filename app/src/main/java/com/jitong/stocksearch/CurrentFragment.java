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
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class CurrentFragment extends Fragment {

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

        final View rootView = inflater.inflate(R.layout.fragment_current, container, false);
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

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String symbol =   getActivity().getIntent().getStringExtra("symbol");
        String url = "http://stocksearch-env.us-west-1.elasticbeanstalk.com/?stock_symbolTable=" + symbol.substring(0, symbol.indexOf("-"));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String symbolTable = response.getString("Stock Ticker Symbol");
                            String priceTable = response.getString("Last Price");
                            String changeTable = response.getString("Change");
                            String timeStampTable = response.getString("TimeStamp");
                            String openTable = response.getString("Open");
                            String closeTable = response.getString("Close");
                            String rangeTable = response.getString("Days Range");
                            String volumeTable = response.getString("Volume");
                            String changePercentTable = response.getString("Change Percent");
                            String change = changeTable + " (" + changePercentTable + ")";
                            stockTable.add(symbolTable);
                            stockTable.add(priceTable);
                            stockTable.add(change);
                            stockTable.add(timeStampTable);
                            stockTable.add(openTable);
                            stockTable.add(closeTable);
                            stockTable.add(rangeTable);
                            stockTable.add(volumeTable);

                            ListView stockDetails = rootView.findViewById(R.id.stockDetailsListView);
                            ListVIewAdapter arrayAdapter = new ListVIewAdapter(getActivity(),keyTable, stockTable);
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
        queue.add(jsObjRequest);

        return rootView;
    }
}



// sim-outorder -config test.config -redir:sim artWidth8.out art -scanfile c756hel.in -trainfile1 a10.img
// sim-outorder -config test.config -redir:sim bitcntWidth8.out bitcnts 1125000