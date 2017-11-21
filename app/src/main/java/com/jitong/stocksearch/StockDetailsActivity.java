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
        setTitle("History");

        Intent intent = getIntent();
        Toast.makeText(this, intent.getStringExtra("symbol"), Toast.LENGTH_SHORT).show();
    }
}
