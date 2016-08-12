package com.codeburrow.animations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CrossfadeActivity extends AppCompatActivity {

    private static final String LOG_TAG = CrossfadeActivity.class.getSimpleName();

    private View mContentView;
    private View mLoadingView;
    private int mShortAnimationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crossfade);

        mContentView = findViewById(R.id.content);
        mLoadingView = findViewById(R.id.loading_spinner);

        // Initially hide the content view.
        mContentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }
}
