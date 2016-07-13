package com.codeburrow.printcontent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // Load the HTML resource into the WebView object.
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeActionOverflowMenuShown();
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
//                createWebPrintJob(webView);
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
//        webView.loadDataWithBaseURL("file:///android_asset/images/", htmlDocument, "text/HTML", "UTF-8", null);
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
}
