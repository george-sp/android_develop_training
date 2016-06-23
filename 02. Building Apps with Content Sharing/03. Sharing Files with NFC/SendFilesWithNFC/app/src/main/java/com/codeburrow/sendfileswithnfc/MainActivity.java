package com.codeburrow.sendfileswithnfc;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    NfcAdapter mNfcAdapter;
    // Flag to indicate that Android Beam is available.
    boolean mAndroidBeamAvailable = false;
    // List of URIs to provide to Android Beam
    private Uri[] mFileUris = new Uri[10];
    // Instance that returns available files from this app
    private FileUriCallback mFileUriCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NFC isn't available on the device
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            /*
             * Disable NFC features here.
             * For example, disable menu items or buttons that activate
             * NFC-related features
             */
            Log.e(LOG_TAG, "NFC isn't available on device.");
        }
        // Android Beam file transfer isn't supported
        else if (Build.VERSION.SDK_INT <
                Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // If Android Beam isn't available, don't continue.
            mAndroidBeamAvailable = false;
            /*
             * Disable Android Beam file transfer features here.
             */
            Log.e(LOG_TAG, "Android Beam file transfer isn't supported.");
        }
        // Android Beam file transfer is available, continue.
        else {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            Log.e(LOG_TAG, "Android Beam file transfer is available, continue.");
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            /*
             * Instantiate a new FileUriCallback to handle requests for URIs
             */
            mFileUriCallback = new FileUriCallback();
            // Set the dynamic callback for URI requests.
            mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback, this);
        }
    }

    /**
     * Callback that Android Beam file transfer calls to get files to share
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {

        public FileUriCallback() {
        }

        /**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {
            return mFileUris;
        }
    }
}
