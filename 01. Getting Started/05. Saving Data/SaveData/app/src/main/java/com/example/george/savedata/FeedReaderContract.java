package com.example.george.savedata;

import android.provider.BaseColumns;

/*
 * Companion class, known as a contract class, which explicitly specifies
 * the layout of your schema in a systematic and self-documenting way.
 *
 * A good way to organize a contract class is to put definitions
 * that are global to your whole database in the root level of the class.
 * Then create an inner class for each table that enumerates its columns.
 *
 * /

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/11/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {
    }

    /**
     * Inner class that defines the table contents
     */
    public static abstract class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_NULLABLE = null;
    }
}
