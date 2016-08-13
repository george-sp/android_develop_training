
package com.codeburrow.animateviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

public class BasicTransitionActivity extends AppCompatActivity {

    private static final String LOG_TAG = BasicTransitionActivity.class.getSimpleName();

    // There are two Scene objects based on view hierarchies.
    // Create scenes from layout resources.
    Scene mAScene;
    Scene mAnotherScene;
    // Both scenes use the scene root defined by the FrameLayout element in activity_basic_transition.
    private ViewGroup mSceneRoot;

    // Create a Transaction instance from a resource file.
    Transition mFadeTransition;
    Transition mMultipleTransitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_transition);

        // Create the scene root for the scenes in this app
        mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);

        // Create the scenes
        mAScene = Scene.getSceneForLayout(mSceneRoot, R.layout.a_scene, this);
        mAnotherScene = Scene.getSceneForLayout(mSceneRoot, R.layout.another_scene, this);

        // Create a transition instance from a resource file.
        mFadeTransition = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        mMultipleTransitions = TransitionInflater.from(this).inflateTransition(R.transition.multiple_transitions);
    }

    public void applyFadeTransition(View view) {
        // Apply a Transition.
        TransitionManager.go(mAnotherScene, mFadeTransition);
    }

    public void applyMultipleTransitions(View view) {
        // Apply a Transition.
        TransitionManager.go(mAnotherScene, mMultipleTransitions);
    }
}