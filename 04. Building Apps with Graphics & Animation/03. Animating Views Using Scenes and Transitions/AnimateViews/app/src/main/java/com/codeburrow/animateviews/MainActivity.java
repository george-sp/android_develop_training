package com.codeburrow.animateviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startBasicTransitionActivity(View view) {
        startActivity(new Intent(MainActivity.this, BasicTransitionActivity.class));
    }

    public void startCustomTransitionActivity(View view) {
        startActivity(new Intent(MainActivity.this, CustomTransitionActivity.class));
    }
}
