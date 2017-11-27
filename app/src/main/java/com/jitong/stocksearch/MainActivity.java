package com.jitong.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public void getQuote(View view) {

        AutoCompleteTextView stockSymbolTextView = findViewById(R.id.StockEditText);
        String symbol = stockSymbolTextView.getText().toString();

        if(TextUtils.isEmpty(stockSymbolTextView.getText().toString().trim())){

            Toast.makeText(this, "Please enter a stock name or symbol", Toast.LENGTH_SHORT).show();

        }else {

            //go to the stock details activity
            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
            intent.putExtra("symbol", symbol);
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

        ArrayList<String> symbolFav = new ArrayList<>();
        ArrayList<String> priceFav = new ArrayList<>();
        ArrayList<String> changeFav = new ArrayList<>();

        //Creating the instance of AutoCompleteAdapter
        AutoCompleteAdapter adapter = new AutoCompleteAdapter (this, android.R.layout.select_dialog_item);
        //Getting the instance of AutoCompleteTextView
        DelayAutoCompleteTextView stockSymbolTextView = findViewById(R.id.StockEditText);
        //will start working from first character
        stockSymbolTextView.setThreshold(1);
        //setting the adapter data into the AutoCompleteTextView
        stockSymbolTextView.setAdapter(adapter);
        //set progressBar
        stockSymbolTextView.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.AutoCompleteProgressBar));

        //favorite list
        SharedPreferences sharedPref = this.getSharedPreferences("favList",Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.i("map values", entry.getKey() + ": " + entry.getValue().toString());

            try {

                JSONObject obj = new JSONObject(entry.getValue().toString());
                symbolFav.add(obj.getString("symbol"));
                priceFav.add(obj.getString("price"));
                changeFav.add(obj.getString("change"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        FavoriteAdapter arrayAdapter = new FavoriteAdapter(this, symbolFav, priceFav, changeFav);
        ListView favoriteListView = findViewById(R.id.favoriteListView);
        favoriteListView.setAdapter(arrayAdapter);
    }
}
