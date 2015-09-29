package labex.feevale.br.oinc.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.utils.AppVariables;
import labex.feevale.br.oinc.utils.CreateSequence;
import labex.feevale.br.oinc.views.activities.PreviewPictureActivity;

/**
 * Created by Jeferson klaus on 05/02/2015.
 */
public class CreateTrip extends AsyncTask<Void, Boolean, Boolean> {

    private ProgressDialog progressDialog;
    private List<String> comments;
    private Activity activity;
    private Entry entry;

    public CreateTrip(Entry entry, Activity activity) {
        this.entry = entry;
        this.activity = activity;
        this.comments = entry.getComments();
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage("Gerando tira...");
        this.progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return CreateSequence.generateSequence(entry, comments, activity, generateRandomPictureName());
    }

    @Override
    protected void onPostExecute(Boolean status) {
        if(progressDialog.isShowing())
            progressDialog.dismiss();

        if(!status)
            Toast.makeText(activity, "Problemas ao gerar tira.", Toast.LENGTH_LONG).show();
        else{
            Intent intentOpenCamera = new Intent(activity.getApplicationContext(), PreviewPictureActivity.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable(AppVariables.ENTRY_KEY, entry);
            intentOpenCamera.putExtras(bundle);
            activity.startActivity(intentOpenCamera);
        }
    }

    private String generateRandomPictureName(){
        return new SimpleDateFormat("ddMMyyyy_HHmm")
                .format(Calendar.getInstance().getTime())+
                (Math.round(Math.random()*10))+".jpg";
    }
}
