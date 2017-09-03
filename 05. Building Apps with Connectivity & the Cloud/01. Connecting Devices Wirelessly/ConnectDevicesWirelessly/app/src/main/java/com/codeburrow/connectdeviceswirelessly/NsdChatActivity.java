package com.codeburrow.connectdeviceswirelessly;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NsdChatActivity extends AppCompatActivity {

    private static final String LOG_TAG = NsdChatActivity.class.getSimpleName();

    NsdHelper mNsdHelper;
    ChatConnection mConnection;

    private TextView mStatusView;
    private Handler mUpdateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Creating chat activity");
        setContentView(R.layout.activity_nsd_chat);

        mStatusView = (TextView) findViewById(R.id.status);

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
                addChatLine(chatLine);
            }
        };
    }

    public void clickAdvertise(View view) {
        // Register service
        if (mConnection.getLocalPort() > -1) {
            mNsdHelper.registerService(mConnection.getLocalPort());
        } else {
            Log.w(LOG_TAG, "ServerSocket isn't bound.");
        }
    }

    public void clickDiscover(View v) {
        mNsdHelper.discoverServices();
    }

    public void clickSend(View view) {
        EditText messageView = (EditText) this.findViewById(R.id.chatInput);
        if (messageView != null) {
            String messageString = messageView.getText().toString();
            if (!messageString.isEmpty()) {
                mConnection.sendMessage(messageString);
            }
            messageView.setText("");
        }
    }

    public void addChatLine(String line) {
        mStatusView.append("\n" + line);
    }

    @Override
    public void onStart() {
        Log.d(LOG_TAG, "Starting");

        mConnection = new ChatConnection(mUpdateHandler);
        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();

        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "Pausing");

        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "Resuming");

        super.onResume();

        if (mNsdHelper != null) {
            mNsdHelper.discoverServices();
        }
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "Being stopped");

        mNsdHelper.tearDown();
        mConnection.tearDown();
        mNsdHelper = null;
        mConnection = null;

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "Being destroyed");

        super.onDestroy();
    }
}
