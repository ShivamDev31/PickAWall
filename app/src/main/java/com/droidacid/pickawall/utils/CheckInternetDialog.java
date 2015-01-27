package com.droidacid.pickawall.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by shivam.chopra on 28-01-2015.
 */
public class CheckInternetDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder internetDialog = new AlertDialog.Builder(getActivity());
        internetDialog.setTitle("Internet error!");
        internetDialog.setMessage("No internet connection found. Please check your internet connection.");
        internetDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        return super.onCreateDialog(savedInstanceState);
    }
}
