
package com.codeburrow.animateviews;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // There are two Scene objects based on view hierarchies.
    Scene mAScene;
    Scene mAnotherScene;
    Scene mEndingScene;
    // Both scenes use the scene root defined by the FrameLayout element in activity_main.
    private ViewGroup mSceneRoot;

    Scene mASceneInCode;
    View mViewHierarchy;

    // Create a Transaction instance from a resource file.
    Transition mFadeTransition;

    private TextView mLabelText;
    private Fade mFade;
    private ViewGroup mRootView;

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
//        mSceneRoot = (ViewGroup) mSomeLayoutElement;

        // Obtain the view hierarchy to add as a child of
        // the scene root when this scene is entered
//        mViewHierarchy = (ViewGroup) someOtherLayoutElement;

        // Create a scene
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mASceneInCode = new Scene(mSceneRoot, mViewHierarchy);
        }

        // Create a transition instance from a resource file.
        mFadeTransition = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        // Create a transition instance in your code.
//        Transition mFadeTransitionInCode = new Fade();

        mEndingScene = mAnotherScene;
        // Apply a Transition.
        TransitionManager.go(mEndingScene, mFadeTransition);

        // Create a new TextView and set some View properties
        mLabelText = new TextView(this);
        mLabelText.setText("Label");
        mLabelText.setId(1);

        // Get the root view and create a transition
        mRootView = mSceneRoot;
        mFade = new Fade(Fade.IN);

        // Start recording changes to the view hierarchy
        TransitionManager.beginDelayedTransition(mRootView, mFade);

        // Add the new TextView to the view hierarchy
        mRootView.addView(mLabelText);

        // When the system redraws the screen to show this update,
        // the framework will animate the addition as a fade in

    }
}