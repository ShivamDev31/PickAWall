package com.droidacid.pickawall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.droidacid.pickawall.L;
import com.droidacid.pickawall.app.AppConstants;
import com.droidacid.pickawall.picasa.model.Category;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shivam.chopra on 14-01-2015.
 * This class takes care of storing the data in Shared Preferences.
 * The data like google username, picasa wallpaper categories,
 * gallery name and other things will be stored in shared preferences.
 */
public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    // Shared Preferences Object
    SharedPreferences pref;

    // Editor for shared preferences
    SharedPreferences.Editor editor;

    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // SharedPreferences file name
    private static final String PREF_NAME = "PicAWall";

    // Google / Picassa
    private static final String KEY_GOOGLE_USERNAME = "google_username";

    // No of grid columns
    private static final String KEY_NO_OF_COLUMNS = "no_of_columns";

    // Gallery directory name
    private static final String KEY_GALLERY_NAME = "gallery_name";

    // Gallery album key
    private static final String KEY_ALBUMS = "albums";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    // Storing Google Username
    public void setGoogleUsername(String googleUsername) {
        editor = pref.edit();
        editor.putString(KEY_GOOGLE_USERNAME, googleUsername);
        // Commit changes
        editor.apply();
    }

    // Get Google Username from SharedPrefs
    // and provide a default value too
    public String getGoogleUsername() {
        return pref.getString(KEY_GOOGLE_USERNAME, AppConstants.PICASA_USERNAME);
    }

    // Store number of Grid Columns
    public void setNoOfColumns(int cols) {
        editor = pref.edit();
        editor.putInt(KEY_NO_OF_COLUMNS, cols);
        // Commit changes
        editor.apply();
    }

    // Get the no of stored Grid Columns from SharedPrefs
    public int getNoOfColumns() {
        return pref.getInt(KEY_NO_OF_COLUMNS, AppConstants.NUM_OF_COLS);
    }

    // Store Gallery Name
    public void setGalleryName(String galleryName) {
        editor = pref.edit();
        editor.putString(KEY_GALLERY_NAME, galleryName);
        //Commit changes
        editor.apply();
    }

    // Get gallery name data from shared prefs
    public String getGalleryName() {
        return  pref.getString(KEY_GALLERY_NAME, AppConstants.SDCARD_DIR);
    }

    // Storing albums in Shared Prefs
    public void storeCategories(List<Category> albums) {
        editor = pref.edit();

        // Get the Gson Object and get data from it
        Gson gson = new Gson();
        L.l("Albums : " + gson.toJson(albums));

        editor.putString(KEY_ALBUMS, gson.toJson(albums));

        // Save/ commit/ apply changes in shared prefs
        editor.apply();
    }

    /*
    **Fetching albums from shared prefs
    **Albums will be sorted before returning in alphabetical order
    */
    public List<Category> getCategories() {
        List<Category> albums = new ArrayList<Category>();

        if(pref.contains(KEY_ALBUMS)) {
            String json = pref.getString(KEY_ALBUMS, null);
            Gson gson = new Gson();
            Category[] albumArray = gson.fromJson(json, Category[].class);

            albums = Arrays.asList(albumArray);
            albums = new ArrayList<Category>(albums);
        } else {
            return null;
        }

        List<Category> allAlbums = albums;

        // Sort the albums in alphabetical order
        Collections.sort(allAlbums, new Comparator<Category>() {
            @Override
            public int compare(Category cat1, Category cat2) {
                return cat1.getTitle().compareToIgnoreCase(cat2.getTitle());
            }
        });
        return allAlbums;
    }

    // Comparing albums titles for sorting
    public class CustomComparator implements Comparator<Category> {

        @Override
        public int compare(Category cat1, Category cat2) {
            return cat1.getTitle().compareTo(cat2.getTitle());
        }
    }
}
