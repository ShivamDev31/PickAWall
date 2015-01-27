package com.droidacid.pickawall.utils;


import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.droidacid.pickawall.L;
import com.droidacid.pickawall.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by shivam.chopra on 13-01-2015.
 * Utils class get the Screen Size
 * Saves the image to SDCARD
 * Sets the image as wallpaper
 */
public class Utils {
    private String TAG = Utils.class.getSimpleName();
    private Context context;
    private PrefManager pref;

    // Utils constructor
    public Utils(Context context) {
        this.context = context;
        pref = new PrefManager(context);
    }

    // Getting the screen width
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public void saveImageToSDCard(Bitmap bitmap) {
        File myDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), pref.getGalleryName());

        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fName = "PicAWall-" + n + ".jpg";
        File file = new File(myDir, fName);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            L.t(context, context.getString(R.string.toast_saved).replace("#", "\"" +
                    pref.getGalleryName() + "\""));
            L.l("Wallpaper saved to : " + file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            L.l(TAG, e.toString());
            L.t(context, R.string.toast_saved_failed);
        } catch (IOException e) {
            L.l(TAG, e.toString());
            L.t(context, R.string.toast_saved_failed);
        }

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(context,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    public void setAsWallpaper(Bitmap bitmap) {

        WallpaperManager wallMan;

        try {
            wallMan = WallpaperManager.getInstance(context);
            wallMan.setBitmap(bitmap);
            L.t(context, R.string.toast_wallpaper_set);
        } catch (IOException e) {
            L.l(e.toString());
            L.t(context, R.string.toast_wallpaper_set_failed);
        }
    }


    /*
    * public void shareImage(){
BitmapDrawable bitmapDrawable = (BitmapDrawable)fullImageView.getDrawable();
Bitmap bitmap = bitmapDrawable.getBitmap();
// Save this bitmap to a file.
File cache = this.getExternalCacheDir();
Random generator = new Random();
int n = 10000;
n = generator.nextInt(n);
File sharefile = new File(cache, "Wallpaper-" + n + ".jpg"); //give your name and save it.
try {
FileOutputStream out = new FileOutputStream(sharefile);

bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

out.flush();

out.close();

} catch (IOException e) {

}

// Now send it out to share

Intent share = new Intent(android.content.Intent.ACTION_SEND);

share.setType("image/*");

share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));

try {

this.startActivity(Intent.createChooser(share, "Share photo"));

} catch (Exception e) {

}

}
return true;
}

}
    * */
}
