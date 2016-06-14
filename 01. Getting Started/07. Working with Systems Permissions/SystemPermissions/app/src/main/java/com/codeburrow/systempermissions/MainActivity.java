package com.codeburrow.systempermissions;

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

    public void startDeclarePermissionsActivity(View view) {
        startActivity(new Intent(MainActivity.this, DeclarePermissionsActivity.class));
    }

    public void startRequestPermissionsAtRunTimeActivity(View view) {
        startActivity(new Intent(MainActivity.this, RequestPermissionsAtRunTimeActivity.class));
    }
}
