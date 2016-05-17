package com.example.george.savedata;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/17/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class LoadKeyValueSetsActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoadKeyValueSetsActivity.class.getSimpleName();

    TextView preferencesTextView, sharedPreferencesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_key_value_sets);

        preferencesTextView = (TextView) findViewById(R.id.prefs_text_view);
        sharedPreferencesTextView = (TextView) findViewById(R.id.shared_prefs_text_view);

        /*
         * You cannot read the Preferences of SaveKeyValueSetsActivity
         */
        preferencesTextView.setText("Preferences:\n" + readFromPreferences());
        /*
         * But, you can read the SharedPreferences
         */
        sharedPreferencesTextView.setText("Shared\nPreferences:\n" + readFromSharedPreferences());
    }

    private String readFromPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default);
        int highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
        return String.valueOf(highScore);
    }

    private String readFromSharedPreferences() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default);
        int highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
        return String.valueOf(highScore);
    }
}
