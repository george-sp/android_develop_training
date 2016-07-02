package com.codeburrow.controlcamera;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // The Camera Object.
    private Camera mCamera;
    // The Preview Object.
    private Preview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        safeCameraOpen(0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCameraAndPreview();
    }

    /**
     * Helper Method.
     * <p/>
     * Getting an instance of the Camera object
     * is the first step in the process of directly controlling the camera.
     * <p/>
     * As Android's own Camera application does,
     * the recommended way to access the camera is to open Camera
     * on a separate thread that's launched from onCreate().
     * This approach is a good idea since it can take a while
     * and might bog down the UI thread.
     * <p/>
     * In a more basic implementation, opening the camera can be deferred
     * to the onResume() method to facilitate code reuse and
     * keep the flow of control simple.
     *
     * @param id
     * @return
     */
    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            /*
             * Calling Camera.open() throws an exception
             * if the camera is already in use by another application,
             * so we wrap it in a try block.
             */
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    /**
     * Helper Method.
     * <p/>
     * Release the camera for use by other applications.
     * Applications should release the camera immediately in onPause().
     */
    private void releaseCameraAndPreview() {
        mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}
