package com.example.george.supportdifferentdevices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDifferentLanguages(View view) {
        Intent intent = new Intent(this, DifferentLanguagesActivity.class);
        startActivity(intent);
    }

    public void startDifferentScreens(View view) {
        Intent intent = new Intent(this, DifferentScreensActivity.class);
        startActivity(intent);
    }

    public void startDifferentPlatformVersions(View view) {
        Intent intent = new Intent(this, DifferentPlatformVersionsActivity.class);
        startActivity(intent);
    }
}
