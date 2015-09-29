package labex.feevale.br.oinc.views.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Créditos para http://www.androidzeitgeist.com/
 * Post: http://www.androidzeitgeist.com/2012/10/using-fragment-for-camera-preview.html
 * Git: https://github.com/pocmo/Instant-Mustache/blob/master/src/com/androidzeitgeist/mustache/listener/CameraOrientationListener.java
 * @author Sebastian Kaspari <sebastian@androidzeitgeist.com>
 */
public class CameraPreview extends SurfaceView{

    private static final double ASPECT_RATIO = 2.0/3.0;

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreview(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        if (width > height * ASPECT_RATIO) {
            width = (int) (height * ASPECT_RATIO + .5);
        } else {
            height = (int) (width / ASPECT_RATIO + .5);
        }

        setMeasuredDimension(width, height);
    }
}
