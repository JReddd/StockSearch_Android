package com.jitong.stocksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

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
import java.util.List;
import java.util.Objects;


public class CurrentFragment extends Fragment {

    private View rootView;
    private String selectedChart;
    private String selectedChartInSpinner;

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
        if (rootView == null) {

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
            final Button button = rootView.findViewById(R.id.indicatorButton);

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            final String symbol = getActivity().getIntent().getStringExtra("symbol");
            final String realSymbol = symbol.substring(0, symbol.indexOf("-")).trim();
            String url = "http://stocksearch-env.us-west-1.elasticbeanstalk.com/?stock_symbolTable=" + realSymbol;
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

                                if (Objects.equals(changeTable.substring(0, 1), "-")) {
                                    arrayAdapter = new ListVIewAdapter(getActivity(), keyTable, stockTable, downId);
                                } else {
                                    arrayAdapter = new ListVIewAdapter(getActivity(), keyTable, stockTable, upId);
                                }

                                stockDetails.setAdapter(arrayAdapter);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            // TODO Auto-generated method stub
                            Log.i("error", String.valueOf(error));

                        }
                    });

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsObjRequest);

            Spinner spinner = rootView.findViewById(R.id.indicatorSpinner);
            final List<String> spinnerList = new ArrayList<>();

            spinnerList.add("Price");
            spinnerList.add("SMA");
            spinnerList.add("EMA");
            spinnerList.add("STOCH");
            spinnerList.add("RSI");
            spinnerList.add("ADX");
            spinnerList.add("CCI");
            spinnerList.add("BBANDS");
            spinnerList.add("MACD");
            ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,spinnerList);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedChartInSpinner = spinnerList.get(position);
                    if(Objects.equals(selectedChartInSpinner, selectedChart)){
                        button.setEnabled(false);
                        button.setTextColor(getResources().getColor(R.color.gray));
                    } else {
                        button.setEnabled(true);
                        button.setTextColor(getResources().getColor(R.color.black));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            final WebView webview = rootView.findViewById(R.id.indicatorWebView);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            webview.loadUrl("file:///android_asset/charts.html");
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void  onPageStarted(WebView view, String url, Bitmap favicon) {
                    //设定加载开始的操作
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    webview.evaluateJavascript("javascript:showPrice('" + realSymbol + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });

                    selectedChart = "Price";
                }

            });

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    webview.evaluateJavascript("javascript:show" + selectedChartInSpinner + "('"+realSymbol+"')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });
                    selectedChart = selectedChartInSpinner;

                    if(Objects.equals(selectedChartInSpinner, selectedChart)){
                        button.setEnabled(false);
                        button.setTextColor(getResources().getColor(R.color.gray));
                    } else {
                        button.setEnabled(true);
                        button.setTextColor(getResources().getColor(R.color.black));
                    }
                }

            });

        }

        return rootView;
    }

}
