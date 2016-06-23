### Sharing Files with NFC
Android allows you to transfer large files between devices using the Android Beam file transfer feature.
This feature has a simple API and allows users to start the transfer process by simply touching devices.
In response, Android Beam file transfer automatically copies files from one device to the other and notifies the user when it's finished.

> While the Android Beam file transfer API handles large amounts of data, the Android Beam NDEF transfer API introduced in Android 4.0 (API level 14) handles small amounts of data such as URIs or other small messages. 

Dependencies and prerequisites
- Android 4.1 (API Level 16) or higher
- At least two NFC-enabled Android devices (NFC is not supported in the emulator)

--------------------------------------------------------------------

#### Sending Files to Another Device
- Learn how to set up your app to send files to another device.

This lesson shows you how to design your app to send large files to another device using Android Beam file transfer. 
To send files, you request permission to use NFC and external storage, test to ensure your device supports NFC, and provide URIs to Android Beam file transfer.

> The **Android Beam** file transfer feature has the following requirements:
> 
> 1. Android Beam file transfer for large files is only available in _Android 4.1 (API level 16)_ and higher.
> 
> 2. Files you want to transfer must reside in external storage. To learn more about using external storage, read [Using the External Storage](https://developer.android.com/guide/topics/data/data-storage.html#filesExternal).
> 
> 3. Each file you want to transfer must be world-readable. You can set this permission by calling the method [`File.setReadable(true,false)`](https://developer.android.com/reference/java/io/File.html#setReadable(boolean)).
> 
> 4. You must provide a file URI for the files you want to transfer. Android Beam file transfer is unable to handle content URIs generated by [`FileProvider.getUriForFile`](https://developer.android.com/reference/android/support/v4/content/FileProvider.html#getUriForFile(android.content.Context, java.lang.String, java.io.File)).