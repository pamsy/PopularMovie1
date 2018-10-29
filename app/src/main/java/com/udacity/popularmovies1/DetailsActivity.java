package com.udacity.popularmovies1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE= "original_title";
    public static final String EXTRA_POSTER= "poster_path";
    public static final String EXTRA_OVERVIEW= "overview";
    public static final String EXTRA_VOTE= "vote_average";
    public static final String EXTRA_RELEASE= "release_date";

    //The Views
    @BindView(R.id.thumbnail_iv) ImageView mTumbnail_iv;
    @BindView(R.id.rating_tv)TextView mUserrating_tv;
    @BindView(R.id.title_tv) TextView mTitle_tv;
    @BindView(R.id.release_tv) TextView mRelease_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
