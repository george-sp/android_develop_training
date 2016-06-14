package com.codeburrow.systempermissions;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RequestPermissionsAtRunTimeActivity extends AppCompatActivity {

    private static final String LOG_TAG = RequestPermissionsAtRunTimeActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions_at_run_time);
    }

    /**
     * Handle the permissions request response.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e(LOG_TAG, "onRequestPermissionsResult()");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    Log.e(LOG_TAG, "grantResults.length > 0");

                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        Toast.makeText(RequestPermissionsAtRunTimeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                        Toast.makeText(RequestPermissionsAtRunTimeActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Request the permission you need.
     * <p/>
     * The following code checks if the app has permission to read the user's contacts,
     * and requests the permission if necessary.
     *
     * @param view
     */
    public void requestContactsPermissions(View view) {
        // Build.VERSION_CODES.M == 23
        if (Build.VERSION.SDK_INT >= 23) {
            int hasReadContactsPermission;
            hasReadContactsPermission = checkSelfPermission("android.permission.READ_CONTACTS");

            if (hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    showMessageOKCancel("Custom Message: You need to allow access to Contacts",
                            new DialogInterface.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                                }
                            });
                } else {

                    // No explanation needed, we can request the permission.

                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                Toast.makeText(RequestPermissionsAtRunTimeActivity.this, "Permission already granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RequestPermissionsAtRunTimeActivity.this, "Use a device with SDK Version 23\n\n ANDROID 6.0", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper Method to show an AlertDialog
     *
     * @param message
     * @param okListener
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(RequestPermissionsAtRunTimeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
