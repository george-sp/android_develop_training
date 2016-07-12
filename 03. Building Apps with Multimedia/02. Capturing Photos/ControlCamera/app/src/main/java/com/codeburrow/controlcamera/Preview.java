package com.codeburrow.controlcamera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jul/03/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
/*
 * Taking a picture usually requires that your users see a preview
 * of their subject before clicking the shutter.
 * To do so, you can use a SurfaceView to draw previews
 * of what the camera sensor is picking up.
 */
public class Preview extends ViewGroup implements SurfaceHolder.Callback {

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    // The Camera object that the preview class must be passed to,
    // before the live image preview can be started.
    Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;
    // The previewed image size (width and height dimensions).
    Size mPreviewSize;

    Preview(Context context) {
        super(context);

        mSurfaceView = new SurfaceView(context);
        addView(mSurfaceView);

        /*
         * Install a SurfaceHolder.Callback so we get notified when the
         * underlying surface is created and destroyed.
         */
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    /**
     * Helper Method.
     * <p>
     * Creates a camera instance and its related preview, in a specific order.
     * - camera object
     * - preview
     * <p>
     * The process of initializing the camera is encapsulated
     * so that Camera.startPreview() is called by the setCamera() method,
     * whenever the user does something to change the camera.
     * <p>
     * The preview must also be restarted in the preview class
     * surfaceChanged() callback method.
     *
     * @param camera
     */
    public void setCamera(Camera camera) {
        if (mCamera == camera) {
            return;
        }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
            mSupportedPreviewSizes = localSizes;
            requestLayout();

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
             * Important:
             *              Call startPreview() to start updating the preview surface.
              *             Preview must be started before you can take a picture.
             */
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        /*
         * Now that the size is known,
         * set up the camera parameters and begin the preview.
         */
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        requestLayout();
        mCamera.setParameters(parameters);

        /*
         * Important:
         *              Call startPreview() to start updating the preview surface.
         *              Preview must be started before you can take a picture.
         */
        mCamera.startPreview();
    }

    /**
     * This is called immediately before a surface is being destroyed.
     * After returning from this call, you should no longer try to access this surface.
     * If you have a rendering thread that directly accesses the surface,
     * you must ensure that thread is no longer touching the Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        /*
         * Once your application is done using the camera, it's time to clean up.
         * In particular, you must release the Camera object, or you risk crashing other applications,
         * including new instances of your own application.
         *
         * When should you stop the preview and release the camera?
         * Well, having your preview surface destroyed is a pretty good hint
         * that it’s time to stop the preview and release the camera
         */
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
        }
    }

    /**
     * When this function returns, mCamera will be null.
     * -------------------------------------------------
     * This procedure was also part of the setCamera() method,
     * so initializing a camera always begins with stopping the preview.
     */
    private void stopPreviewAndFreeCamera() {

        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();

            mCamera = null;
        }
    }
}
