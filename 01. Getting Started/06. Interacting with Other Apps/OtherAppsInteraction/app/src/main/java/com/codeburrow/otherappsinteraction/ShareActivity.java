package com.codeburrow.otherappsinteraction;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShareActivity.class.getSimpleName();

    // Shared text received from an incoming intent.
    private String sharedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Get the intent that started this activity
        Intent intent = getIntent();
        Uri data = intent.getData();

        // Figure out what to do based on the intent type
        if (intent.getType().contains("image/")) {
            // Handle intents with image data ...
        } else if (intent.getType().equals("text/plain")) {
            // Handle intents with text ...
            sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            Toast.makeText(ShareActivity.this, LOG_TAG + ": " + sharedText, Toast.LENGTH_SHORT).show();
        }
    }

    public void returnResult(View view) {
        // Create intent to deliver some kind of result data
        Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri"));
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}