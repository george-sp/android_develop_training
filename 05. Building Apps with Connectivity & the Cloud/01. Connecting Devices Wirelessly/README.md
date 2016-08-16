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
>
> - **Discover Services on the Network**
>
> Your application needs to listen to service broadcasts on the network to see what services are available, and filter out anything the application can't work with.
>
> Service discovery, like service registration, has two steps: setting up a discovery listener with the relevant callbacks, and making a single asynchronous API call to [`discoverServices()`](https://developer.android.com/reference/android/net/nsd/NsdManager.html#discoverServices(java.lang.String, int, android.net.nsd.NsdManager.DiscoveryListener)).
>
> First, instantiate an anonymous class that implements [`NsdManager.DiscoveryListener`](https://developer.android.com/reference/android/net/nsd/NsdManager.DiscoveryListener.html). 
>
> The NSD API uses the methods in this interface to inform your application when discovery is started, when it fails, and when services are found and lost (lost means "is no longer available"). Notice that this snippet does several checks when a service is found.
>
> 1. The service name of the found service is compared to the service name of the local service to determine if the device just picked up its own broadcast (which is valid).
> 2. The service type is checked, to verify it's a type of service your application can connect to.
> 3. The service name is checked to verify connection to the correct application.
>
> Checking the service name isn't always necessary, and is only relevant if you want to connect to a specific application. For instance, the application might only want to connect to instances of itself running on other devices. However, if the application wants to connect to a network printer, it's enough to see that the service type is "_ipp._tcp".
>
> After setting up the listener, call [`discoverServices()`](https://developer.android.com/reference/android/net/nsd/NsdManager.html#discoverServices(java.lang.String, int, android.net.nsd.NsdManager.DiscoveryListener)), passing in the service type your application should look for, the discovery protocol to use, and the listener you just created.
> ```
    mNsdManager.discoverServices(
        SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
```
>
> - **Connect to Services on the Network**
>
> When your application finds a service on the network to connect to, it must first determine the connection information for that service, using the [`resolveService()`](https://developer.android.com/reference/android/net/nsd/NsdManager.html#resolveService(android.net.nsd.NsdServiceInfo, android.net.nsd.NsdManager.ResolveListener)) method. Implement a [`NsdManager.ResolveListener`](https://developer.android.com/reference/android/net/nsd/NsdManager.ResolveListener.html) to pass into this method, and use it to get a [`NsdServiceInfo`](https://developer.android.com/reference/android/net/nsd/NsdServiceInfo.html) containing the connection information.
>
> Once the service is resolved, your application receives detailed service information including an IP address and port number. This is everything you need to create your own network connection to the service.
