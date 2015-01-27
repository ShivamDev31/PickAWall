package com.droidacid.pickawall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.droidacid.pickawall.utils.PrefManager;


public class SettingsActivity extends ActionBarActivity {

    private PrefManager pref;
    private EditText etGoogleUsername, etNoOfGridColumns, etGalleryName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        etGoogleUsername = (EditText) findViewById(R.id.etGoogleUsername);
        etNoOfGridColumns = (EditText) findViewById(R.id.etNoOfCols);
        etGalleryName = (EditText) findViewById(R.id.etGalleryName);

        pref = new PrefManager(getApplicationContext());

        // Display EditText values stored in shared preferences

        // Google Username
        etGoogleUsername.setText(pref.getGoogleUsername());

        // Number of grid columns
        etNoOfGridColumns.setText(String.valueOf(pref.getNoOfColumns()));

        // Gallery name
        etGalleryName.setText(pref.getGalleryName());
    }


    // save the settings
    public void saveSettings(View view) {
        // validating the data before saving it into shared preferences

        // validating Google username
        String googleUsername = etGoogleUsername.getText().toString().trim();

        if(googleUsername.length() == 0) {
            L.t(getApplicationContext(), getString(R.string.toast_enter_google_username));
            return;
        }

        // Validating number of grid columns
        String noOfGridColumns = etNoOfGridColumns.getText().toString().trim();

        if(noOfGridColumns.length() == 0 || !isInteger(noOfGridColumns)) {
            L.t(getApplicationContext(), getString(R.string.toast_enter_valid_grid_columns));
            return;
        }

        // Validate gallery name
        String galleryName = etGalleryName.getText().toString().trim();
        if(galleryName.length() == 0) {
            L.t(getApplicationContext(), getString(R.string.toast_enter_gallery_name));
            return;
        }

        // Check for setting changes

        if(!googleUsername.equalsIgnoreCase(pref.getGoogleUsername()) ||
                !noOfGridColumns.equalsIgnoreCase(String.valueOf(pref.getGoogleUsername()))
                || !galleryName.equalsIgnoreCase(pref.getGalleryName())) {

            // User changed the settings
            // save the changes and launch SplashScreen to initialize the app again
            pref.setGoogleUsername(googleUsername);
            pref.setNoOfColumns(Integer.parseInt(noOfGridColumns));
            pref.setGalleryName(galleryName);

            // start the app again from SplashScreen
            Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);

            // Clear all the previous activities
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // user not modified any values in the form
            // skip saving to shared preferences
            // just go back to previous activity
            onBackPressed();
        }
    }
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void resetSettings(View view) {
        // Display default EditText values stored in shared preferences

        // Default Google Username
        pref.setGoogleUsername("picawall");

        // Default  Number of grid columns
        pref.setNoOfColumns(2);

        // Default Gallery name
        pref.setGalleryName("PicAWall");
    }
}
