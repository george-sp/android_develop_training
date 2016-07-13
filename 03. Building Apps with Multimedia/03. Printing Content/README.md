### Printing Content
Being able to print information from your Android application gives users a way to see a larger version of the content from your app 
or share it with another person who is not using your application.

Printing also allows them to create a snapshot of information that does not depend on having a device, sufficient battery power, or a wireless network connection.

Dependencies and prerequisites
- Android 4.4 (API Level 19) or higher

Video
- [DevBytes: Android 4.4 Printing API](https://developer.android.com/training/printing/index.html)

-----------------------------------------------------------

#### Printing Photos
- This lesson shows you how to print an image.

This lesson shows you how to print an image using the v4 support library [PrintHelper](https://developer.android.com/reference/android/support/v4/print/PrintHelper.html) class.

Taking and sharing photos is one of the most popular uses for mobile devices.
If your application takes photos, displays them, or allows users to share images, you should consider enabling printing of those images in your application.
The [Android Support Library](https://developer.android.com/topic/libraries/support-library/index.html) provides a convenient function for enabling image printing using a minimal amount of code and simple set of print layout options.

#### Printing HTML Documents
- This lesson shows you how to print an HTML document.

This lesson shows you how to quickly build an HTML document containing text and graphics and use [WebView](https://developer.android.com/reference/android/webkit/WebView.html) to print it.

Printing out content beyond a simple photo on Android requires composing text and graphics in a print document.
The Android framework provides a way to use HTML to compose a document and print it with a minimum of code.
In Android 4.4 (API level 19), the [WebView](https://developer.android.com/reference/android/webkit/WebView.html) class has been updated to enable printing HTML content.
The class allows you to load a local HTML resource or download a page from the web, create a print job and hand it off to Android's print services.

|When using [WebView](https://developer.android.com/reference/android/webkit/WebView.html) for creating print documents, you should be aware of the following limitations:|
---
|- You cannot add headers or footers, including page numbers, to the document.
- The printing options for the HTML document do not include the ability to print page ranges, for example: Printing page 2 to 4 of a 10 page HTML document is not supported.|
- An instance of WebView can only process one print job at a time.|
- An HTML document containing CSS print attributes, such as landscape properties, is not supported.|
- You cannot use JavaScript in a HTML document to trigger printing.|

> **Note:** The content of a WebView object that is included in a layout can also be printed once it has loaded a document.
