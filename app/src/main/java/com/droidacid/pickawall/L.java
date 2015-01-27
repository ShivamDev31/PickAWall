package com.droidacid.pickawall;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {
	public static void t(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

    public static void t(Context context, int res) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
    }

	public static void l(String msg) {
		Log.d("LOG", msg);
	}
	
	public static void l(String tag, String msg) {
		Log.d(tag, msg);
	}
}
