package com.thanhtran.redstring.utils;

import android.Manifest;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

/**
 * Created by admin on 12/4/15.
 */
public class PermissionUtils{
    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};


    public static void requestStoragePermissions(final Activity activity, View view) {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(view, " permissions are needed to demonstrate access.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                                    Constants.REQUEST_STORAGE);
                        }
                    }).show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, Constants.REQUEST_STORAGE);
        }
        // END_INCLUDE(contacts_permission_request)
    }
}
