package com.codeburrow.connectdeviceswirelessly;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ChatConnection {

    private static final String LOG_TAG = ChatConnection.class.getSimpleName();

    private Handler mHandler;
    private ChatServer mChatServer;
    private ChatClient mChatClient;

    private Socket mSocket;
    private int mPort = -1;

    public ChatConnection(Handler handler) {
        mHandler = handler;
        mChatServer = new ChatServer(handler);
    }

    public void tearDown() {
        mChatServer.tearDown();
        if (mChatClient != null) {
            mChatClient.tearDown();
        }
    }

    public void connectToServer(InetAddress address, int port) {
        mChatClient = new ChatClient(address, port);
    }

    public void sendMessage(String msg) {
        if (mChatClient != null) {
            mChatClient.sendMessage(msg);
        }
    }

    public int getLocalPort() {
        return mPort;
    }

    public void setLocalPort(int port) {
        mPort = port;
    }

    public synchronized void updateMessages(String msg, boolean local) {
        Log.d(LOG_TAG, "Updating message: " + msg);

        if (local) {
            msg = "me: " + msg;
        } else {
            msg = "them: " + msg;
        }

        Bundle messageBundle = new Bundle();
        messageBundle.putString("msg", msg);
        Message message = new Message();
        message.setData(messageBundle);
        mHandler.sendMessage(message);
    }

    private synchronized void setSocket(Socket socket) {
        Log.d(LOG_TAG, "setSocket being called.");

        if (socket == null) {
            Log.d(LOG_TAG, "Setting a null socket.");
        }

        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mSocket = socket;
    }

    private Socket getSocket() {
        return mSocket;
    }

    private class ChatServer {

        private final String SERVER_LOG_TAG = ChatServer.class.getName();
        ServerSocket mServerSocket = null;
        Thread mThread = null;

        public ChatServer(Handler handler) {
            Log.d(SERVER_LOG_TAG, "Creating chatClient");
            mThread = new Thread(new ServerThread());
            mThread.start();
        }

        public void tearDown() {
            mThread.interrupt();
            try {
                mServerSocket.close();
            } catch (IOException ioe) {
                Log.e(SERVER_LOG_TAG, "Error when closing server socket.");
                ioe.printStackTrace();
            }
        }

        class ServerThread implements Runnable {

            @Override
            public void run() {
                try {
                    /*
                     * Since discovery will happen via NSD, we don't need to care which port is used.
                     * Just grab an available one and advertise it via NSD.
                     */
                    mServerSocket = new ServerSocket(0);
                    setLocalPort(mServerSocket.getLocalPort());

                    while (!Thread.currentThread().isInterrupted()) {
                        Log.d(SERVER_LOG_TAG, "Server socket created, awaiting connection");
                        setSocket(mServerSocket.accept());
                        Log.d(SERVER_LOG_TAG, "Connected.");

                        if (mChatClient == null) {
                            int port = mSocket.getPort();
                            InetAddress address = mSocket.getInetAddress();
                            connectToServer(address, port);
                        }
                    }
                } catch (IOException ioe) {
                    Log.e(SERVER_LOG_TAG, "Error creating server socket: ", ioe);
                    ioe.printStackTrace();
                }
            }
        }
    }

    private class ChatClient {

        private InetAddress mAddress;
        private int PORT;
        private final String CLIENT_LOG_TAG = ChatClient.class.getName();
        private Thread mSendThread;
        private Thread mRecThread;

        public ChatClient(InetAddress address, int port) {
            Log.d(CLIENT_LOG_TAG, "Creating chatClient");
            this.mAddress = address;
            this.PORT = port;
            mSendThread = new Thread(new SendingThread());
            mSendThread.start();
        }

        class SendingThread implements Runnable {

            BlockingQueue<String> mMessageQueue;
            private int QUEUE_CAPACITY = 10;

            public SendingThread() {
                mMessageQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
            }

            @Override
            public void run() {
                try {
                    if (getSocket() == null) {
                        setSocket(new Socket(mAddress, PORT));
                        Log.d(CLIENT_LOG_TAG, "Client-side socket initialized.");
                    } else {
                        Log.d(CLIENT_LOG_TAG, "Socket already initialized. skipping!");
                    }
                    mRecThread = new Thread(new ReceivingThread());
                    mRecThread.start();
                } catch (UnknownHostException e) {
                    Log.e(CLIENT_LOG_TAG, "Initializing socket failed, UHE", e);
                } catch (IOException e) {
                    Log.e(CLIENT_LOG_TAG, "Initializing socket failed, IOE.", e);
                }

                while (true) {
                    try {
                        String msg = mMessageQueue.take();
                        sendMessage(msg);
                    } catch (InterruptedException ie) {
                        Log.e(CLIENT_LOG_TAG, "Message sending loop interrupted, exiting");
                    }
                }
            }
        }

        class ReceivingThread implements Runnable {

            @Override
            public void run() {
                BufferedReader input;
                try {
                    input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    while (!Thread.currentThread().isInterrupted()) {
                        String messageStr = null;
                        messageStr = input.readLine();

                        if (messageStr != null) {
                            Log.d(CLIENT_LOG_TAG, "Read from the stream: " + messageStr);
                            updateMessages(messageStr, false);
                        } else {
                            Log.d(CLIENT_LOG_TAG, "The nulls! The nulls!");
                            break;
                        }
                    }
                    input.close();
                } catch (IOException e) {
                    Log.e(CLIENT_LOG_TAG, "Server loop error: ", e);
                }
            }
        }

        public void tearDown() {
            try {
                getSocket().close();
            } catch (IOException ioe) {
                Log.e(CLIENT_LOG_TAG, "Error when closing server socket.");
            }
        }

        public void sendMessage(String msg) {
            try {
                Socket socket = getSocket();

                if (socket == null) {
                    Log.d(CLIENT_LOG_TAG, "Socket is null, wtf?");
                } else if (socket.getOutputStream() == null) {
                    Log.d(CLIENT_LOG_TAG, "Socket output stream is null, wtf?");
                }

                PrintWriter out = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream())), true);
                out.println(msg);
                out.flush();
                updateMessages(msg, true);
            } catch (UnknownHostException e) {
                Log.e(CLIENT_LOG_TAG, "Unknown Host", e);
            } catch (IOException e) {
                Log.e(CLIENT_LOG_TAG, "I/O Exception", e);
            } catch (Exception e) {
                Log.e(CLIENT_LOG_TAG, "Exception", e);
            }

            Log.d(CLIENT_LOG_TAG, "Client sent message: " + msg);
        }
    }
}
