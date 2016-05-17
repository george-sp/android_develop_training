package com.example.george.savedata;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/17/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class SaveKeyValueSetsFragment extends Fragment {

    private static final String LOG_TAG = SaveKeyValueSetsFragment.class.getSimpleName();

    Button saveButton;
    EditText scoreEditText;
    TextView fragmentTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_save_key_value_sets, container, false);

        fragmentTextView = (TextView) fragmentView.findViewById(R.id.fragment_text_view);
        fragmentTextView.setText(readFromPreferences());

        scoreEditText = (EditText) fragmentView.findViewById(R.id.save_high_score_edit_text);

        saveButton = (Button) fragmentView.findViewById(R.id.save_high_score_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate not an empty String
                int scoreToBeSaved = !scoreEditText.getText().toString().equals("") ? Integer.parseInt(scoreEditText.getText().toString()) : 0;
                writeToPreferences(scoreToBeSaved);
                writeToSharedPreferences(scoreToBeSaved);
            }
        });

        return fragmentView;
    }

    /**
     * Code Sample
     */
    private void getAccessToSharedPreferences() {
        Context context = getActivity();
        /*
         * It accesses the shared preferences file that's identified by the resource string
         * R.string.preference_file_key and opens it using the private mode so the file is
         * accessible by only your app.
         */
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );

        /*
         * Alternatively, if you need just one shared preference file for your activity, you can
         * use the getPreferences() method:
         */
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    /*
     * Preferences: Write and Read
     */
    private void writeToPreferences(int newHighScore) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
    }

    private String readFromPreferences() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default);
        int highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
        return String.valueOf(highScore);
    }

    /*
     * SharedPreferences: Write and Read
     */
    private void writeToSharedPreferences(int newHighScore) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
    }

    private String readFromSharedPreferences() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default);
        int highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
        return String.valueOf(highScore);
    }

}
