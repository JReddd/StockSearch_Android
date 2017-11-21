//package com.jitong.stocksearch;
//
//import android.annotation.SuppressLint;
//import android.app.Notification;
//import android.content.Context;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.AutoCompleteTextView;
//import android.widget.ProgressBar;
//
//
///**
// * Created by jitong on 11/20/17.
// */
//
//public class DelayAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
//
//    private static final int MESSAGE_TEXT_CHANGED = 100;
//    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;
//
//    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
//    private ProgressBar mLoadingIndicator;
//
//    @SuppressLint("HandlerLeak")
//    private final Handler mHandler = new Handler() {
//
//    };
//
//    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public void setLoadingIndicator(ProgressBar progressBar) {
//        mLoadingIndicator = progressBar;
//    }
//
//    public void setAutoCompleteDelay(int autoCompleteDelay) {
//        mAutoCompleteDelay = autoCompleteDelay;
//    }
//
//    @Override
//    protected void performFiltering(CharSequence text, int keyCode) {
//        if (mLoadingIndicator != null) {
//            mLoadingIndicator.setVisibility(View.VISIBLE);
//        }
//        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
//        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
//    }
//
//    @Override
//    public void onFilterComplete(int count) {
//        if (mLoadingIndicator != null) {
//            mLoadingIndicator.setVisibility(View.GONE);
//        }
//        super.onFilterComplete(count);
//    }
//
//
//}
