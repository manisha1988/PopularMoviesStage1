package com.project.android.popularmoviesstage1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.project.android.helper.remotemanagement.AndroidHttpReq;
import com.project.android.helper.remotemanagement.AndroidTaskExecutor;
import com.project.android.helper.remotemanagement.TaskListener;
import com.project.android.helper.remotemanagement.UserListener;
import com.project.android.helper.utility.PropertyManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AppConstants {

    List<String> imageUrl = null;
    ArrayList<MovieDetails> movieDetailsList;
    ImageAdapter imageAdapter;

    String apiKey;
    String movieBaseUrl;
    String imageBaseUrl;
    String imageSize;

    GridView gridview;
    boolean isPropReadSuccessfully = false;

    //Preferences object
    SharedPreferences appPrefernces;

    Menu menu;

    private void readProperties() {
        PropertyManager propertyManager = new PropertyManager(getApplicationContext(), "config");

        //Read al the properties
        //ToDo: API key needs to be updated acc to one's API key
        apiKey = propertyManager.getValue(API_KEY);
        if (apiKey.equalsIgnoreCase("")) {
            Dialog.createDialogAndExit(MainActivity.this, "Missing API key", "Please update your API key");
            isPropReadSuccessfully = false;
            return;
        }

        movieBaseUrl = propertyManager.getValue(MOVIE_BASE_URL);
        imageBaseUrl = propertyManager.getValue(IMAGE_BASE_URL);
        imageSize = propertyManager.getValue(IMAGE_SIZE);

        if (movieBaseUrl.equalsIgnoreCase("") || imageBaseUrl.equalsIgnoreCase("") || imageSize.equalsIgnoreCase("")) {
            Dialog.createDialogAndExit(MainActivity.this, "Missing Data", "Please update the properties properly");
            isPropReadSuccessfully = false;
            return;
        }

        isPropReadSuccessfully = true;
    }

    private void getMovieDetails(SortType sortType) {

        String sortingType = null;
        if (sortType.toString().equalsIgnoreCase(SortType.SORT_POPULAR.toString())) {
            sortingType = "popularity.desc";
        } else if (sortType.toString().equalsIgnoreCase(SortType.SORT_RATING.toString())) {
            sortingType = "vote_average.desc";
        }

        //Create the URL string
        final String urlString = String.format(movieBaseUrl, sortingType, apiKey);

        //Create http Object
        new AndroidTaskExecutor().execute(new UserListener() {
            ProgressDialog progressBar;

            @Override
            public void onPreExecute() {
                // prepare for a progress bar dialog
                progressBar = new ProgressDialog(MainActivity.this);
                progressBar.setCancelable(false);
                progressBar.setMessage("Retrieving Movie Details...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
            }

            @Override
            public void onPostExecute(Object result) {
                if (progressBar != null)
                    progressBar.dismiss();

                if (result != null) {
                    //Parse Json String as received from Server
                    try {
                        //Update movieDataList
                        JSONObject jsonObject = new JSONObject((String) result);
                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        movieDetailsList = new ArrayList<>();
                        imageUrl = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonObject = jsonArray.getJSONObject(i);

                            //Update ImagePath
                            String strImagePath = imageBaseUrl + "/" + imageSize + "/" + jsonObject.getString("poster_path");
                            imageUrl.add(strImagePath);
                            movieDetailsList.add(new MovieDetails(jsonObject.getString("id"), strImagePath,
                                    jsonObject.getString("title"), jsonObject.getString("overview"),
                                    jsonObject.getString("vote_average"), jsonObject.getString("release_date")));
                        }

                        if (movieDetailsList != null) {
                            //Set Adapter
                            imageAdapter = new ImageAdapter(getApplicationContext(), imageUrl);
                            gridview.setAdapter(imageAdapter);
                        }
                    } catch (JSONException e) {
                        movieDetailsList = null;
                    }
                }
            }
        }, new TaskListener() {
            @Override
            public Object execute() {

                AndroidHttpReq httpReq = new AndroidHttpReq(urlString);
                return httpReq.httpGetRequest();
            }
        });


    }

    /**
     * Returns the current sort Order from SharedPreferneces
     *
     * @return
     */
    private SortType getCurrentSortType() {

        //Default by popularity
        String sortStringVal = appPrefernces.getString(SORT_TYPE, SortType.SORT_POPULAR.toString());
        return SortType.valueOf(sortStringVal);
    }

    /**
     * Updates the sort Order in SharedPreferneces
     *
     * @return
     */
    private void updateSortType(SortType sortType) {

        SharedPreferences.Editor editor = appPrefernces.edit();
        editor.putString(SORT_TYPE, sortType.toString());
        editor.commit();
    }

    private void hideMenuItem(MenuItem item) {

        //Set visibility of all other items as true
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(true);
        }

        //Set visibility of the given item as false
        item.setVisible(false);
    }

    private void chkInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return;
        }

        Dialog.createDialogAndExit(MainActivity.this, "Internet Connection Problem", "Please check your internet connection");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPrefernces = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);

        gridview = (GridView) findViewById(R.id.gridView);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent details = new Intent(getApplicationContext(), DetailsActivity.class);
                details.putExtra(Intent.EXTRA_TEXT, movieDetailsList.get(position));
                startActivity(details);
            }
        });

        //Read all the properties
        readProperties();
        if (isPropReadSuccessfully) {

            //Chk if internet is connected
            chkInternetConnection();

            //Get Movie Details from the server
            getMovieDetails(getCurrentSortType());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //get the Menu object
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sort, menu);

        //get Current Sort Type
        switch (getCurrentSortType()) {

            case SORT_RATING:
                hideMenuItem(menu.findItem(R.id.sort_rating));
                break;

            case SORT_POPULAR:
                hideMenuItem(menu.findItem(R.id.sort_popular).setVisible(false));
                break;

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (isPropReadSuccessfully) {
            int id = item.getItemId();

            //Hide the chosen menu item
            hideMenuItem(item);

            SortType sortType = SortType.SORT_POPULAR;
            switch (id) {

                case R.id.sort_popular:
                    sortType = SortType.SORT_POPULAR;
                    break;

                case R.id.sort_rating:
                    sortType = SortType.SORT_RATING;
                    break;

                default:
                    break;
            }

            //Update Sort Type
            updateSortType(sortType);
            getMovieDetails(sortType);
        }

        return super.onOptionsItemSelected(item);
    }

}
