package com.codeburrow.otherappsinteraction;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

;

public class GetResultFromAnotherActivityActivity extends AppCompatActivity {

    private static final String LOG_TAG = GetResultFromAnotherActivityActivity.class.getSimpleName();

    // Request code
    private static final int PICK_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_result_from_another);
    }

    /**
     * Receive the Result
     * When the user is done with the subsequent activity and returns,
     * the system calls your activity's onActivityResult() method.
     *
     * This method includes three arguments:
     *  - The request code you passed to startActivityForResult().
     *  - A result code specified by the second activity.
     *    This is either RESULT_OK if the operation was successful
     *    or RESULT_CANCELED if the user backed out or the operation failed for some reason.
     *  - An Intent that carries the result data.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {Phone.NUMBER};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(column);

                // Do something with the phone number...
                Toast.makeText(GetResultFromAnotherActivityActivity.this, LOG_TAG + ": " + number, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        // Show user only contacts w/ phone numbers.
        pickContactIntent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }
}