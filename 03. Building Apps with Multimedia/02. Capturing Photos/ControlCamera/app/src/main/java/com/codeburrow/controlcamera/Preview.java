package com.codeburrow.controlcamera;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

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
}
