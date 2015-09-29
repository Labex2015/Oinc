package labex.feevale.br.oinc.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import labex.feevale.br.oinc.tasks.CartoonTask;

/**
 * Created by 0126128 on 26/01/2015.
 */
public class ArchiveUtility {

    public static final String TEMPORARY_IMAGE = "temp";
    private Activity activity;

    public ArchiveUtility(Activity activity) {
        this.activity = activity;
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String directoryName, String fileName){

        FileOutputStream fos = null;
        File myPath = loadFile(directoryName, fileName);
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
            return myPath.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Erro", e.getMessage());
            return null;
        }finally {
            bitmapImage.recycle();
            fos = null;
        }
    }

    public String saveTemporaryImage(Bitmap bitmap, String extension){
        return saveImageToInternalStorage(bitmap, TEMPORARY_IMAGE + extension);
    }

    public String saveImageToInternalStorage(Bitmap bitmap, String fileName){
        String directory = AppVariables.IMAGES_DIRECTORY;
        return saveToInternalStorage(bitmap, directory, fileName);
    }

    public String savePicturesToInternalStorage(Bitmap bitmap, String fileName){
        String directory = AppVariables.PICTURES_DIRECTORY;
        return saveToInternalStorage(bitmap, directory, fileName);
    }

    public String saveImageToInternalStorage(String pathToCopy, String fileName) throws IOException {
        String directory = AppVariables.IMAGES_DIRECTORY;
        File imageDestiny = loadFile(directory, fileName);
        copyFiles(new File(pathToCopy), imageDestiny);
        return imageDestiny.getAbsolutePath();
    }

    public String saveImageToInternalStorage(Uri uriImage, String fileName) throws IOException {
        String directory = AppVariables.IMAGES_DIRECTORY;
        File imageDestiny = loadFile(directory, fileName);
        copyFiles(getDeviceFileAsInputStream(uriImage, activity), imageDestiny);
        return imageDestiny.getAbsolutePath();
    }

    private static void copyFiles(File source, File dest)
            throws IOException {
        copyFiles(getFileAsInputStream(source), dest);
    }

    private static void copyFiles(InputStream source, File dest)
            throws IOException {
        OutputStream output = null;
        int bytesRead = 0;
        byte[] buf = null;
        try {
            output = new FileOutputStream(dest);
            buf = new byte[1024];
            while ((bytesRead = source.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            source.close();
            output.close();
            bytesRead = 0;
            output = null;
            source = null;
            buf = null;
        }
    }

    private File loadFile(String directoryName, String fileName){
        //ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        File root = Environment.getExternalStorageDirectory();
        //File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);
        File directory = new File(root.getAbsolutePath()+assemblyDirs("Oinc"));
        if(!directory.exists())
            directory.mkdirs();

        return new File(directory, fileName);
    }

    private String assemblyDirs(String...strings){
        StringBuilder sbDir = new StringBuilder();
        for(String dir:strings){
            sbDir.append(File.separator);
            sbDir.append(dir);
        }
        return sbDir.toString();
    }

    public File generatePicturesFile(String fileName){
        String directory = AppVariables.PICTURES_DIRECTORY;
        return loadFile(directory, fileName);
    }

    private static InputStream getFileAsInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    private static InputStream getDeviceFileAsInputStream(Uri uri, Activity activity) throws FileNotFoundException {
        return activity.getContentResolver().openInputStream(uri);
    }

    public String takePicture(Bitmap bitmapImage, String fileName){
        return saveToInternalStorage(bitmapImage, AppVariables.PICTURES_DIRECTORY, fileName);
    }

    public Boolean removeFile(String absolutePath){
        File file = new File(absolutePath);
        if(file.exists())
            return file.delete();
        else
            return false;
    }

    public static void decodeUri(Uri selectedImage, Activity activity) throws FileNotFoundException {

        CartoonTask.invalidateBitmap();
        InputStream stream = null;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o);

            int width, height;

            if ((o.outWidth * o.outHeight) > (1024 * 748)) {
                width = (o.outWidth * (o.outHeight / 1024)) / 5;
                height = 1024 / 5;
            } else {
                width = o.outWidth / 5;
                height = o.outHeight / 5;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.outHeight = height;
            o2.outWidth = width;
            o2.inSampleSize = 5;
            stream = activity.getContentResolver().openInputStream(selectedImage);
            CartoonTask.bitmap = BitmapFactory.decodeStream(stream, null, o2);
        }finally {
            System.gc();
            if (stream != null)
                try {
                    stream.close();
                    stream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
