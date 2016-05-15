package com.example.george.activity_lifecycle_demo_1.util;

import android.os.Handler;
import android.widget.TextView;

import java.util.List;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since May/15/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class Utils {

    private static StatusTracker mStatusTracker = StatusTracker.getInstance();

    /**
     * Helper method to print out the lifecycle state of each Activity.  Note this has
     * been wrapped in a Handler to delay the output due to overlaps in lifecycle state
     * changes as one Activity launches another.
     *
     * @param viewMethods TextView to list out the lifecycle methods called
     * @param viewStatus  TextView to list out the status of all Activity classes
     * @link http://developer.android.com/guide/topics/fundamentals/activities.html#CoordinatingActivities
     */
    public static void printStatus(final TextView viewMethods, final TextView viewStatus) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Get the stack of Activity lifecycle methods called and print to TextView
                StringBuilder sbMethods = new StringBuilder();
                List<String> listMethods = mStatusTracker.getMethodList();
                for (String method : listMethods) {
                    sbMethods.insert(0, method + "\r\n");
                }
                if (viewMethods != null) {
                    viewMethods.setText(sbMethods.toString());
                }

                // Get the status of all Activity classes and print to TextView
                StringBuilder sbStatus = new StringBuilder();
                for (String key : mStatusTracker.keySet()) {
                    sbStatus.insert(0, key + ": " + mStatusTracker.getStatus(key) + "\n");
                }
                if (viewStatus != null) {
                    viewStatus.setText(sbStatus.toString());
                }
            }
        }, 750);
    }

}
