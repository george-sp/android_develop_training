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
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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
            default:
                return false;
        }
    }

    /**
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
}
