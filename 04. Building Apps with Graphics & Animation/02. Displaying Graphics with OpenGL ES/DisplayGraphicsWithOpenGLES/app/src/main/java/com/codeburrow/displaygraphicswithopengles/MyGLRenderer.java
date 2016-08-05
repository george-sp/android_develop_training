package com.codeburrow.displaygraphicswithopengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * A very basic implementation of an OpenGL ES renderer,
 * that does nothing more than draw a black background in the GLSurfaceView
 * <p/>
 * The implementation of the GLSurfaceView.Renderer class, or renderer,
 * within an application that uses OpenGL ES is where things start to get interesting.
 * This class controls what gets drawn on the GLSurfaceView with which it is associated.
 * There are three methods in a renderer that are called by the Android system
 * in order to figure out what and how to draw on a GLSurfaceView:
 * - onSurfaceCreated() - Called once to set up the view's OpenGL ES environment.
 * - onDrawFrame() - Called for each redraw of the view.
 * - onSurfaceChanged() - Called if the geometry of the view changes, for example when the device's screen orientation changes.
 * <p/>
 * Note:
 * You may wonder why these methods have a GL10 parameter, when you are using the OpengGL ES 2.0 APIs.
 * These method signatures are simply reused for the 2.0 APIs to keep the Android framework code simpler.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String LOG_TAG = MyGLRenderer.class.getSimpleName();

    /**
     * Called once to set up the view's OpenGL ES environment.
     *
     * @param unused GL10: the GL interface. Use instanceof to test if the interface supports GL11 or higher interfaces.
     * @param config EGLConfig: the EGLConfig of the created surface. Can be used to create matching pbuffers.
     */
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Called for each redraw of the view.
     *
     * @param unused GL10: the GL interface. Use instanceof to test if the interface supports GL11 or higher interfaces.
     */
    @Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Called if the geometry of the view changes, for example when the device's screen orientation changes.
     *
     * @param unused GL10: the GL interface. Use instanceof to test if the interface supports GL11 or higher interfaces.
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
