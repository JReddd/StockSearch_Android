package com.jitong.stocksearch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CurrentFragment extends Fragment {

    private View rootView;
    private String selectedChart;
    private String selectedChartInSpinner;
    private boolean tableDone;
    private boolean inFavList = false;
    private String sharedPreJson;
    private String urlPic;
    private ProgressBar webViewProgressBar;
    private WebView webview;
    private TextView indicatorsFailedTextView;
    private ImageView fbImageView;

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

            tableDone = false;
            String symbol = getActivity().getIntent().getStringExtra("symbol");
            final String realSymbol;

            if (symbol.contains("-")){
                realSymbol = symbol.substring(0, symbol.indexOf("-")).trim();
            } else {
                realSymbol = symbol;
            }

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
            webViewProgressBar = rootView.findViewById(R.id.indicatorsProgressBar);
            final Button button = rootView.findViewById(R.id.indicatorButton);
            final TextView dataFiledTextView = rootView.findViewById(R.id.dataFailedTextView);
            indicatorsFailedTextView = rootView.findViewById(R.id.indicatorFailedTextView);
            final ImageView emptyStarImageView = rootView.findViewById(R.id.emptyStarImageView);
            final ImageView filledStarImageView = rootView.findViewById(R.id.filledStarImageView);
            fbImageView = rootView.findViewById(R.id.fbImageView);

            emptyStarImageView.setEnabled(false);
            filledStarImageView.setEnabled(false);
            fbImageView.setEnabled(false);

            //volley
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

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

                                if (symbolTable.equals("null")) {

                                    dataFiledTextView.setVisibility(View.VISIBLE);

                                } else {

                                    emptyStarImageView.setEnabled(true);
                                    filledStarImageView.setEnabled(true);
                                    tableDone = true;

                                    stockTable.add(symbolTable);
                                    stockTable.add(priceTable);
                                    stockTable.add(change);
                                    stockTable.add(timeStampTable);
                                    stockTable.add(openTable);
                                    stockTable.add(closeTable);
                                    stockTable.add(rangeTable);
                                    stockTable.add(volumeTable);

                                    sharedPreJson = "{'symbol':'" + symbolTable + "','price':'" + priceTable + "','change':'" + changeTable + "','changePercent':'" + changePercentTable + "'}";

                                    ListView stockDetails = rootView.findViewById(R.id.stockDetailsListView);
                                    stockDetails.setVisibility(View.VISIBLE);
                                    ListVIewAdapter arrayAdapter;

                                    if (Objects.equals(changeTable.substring(0, 1), "-")) {
                                        arrayAdapter = new ListVIewAdapter(getActivity(), keyTable, stockTable, downId);
                                    } else {
                                        arrayAdapter = new ListVIewAdapter(getActivity(), keyTable, stockTable, upId);
                                    }

                                    stockDetails.setAdapter(arrayAdapter);
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            dataFiledTextView.setVisibility(View.VISIBLE);
                            // TODO Auto-generated method stub
                            Log.i("error", String.valueOf(error));

                        }
                    });

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsObjRequest);

            //favorite icon
            final SharedPreferences sharedPref = getActivity().getSharedPreferences("favList",Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPref.edit();

            Map<String, ?> allEntries = sharedPref.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                //Log.i("map values", entry.getKey() + ": " + entry.getValue().toString());

                if (entry.getKey().equals(realSymbol)){
                    inFavList = true;
                }

            }

            if (inFavList){
                emptyStarImageView.setVisibility(View.GONE);
                filledStarImageView.setVisibility(View.VISIBLE);
            }

            emptyStarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    emptyStarImageView.setVisibility(View.GONE);
                    filledStarImageView.setVisibility(View.VISIBLE);

                    editor.putString(realSymbol, sharedPreJson).apply();
                }
            });

            filledStarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filledStarImageView.setVisibility(View.GONE);
                    emptyStarImageView.setVisibility(View.VISIBLE);

                    editor.remove(realSymbol).apply();
                }
            });

            //spinner
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

            //webView
            webview = rootView.findViewById(R.id.indicatorWebView);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webview.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");

            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            webview.loadUrl("file:///android_asset/charts.html");
            webview.clearCache(true);
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void  onPageStarted(WebView view, String url, Bitmap favicon) {

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    //

                    webview.evaluateJavascript("javascript:showPrice('" + realSymbol + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });

                    selectedChart = "Price";

                }

            });

            //facebook share
            fbImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(urlPic))
                            .build();
                    ShareDialog shareDialog = new ShareDialog(getActivity());
                    shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

                }
            });


            //button change indicator
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    webview.setVisibility(View.GONE);
                    webViewProgressBar.setVisibility(View.VISIBLE);
                    indicatorsFailedTextView.setVisibility(View.GONE);

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

    public class JsInterface {

        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void showInfoFromJs(String urlPicInJs) {

            urlPic = urlPicInJs;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webViewProgressBar.setVisibility(View.GONE);
                    webview.setVisibility(View.VISIBLE);
                    fbImageView.setEnabled(true);
                }
            });

        }

        @JavascriptInterface
        public void webViewLoadError(){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webview.setVisibility(View.GONE);
                    webViewProgressBar.setVisibility(View.GONE);
                    indicatorsFailedTextView.setVisibility(View.VISIBLE);
                }
            });

        }

    }

}
