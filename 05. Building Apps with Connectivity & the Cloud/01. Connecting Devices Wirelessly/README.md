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

