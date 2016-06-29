package com.codeburrow.manageaudioplayback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/29/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
/*
 * Media playback buttons such as play, pause, stop, skip, and previous
 * are available on some handsets and many connected or wireless headsets.
 * Whenever a user presses one of these hardware keys,
 * the system broadcasts an intent with the ACTION_MEDIA_BUTTON action.
 *
 * To respond to media button clicks, you need to register a BroadcastReceiver
 * in your manifest that listens for this action broadcast.
 */
public class RemoteControlReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = RemoteControlReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
                // Handle key press.
                Log.e(LOG_TAG, "MEDIA BUTTON is pressed");
            }

            /* The following snippet shows how to extract the media button pressed
             * and affects the media playback accordingly.

            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                // Handle key press.
            }

             */
        }
    }
}
