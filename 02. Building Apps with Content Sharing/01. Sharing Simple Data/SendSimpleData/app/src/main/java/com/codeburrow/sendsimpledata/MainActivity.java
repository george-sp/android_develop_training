package com.codeburrow.sendsimpledata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // MIME type = text/plain
    private static final String MIME_TYPE = "text/plain";
    // Edit Text
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edittext);
    }

    public void sendTextContent(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mEditText.getText().toString());
        sendIntent.setType(MIME_TYPE);
        startActivity(sendIntent);
    }

    public void sendTextContentChooser(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, mEditText.getText().toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Custom Chooser:"));
    }

    public void sendBinaryContent(View view) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        Uri imageUri = Uri.parse("android.resource://com.codeburrow.sendsimpledata/drawable/fingers");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "Custom Chooser:"));
    }

    public void sendMultiplePiecesOfContent(View view) {
        ArrayList<Uri> imageUris = new ArrayList<>();
        // Add your image URIs here.
        imageUris.add(Uri.parse("android.resource://com.codeburrow.sendsimpledata/drawable/nice_image"));
        imageUris.add(Uri.parse("android.resource://com.codeburrow.sendsimpledata/drawable/fingers"));

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }
}