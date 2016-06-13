package com.codeburrow.helperapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static int REQUEST_CODE = 1;

    private EditText sharedEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedEditText = (EditText) findViewById(R.id.shared_edittext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the result string from the intent
                Uri resultUri = data.getData();
                Toast.makeText(MainActivity.this, LOG_TAG + ": " + resultUri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sharePlainText(View view) {
        String sharedString = sharedEditText.getText().toString();
        Intent sharedTextIntent = new Intent(Intent.ACTION_SEND);
        sharedTextIntent.setType("text/plain");
        sharedTextIntent.putExtra(Intent.EXTRA_TEXT, sharedString);
        startActivityForResult(sharedTextIntent, REQUEST_CODE);
    }
}
