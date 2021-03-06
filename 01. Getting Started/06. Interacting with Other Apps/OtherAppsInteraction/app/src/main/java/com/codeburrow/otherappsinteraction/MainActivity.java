package com.codeburrow.otherappsinteraction;

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

    public void startSendUserToAnotherAppActivity(View view) {
        startActivity(new Intent(MainActivity.this, SendUserToAnotherAppActivity.class));
    }

    public void startGetResultFromAnotherActivityActivity(View view) {
        startActivity(new Intent(MainActivity.this, GetResultFromAnotherActivityActivity.class));
    }
}