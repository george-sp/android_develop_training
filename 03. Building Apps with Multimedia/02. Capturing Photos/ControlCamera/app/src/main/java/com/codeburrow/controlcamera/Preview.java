package com.codeburrow.controlcamera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
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
 * The preview also requires an implementation of the SurfaceHolder.Callback interface,
 * which is used to pass image data from the camera hardware to the application.
 */
public class Preview extends ViewGroup implements SurfaceHolder.Callback {

    private static final String LOG_TAG = "Camera/Preview";
    // Use a SurfaceView to draw previews of what the camera sensor is picking up.
    SurfaceView mSurfaceView;
    // Abstract interface to someone holding a display surface.
    SurfaceHolder mHolder;
    // The Camera object that the preview class must be passed to,
    // before the live image preview can be started.
    Camera mCamera;
    // A list of supported Size objects.
    List<Camera.Size> mSupportedPreviewSizes;
    // The previewed image size (width and height dimensions).
    Size mPreviewSize;

    Preview(Context context) {
        super(context);
        Log.e(LOG_TAG, "=====> Preview Constructor");

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
     * Measures the view and its content to determine the measured width and measured height.
     * <p>
     * If this method is overridden,
     * it is the subclass's responsibility to make sure the measured height and width
     * are at least the view's minimum height and width
     * (getSuggestedMinimumHeight() and getSuggestedMinimumWidth()).
     * -------------------------------------------------------------------------------
     * http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
     *
     * @param widthMeasureSpec  int: Horizontal space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     * @param heightMeasureSpec int: Vertical space requirements as imposed by the parent. The requirements are encoded with View.MeasureSpec.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(LOG_TAG, "=====> onMeasure");

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        // This method must be called by onMeasure(int, int) to store the measured height.
        // Failing to do so will trigger an exception at measurement time.
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            int i = 0;
            for (Size size : mSupportedPreviewSizes) {
                i++;
                Log.e(LOG_TAG, "Size_" + i + ": (width) " + size.width + " /(height) " + size.height);
            }
            mPreviewSize = mSupportedPreviewSizes.get(0);
        }
    }

    /**
     * Called from layout when this view should assign a size and position to each of this children.
     * Derived classes with children should override this method
     * and call layout on each of their children.
     *
     * @param changed boolean: This is a new size or position for this view.
     * @param left    int: Left position, relative to parent.
     * @param top     int: Top position, relative to parent.
     * @param right   int: Right position, relative to parent.
     * @param bottom  int: Bottom position, relative to parent.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e(LOG_TAG, "=====> onLayout");

        if (changed && getChildCount() > 0) {
            final View child = getChildAt(0);

            final int width = right;
            final int height = bottom;

            /*
             * Assign a size and position to a view and all of its descendants.
             * This is the second phase of the layout mechanism. (The first is measuring).
             * In this phase, each parent calls layout on all of its children to position them.
             */
            child.layout(0, 0, width, height);
        }

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
        Log.e(LOG_TAG, "=====> setCamera");

        if (mCamera == camera) {
            return;
        }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
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
        Log.e(LOG_TAG, "=====> surfaceCreated");

        try {
            if (mCamera != null) {
                // Set the Surface to be used for live preview.
                mCamera.setPreviewDisplay(surfaceHolder);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.e(LOG_TAG, "=====> surfaceChanged");

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
        Log.e(LOG_TAG, "=====> surfaceDestroyed");

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
        Log.e(LOG_TAG, "=====> stopPreviewAndFreeCamera");

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
