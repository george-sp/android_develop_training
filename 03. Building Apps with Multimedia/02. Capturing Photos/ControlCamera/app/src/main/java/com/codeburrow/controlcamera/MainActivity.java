package com.codeburrow.controlcamera;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Camera.PictureCallback {

    // The Camera Object.
    private Camera mCamera;
    // The Preview Object.
    private Preview mPreview;
    private int mPreviewState;
    private static final int K_STATE_FROZEN = 0;
    private static final int K_STATE_PREVIEW = 1;
    private static final int K_STATE_BUSY = 2;

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
     * <p>
     * Getting an instance of the Camera object
     * is the first step in the process of directly controlling the camera.
     * <p>
     * As Android's own Camera application does,
     * the recommended way to access the camera is to open Camera
     * on a separate thread that's launched from onCreate().
     * This approach is a good idea since it can take a while
     * and might bog down the UI thread.
     * <p>
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
     * <p>
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

    /**
     * Helper Method.
     * <p>
     * Most camera applications lock the display into landscape mode
     * because that is the natural orientation of the camera sensor.
     * <p>
     * This setting does not prevent you from taking portrait-mode photos,
     * because the orientation of the device is recorded in the EXIF header.
     * <p>
     * ----------------------------------------------------------------------
     * <p>
     * setDisplayOrientation:
     * Set the clockwise rotation of preview display in degrees.
     * This affects the preview frames and the picture displayed after snapshot.
     * This method is useful for portrait mode applications.
     * Note that preview display of front-facing cameras is flipped horizontally
     * before the rotation, that is, the image is reflected
     * along the central vertical axis of the camera sensor.
     * So the users can see themselves as looking into a mirror.
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void takePictureClicked(View view) {
        /*
         * Use the Camera.takePicture() method to take a picture
         * once the preview is started.
         *
         * You can create
         * - Camera.PictureCallback
         * - Camera.ShutterCallback
         * objects and pass them into Camera.takePicture().
         *
         * If you want to grab images continuously,
         * you can create a Camera.PreviewCallback that implements onPreviewFrame().
         * For something in between, you can capture only selected preview frames,
         * or set up a delayed action to call takePicture().
         */
//        mCamera.takePicture(null, null, this);
        switch (mPreviewState) {
            case K_STATE_FROZEN:
                mCamera.startPreview();
                mPreviewState = K_STATE_PREVIEW;
                break;

            default:
                mCamera.takePicture(null, this, null);
                mPreviewState = K_STATE_BUSY;
        } // switch
        shutterBtnConfig();
    }

    /**
     * After a picture is taken, you must restart the preview
     * before the user can take another picture.
     * (In this example) The restart is done by overloading the shutter button.
     */
    public void shutterBtnConfig() {
    }

    /**
     * Called when image data is available after a picture is taken.
     * The format of the data depends on:
     * - the context of the callback
     * - Camera.Parameters settings
     *
     * @param bytes  A byte array of the picture data.
     * @param camera The Camera service object.
     */
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {

    }
}
