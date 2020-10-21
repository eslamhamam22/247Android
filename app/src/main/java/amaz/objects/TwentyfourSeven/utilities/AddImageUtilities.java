package amaz.objects.TwentyfourSeven.utilities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import amaz.objects.TwentyfourSeven.listeners.OnRequestImageIntentListener;

/**
 * Created by objects on 24/04/18.
 */

public class AddImageUtilities {

    public static void openGalleryOrCameraIntent(int galleryOrCamera, Activity activity, OnRequestImageIntentListener listener){
        Log.e("flag","flag"+galleryOrCamera);
        if (galleryOrCamera == Constants.GALLERY){
            Log.e("gallery","gallery");
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            listener.onRequestGallery(galleryIntent);
        }
        else{
            Log.e("camera","camera");
            Uri capturedImageUri;
            File capturedImage = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                try {
                    capturedImage = createImageFile(activity);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                capturedImageUri = FileProvider.getUriForFile(activity,
                        activity.getApplicationContext().getPackageName() + ".provider",
                        capturedImage);
            }
            else {
                capturedImage = new File(Environment.getExternalStorageDirectory(),"Pic"+randomString(4)+".jpg");
                capturedImageUri = Uri.fromFile(capturedImage);
            }
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,capturedImageUri);
            listener.onRequestCamera(cameraIntent,capturedImage);
        }

    }

    public static String getImagePath(Uri imageUri, Context context){
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri,filePath,null,null,null);
        String imagePath = "";
        if (cursor != null){
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }

    private static String randomString(int len) {
        final String AB = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri uri, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static void resizeImage(File imageFile) {

        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imageFile.getPath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / 800, photoH / 800);
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;

            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
                BitmapFactory.decodeFile(imageFile.getPath(), bmOptions);
        } catch (OutOfMemoryError e2) {

           // Utils.showToast(this, getResources().getString(R.string.image_size));
        }
    }


}
