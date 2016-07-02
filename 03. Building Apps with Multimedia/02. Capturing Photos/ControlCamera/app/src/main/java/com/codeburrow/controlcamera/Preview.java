package com.codeburrow.controlcamera;

import android.content.Context;
import android.hardware.Camera;
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
public class Preview extends ViewGroup implements SurfaceHolder.Callback{

    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    // The Camera object that the preview class must be passed to,
    // before the live image preview can be started.
    Camera mCamera;
    private List<Camera.Size> mSupportedPreviewSizes;

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

    /**
     * Helper Method.
     *
     * Creates a camera instance and its related preview, in a specific order.
     * - camera object
     * - preview
     *
     * The process of initializing the camera is encapsulated
     * so that Camera.startPreview() is called by the setCamera() method,
     * whenever the user does something to change the camera.
     *
     * The preview must also be restarted in the preview class
     * surfaceChanged() callback method.
     *
     * @param camera
     */
    public void setCamera(Camera camera) {
        if (mCamera == camera) { return; }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
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

}
