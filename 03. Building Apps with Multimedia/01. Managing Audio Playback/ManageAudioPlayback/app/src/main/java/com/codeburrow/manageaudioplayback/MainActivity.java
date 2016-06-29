package com.codeburrow.manageaudioplayback;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /*
         * Android maintains a separate audio stream for playing:
         * - music
         * - alarms
         * - notifications
         * - the incoming call ringer
         * - system sounds
         * - in-call volume
         * - and DTMF tones.
         *
         * This is done primarily to allow users to control
         * the volume of each stream independently.
         *
         * Most of these streams are restricted to system events,
         * so unless your app is a replacement alarm clock,
         * you’ll almost certainly be playing your audio using the STREAM_MUSIC stream.
         */
        // Identify Which Audio Stream to Use.
        int streamType = AudioManager.STREAM_MUSIC;
        // Use Hardware Volume Keys to Control Your App's Audio Volume.
        setVolumeControlStream(streamType);
    }

    /*
     * A better approach is to register and unregister the media button event receiver
     * when your application gains and loses the audio focus.
     * This is covered in detail in the next lesson.
     */

    @Override
    protected void onResume() {
        super.onResume();

        registerMediaButtonEventReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterMediaButtonEventReceiver();
    }

    /**
     * Helper Method.
     * <p/>
     * Register your media button event receiver using the AudioManager.
     * <p/>
     * When registered, your broadcast receiver is the exclusive receiver of all media button broadcasts.
     * <p/>
     * Because multiple applications might want to listen for media button presses,
     * you must also programmatically control when your app should receive media button press events.
     */
    private void registerMediaButtonEventReceiver() {
        // Start listening for button presses
        audioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));
    }

    /**
     * Helper Method.
     * <p/>
     * Typically, apps should unregister most of their receivers whenever they become
     * inactive or invisible (such as during the onStop() callback).
     * However, responding to media playback buttons is most important when
     * your application isn’t visible and therefore can’t be controlled by the on-screen UI.
     */
    private void unregisterMediaButtonEventReceiver() {
        // Stop listening for button presses
        audioManager.unregisterMediaButtonEventReceiver(new ComponentName(getPackageName(), RemoteControlReceiver.class.getName()));
    }
}
