package labex.feevale.br.oinc.views.fragments.listeners;

import android.graphics.Bitmap;

/**
 * Created by 0126128 on 09/02/2015.
 */
/**
 * Créditos para http://www.androidzeitgeist.com/
 * Post: http://www.androidzeitgeist.com/2012/10/using-fragment-for-camera-preview.html
 * Git: https://github.com/pocmo/Instant-Mustache/blob/master/src/com/androidzeitgeist/mustache/listener/CameraFragmentListener.java
 * @author Sebastian Kaspari <sebastian@androidzeitgeist.com>
 */
public interface CameraListener {
    public void onCameraError();
    void onPictureTaken(Bitmap bitmap);
}
