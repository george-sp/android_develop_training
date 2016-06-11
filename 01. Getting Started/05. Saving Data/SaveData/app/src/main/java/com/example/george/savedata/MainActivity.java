package com.example.george.savedata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/17/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveKeyValueSets(View view) {
        startActivity(new Intent(this, SaveKeyValueSetsActivity.class));
    }

    public void saveFiles(View view) {
        startActivity(new Intent(this, SaveFilesActivity.class));
    }

    public void saveInDatabase(View view) {
        startActivity(new Intent(this, SaveDataInSqlDatabasesActivity.class));
    }
}