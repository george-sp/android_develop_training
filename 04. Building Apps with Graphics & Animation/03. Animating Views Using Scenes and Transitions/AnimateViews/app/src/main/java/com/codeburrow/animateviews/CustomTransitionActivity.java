package com.codeburrow.animateviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class CustomTransitionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = CustomTransitionActivity.class.getSimpleName();

    private static final String STATE_CURRENT_SCENE = "current_scene";

    /**
     * These are the Scenes we use.
     */
    private Scene[] mScenes;

    /**
     * The current index for mScenes.
     */
    private int mCurrentScene;

    /**
     * This is the custom Transition we use in this sample.
     */
    private Transition mTransition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_transition);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        findViewById(R.id.show_next_scene).setOnClickListener(this);
        if (null != savedInstanceState) {
            mCurrentScene = savedInstanceState.getInt(STATE_CURRENT_SCENE);
        }
        // We set up the Scenes here.
        mScenes = new Scene[]{
                Scene.getSceneForLayout(container, R.layout.scene1, this),
                Scene.getSceneForLayout(container, R.layout.scene2, this),
                Scene.getSceneForLayout(container, R.layout.scene3, this),
        };
        // This is the custom Transition.
        mTransition = new ChangeColor();
        // Show the initial Scene.
        TransitionManager.go(mScenes[mCurrentScene % mScenes.length]);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_SCENE, mCurrentScene);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_next_scene: {
                mCurrentScene = (mCurrentScene + 1) % mScenes.length;
                Log.e(LOG_TAG, "Transitioning to scene #" + mCurrentScene);
                // Pass the custom Transition as second argument for TransitionManager.go
                TransitionManager.go(mScenes[mCurrentScene], mTransition);
                break;
            }
        }
    }

}
