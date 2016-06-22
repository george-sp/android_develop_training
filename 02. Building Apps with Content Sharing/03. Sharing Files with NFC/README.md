### Sharing Files with NFC
Android allows you to transfer large files between devices using the Android Beam file transfer feature.
This feature has a simple API and allows users to start the transfer process by simply touching devices.
In response, Android Beam file transfer automatically copies files from one device to the other and notifies the user when it's finished.

> While the Android Beam file transfer API handles large amounts of data, the Android Beam NDEF transfer API introduced in Android 4.0 (API level 14) handles small amounts of data such as URIs or other small messages. 

--------------------------------------------------------------------

Dependencies and prerequisites
- Android 4.1 (API Level 16) or higher
- At least two NFC-enabled Android devices (NFC is not supported in the emulator)
