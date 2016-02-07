package com.project.android.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity implements AppConstants {

    MovieDetails movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Movie Details
        movieDetails = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);

        //Update Details
        updateMovieDetails();
    }

    private void updateMovieDetails() {

        //Update Image
        final ImageView movieImage = (ImageView) findViewById(R.id.movie_image);
        Picasso.with(getApplicationContext()).load(movieDetails.getImageUrl()).into(movieImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                movieImage.setImageResource(R.drawable.img_not_found);
            }
        });

        //Get Title
        TextView updatedText = (TextView) findViewById(R.id.editTitle);
        if (!movieDetails.getOrgTitle().equalsIgnoreCase("")) {
            updatedText.setText(movieDetails.getOrgTitle());
        }

        //Get Release date
        updatedText = (TextView) findViewById(R.id.textRelDate);
        updatedText.setText(movieDetails.getReleaseDate().split("-")[0]);

        //Get Synopsis
        updatedText = (TextView) findViewById(R.id.textSynopsis);
        if (!movieDetails.getPlotSynopsis().equalsIgnoreCase("")) {
            updatedText.setText(movieDetails.getPlotSynopsis());
        }

        //Get Rating
        updatedText = (TextView) findViewById(R.id.textRating);
        if (!movieDetails.getUserRating().equalsIgnoreCase("")) {
            updatedText.setText(movieDetails.getUserRating() + "/10");
        }
    }

}
