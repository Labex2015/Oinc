package labex.feevale.br.oinc.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.views.actions.SaveImageTaskAction;

/**
 * Created by 0126128 on 03/03/2015.
 */
public class CopyImageTask extends AsyncTask<Void,Boolean, Boolean> {

    private Activity activity;
    private String imageName;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private SaveImageTaskAction saveImageTaskAction;

    public CopyImageTask(Activity activity, Uri imageUri, String imageName, SaveImageTaskAction saveImageTaskAction) {
        this.activity = activity;
        this.saveImageTaskAction = saveImageTaskAction;
        this.imageName = imageName;
        this.imageUri = imageUri;
    }

    public CopyImageTask(Activity activity, String imageName, SaveImageTaskAction saveImageTaskAction) {
        this.activity = activity;
        this.saveImageTaskAction = saveImageTaskAction;
        this.imageName = imageName;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.loading));
        progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String imagePath = "";
            if(imageUri  != null) {
                imagePath = new ArchiveUtility(activity).saveImageToInternalStorage(imageUri,
                        imageName.trim().replaceAll(" ", "_"));
            }else if(CartoonTask.bitmap != null){
                imagePath = new ArchiveUtility(activity).saveImageToInternalStorage(CartoonTask.bitmap,
                        imageName.trim().replaceAll(" ", "_"));
            }
            return saveImageTaskAction.doInBackground(imagePath);
        }catch (Exception e){
            Log.e("ERRO", "Ops, problemas em salvar dados.");
            e.printStackTrace();
            return false;
        }finally {
            CartoonTask.invalidateBitmap();
        }
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        if(status)
            saveImageTaskAction.postExecuteSuccess();
        else
            saveImageTaskAction.postExecuteFail();
    }
}
