package com.example.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.Movie;
import com.squareup.picasso.Picasso;

import static com.example.test.util.Constants.IMAGE_BASE_URL;
import static com.example.test.util.Constants.IMAGE_FILE_SIZE_FOR_DETAILS;
import static com.example.test.util.Variables.movies;

public class DisplayDetailsActivity extends AppCompatActivity {
    private ImageView ivPoster;
    private TextView tvId;
    private TextView tvTitle;
    private TextView tvOriginalTitle;
    private TextView tvOriginalLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);

        Intent intent = getIntent();
        int positionOfMovieOnList = intent.getIntExtra("MOVIE_POSITION_ON_LIST", 0);

        bindViews();
        setData(positionOfMovieOnList);
    }

    private void setData(int position) {
        Movie chosenMovie = movies.get(position);

        String url = IMAGE_BASE_URL + IMAGE_FILE_SIZE_FOR_DETAILS + chosenMovie.getPosterPath();
        Picasso.get().load(url).into(ivPoster);

        tvId.setText(getString(R.string.activity_display_details_textview_id,
                chosenMovie.getId()));
        tvTitle.setText(getString(R.string.activity_display_details_textview_title,
                chosenMovie.getTitle()));
        tvOriginalTitle.setText(getString(R.string.activity_display_details_textview_originalTitle,
                chosenMovie.getOriginalTitle()));
        tvOriginalLanguage.setText(getString(R.string.activity_display_details_textview_originalLanguage,
                chosenMovie.getOriginalLanguage()));
    }

    private void bindViews() {
        ivPoster = findViewById(R.id.imageview_details_poster);
        tvId = findViewById(R.id.textview_details_id);
        tvTitle = findViewById(R.id.textview_details_title);
        tvOriginalTitle = findViewById(R.id.textview_details_originalTitle);
        tvOriginalLanguage = findViewById(R.id.textview_details_originalLanguage);
    }
}
