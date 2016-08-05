package com.codeburrow.displaygraphicswithopengles;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

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

    private Triangle mTriangle;
    private Square mSquare;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

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

        /*
         * Before you do any drawing,
         * you must initialize and load the shapes you plan to draw.
         * Unless the structure (the original coordinates) of the shapes you use
         * in your program change during the course of execution,
         * you should initialize them in the onSurfaceCreated() method of your renderer
         * for memory and processing efficiency.
         */
        // Initialize a triangle.
        mTriangle = new Triangle();
        // Initialize a square.
        mSquare = new Square();
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

        // Draw the triangle object.
        mTriangle.draw();
    }

    /**
     * Called if the geometry of the view changes, for example when the device's screen orientation changes.
     *
     * @param unused GL10: the GL interface. Use instanceof to test if the interface supports GL11 or higher interfaces.
     * @param width  Width of the GLSurfaceView.
     * @param height Height of the GLSurfaceView.
     */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        /*
         * Note:
         *      Just applying a projection transformation to your drawing objects
         *      typically results in a very empty display.
         *      In general, you must also apply a camera view transformation
         *      in order for anything to show up on screen.
         *
         * This code populates a projection matrix, mProjectionMatrix
         * which you can then combine with a camera view transformation in the onDrawFrame().
         */
        // This projection matrix is applied to object coordinates in the onDrawFrame().
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * @param type       Vertex or Fragment shader type.
     * @param shaderCode String containing the shader code.
     * @return Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode) {
        // Create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER).
        int shader = GLES20.glCreateShader(type);

        // Add the source code to the shader and compile it.
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
