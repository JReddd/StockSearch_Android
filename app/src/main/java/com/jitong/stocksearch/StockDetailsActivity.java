package com.jitong.stocksearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StockDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);


        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");
        Toast.makeText(this, symbol , Toast.LENGTH_SHORT).show();

        setTitle(symbol.substring(0, symbol.indexOf("-")));
    }
}
