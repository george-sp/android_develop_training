package com.codeburrow.displaygraphicswithopengles;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * A minimal implementation of an activity that uses a GLSurfaceView as its primary view
 * <p/>
 * Android applications that use OpenGL ES have activities just like any other application that has a user interface.
 * The main difference from other applications is what you put in the layout for your activity.
 * While in many applications you might use TextView, Button and ListView, in an app that uses OpenGL ES, you can also add a GLSurfaceView.
 */
public class OpenGLES20Activity extends AppCompatActivity {

    private static final String LOG_TAG = OpenGLES20Activity.class.getSimpleName();

    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }
}
