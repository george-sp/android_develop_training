package com.codeburrow.capturephotos;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // The request code for the take picture intent.
    static final int REQUEST_IMAGE_CAPTURE = 1;
    // Image View to display the picture captured by the user.
    private ImageView mImageView;
    // A collision-resistant file name.
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.result_picture_image_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*
             * The Android Camera application encodes the photo in the return Intent
             * delivered to onActivityResult() as a small Bitmap in the extras,
             * under the key "data".
             *
             * The following code retrieves this image and displays it in an ImageView.
             */
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
            /* Note:
                     This thumbnail image from "data" might be good for an icon,
                     but not a lot more.
                     Dealing with a full-sized image takes a bit more work. */

            // Decode a Scaled Image.
            setPic();
            // Add the Photo to a Gallery.
            galleryAddPic();
        }
    }

    /**
     * Helper Method.
     * <p/>
     * The Android way of delegating actions to other applications is
     * to invoke an Intent that describes what you want done.
     * <p/>
     * This process involves three pieces:
     * - the Intent itself
     * - a call to start the external Activity
     * - and some code to handle the image data when focus returns to your activity.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there is a camera activity to handle the intent.
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // Error occurred while creating the File.
                Log.e(LOG_TAG, e.getMessage());
            }
            // Continue only if the File was successfully created.
            if (photoFile != null) {
                /*
                 * NOTE:
                 *      We are using getUriForFile(Context, String, File)
                 *      which returns a content:// URI.
                 *
                 *      For more recent apps targeting Android N and higher,
                 *      passing a file:// URI across a package boundary
                 *      causes a FileUriExposedException.
                 *
                 *      Therefore, we now present a more generic way of storing images
                 *      using a FileProvider.
                 */
                Uri photoURI = FileProvider.getUriForFile(this, "com.codeburrow.capturephotos.fileprovider", photoFile);
                Log.e(LOG_TAG, "photoURI: " + photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // Retrieve all activities that can be performed for the given intent.
                List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                // Grant permission to access the photoUri to these activities.
                for (ResolveInfo resolveInfo : resolveInfoList) {
                    String toPackageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(
                            // The package you would like to allow to access the Uri.
                            toPackageName,
                            // The Uri you would like to grant access the Uri.
                            photoURI,
                            // The desired access modes.
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );
                }
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Creates a file for the photo.
     *
     * @return A unique file name for a new photo using a date-time stamp.
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        /*
         * Note:
         *      If you saved your photo to the directory provided by
         *      getExternalFilesDir(), the media scanner cannot access the files
         *      because they are private to your app.
         *
         * /storage/sdcard0/Android/data/com.codeburrow.capturephotos/files/Pictures
         */
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory*/
        );
        // Save a file: path for use with ACTION_VIEW intents.
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e(LOG_TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return image;
    }

    public void takePictureButtonPressed(View view) {
        dispatchTakePictureIntent();
    }

    /**
     * Helper Method.
     * <p/>
     * Invokes the system's Media Scanner
     * to add your photo to the Media Provider's database,
     * making it available in the Android Gallery application and to other apps.
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        Log.e(LOG_TAG, "contentUri: " + contentUri.toString());
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        /*
         * Note:
         *      If you saved your photo to the directory provided
         *      by getExternalFilesDir(), the media scanner cannot access the files
         *      because they are private to your app.
         *
         *      In such a case, do the follow:
         */
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, mCurrentPhotoPath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * Helper Method.
     * <p/>
     * Reduces the amount of dynamic heap used
     * by expanding the JPEG into a memory array
     * that's already scaled to match the size of the destination view.
     */
    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
