package amaz.objects.TwentyfourSeven.listeners;

import android.content.Intent;

import java.io.File;

/**
 * Created by objects on 24/04/18.
 */

public interface OnRequestImageIntentListener {
    void onRequestGallery(Intent gallery);
    void onRequestCamera(Intent camera, File image);
}
