package com.codeburrow.printcontent;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // Load the HTML resource into the WebView object.
    private WebView mWebView;
    private List<PrintJob> mPrintJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Show the overflow menu button.
        makeActionOverflowMenuShown();
        // Initialize mPrintJobs.
        mPrintJobs = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_print_photos:
                Toast.makeText(MainActivity.this, "Printing Photos", Toast.LENGTH_SHORT).show();
                doPhotoPrint();
                return true;
            case R.id.menu_print_html_documents:
                Toast.makeText(MainActivity.this, "Printing HTML Documents", Toast.LENGTH_SHORT).show();
                doWebViewPrint();
                return true;
            case R.id.menu_print_custom_documents:
                Toast.makeText(MainActivity.this, "Printing Custom Documents", Toast.LENGTH_SHORT).show();
                doPrint();
                return true;
            default:
                return false;
        }
    }

    /**
     * Helper Method.
     * <p/>
     * Show the action overflow menu in devices with physical menu button (e.g. Samsung Note)
     */
    private void makeActionOverflowMenuShown() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Helper Method.
     * <p/>
     * Prints a Photo.
     * <p/>
     * This method can be called as the action for a menu item.
     * Note that actions that are not always supported (such as printing)
     * should be placed in the overflow menu.
     * ------------------------------------------------------------------
     * setScaleMode() allows you to print with one of two options:
     * - SCALE_MODE_FIT: This option sizes the image so that the whole image is shown within
     * the printable area of the page.
     * - SCALE_MODE_FILL: This option scales the image so that it fills the entire printable area of the page.
     * (Choosing this setting means that some portion of the top and bottom, or left and right edges of the images is not printed.
     * This option is the default value if you do not set a scale mode.)
     */
    private void doPhotoPrint() {
        // Get the print manager.
        PrintHelper photoPrinter = new PrintHelper(this);
        // Set the desired scale mode.
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        // Get the bitmap from the drawables.
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droids);
        // Print the bitmap.
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    /**
     * Helper Method.
     * <p/>
     * Creates a simple WebViewClient and load an HTML document created on the fly.
     * <p/>
     * Printing an HTML document with WebView involves
     * - loading an HTML resource OR
     * - building an HTML document as a string.
     * <p/>
     * This section describes how to build an HTML string and load it into a WebView for printing.
     * This view object is typically used as part of an activity layout.
     * However, if your application is not using a WebView,
     * you can create an instance of the class specifically for printing purposes.
     * <p/>
     * The main steps for creating this custom print view are:
     * 1. Create a WebViewClient that starts a print job after the HTML resource is loaded.
     * 2. Load the HTML resource into the WebView object.
     */
    private void doWebViewPrint() {
        // Create a WebView object specifically for printing.
        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                Log.e(LOG_TAG, "Page finished loading: " + url);

                /*
                 *  NOTE:
                 *          Make sure your call for generating a print job happens
                 *          in the onPageFinished() method of the WebViewClient.
                 *          If you don't wait until page loading is finished,
                 *          the print output may be incomplete or blank,
                 *          or may fail completely.
                 */
                createWebPrintJob(webView);
                mWebView = null;
            }
        });
        // Generate an HTML document on the fly:
        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " +
                "testing, testing...</p></body></html>";
//        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
        /*
         * If you want to include graphics in the page,
         * place the graphic files in the assets/ directory of your project
         * and specify a base URL in the first parameter of the loadDataWithBaseURL().
          */
        String htmlBody = "<html><body><h1>Test Content - Image</h1><img src=\"foo.jpg\" /></body></html>";
//        webView.loadDataWithBaseURL("file:///android_asset/images/", htmlBody, "text/HTML", "UTF-8", null);
        /*
         * You can also load a web page for printing
         * by replacing the loadDataWithBaseURL() method with loadUrl()
         *
         * Print an existing web page (remember to request INTERNET permission!)
         */
        webView.loadUrl("http://developer.android.com/about/index.html");
        // Hold an instance of the WebView object so that is it not garbage collected before the print job is created.
        // Keep a reference to WebView object until you pass the PrintDocumentAdapter to the PrintManager.
        mWebView = webView;
    }

    /**
     * Helper Method.
     * <p/>
     * Prints an HTML Document.
     * <p/>
     * The steps are:
     * - accessing the PrintManager
     * - creating a print adapter
     * - creating a print job.
     *
     * @param webView
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void createWebPrintJob(WebView webView) {
        // Get a PrintManager instance.
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        // Get a print adapter instance.
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        // Create a print job with name and adapter instance.
        String jobName = getString(R.string.app_name) + " Document";
        PrintJob printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
        // Save the job object for later status checking. (is not required)
        mPrintJobs.add(printJob);
    }

    /**
     * Helper Method.
     * <p/>
     * Initializes a print job and begin the printing lifecycle.
     * <p/>
     * When your application manages the printing process directly,
     * the first step after receiving a print request from your user
     * is to connect to Android print framework
     * and obtain an instance of the PrintManager class.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void doPrint() {
        // Get a PrintManager instance.
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        // Set job name, which will be displayed in the print queue.
        String jobName = getString(R.string.app_name) + " Document";
        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document.
        /*
         * The last parameter in the print() method takes a PrintAttributes object.
         * You can use this parameter to provide hints to the printing framework
         * and pre-set options based on the previous printing cycle,
         * thereby improving the UX - user experience.
         * You may also use this parameter to set options that are more appropriate
         * to the content being printed, such as setting the orientation
         * to landscape when printing a photo that is in that orientation.
         */
        printManager.print(jobName, new MyPrintDocumentAdapter(this), null);
    }

    /**
     * A Custom PrintDocumentAdapter.
     * <p/>
     * The PrintDocumentAdapter abstract class is designed to handle the printing lifecycle,
     * which has four main callback methods:
     * - onStart()
     * - onLayout()
     * - onWrite()
     * - onFinish()
     * You must implement these methods in your print adapter in order to interact
     * properly with the print framework.
     * ------------------------------------------------------------------------------
     * Note: These adapter methods are called on the main thread of your application.
     * If you expect the execution of these methods in your implementation
     * to take a significant amount of time,
     * implement them to execute within a separate thread.
     * <p/>
     * For example, you can encapsulate the layout or print document writing
     * work in separate AsyncTask objects.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class MyPrintDocumentAdapter extends PrintDocumentAdapter {

        private Context mContext;
        // A PDF document.
        private PrintedPdfDocument mPdfDocument;
        // A sparse array of integers to keep track which pages are written.
        private SparseIntArray mWrittenPagesArray;
        // Number of pages to be printed.
        private int mPages;

        public MyPrintDocumentAdapter(Context context) {
            mContext = context;
            mWrittenPagesArray = new SparseIntArray();
        }

        /**
         * Called when the print attributes (page size, density, etc) changed
         * giving you a chance to layout the content such that it matches the new constraints.
         * <p/>
         * This method is invoked on the main thread.
         * <p/>
         * The execution of onLayout() method can have three outcomes:
         * 1. completion
         * 2. cancellation
         * 3. failure in the case where calculation of the layout cannot be completed.
         * <p/>
         * You must indicate one of these results by calling the appropriate method of
         * the PrintDocumentAdapter.LayoutResultCallback object.
         *
         * @param oldAttributes        The old print attributes.
         * @param newAttributes        The new print attributes.
         * @param cancellationSignal   Signal for observing cancel layout requests.
         * @param layoutResultCallback Callback to inform the system for the layout result.
         * @param bundle               Additional information about how to layout the content.
         */
        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback layoutResultCallback,
                             Bundle bundle) {
            // Create a new PdfDocument with the requested page attributes - newAttributes.
            mPdfDocument = new PrintedPdfDocument(mContext, newAttributes);
            // Respond to cancellation request.
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
                return;
            }
            // Compute the expected number of printed pages.
            mPages = computePageCount(newAttributes);
            if (mPages > 0) {
                // Return print information to print framework.
                PrintDocumentInfo.Builder infoBuilder = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(mPages);
                PrintDocumentInfo info = infoBuilder.build();
                // Content layout reflow is complete.
                /*
                 * Note:
                 *          The boolean parameter of the onLayoutFinished() method
                 *          indicates whether or not the layout content has actually
                 *          changed since the last request.
                 *          Setting this parameter properly allows the print framework
                 *          to avoid unnecessarily calling the onWrite() method,
                 *          essentially caching the previously written print document
                 *          and improving performance.
                 */
                layoutResultCallback.onLayoutFinished(info, true);
            } else {
                // Otherwise report an error to the print framework.
                layoutResultCallback.onLayoutFailed("Page count calculation failed.");
            }
        }

        /**
         * The main work of onLayout() is calculating the number of pages that are expected
         * as output given the attributes of the printer.
         * How you can calculate this number is highly dependent on how your application
         * lays out pages for printing.
         * -----------------------------
         * The computePageCount shows an implementation where the number of pages
         * is determined by the print orientation.
         *
         * @param printAttributes The attributes of the printer.
         * @return int: The number of pages to be printed.
         */
        private int computePageCount(PrintAttributes printAttributes) {
            // Default item count for portrait mode.
            int itemsPerPage = 4;
            // Get the media size.
            PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
            if (!pageSize.isPortrait()) {
                // Six items per page in landscape orientation.
                itemsPerPage = 6;
            }
            // Determine number of print items.
            int printItemCount = getPrintItemCount();
            /*
             * Math.ceil(double x):
             *          Returns the smallest double value that is greater than or equal
             *          to the argument and is equal to a mathematical integer.
             */
            return (int) Math.ceil(printItemCount / itemsPerPage);
        }

        public int getPrintItemCount() {
            return 5;
        }

        /**
         * Called when specific pages of the content should be written
         * in the form of a PDF file to the given file descriptor.
         * <p/>
         * This method is invoked in the main thread.
         * <p/>
         * When it is time to write print output to a file,
         * the Android print framework calls the onWrite() method
         * of your application's PrintDocumentAdapter class.
         * <p/>
         * When this process is complete, you call the onWriteFinished() method of the callback object.
         * <p/>
         * Note:
         * The Android print framework may call the onWrite() method one or more times
         * for every call to onLayout().
         * For this reason, it is important to set the boolean parameter of
         * onLayoutFinished() method to false
         * when the print content layout has not changed,
         * to avoid unnecessary re-writes of the print document.
         * <p/>
         * As with layout, execution of onWrite() method can have three outcomes:
         * 1. completion
         * 2. cancellation
         * 3. failure in the case where the the content cannot be written.
         * You must indicate one of these results by calling the appropriate method
         * of the PrintDocumentAdapter.WriteResultCallback object.
         *
         * @param pageRanges           The pages whose content to print - non-overlapping in ascending order.
         * @param parcelFileDescriptor The destination file descriptor to which to write.
         * @param cancellationSignal   Signal for observing cancel writing requests.
         * @param writeResultCallback  Callback to inform the system for the write result.
         */
        @Override
        public void onWrite(final PageRange[] pageRanges,
                            final ParcelFileDescriptor parcelFileDescriptor, // destination
                            final CancellationSignal cancellationSignal,
                            final WriteResultCallback writeResultCallback) {
            // Iterate over each page of the document,
            // check if it's in the output range.
            for (int i = 0; i < mPages; i++) {
                // Check to see if this page is in the output range.
                if (containsPage(pageRanges, i)) {
                    // If so, add it to mWrittenPagesArray.
                    // mWrittenPagesArray.size() is used to compute the next output page index.
                    mWrittenPagesArray.append(mWrittenPagesArray.size(), i);
                    PdfDocument.Page page = mPdfDocument.startPage(i);
                    // Check for cancellation.
                    if (cancellationSignal.isCanceled()) {
                        writeResultCallback.onWriteCancelled();
                        mPdfDocument.close();
                        mPdfDocument = null;
                        return;
                    }
                    // Draw page content for printing.
                    drawPage(page);
                    // Rendering is complete, so page can be finalized.
                    mPdfDocument.finishPage(page);
                }
            }
            // Write PDF document to file.
            try {
                mPdfDocument.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
            } catch (IOException e) {
                writeResultCallback.onWriteFailed(e.toString());
            } finally {
                mPdfDocument.close();
                mPdfDocument = null;
            }
            PageRange[] writtenPages = computeWrittenPages();
            // Signal the print framework the document is complete.
            writeResultCallback.onWriteFinished(writtenPages);
        }

        /**
         * Identifies whether the specified page number is within the page ranges.
         *
         * @param pageRanges The page ranges in which the specified page number should be.
         * @param page       int The page number to be checked.
         * @return boolean: True if the specified page number is within the page ranges, else False.
         */
        private boolean containsPage(PageRange[] pageRanges, int page) {
            final int pageRangeCount = pageRanges.length;
            for (int i = 0; i < pageRangeCount; i++) {
                if (pageRanges[i].getStart() <= page && pageRanges[i].getEnd() >= page) {
                    return true;
                }
            }
            return false;
        }

        private PageRange[] computeWrittenPages() {
            List<PageRange> pageRanges = new ArrayList<>();
            int start = 0;
            int end;
            final int writtenPageCount = mWrittenPagesArray.size();
            for (int i = 0; i < writtenPageCount; i++) {
                if (start < 0) {
                    start = mWrittenPagesArray.valueAt(i);
                }
                int oldEnd = end = start;
                while (i < writtenPageCount && (end - oldEnd) <= 1) {
                    oldEnd = end;
                    end = mWrittenPagesArray.valueAt(i);
                    i++;
                }
                try {
                    PageRange pageRange = new PageRange(start, end);
                    pageRanges.add(pageRange);
                } catch (IllegalArgumentException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
                start = end = -1;
            }
            PageRange[] pageRangesArray = new PageRange[pageRanges.size()];
            pageRanges.toArray(pageRangesArray);
            return pageRangesArray;
        }

        /**
         * Draw elements on the printed page using the Canvas draw methods.
         * <p/>
         * Tip:
         * While the Canvas object allows you to place print elements
         * on the edge of a PDF document, many printers are not able to print
         * the edge of a physical piece of paper.
         * Make sure that you account for the unprintable edges of the page
         * when you build a print document with this class.
         *
         * @param page The current Pdf page.
         */
        private void drawPage(PdfDocument.Page page) {
            Canvas canvas = page.getCanvas();
            // Units are in points (1/72 of an inch).
            // Make sure you use this unit of measure for specifying the size of elements on the page.
            int titleBaseLine = 72;
            int leftMargin = 54;
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(36);
            canvas.drawText("Test Title", leftMargin, titleBaseLine, paint);
            paint.setTextSize(11);
            canvas.drawText("Test paragraph", leftMargin, titleBaseLine + 25, paint);
            paint.setColor(Color.BLUE);
            canvas.drawRect(100, 100, 172, 172, paint);
        }
    }
}
