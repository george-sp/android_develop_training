package com.codeburrow.otherappsinteraction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.List;

public class SendUserToAnotherAppActivity extends AppCompatActivity {

    private static final String LOG_TAG = SendUserToAnotherAppActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_user_to_another_app);
    }


    /**
     * View a map.
     *
     * @param view
     */
    public void intentToViewMap(View view) {
        // Build the intent.
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        // Verify it resolves.
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe.
        if (isIntentSafe) {
            startActivity(mapIntent);
        }

    }

    /**
     * When your app invokes this intent by calling startActivity(),
     * the Phone app initiates a DIAL to the given phone number.
     *
     * @param view
     */
    public void intentToDialNumber(View view) {
        // Always use string resources for UI text.
        // This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
        // If your data is a Uri, there's a simple Intent() constructor you can use to define the action and data.
        Uri number = Uri.parse("tel:0123456789");
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, number);

        // Create intent to show chooser
        Intent chooserDialIntent = Intent.createChooser(dialIntent, title);

        // Verify There is an App to Receive the Intent.
        PackageManager packageManager = getPackageManager();
        List activities = packageManager
                .queryIntentActivities(dialIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            Log.e(LOG_TAG, activities.size() + "\n" + activities.toString());
            // Start an Activity with the Intent.
            startActivity(chooserDialIntent);
        }
    }

    /**
     * When your app invokes this intent by calling startActivity(),
     * the Phone app initiates a DIAL to the given phone number.
     * <p/>
     * WITHOUT USING A CHOOSER INTENT - DEFAULT APP.
     *
     * @param view
     */
    public void intentToDialNumberWithDefaultApp(View view) {
        Uri number = Uri.parse("tel:0123456789");
        Intent defaultDialIntent = new Intent(Intent.ACTION_DIAL, number);
//        defaultDialIntent.setPackage("com.android.phone");
        defaultDialIntent.setPackage("com.android.dialer");
        startActivity(defaultDialIntent);
    }

    /**
     * When your app invokes this intent by calling startActivity(),
     * the Phone app initiates a CALL to the given phone number.
     * <p/>
     * WITHOUT USING A CHOOSER INTENT - DEFAULT APP.
     *
     * @param view
     */
    public void intentToMakeCallWithDefaultApp(View view) {
        Intent defaultCallIntent = new Intent(Intent.ACTION_CALL);
        defaultCallIntent.setPackage("com.android.phone");
        defaultCallIntent.setData(Uri.parse("tel:0123456789"));
        startActivity(defaultCallIntent);
    }

    /**
     * View a web page.
     *
     * @param view
     */
    public void intentToViewWebPage(View view) {
        Uri webpage = Uri.parse("http://www.android.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        // Verify There is an App to Receive the Intent.
        PackageManager packageManager = getPackageManager();
        List activities = packageManager
                .queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            // Log the possible apps
            Log.e(LOG_TAG, activities.size() + "\n" + activities.toString());
            // Start an Activity with the Intent.
            startActivity(webIntent);
        }
    }

    /**
     * Send an email.
     *
     * @param view
     */
    public void intentToSendEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type.
        emailIntent.setType("text/plain");
        // recipients
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"george@codeburrow.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Custom Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Custom Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        /*
         * You can also attach multiple items by passing an ArrayList of Uris
         */

        // Verify There is an App to Receive the Intent.
        PackageManager packageManager = getPackageManager();
        List activities = packageManager
                .queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            Log.e(LOG_TAG, activities.size() + "\n" + activities.toString());
            // Start an Activity with the Intent.
            startActivity(emailIntent);
        }
    }

    /**
     * NOTE: This intent for a calendar event is supported only with API level 14 and higher.
     * <p/>
     * Create a calendar event.
     *
     * @param view
     */
    public void intentToCreateCalendarEvent(View view) {
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 0, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 9, 19, 10, 30);

        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());

        calendarIntent.putExtra(Events.TITLE, "Ninja class");
        calendarIntent.putExtra(Events.EVENT_LOCATION, "Secret dojo");

        // Verify There is an App to Receive the Intent.
        PackageManager packageManager = getPackageManager();
        List activities = packageManager
                .queryIntentActivities(calendarIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            Log.e(LOG_TAG, activities.size() + "\n" + activities.toString());
            // Start an Activity with the Intent.
            startActivity(calendarIntent);
        }
    }
}