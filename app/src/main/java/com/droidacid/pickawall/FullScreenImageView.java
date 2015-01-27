package com.droidacid.pickawall;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.droidacid.pickawall.app.AppController;
import com.droidacid.pickawall.picasa.model.Wallpaper;
import com.droidacid.pickawall.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
* This class takes care of calculating the image aspect ratio in fullscreen mode.
* The image width will calculated respective to device height.
* Below steps will be execute in order show the fullscreen wallpaper.
*
*> User selects the wallpaper from GridView. The selected wallpaper will be passed to fullscreen image activity.
*> In fullscreen activity, we make another call to Picasa to get the selected wallpaper’s high resolution version.
*> The downloaded wallpaper width will be calculated depending upon device height.
*> Then the image will be displayed in ImageView. The HorizontalScrollView make the wallpaper scrollable horizontally.
*/

public class FullScreenImageView extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = FullScreenImageView.class.getSimpleName();
    public static final String TAG_SEL_IMAGE = "selectedImage";
    private Wallpaper selectedPhoto;
    private ImageView fullImageView;
    private LinearLayout llSetWallpaper, llDownloadWallpaper;
    private Utils utils;
    private ProgressBar pbLoader;

    // Picasa JSON response node keys
    private static final String TAG_ENTRY = "entry", TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
            TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image_view);

        fullImageView = (ImageView) findViewById(R.id.imgFullScreen);
        llSetWallpaper = (LinearLayout) findViewById(R.id.llSetWallpaper);
        llDownloadWallpaper = (LinearLayout) findViewById(R.id.llDownloadWallpaper);
        pbLoader = (ProgressBar) findViewById(R.id.pbLoader);

        // hide the action bar in fullscreen mode
        getSupportActionBar().hide();

        utils = new Utils(getApplicationContext());

        // Layout click listeners
        llSetWallpaper.setOnClickListener(this);
        llDownloadWallpaper.setOnClickListener(this);

        // Setting layout buttons alpha/opacity
        llSetWallpaper.getBackground().setAlpha(70);
        llDownloadWallpaper.getBackground().setAlpha(70);

        Intent intent = getIntent();
        selectedPhoto = (Wallpaper) intent.getSerializableExtra(TAG_SEL_IMAGE);

        // check for selected photo is null or not
        if(selectedPhoto != null) {
            // fetch photo full resolution image by making another json request
            fetchFullResolutionImage();
        } else {
            L.t(getApplicationContext(), getString(R.string.msg_unknown_error));
        }
    }

    // Fetching image full resolution image
    private void fetchFullResolutionImage() {
        String url = selectedPhoto.getPhotoJson();

        // Show the loader before making the request
        pbLoader.setVisibility(View.VISIBLE);
        llSetWallpaper.setVisibility(View.GONE);
        llDownloadWallpaper.setVisibility(View.GONE);

        // Volley's json object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        L.l(TAG, "Image full resolution json : " + response.toString());
                        try {
                            // Parsing the json response
                            JSONObject entry = response.getJSONObject(TAG_ENTRY);
                            JSONArray mediaContentArray = entry.getJSONObject(TAG_MEDIA_GROUP).
                            getJSONArray(TAG_MEDIA_CONTENT);
                            JSONObject mediaObj = (JSONObject) mediaContentArray.get(0);

                            // get the full resolution url
                            String fullResolutionImageUrl = mediaObj.getString(TAG_IMG_URL);

                            // Image full resolution width and height
                            final int width = mediaObj.getInt(TAG_IMG_WIDTH);
                            final int height = mediaObj.getInt(TAG_IMG_HEIGHT);

                            L.l(TAG, " Full resolution image url is : " + fullResolutionImageUrl
                                    + ", width : " + width + " , height : " + height);

                            ImageLoader imageLoader = AppController.getInstance().getImageLoader();

                            // We download image into ImageView instead of NetworkImageView
                            // to have callback methods
                            // Currently NetworkImageView doesn't have callback methods

                            imageLoader.get(fullResolutionImageUrl, new ImageLoader.ImageListener() {
                                @Override
                                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                                    if(imageContainer.getBitmap() != null) {
                                        // load bitmap into ImageView
                                        fullImageView.setImageBitmap(imageContainer.getBitmap());
                                        adjustImageAspect(width, height);

                                        // Hide loader and show set and download buttons
                                        pbLoader.setVisibility(View.GONE);
                                        llSetWallpaper.setVisibility(View.VISIBLE);
                                        llDownloadWallpaper.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    L.t(getApplicationContext(), getString(R.string.msg_wall_fetch_error));
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            L.t(getApplicationContext(), getString(R.string.msg_unknown_error));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.l(TAG, "Error : " + volleyError.getMessage());
                // Unable to fetch wallpapers
                // either google username is wrong pr
                // device dosn't have internet connection
                L.t(getApplicationContext(), getString(R.string.msg_wall_fetch_error));
            }
        });

        // Remove the url from the cache
        AppController.getInstance().getRequestQueue().getCache().remove(url);

        // Disable the cache for this url, so that it always fetches updated json
        jsonObjectRequest.setShouldCache(false);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }
    /*
    **Adjusting the image aspect ration to scroll horizontally, Image height
    **will be screen height and width will be calculated respected to height
    */
    private void adjustImageAspect(int bWidth, int bHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );

        if(bWidth == 0 || bHeight == 0) {
            return;
        }

        int sHeight = 0;

        if(Build.VERSION.SDK_INT >= 13) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sHeight = size.y;
        } else {
           Display display = getWindowManager().getDefaultDisplay();
           sHeight = display.getHeight();
        }

        int new_width = (int) Math.floor((double) bWidth * (double) sHeight
         / (double) bHeight);
        params.weight = new_width;
        params.height = sHeight;

        L.l(TAG, "Fullscreen image new dimensions : width = " + new_width +
        ", height = " + sHeight);

        fullImageView.setLayoutParams(params);
    }


    // Click listeners

    @Override
    public void onClick(View v) {
        Bitmap bitmap = ((BitmapDrawable) fullImageView.getDrawable()).getBitmap();

        switch(v.getId()) {
            // Download button pressed
            case R.id.llDownloadWallpaper :
                utils.saveImageToSDCard(bitmap);
                break;
            // Set the image as wallpaper
            case R.id.llSetWallpaper :
                utils.setAsWallpaper(bitmap);
                break;
            default :
                break;
        }
    }
}
