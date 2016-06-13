package com.codeburrow.systempermissions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DeclarePermissionsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DeclarePermissionsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_permissions);
    }
}
