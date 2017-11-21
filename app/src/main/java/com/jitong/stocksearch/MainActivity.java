package com.jitong.stocksearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void getQuote(View view) {

        AutoCompleteTextView stockSymbolTextView = findViewById(R.id.StockEditText);
        if(TextUtils.isEmpty(stockSymbolTextView.getText().toString().trim())){

            Toast.makeText(this, "Please enter a stock name or symbol", Toast.LENGTH_SHORT).show();

        }else {

            //go to the stock details activity
            Intent intent = new Intent(getApplicationContext(), StockDetailsActivity.class);
            intent.putExtra("symbol",stockSymbolTextView.getText().toString() );
            startActivity(intent);

        }
    }

    public void clearQuote(View view) {

        AutoCompleteTextView stockSymbolTextView = findViewById(R.id.StockEditText);
        stockSymbolTextView.getText().clear();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Creating the instance of AutoCompleteAdapter
        AutoCompleteAdapter adapter = new AutoCompleteAdapter (this, android.R.layout.select_dialog_item);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView stockSymbolTextView = findViewById(R.id.StockEditText);
        stockSymbolTextView.setThreshold(1);//will start working from first character
        stockSymbolTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

    }
}
