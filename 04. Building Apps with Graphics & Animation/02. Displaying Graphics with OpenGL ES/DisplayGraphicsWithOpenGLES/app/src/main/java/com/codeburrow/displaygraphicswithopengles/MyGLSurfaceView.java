package com.codeburrow.displaygraphicswithopengles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * A GLSurfaceView is a specialized view where you can draw OpenGL ES graphics.
 * It does not do much by itself.
 * The actual drawing of objects is controlled in the GLSurfaceView.Renderer
 * that you set on this view.
 * In fact, the code for this object is so thin, you may be tempted to skip extending it
 * and just create an unmodified GLSurfaceView instance, but don’t do that.
 * You need to extend this class in order to capture touch events,
 * which is covered in the Responding to Touch Events lesson.
 * <p/>
 * The essential code for a GLSurfaceView is minimal, so for a quick implementation,
 * it is common to just create an inner class in the activity that uses it
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private static final String LOG_TAG = MyGLSurfaceView.class.getSimpleName();

    private final MyGLRenderer mRenderer;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();
        // Set the Renderer for drawing on the GLSurfaceView.
        setRenderer(mRenderer);

        /*
         * Render the view only when there is a change in the drawing data.
         * Unless you have objects changing without any user interaction, it’s usually a good idea have this flag turned on.
         * To allow the triangle to rotate automatically, this line is commented out:
         */
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen and other input controls.
        // In this case, you are only interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                // Reverse direction of rotation above the mid-line.
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // Reverse direction of rotation to left of the mid-line.
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
