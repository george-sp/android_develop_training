### Connecting Devices Wirelessly
Learn how to find and connect to local devices using Network Service Discovery and how to create peer-to-peer connections with Wi-Fi.

This class describes the key APIs for finding and connecting to other devices from your application. Specifically, it describes the **NSD API** for discovering available services and the **Wi-Fi Peer-to-Peer (P2P) API** for doing peer-to-peer wireless connections. 

This class also shows you how to use NSD and Wi-Fi P2P in combination to detect the services offered by a device and connect to the device when neither device is connected to a network.

Besides enabling communication with the cloud, Android's wireless APIs also enable communication with other devices on the same local network, and even devices which are not on a network, but are physically nearby. The addition of Network Service Discovery (NSD) takes this further by allowing an application to seek out a nearby device running services with which it can communicate. Integrating this functionality into your application helps you provide a wide range of features, such as playing games with users in the same room, pulling images from a networked NSD-enabled webcam, or remotely logging into other machines on the same network.

Dependencies and prerequisites
- Android 4.1 or higher

You should also read
- [Wi-Fi P2P](https://developer.android.com/guide/topics/connectivity/wifip2p.html)

Video
- [DevBytes: Network Service Discovery](https://developer.android.com/training/connect-devices-wirelessly/index.html)

-----------------------------------------------------------

#### Using Network Service Discovery
- Learn how to broadcast services offered by your own application, discover services offered on the local network, and use NSD to determine the connection details for the service you wish to connect to.

This lesson shows you how to build an application that can broadcast its name and connection information to the local network and scan for information from other applications doing the same. 
Finally, this lesson shows you how to connect to the same application running on another device.

Adding **Network Service Discovery (NSD)** to your app allows your users to identify other devices on the local network that support the services your app requests. This is useful for a variety of peer-to-peer applications such as file sharing or multi-player gaming. Android's NSD APIs simplify the effort required for you to implement such features.

> - **Register Your Service on the Network**
>
>> **Note:** This step is optional. If you don't care about broadcasting your app's services over the local network, you can skip forward to the next section, [Discover Services on the Network](https://developer.android.com/training/connect-devices-wirelessly/nsd.html#discover).
>
> To register your service on the local network, first create a [`NsdServiceInfo`](https://developer.android.com/reference/android/net/nsd/NsdServiceInfo.html) object. This object provides the information that other devices on the network use when they're deciding whether to connect to your service.
>
>> **Note:** The International Assigned Numbers Authority (IANA) manages a centralized, authoritative list of service types used by service discovery protocols such as NSD and Bonjour. You can download [the list from the IANA list of service names and port numbers](http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml). If you intend to use a new service type, you should reserve it by filling out the [IANA Ports and Service registration form](http://www.iana.org/form/ports-services).
>
> When setting the port for your service, avoid hardcoding it as this conflicts with other applications. For instance, assuming that your application always uses port 1337 puts it in potential conflict with other installed applications that use the same port. Instead, use the device's next available port. Because this information is provided to other apps by a service broadcast, there's no need for the port your application uses to be known by other applications at compile-time. Instead, the applications can get this information from your service broadcast, right before connecting to your service.
>
>If you're working with sockets, here's how you can initialize a socket to any available port simply by setting it to 0.
> ```
public void initializeServerSocket() {
    // Initialize a server socket on the next available port.
    mServerSocket = new ServerSocket(0);
    // Store the chosen port.
    mLocalPort =  mServerSocket.getLocalPort();
    ...
}
```
>
> Now that you've defined the [`NsdServiceInfo`](https://developer.android.com/reference/android/net/nsd/NsdServiceInfo.html) object, you need to implement the [`RegistrationListener`](https://developer.android.com/reference/android/net/nsd/NsdManager.RegistrationListener.html) interface. This interface contains callbacks used by Android to alert your application of the success or failure of service registration and unregistration.
>
> Now you have all the pieces to register your service. Call the method [`registerService()`](https://developer.android.com/reference/android/net/nsd/NsdManager.html#registerService(android.net.nsd.NsdServiceInfo, int, android.net.nsd.NsdManager.RegistrationListener)).
>
> Note that this method is asynchronous, so any code that needs to run after the service has been registered must go in the [`onServiceRegistered()`](https://developer.android.com/reference/android/net/nsd/NsdManager.RegistrationListener.html#onServiceRegistered(android.net.nsd.NsdServiceInfo)) method.
