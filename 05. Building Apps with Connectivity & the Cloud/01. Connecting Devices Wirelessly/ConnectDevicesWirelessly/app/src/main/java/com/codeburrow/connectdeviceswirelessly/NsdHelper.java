package com.codeburrow.connectdeviceswirelessly;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import java.net.ServerSocket;

public class NsdHelper {

    public static final String LOG_TAG = NsdHelper.class.getSimpleName();

    Context mContext;
    NsdManager mNsdManager;
    NsdManager.RegistrationListener mRegistrationListener;

    public String mServiceName;
    private ServerSocket mServerSocket;
    private int mLocalPort;

    public NsdHelper(Context context) {
        mContext = context;
    }

    /**
     * - Sets the service name to "NsdChat".
     * The name is visible to any device on the network that is using NSD to look for local services.
     * <p/>
     * Keep in mind that the name must be unique for any service on the network,
     * and Android automatically handles conflict resolution.
     * If two devices on the network both have the NsdChat application installed,
     * one of them changes the service name automatically, to something like "NsdChat (1)".
     * <p/>
     * - Sets the service type, specifies which protocol and transport layer
     * the application uses.
     * The syntax is "_<protocol>._<transportlayer>".
     * In the code snippet, the service uses HTTP protocol running over TCP.
     * An application offering a printer service (for instance, a network printer)
     * would set the service type to "_ipp._tcp".
     *
     * @param port
     */
    public void registerService(int port) {
        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName("NsdChat");
        serviceInfo.setServiceType("_http._tcp.");
        serviceInfo.setPort(port);

        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);

        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void initializeServerSocket() {
        // Initialize a server socket on the next available port.
        mServerSocket = new ServerSocket(0);

        // Store the chosen port.
        mLocalPort = mServerSocket.getLocalPort();

        // ...
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                // Save the service name.  Android may have changed it in order to
                // resolve a conflict, so update the name you initially requested
                // with the name Android actually used.
                mServiceName = NsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Registration failed!  Put debugging code here to determine why.
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                // Service has been unregistered.  This only happens when you call
                // NsdManager.unregisterService() and pass in this listener.
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Unregistration failed.  Put debugging code here to determine why.
            }
        };
    }
}
