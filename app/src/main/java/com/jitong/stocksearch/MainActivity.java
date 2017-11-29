package com.jitong.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private boolean ascending = true;
    private String sortBy = "Sort By";
    private int positionInSortSpinner = 0;
    private int positionInOrderSpinner = 0;

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

        SharedPreferences sharedPref = this.getSharedPreferences("favList",Context.MODE_PRIVATE);
        final ListView favoriteListView = findViewById(R.id.favoriteListView);

        //favorite list
        final ArrayList<StockInFav> StockInFavArrayList = new ArrayList<>();
        Map<String, ?> allEntries = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.i("favorite list", entry.getKey() + ": " + entry.getValue().toString());

            try {

                JSONObject obj = new JSONObject(entry.getValue().toString());
                String symbol = obj.getString("symbol");
                double price = Double.parseDouble(obj.getString("price"));
                double change = Double.parseDouble(obj.getString("change"));
                double changePercent = Double.parseDouble(obj.getString("changePercent").replace("%",""));
                long addedTime = Long.parseLong(obj.getString("addedTime"));

                StockInFavArrayList.add(new StockInFav(symbol,price,change,changePercent,addedTime));


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Collections.sort(StockInFavArrayList,StockInFav.DefaultComparator);
        FavoriteAdapter arrayAdapter = new FavoriteAdapter(this, StockInFavArrayList);
        favoriteListView.setAdapter(arrayAdapter);

        favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //go to the stock details activity
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("symbol", StockInFavArrayList.get(position).getSymbol());
                startActivity(intent);

            }
        });

        //spinner sort by
        Spinner spinner = this.findViewById(R.id.SortBySpinner);
        final Spinner orderSpinner = this.findViewById(R.id.orderSpinner);
        final List<String> spinnerList = new ArrayList<>();

        spinnerList.add("Sort By");
        spinnerList.add("Default");
        spinnerList.add("Symbol");
        spinnerList.add("Price");
        spinnerList.add("Change");
        spinnerList.add("Change Percent");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnerList){

            @Override
            public boolean isEnabled(int position){
                if(position == 0 || position == positionInSortSpinner)
                {
                    // Disable the second item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position== 0 || position == positionInSortSpinner) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (spinnerList.get(position)) {
                    case "Default":
                        sortBy = "Default";
                        positionInSortSpinner = 1;
                        orderSpinner.setEnabled(false);
                        Collections.sort(StockInFavArrayList,StockInFav.DefaultComparator);
                        FavoriteAdapter arrayAdapterDefault = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                        favoriteListView.setAdapter(arrayAdapterDefault);
                        break;
                    case "Symbol" :
                        sortBy = "Symbol";
                        positionInSortSpinner = 2;
                        orderSpinner.setEnabled(true);
                        if (ascending){
                            Collections.sort(StockInFavArrayList,StockInFav.SymbolComparator);
                            FavoriteAdapter arrayAdapterSymbol = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterSymbol);
                        } else {
                            Collections.sort(StockInFavArrayList,StockInFav.SymbolComparatorNeg);
                            FavoriteAdapter arrayAdapterSymbol = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterSymbol);
                        }
                        break;
                    case "Price" :
                        sortBy = "Price";
                        positionInSortSpinner = 3;
                        orderSpinner.setEnabled(true);
                        if (ascending){
                            Collections.sort(StockInFavArrayList,StockInFav.PriceComparator);
                            FavoriteAdapter arrayAdapterPrice = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterPrice);
                        } else {
                            Collections.sort(StockInFavArrayList,StockInFav.PriceComparatorNeg);
                            FavoriteAdapter arrayAdapterPrice = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterPrice);
                        }
                        break;
                    case "Change":
                        sortBy = "Change";
                        positionInSortSpinner = 4;
                        orderSpinner.setEnabled(true);
                        if (ascending){
                            Collections.sort(StockInFavArrayList,StockInFav.ChangeComparator);
                            FavoriteAdapter arrayAdapterChange = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterChange);
                        } else {
                            Collections.sort(StockInFavArrayList,StockInFav.ChangeComparatorNeg);
                            FavoriteAdapter arrayAdapterChange = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterChange);
                        }
                        break;
                    case "Change Percent":
                        sortBy = "Change Percent";
                        positionInSortSpinner = 5;
                        orderSpinner.setEnabled(true);
                        if (ascending){
                            Collections.sort(StockInFavArrayList,StockInFav.ChangePercentComparator);
                            FavoriteAdapter arrayAdapterChangePercent = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterChangePercent);
                        } else {
                            Collections.sort(StockInFavArrayList,StockInFav.ChangePercentComparatorNeg);
                            FavoriteAdapter arrayAdapterChangePercent = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                            favoriteListView.setAdapter(arrayAdapterChangePercent);
                        }
                        break;
                    default:break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //spinner order
        final List<String> orderSpinnerList = new ArrayList<>();

        orderSpinnerList.add("Order");
        orderSpinnerList.add("Ascending");
        orderSpinnerList.add("Descending");
        ArrayAdapter orderSpinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,orderSpinnerList){

            @Override
            public boolean isEnabled(int position){
                if(position == 0 || position ==positionInOrderSpinner)
                {
                    // Disable the second item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position== 0 || position == positionInOrderSpinner) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        orderSpinner.setAdapter(orderSpinnerAdapter);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (orderSpinnerList.get(position)) {

                    case "Ascending" :
                        ascending = true;
                        positionInOrderSpinner = 1;
                        switch (sortBy) {
                            case "Default":
                                break;
                            case "Symbol":  Collections.sort(StockInFavArrayList,StockInFav.SymbolComparator);
                                            FavoriteAdapter arrayAdapterSymbol = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                            favoriteListView.setAdapter(arrayAdapterSymbol);
                                            break;
                            case "Price":   Collections.sort(StockInFavArrayList,StockInFav.PriceComparator);
                                            FavoriteAdapter arrayAdapterPrice = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                            favoriteListView.setAdapter(arrayAdapterPrice);
                                            break;
                            case "Change":  Collections.sort(StockInFavArrayList,StockInFav.ChangeComparator);
                                            FavoriteAdapter arrayAdapterChange = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                            favoriteListView.setAdapter(arrayAdapterChange);
                                            break;
                            case "Change Percent":
                                            Collections.sort(StockInFavArrayList,StockInFav.ChangePercentComparator);
                                            FavoriteAdapter arrayAdapterChangePercent = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                            favoriteListView.setAdapter(arrayAdapterChangePercent);
                                            break;
                            default:break;
                        }
                        break;
                    case "Descending" :
                        ascending = false;
                        positionInOrderSpinner = 2;
                        switch (sortBy) {
                            case "Default":
                                break;
                            case "Symbol":  Collections.sort(StockInFavArrayList,StockInFav.SymbolComparatorNeg);
                                FavoriteAdapter arrayAdapterSymbol = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                favoriteListView.setAdapter(arrayAdapterSymbol);
                                break;
                            case "Price":   Collections.sort(StockInFavArrayList,StockInFav.PriceComparatorNeg);
                                FavoriteAdapter arrayAdapterPrice = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                favoriteListView.setAdapter(arrayAdapterPrice);
                                break;
                            case "Change":  Collections.sort(StockInFavArrayList,StockInFav.ChangeComparatorNeg);
                                FavoriteAdapter arrayAdapterChange = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                favoriteListView.setAdapter(arrayAdapterChange);
                                break;
                            case "Change Percent":
                                Collections.sort(StockInFavArrayList,StockInFav.ChangePercentComparatorNeg);
                                FavoriteAdapter arrayAdapterChangePercent = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                favoriteListView.setAdapter(arrayAdapterChangePercent);
                                break;
                            default:break;
                        }
                        break;
                    default:break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //popup menu
        favoriteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popupMenu = new PopupMenu(MainActivity.this,view, Gravity.CENTER);
                MenuInflater menuInflater = getMenuInflater();

                menuInflater.inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.getMenu().findItem(R.id.titleMenu).setEnabled(false);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.noMenu:
                                break;
                            case R.id.yesMenu:
                                SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("favList",Context.MODE_PRIVATE);
                                sharedPref.edit().remove(StockInFavArrayList.get(position).getSymbol()).apply();
                                StockInFavArrayList.remove(position);

                                FavoriteAdapter arrayAdapter = new FavoriteAdapter(MainActivity.this, StockInFavArrayList);
                                favoriteListView.setAdapter(arrayAdapter);
                                break;
                            default:break;
                        }

                        return true;
                    }
                });

                return true;
            }
        });

    }
}
