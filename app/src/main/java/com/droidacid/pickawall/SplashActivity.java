package com.droidacid.pickawall;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.droidacid.pickawall.app.AppConstants;
import com.droidacid.pickawall.app.AppController;
import com.droidacid.pickawall.picasa.model.Category;
import com.droidacid.pickawall.utils.CheckInternetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends ActionBarActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_GPHOTO_ID = "gphoto$id", TAG_T = "$t", TAG_ALBUM_TITLE = "title";
    ProgressBar pbSplashScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        pbSplashScreen = (ProgressBar) findViewById(R.id.pbSplashLoader);
        pbSplashScreen.setVisibility(View.VISIBLE);
        checkInternetConnection();
        getJsonData();
    }

    private void checkInternetConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if(netInfo != null && !netInfo.isConnected()) {
            CheckInternetDialog checkInternetDialog = new CheckInternetDialog();
            checkInternetDialog.show(getSupportFragmentManager(), "checkInternetConnection");
        }
    }

    private void getJsonData() {

        // Picasa request to get list of albums
        String url = AppConstants.PICASA_ALBUMS_URL
                .replace("_PICASA_USER_", AppController.
                        getInstance().getPrefManager().getGoogleUsername());
        L.l("Albums request url : " + url);

        // Preparing volley's json object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        L.l("Albums Response: " + response.toString());
                        List<Category> albums = new ArrayList<Category>();

                        try {
                            JSONArray albumEntry = response.getJSONObject(TAG_FEED)
                                    .getJSONArray(TAG_ENTRY);
                            // Loop through albums nodes and add them to the album list
                            for (int i = 0; i < albumEntry.length(); i++) {
                                JSONObject albumObj = (JSONObject) albumEntry.get(i);
                                // album id
                                String albumId = albumObj.getJSONObject(TAG_GPHOTO_ID)
                                        .getString(TAG_T);

                                // album title
                                String albumTitle = albumObj.getJSONObject(TAG_ALBUM_TITLE)
                                        .getString(TAG_T);

                                Category album = new Category();
                                album.setId(albumId);
                                album.setTitle(albumTitle);

                                // add album to list
                                albums.add(album);

                                L.l("Album Id: " + albumId + ", Album Title: " + albumTitle);
                            }
                            // Store albums in shared prefs
                            AppController.getInstance().getPrefManager().storeCategories(albums);

                            // Hiding the progressbar before starting next activity
                            pbSplashScreen.setVisibility(View.GONE);

                            // String the main activity
                            Intent intent = new Intent(getApplicationContext(), MyMainActivity.class);
                            startActivity(intent);
                            // Closing splash screen after MainActivity opens
                            finish();
                        } catch (JSONException e) {
                            L.l("Volley catch exception : " + e.toString());
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        L.l("Volley Error in Splash Screen : " + volleyError.toString());
                        L.t(getApplicationContext(), "You are a bad boy");

                        // Unable to fetch albums
                        // check for existing Albums data in Shared Preferences
                        if (AppController.getInstance().getPrefManager().getCategories()
                                != null && AppController.getInstance().getPrefManager().getCategories()
                                .size() > 0) {

                            // Hiding the progressbar before starting next activity
                            pbSplashScreen.setVisibility(View.GONE);
                            // String to MainActivity
                            startActivity(new Intent(getApplicationContext(), MyMainActivity.class));
                            // Close the splash screen after opening MainActivity
                            finish();
                        } else {
                            // Albums data not present in the shared preferences
                            // Launch settings activity, so that user can modify
                            // the settings

                            // Hiding the progressbar before starting next activity
                            pbSplashScreen.setVisibility(View.GONE);

                            Intent intent = new Intent(SplashActivity.this,
                                    SettingsActivity.class);
                            // clear all the activities
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                });
        // disable the cache for this request, so that it always fetches updated
        // json
        jsonObjectRequest.setShouldCache(false);

        // Making the request
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
