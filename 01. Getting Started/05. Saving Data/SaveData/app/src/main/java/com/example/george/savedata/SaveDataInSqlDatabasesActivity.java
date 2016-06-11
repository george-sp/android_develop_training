package com.example.george.savedata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.george.savedata.FeedReaderContract.FeedEntry;

public class SaveDataInSqlDatabasesActivity extends AppCompatActivity {

    private static final String LOG_TAG = SaveDataInSqlDatabasesActivity.class.getSimpleName();

    // Edit Texts
    private EditText entryIdEditText;
    private EditText titleEditText;
    private EditText subtitleEditText;

    // SQLiteOpenHelper
    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data_in_sql_databases);

        // Find the Edit Texts.
        entryIdEditText = (EditText) findViewById(R.id.entry_id_edittext);
        titleEditText = (EditText) findViewById(R.id.title_edittext);
        subtitleEditText = (EditText) findViewById(R.id.subtitle_edittext);

        // To access your database, instantiate your subclass of SQLiteOpenHelper:
        mDbHelper = new FeedReaderDbHelper(this);

    }

    /**
     * Insert data into the database by passing a ContentValues object
     * to the insert() method.
     *
     * @param view
     */
    public void putInfoIntoDatabase(View view) {
        /* Gets the data repository in write mode.
         * Note:
         *
         * Because they can be long-running, be sure that you call
         * getWritableDatabase() or getReadableDatabase()
         * in a background thread,
         * such as with AsyncTask or IntentService.
         */
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, entryIdEditText.getText().toString());
        values.put(FeedEntry.COLUMN_NAME_TITLE, titleEditText.getText().toString());
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitleEditText.getText().toString());

        /* Insert the new row, returning the primary key value of the new row
         *
         * The first argument for insert() is simply the table name.
         * The second argument provides the name of a column in which the framework
         * can insert NULL in the event that the ContentValues is empty
         * (if you instead set this to "null",
         * then the framework will not insert a row when there are no values).
         */
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);

        // Debugging.
        Toast.makeText(SaveDataInSqlDatabasesActivity.this, "new Row ID: " + newRowId, Toast.LENGTH_SHORT).show();
        Log.e(LOG_TAG, "new Row ID: " + newRowId + "\n" + values.toString());
    }

    /**
     * To read from a database,
     * use the query() method, passing it your selection criteria and desired columns.
     * The method combines elements of insert() and update(),
     * except the column list defines the data you want to fetch,
     * rather than the data to insert.
     * The results of the query are returned to you in a Cursor object.
     *
     * @param view
     */
    public void readInfoFromDatabase(View view) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        String selection = null;
        String[] selectionArgs = null;

        Cursor c = db.query(
                // The table to query
                FeedEntry.TABLE_NAME,
                // The columns to return
                projection,
                // The columns for the WHERE clause
                selection,
                // The values for the WHERE clause
                selectionArgs,
                // don't group the rows
                null,
                // don't filter by row groups
                null,
                // The sort order
                sortOrder
        );

        try {
            String resultStr = "";

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        long itemId = c.getLong(c.getColumnIndexOrThrow(FeedEntry._ID));

                        resultStr += itemId + ". "
                                + c.getString(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_ENTRY_ID)) + ", "
                                + c.getString(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE)) + ", "
                                + c.getString(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE)) + "\n";
                    } while (c.moveToNext());
                }
            }

            // Debugging.
            Toast.makeText(SaveDataInSqlDatabasesActivity.this, resultStr, Toast.LENGTH_SHORT).show();
            Log.e(LOG_TAG, resultStr);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Something went wrong: " + e.getMessage());
        }
    }

    /**
     * To delete rows from a table,
     * you need to provide selection criteria that identify the rows.
     * <p/>
     * The mechanism divides the selection specification into
     * a selection clause and selection arguments.
     * <p/>
     * The clause defines the columns to look at,
     * and also allows you to combine column tests.
     * <p/>
     * The arguments are values to test against that are bound into the clause.
     *
     * @param view
     */
    public void deleteInfoFromDatabase(View view) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {entryIdEditText.getText().toString()};
        // Issue SQL statement.
        int rowsDeleted = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted == 0) {
            Toast.makeText(SaveDataInSqlDatabasesActivity.this, "Enter a valid Entry ID", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SaveDataInSqlDatabasesActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * When you need to modify a subset of your database values, use the update() method.
     * <p/>
     * Updating the table combines :
     * the content values syntax of insert() with the where syntax of delete().
     *
     * @param view
     */
    public void updateDatabase(View view) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, titleEditText.getText().toString());

        // Which row to update, based on the ID
        String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {entryIdEditText.getText().toString()};

        // Issue SQL statement.
        int rowsUpdated = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        Toast.makeText(SaveDataInSqlDatabasesActivity.this, "Rows Updated: " + rowsUpdated, Toast.LENGTH_SHORT).show();
        if (rowsUpdated == 0) {
            Toast.makeText(SaveDataInSqlDatabasesActivity.this, "Set title to be updated, based on Entry ID", Toast.LENGTH_SHORT).show();
        }
    }
}
