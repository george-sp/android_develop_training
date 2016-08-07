
package com.codeburrow.animateviews;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // There are two Scene objects based on view hierarchies.
    Scene mAScene;
    Scene mAnotherScene;
    // Both scenes use the scene root defined by the FrameLayout element in activity_main.
    private ViewGroup mSceneRoot;

    Scene mASceneInCode;
    View mViewHierarchy;

    // Create a Transaction instance from a resource file.
    Transition mFadeTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the scene root for the scenes in this app
        mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);

        // Create the scenes
        mAScene = Scene.getSceneForLayout(mSceneRoot, R.layout.a_scene, this);
        mAnotherScene = Scene.getSceneForLayout(mSceneRoot, R.layout.another_scene, this);

        // Obtain the scene root element
        mSceneRoot = (ViewGroup) mSomeLayoutElement;

        // Obtain the view hierarchy to add as a child of
        // the scene root when this scene is entered
        mViewHierarchy = (ViewGroup) someOtherLayoutElement;

        // Create a scene
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mASceneInCode = new Scene(mSceneRoot, mViewHierarchy);
        }

        // Create a transition instance from a resource file.
        mFadeTransition = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        // Create a transition instance in your code.
        Transition mFadeTransitionInCode = new Fade();
    }
}