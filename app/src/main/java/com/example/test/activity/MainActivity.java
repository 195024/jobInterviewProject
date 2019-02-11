package com.example.test.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.test.R;
import com.example.test.model.Movie;
import com.example.test.util.ClickListenerInterface;
import com.example.test.util.RecyclerViewAdapter;
import com.example.test.util.RecyclerViewClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.test.util.Constants.API_KEY;
import static com.example.test.util.Constants.BASE_URL;
import static com.example.test.util.Constants.CHANGES_END_DATE_URL;
import static com.example.test.util.Constants.CHANGES_START_DATE_URL;
import static com.example.test.util.Constants.CHANGES_URL;
import static com.example.test.util.Constants.DETAILS_URL;
import static com.example.test.util.Constants.MAX_NR_OF_API_REQUESTS;
import static com.example.test.util.Constants.MAX_NR_OF_DAYS;
import static com.example.test.util.Variables.movies;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RequestQueue requestQueue;

    private RecyclerViewAdapter adapter;

    private RecyclerView rvList;
    private EditText etDays;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        initRecyclerView();
        populateRecyclerViewIfDataExists();
        initVolley();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable())
                    getMovies();
                else
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.activity_main_toast_noInternetConnection),
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateRecyclerViewIfDataExists() {
        if (movies != null && movies.size() > 0) {
            adapter.setData(movies);
            adapter.notifyDataSetChanged();
        }
    }

    private void bindViews() {
        rvList = findViewById(R.id.recyclerview_list);
        etDays = findViewById(R.id.edittext_days);
        btnSearch = findViewById(R.id.button_search);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }

        rvList.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter();
        rvList.setAdapter(adapter);
        rvList.setHasFixedSize(true);

        rvList.addOnItemTouchListener(new RecyclerViewClickListener(this, new ClickListenerInterface() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(getApplicationContext(), DisplayDetailsActivity.class);
                intent.putExtra("MOVIE_POSITION_ON_LIST", position);
                startActivity(intent);
            }
        }));
    }

    private void initVolley() {
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 10); // 10MB cap
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    private String prepareChangedMoviesListUrl() {
        int nrOfDays = MAX_NR_OF_DAYS;

        String nrOfDaysString = etDays.getText().toString();
        if (!nrOfDaysString.isEmpty()) {
            nrOfDays = Integer.valueOf(nrOfDaysString);
            if (nrOfDays == 0 || nrOfDays > 14)
                nrOfDays = 14;
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String endDate = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, -nrOfDays);
        String startDate = dateFormat.format(calendar.getTime());

        String url = BASE_URL + CHANGES_URL + API_KEY +
                CHANGES_START_DATE_URL + startDate +
                CHANGES_END_DATE_URL + endDate;

        return url;
    }

    private Movie createMovieFromJsonObject(JSONObject jsonObject) {
        Movie movie = new Movie();
        try {
            movie.setId(jsonObject.getString("id"));
            movie.setTitle(jsonObject.getString("title"));
            movie.setPosterPath(jsonObject.getString("poster_path"));
            movie.setOriginalLanguage(jsonObject.getString("original_language"));
            movie.setOriginalTitle(jsonObject.getString("original_title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    private void getMovies() {
        String changedMoviesListUrl = prepareChangedMoviesListUrl();

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, changedMoviesListUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArrayOfMovies = response.getJSONArray("results");
                            if (jsonArrayOfMovies != null) {
                                createInitialMovieList(jsonArrayOfMovies);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e(TAG, error.toString());
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void getMovieDetailsWithId(final String id) {
        String movieDetailsUrl = BASE_URL + id + DETAILS_URL + API_KEY;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, movieDetailsUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            movies.add(createMovieFromJsonObject(response));

                            if (movies.size() == MAX_NR_OF_API_REQUESTS) {
                                adapter.setData(movies);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            Log.e(TAG, error.toString());
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void createInitialMovieList(JSONArray jsonArray) throws JSONException {
        movies = new ArrayList<>();

        for (int i = 0; i < MAX_NR_OF_API_REQUESTS; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject != null) {
                String id = jsonObject.getString("id");
                getMovieDetailsWithId(id);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.stop();
    }
}
