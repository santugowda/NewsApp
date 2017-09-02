package com.example.santhosh.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.santhosh.newsapp.adapter.NewsRecyclerViewAdapter;
import com.example.santhosh.newsapp.model.Docs;
import com.example.santhosh.newsapp.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = SearchActivity.class.getSimpleName();
    public final static String NYTIMES_URL =
            "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    public final static String API_KEY = "api-key";
    public final static String PAGE = "page";
    public final static String NY_BEGIN_DATE = "begin_date";
    public final static String NY_SORT_ORDER = "sort";
    public final static String QUERY = "q";
    public final static String RESPONSE = "response";
    public final static String DOCS = "docs";
    private final static String KEY = "d31fe793adf546658bd67e2b6a7fd11a";
    public static String cachedQueryString = "new york times";

    private ArrayList<Docs> newsArticles;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private RecyclerView articlesRecyclerView;

    private enum SortOrder {
        NEWEST("newest");

        private String sortOrder;

        SortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getSortOrder() {
            return sortOrder;
        }
    }

    private static class FilterAttributes {
        static String beginDate = "";
        static SortOrder sortOrder = SortOrder.NEWEST;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        articlesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.search_title);

        newsArticles = new ArrayList<>();
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(this, newsArticles);

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        articlesRecyclerView.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        customLoadMoreDataFromApi(page);
                    }
                });

        articlesRecyclerView.setAdapter(newsRecyclerViewAdapter);
        articlesRecyclerView.setLayoutManager(gridLayoutManager);

        if (!isNetworkAvailable()) {
            Snackbar.make(articlesRecyclerView, R.string.offline_title, Snackbar.LENGTH_LONG).show();
        } else if (!isOnline()) {
            Snackbar.make(articlesRecyclerView, R.string.service_error, Snackbar.LENGTH_LONG).show();
        } else {
            searchArticle(cachedQueryString, 0);
        }
    }

    public void customLoadMoreDataFromApi(int offset) {
        offset = offset % 100;
        Toast.makeText(this, "Loading more...", Toast.LENGTH_SHORT).show();
        searchArticle(cachedQueryString, offset);
        int curSize = newsRecyclerViewAdapter.getItemCount();
        newsRecyclerViewAdapter.notifyItemRangeInserted(curSize, newsArticles.size() - 1);
    }

    public void searchArticle(String searchText, int page) {
        Log.d(TAG, "Search text is " + searchText);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = constructQueryRequestParams(searchText, page);
        asyncHttpClient.get(NYTIMES_URL, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                try {
                    if (response != null) {
                        Gson gson = new GsonBuilder().create();

                        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                        if (jsonObject.has(RESPONSE)) {
                            JsonObject jsonResponseObject = jsonObject.getAsJsonObject(RESPONSE);
                            if (jsonResponseObject != null) {
                                JsonArray jsonDocsArray = jsonResponseObject.getAsJsonArray(DOCS);

                                List<Docs> fetchedArticles = Arrays.asList(gson.fromJson(jsonDocsArray,
                                        Docs[].class));
                                newsArticles.addAll(fetchedArticles);
                                Log.i("SearchActivity", newsArticles.size() + " docsArrayList found");
                            }
                        }
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("AsyncHttpClient", "Exception while parsing json " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Ouch looks like some problem, try searching again",
                            Toast.LENGTH_SHORT).show();

                }
                newsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response,
                                  Throwable throwable) {
                Log.e("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());

                if (!isNetworkAvailable()) {
                    Snackbar.make(articlesRecyclerView, R.string.offline_title, Snackbar.LENGTH_LONG).show();
                }

                if (!isOnline()) {
                    Snackbar.make(articlesRecyclerView, R.string.service_error, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setQueryHint("Search Article");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsArticles.clear();
                newsRecyclerViewAdapter.notifyDataSetChanged();
                cachedQueryString = query;
                searchArticle(query, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public RequestParams constructQueryRequestParams(String searchText, int pageNumber) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(API_KEY, KEY);

        if (!TextUtils.isEmpty(FilterAttributes.beginDate)) {
            requestParams.put(NY_BEGIN_DATE, FilterAttributes.beginDate);
        }
        requestParams.put(NY_SORT_ORDER, FilterAttributes.sortOrder.getSortOrder());
        requestParams.put(PAGE, pageNumber);
        requestParams.put(QUERY, searchText);

        return requestParams;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(R.string.search_title);
    }
}
