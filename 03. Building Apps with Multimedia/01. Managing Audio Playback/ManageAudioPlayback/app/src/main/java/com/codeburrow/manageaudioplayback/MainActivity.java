package com.codeburrow.manageaudioplayback;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Button requestPermanentAudioFocusButton;
    private Button requestTransientAudioFocusButton;
    private Button releaseAudioFocusButton;

    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    // PLAY or RESUME playback, or RAISE VOLUME back to normal.
                    Log.e(LOG_TAG, "AUDIOFOCUS_GAIN");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    Log.e(LOG_TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    Log.e(LOG_TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    // Loss of audio focus for a long time.
                    // STOP playback.
                    Log.e(LOG_TAG, "AUDIOFOCUS_LOSS");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // Loss of audio focus for a short time.
                    // PAUSE playback.
                    Log.e(LOG_TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // Loss of audio focus for a short time.
                    /* Ducking is the process of lowering your audio stream output volume
                     * to make transient audio from another app easier to hear
                     * without totally disrupting the audio from your own application.
                     */
                    // LOWER the volume.
                    Log.e(LOG_TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    Log.e(LOG_TAG, "AUDIOFOCUS_REQUEST_FAILED");
                    break;
                default:
                    //
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get buttons from the activity_main layout.
        requestPermanentAudioFocusButton = (Button) findViewById(R.id.request_permanent_audio_focus);
        requestTransientAudioFocusButton = (Button) findViewById(R.id.request_transient_audio_focus);
        releaseAudioFocusButton = (Button) findViewById(R.id.release_audio_focus);

        // Add OnClickListeners to the buttons above.
        requestPermanentAudioFocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermanentAudioFocus();
            }
        });
        requestTransientAudioFocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTransientAudioFocus();
            }
        });
        releaseAudioFocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseAudioFocus();
            }
        });

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

    /**
     * Helper Method.
     * <p/>
     * Requests PERMANENT audio focus on the music audio stream.
     * <p/>
     * You should request the audio focus immediately before you begin playback,
     * (such as when the user presses the play button
     * or the background music -for the next game level- begins.)
     */
    private void requestPermanentAudioFocus() {
        // Request audio focus for playback.
        int result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Toast.makeText(MainActivity.this, "AUDIOFOCUS_REQUEST_GRANTED", Toast.LENGTH_SHORT).show();
            registerMediaButtonEventReceiver();
            // Start playback.
        }
    }

    /**
     * Helper Method.
     * <p/>
     * Notifies the system that you no longer require focus and
     * unregisters the associated OnAudioFocusChangeListener.
     * <p/>
     * In the case of abandoning transient focus,
     * this allows any interrupted app to continue playback.
     */
    private void releaseAudioFocus() {
        // Abandon audio focus when playback complete.
        Toast.makeText(MainActivity.this, "ABANDON_AUDIOFOCUS", Toast.LENGTH_SHORT).show();
        audioManager.abandonAudioFocus(audioFocusChangeListener);
    }

    /**
     * Helper Method.
     * <p/>
     * When requesting transient audio focus you have an additional option:
     * - whether or not you want to enable "ducking."
     * <p/>
     * Normally, when a well-behaved audio app loses audio focus
     * it immediately silences its playback.
     * <p/>
     * By requesting a transient audio focus that allows ducking
     * you tell other audio apps that it’s acceptable for them to keep playing,
     * provided they lower their volume until the focus returns to them.
     */
    private void requestTransientAudioFocus() {
        // Request audio focus for playback.
        int result = audioManager.requestAudioFocus(
                audioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Toast.makeText(MainActivity.this, "AUDIOFOCUS_REQUEST_GRANTED", Toast.LENGTH_SHORT).show();
            // Start playback.
        }
    }
}
