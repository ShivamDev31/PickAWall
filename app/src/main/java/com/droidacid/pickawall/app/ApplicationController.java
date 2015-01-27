package com.droidacid.pickawall.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.droidacid.pickawall.utils.LruBitmapCache;
import com.droidacid.pickawall.utils.PrefManager;

/**
 * Created by shivam.chopra on 13-01-2015.
 * This is the Application class where all the Volley and Cache setup will be done
 */
public class ApplicationController extends Application {

    public static final String TAG = ApplicationController.class.getSimpleName();
    // Volley RequestQueue for requests
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;
    private static ApplicationController mInstance;
    private PrefManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        pref = new PrefManager(this);
    }

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    public PrefManager getPrefManager() {
        if(pref == null) {
            pref = new PrefManager(this);
        }
        return pref;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if(mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }
        return mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if(mLruBitmapCache == null) {
            mLruBitmapCache = new LruBitmapCache();
        }
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
