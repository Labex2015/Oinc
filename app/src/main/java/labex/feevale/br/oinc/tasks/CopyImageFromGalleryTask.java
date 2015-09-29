package labex.feevale.br.oinc.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.views.actions.ProcessImageAction;

/**
 * Created by 0126128 on 02/02/2015.
 */
public class CopyImageFromGalleryTask extends AsyncTask<Void, Boolean, Boolean> {

    private Uri imageUri;
    private String imageName;
    private String imageAbsolutePath;
    private Activity activity;
    private static Bitmap bitmap;

    private ProcessImageAction processImageAction;

    private ProgressDialog progressDialog;

    public CopyImageFromGalleryTask(String imageName, Uri imageUri,
                                    Activity activity) {
        this.imageName = imageName;
        this.imageUri = imageUri;
        this.activity = activity;
    }

    public CopyImageFromGalleryTask(String imageName, Uri imageUri,
                                    Activity activity, ProcessImageAction processImageAction) {
        this.imageName = imageName;
        this.imageUri = imageUri;
        this.activity = activity;
        this.processImageAction = processImageAction;
    }

    @Override
    protected void onPreExecute() {

        this.progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Obtendo imagem....");
        this.progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
            imageAbsolutePath = new ArchiveUtility(activity).saveImageToInternalStorage(bitmap, imageName);
            bitmap.recycle();
            bitmap = null;
            if (imageAbsolutePath == null || imageAbsolutePath.length() <= 4)
                throw new Exception("Imagem veio nula.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        if(status) {
            if (processImageAction != null){
                processImageAction.executeAction(imageAbsolutePath);
            }else{
                Toast.makeText(activity, "Problemas ao carregar imagem.", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(activity, "Problemas ao obter imagem da galeria.", Toast.LENGTH_LONG).show();
        }
    }
}
