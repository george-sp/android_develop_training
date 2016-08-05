package com.codeburrow.displaygraphicswithopengles;

import android.content.Context;
import android.opengl.GLSurfaceView;

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

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();
        // Set the Renderer for drawing on the GLSurfaceView.
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
