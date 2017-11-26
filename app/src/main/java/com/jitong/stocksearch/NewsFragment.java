package com.jitong.stocksearch;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.lang.reflect.Array;
import java.util.ArrayList;


public class NewsFragment extends Fragment {

    private View rootView;

    public NewsFragment() {
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

            rootView = inflater.inflate(R.layout.fragment_news, container, false);

            final ArrayList<String> newsTitle = new ArrayList<>();
            final ArrayList<String> newsAuthor = new ArrayList<>();
            final ArrayList<String> newsDate = new ArrayList<>();
            final ArrayList<String> newsLink = new ArrayList<>();

            final ProgressBar progressBar = rootView.findViewById(R.id.newsProgressBar);

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            String symbol = getActivity().getIntent().getStringExtra("symbol");
            String url = "http://stocksearch-env.us-west-1.elasticbeanstalk.com/?news=" + symbol.substring(0, symbol.indexOf("-"));
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                progressBar.setVisibility(View.GONE);
                                ListView newsListView = rootView.findViewById(R.id.newsListView);
                                newsListView.setVisibility(View.VISIBLE);

                                JSONArray newsTitleArray = response.getJSONArray("title");
                                JSONArray newsAuthorArray = response.getJSONArray("author");
                                JSONArray newsDateArray = response.getJSONArray("date");
                                JSONArray newsLinkArray = response.getJSONArray("link");

                                for (int i = 0; i < 5; i++) {
                                    String author = newsAuthorArray.getString(i);
                                    JSONObject objAuthor = new JSONObject(author);
                                    String author_inJSON = objAuthor.getString("0");

                                    newsTitle.add(newsTitleArray.getString(i));
                                    newsAuthor.add("Author: " + author_inJSON);
                                    newsDate.add("Date: " + newsDateArray.getString(i));
                                    newsLink.add(newsLinkArray.getString(i));
                                }

                                    NewsAdapter arrayAdapter = new NewsAdapter(getActivity(),newsTitle, newsAuthor, newsDate);;
                                    newsListView.setAdapter(arrayAdapter);

                                    newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {

                                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(newsLink.get(position)));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.setPackage("com.android.chrome");
                                            try {
                                                startActivity(intent);
                                            } catch (ActivityNotFoundException ex) {
                                                // Chrome browser presumably not installed so allow user to choose instead
                                                intent.setPackage(null);
                                                startActivity(intent);
                                            }

                                        }
                                    });



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

            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    4000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            queue.add(jsObjRequest);

        }

        return rootView;
    }

}
