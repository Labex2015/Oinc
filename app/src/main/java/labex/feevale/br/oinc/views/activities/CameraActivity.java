package labex.feevale.br.oinc.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.tasks.CartoonTask;
import labex.feevale.br.oinc.utils.AppVariables;
import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.views.actions.ProcessImageAction;

/**
 * Created by 0126128 on 06/02/2015.
 */
public class CameraActivity extends Activity implements Camera.PictureCallback, SurfaceHolder.Callback, ProcessImageAction {

    public static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    public static final int PREVIEW_SIZE_MAX_WIDTH = 720;
    private static final int SELECT_PICTURE = 1;

    private ImageButton goBackButton, takePictureButton, deviceGalleryButton;
    private RelativeLayout footerButtonsLayout;

    private Camera camera;
    private SurfaceView cameraPreview;
    private View container;
    private int cameraId;
    private int displayOrientation;
    private int layoutOrientation;
    private byte[] pictureData;

    private Entry entry;
    private String imagePath;
    //private static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        entry = (Entry)bundle.getSerializable(AppVariables.ENTRY_KEY);
        if(entry == null){
            Toast.makeText(this, "Problemas ao serializar galeria", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        if(savedInstanceState == null){
            setContentView(R.layout.activity_camera);
            footerButtonsLayout = (RelativeLayout) findViewById(R.id.buttons_footer_camera);

            goBackButton = (ImageButton) footerButtonsLayout.findViewById(R.id.goBackButton);
            goBackButton.setOnClickListener(backButtonEventClick());

            takePictureButton = (ImageButton) footerButtonsLayout.findViewById(R.id.takePictureButton);
            takePictureButton.setOnClickListener(takePictureClickListener());

            deviceGalleryButton = (ImageButton) footerButtonsLayout.findViewById(R.id.deviceGalleryButton);
            deviceGalleryButton.setOnClickListener(openDeviceGalleryButtonEventClick());

            cameraPreview = (SurfaceView) findViewById(R.id.preview_view);
            container = findViewById(R.id.containerCamera);
            final SurfaceHolder holder = cameraPreview.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    private View.OnClickListener backButtonEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    private View.OnClickListener openDeviceGalleryButtonEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.stopPreview();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Selecione uma imagem"), SELECT_PICTURE);
            }
        };
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    ArchiveUtility.decodeUri(data.getData(), this);
                    processImage();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Problema ao carregar imagem da galeria.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                Toast.makeText(CameraActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(entry.getImagesPath().size() > 0)
            super.onBackPressed();

        Intent mIntent = new Intent(this,EntryActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(AppVariables.ENTRY_KEY, entry);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        this.finish();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        CartoonTask.bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        /** Como não vamos permitir rotacionar o app, vou setar a rotation pra 90, sempre portrait
         * Usar esse trecho de código caso se permita rotationar a app;
         int rotation = (
         displayOrientation
         + orientationListener.getRememberedOrientation()
         + layoutOrientation
         ) % 360;
         Log.e("CAMERA_ROTATION",rotation+"");
         **/
        int rotation = 90;//Ver comentário a cima;
        if (rotation != 0) {
            Bitmap oldBitmap = CartoonTask.bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            CartoonTask.bitmap = Bitmap.createBitmap(
                    CartoonTask.bitmap,
                    0,
                    0,
                    CartoonTask.bitmap.getWidth(),
                    CartoonTask.bitmap.getHeight(),
                    matrix,
                    false
            );
            try {
                oldBitmap.recycle();
            }finally {
                oldBitmap = null;
            }
        }
        processImage();
    }

    private void captureImage() {
        camera.takePicture(null, null, this);
    }

    private View.OnClickListener takePictureClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        };
    }

    private View.OnClickListener backPressClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            try {
                camera = Camera.open(cameraId);
                if(camera != null)
                    startCameraPreview();
                camera.startPreview();
            } catch (Exception e) {
                Toast.makeText(CameraActivity.this, "Unable to open camera.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();
    }

    public void determineDisplayOrientation() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees  = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;

            case Surface.ROTATION_90:
                degrees = 90;
                break;

            case Surface.ROTATION_180:
                degrees = 180;
                break;

            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation;

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }
        this.displayOrientation = displayOrientation;
        this.layoutOrientation  = degrees;

        camera.setDisplayOrientation(displayOrientation);
    }

    public void setupCamera() {
        Camera.Parameters parameters = camera.getParameters();

        Camera.Size bestPreviewSize = determineBestPreviewSize(parameters);
        Camera.Size bestPictureSize = determineBestPictureSize(parameters);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);

        camera.setParameters(parameters);
    }

    private Camera.Size determineBestPreviewSize(Camera.Parameters parameters) {
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

        return determineBestSize(sizes, PREVIEW_SIZE_MAX_WIDTH);
    }

    private Camera.Size determineBestPictureSize(Camera.Parameters parameters) {
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

        return determineBestSize(sizes, PICTURE_SIZE_MAX_WIDTH);
    }

    protected Camera.Size determineBestSize(List<Camera.Size> sizes, int widthThreshold) {
        Camera.Size bestSize = null;

        for (Camera.Size currentSize : sizes) {
            boolean isDesiredRatio = (currentSize.width / 4) == (currentSize.height / 3);
            boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
            boolean isInBounds = currentSize.width <= PICTURE_SIZE_MAX_WIDTH;

            if (isDesiredRatio && isInBounds && isBetterSize) {
                bestSize = currentSize;
            }
        }

        if (bestSize == null) {
         //   listener.onCameraError();
            return sizes.get(0);
        }

        return bestSize;
    }

    private String generateRandomPictureName(){
        return new SimpleDateFormat("ddMMyyyy_HHmm")
                .format(Calendar.getInstance().getTime())+
                (Math.round(Math.random()*10))+entry.getImagesPath().size()+".jpg";
    }


    private void closeCameraActivity(){
        Intent mIntent = new Intent(this,EntryActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle mBundle = new Bundle();
        this.entry.getImagesPath().add(imagePath);
        this.entry.getComments().add("");
        mBundle.putSerializable(AppVariables.ENTRY_KEY, entry);
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
        this.finish();
    }

    private void processImage(){
        if(camera != null)
            camera.stopPreview();
        //cameraPreview.getHolder().lockCanvas();
        new CartoonTask(this, generateRandomPictureName(), this, getResources()).execute();
    }

    @Override
    public void executeAction(String image) {
        this.imagePath = image;
        closeCameraActivity();
    }


}
