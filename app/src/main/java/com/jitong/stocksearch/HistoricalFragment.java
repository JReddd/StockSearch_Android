package com.jitong.stocksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HistoricalFragment extends Fragment {

    private View rootView;
    private ProgressBar webViewProgressBar;
    private TextView chartFailedTextView;

    public HistoricalFragment() {
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

            rootView = inflater.inflate(R.layout.fragment_historical, container, false);

            webViewProgressBar = rootView.findViewById(R.id.historicalProgressBar);
            chartFailedTextView = rootView.findViewById(R.id.historicalDataFiledTextView);

            String symbol = getActivity().getIntent().getStringExtra("symbol");
            final String realSymbol;

            if (symbol.contains("-")){
                realSymbol = symbol.substring(0, symbol.indexOf("-")).trim();
            } else {
                realSymbol = symbol;
            }

            final WebView webview = rootView.findViewById(R.id.historicalWebView);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            webview.loadUrl("file:///android_asset/historicalChart.html");
            webview.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
            webview.clearCache(true);
            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void  onPageStarted(WebView view, String url, Bitmap favicon) {

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    webview.evaluateJavascript("javascript:showHistoricalChart('" + realSymbol + "')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                        }
                    });

                }

            });

        }

        // Inflate the layout for this fragment
        return rootView;
    }

    public class JsInterface {

        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void showInfoFromJs() {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webViewProgressBar.setVisibility(View.GONE);
                }
            });

        }

        @JavascriptInterface
        public void webViewLoadError(){

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webViewProgressBar.setVisibility(View.GONE);
                    chartFailedTextView.setVisibility(View.VISIBLE);
                }
            });

        }

    }

}
