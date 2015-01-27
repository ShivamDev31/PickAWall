package com.droidacid.pickawall.app;

/**
 * Created by shivam.chopra on 13-01-2015.
 * In this class app the app constants will be defined
 * which will be called throughout the app.
 */
public class AppConstants {

    // Number of columns in the GridView
    // By default we are setting it to 2 but user can change it in settings
    public static final int NUM_OF_COLS = 2;

    // GridView image padding in dp
    public static final int GRID_PADDING = 4;

    // Directory / folder name in the SDCARD where downloaded wallpapers will be downloaded
    public static final String SDCARD_DIR = "PicAWall";

    // Picasa / Google web album username(default is our own username)
    public static final String PICASA_USERNAME = "freewallpapersapp";
    // public static final String PICASA_USERNAME = "picawall";

    // Public albums list url
    public static final String PICASA_ALBUMS_URL = "https://picasaweb.google.com/data/feed/api/user/_PICASA_USER_?kind=album&alt=json";

    // Picasa album photos url
    public static final String ALBUM_PHOTOS_URL = "https://picasaweb.google.com/data/feed/api/user/_PICASA_USER_/albumid/_ALBUM_ID_?alt=json";

    // Picasa recently added photos url
    public static final String RECENTLY_ADDED_PHOTOS_URL = "https://picasaweb.google.com/data/feed/api/user/_PICASA_USER_?kind=photo&alt=json";

}
