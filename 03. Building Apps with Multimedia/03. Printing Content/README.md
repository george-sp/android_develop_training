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

#### Printing Custom Documents
- This lesson shows you how you connect to the Android print manager, create a print adapter and build content for printing.

This lesson also shows how to use the PrintedPdfDocument class to generate PDF pages from your content. _(You can use any PDF generation library for this purpose.)_

For application that focus on graphic output, creating beautiful printed pages is a key feature. The print output for these types of application requires precise control of everything that goes into the page, including fonts, text flow, page breaks, headers, footers and graphic elements.

_(Creating print output that is completely customized for your application requires more programming investment than the previously discussed approaches. You must build components that communicate with the print framework, adjust to printer settings, draw page elements and manage printing on multiple pages.)_

----------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------------------------------------------------------------------------------------

Create a Print Adapter
--------------------------------
A print adapter interacts with the Android print framework and handles the steps of the printing process. This process requires users to select printers and print options before creating a document for printing. These selections can influence the final output as the user chooses printers with different output capabilities, different page sizes, or different page orientations. As these selections are made, the print framework asks your adapter to lay out and generate a print document, in preparation for final output. Once a user taps the print button, the framework takes the final print document and passes it to a print provider for output. During the printing process, users can choose to cancel the print action, so your print adapter must also listen for and react to a cancellation requests.

The [PrintDocumentAdapter](https://developer.android.com/reference/android/print/PrintDocumentAdapter.html) abstract class is designed to handle the printing lifecycle, which has four main callback methods. You must implement these methods in your print adapter in order to interact properly with the print framework:

- [onStart()](https://developer.android.com/reference/android/print/PrintDocumentAdapter.html#onStart()) - Called once at the beginning of the print process. If your application has any one-time preparation tasks to perform, such as getting a snapshot of the data to be printed, execute them here. Implementing this method in your adapter is not required.
- [onLayout()](https://developer.android.com/reference/android/print/PrintDocumentAdapter.html#onLayout(android.print.PrintAttributes, android.print.PrintAttributes, android.os.CancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback, android.os.Bundle)) - Called each time a user changes a print setting which impacts the output, such as a different page size, or page orientation, giving your application an opportunity to compute the layout of the pages to be printed. At the minimum, this method must return how many pages are expected in the printed document.
- [onWrite()](https://developer.android.com/reference/android/print/PrintDocumentAdapter.html#onLayout(android.print.PrintAttributes, android.print.PrintAttributes, android.os.CancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback, android.os.Bundle)) - Called to render printed pages into a file to be printed. This method may be called one or more times after each onLayout() call.
- [onFinish()](https://developer.android.com/reference/android/print/PrintDocumentAdapter.html#onLayout(android.print.PrintAttributes, android.print.PrintAttributes, android.os.CancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback, android.os.Bundle)) - Called once at the end of the print process. If your application has any one-time tear-down tasks to perform, execute them here. Implementing this method in your adapter is not required.

The following sections describe how to implement the layout and write methods, which are critical to the functioning of a print adapter.

> **Note:** These adapter methods are called on the main thread of your application. If you expect the execution of these methods in your implementation to take a significant amount of time, implement them to execute within a separate thread. For example, you can encapsulate the layout or print document writing work in separate [AsyncTask](https://developer.android.com/reference/android/print/PrintDocumentAdapter.html#onLayout(android.print.PrintAttributes, android.print.PrintAttributes, android.os.CancellationSignal, android.print.PrintDocumentAdapter.LayoutResultCallback, android.os.Bundle)) objects. 
