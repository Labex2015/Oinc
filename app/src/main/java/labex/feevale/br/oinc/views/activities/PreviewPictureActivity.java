package labex.feevale.br.oinc.views.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import labex.feevale.br.oinc.MainActivity;
import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.dao.EntryDao;
import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.utils.AppVariables;

/**
 * Created by 0126128 on 11/02/2015.
 */
public class PreviewPictureActivity extends Activity {


    private ImageButton sharePictureButton;
    private Button finishEntryButton;
    private ImageView previewTripImageView;

    private Entry entry;
    private UiLifecycleHelper uiLifecycleHelper;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiLifecycleHelper = new UiLifecycleHelper(this, null);
        uiLifecycleHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_picture);

        Bundle bundle = getIntent().getExtras();
        entry = (Entry)bundle.getSerializable(AppVariables.ENTRY_KEY);
        if(entry == null){
            Toast.makeText(this, "Problemas ao serializar galeria", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

        Uri image = Uri.fromFile(new File(entry.getPicturePath()));
        Bitmap bitmap;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        }

        previewTripImageView = (ImageView)findViewById(R.id.tripPreviewImageView);
        previewTripImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        previewTripImageView.setImageBitmap(bitmap);


        sharePictureButton = (ImageButton) findViewById(R.id.sharedPictureImageButton);
        sharePictureButton.setOnClickListener(sharePictureEventClick());

        finishEntryButton = (Button) findViewById(R.id.finishEntryButton);
        finishEntryButton.setOnClickListener(saveAndCloseEventClick());
    }

    private View.OnClickListener saveAndCloseEventClick() {
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GoalDao.updateGoal(entry, activity)) {
                    EntryDao.saveWithSound(entry, activity);
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    previewTripImageView.setImageBitmap(null);
                    activity.finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Problema ao tentar salvar lançamento", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode,data, new FacebookDialog.Callback() {
            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle bundle) {
                Toast.makeText(getApplicationContext(),"Imagem compartilhada.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception e, Bundle bundle) {
                Toast.makeText(getApplicationContext(),"Problemas ao compartilhar imagem.", Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        uiLifecycleHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(AppVariables.ENTRY_KEY, entry);
        super.onSaveInstanceState(savedInstanceState);
        uiLifecycleHelper.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    private void sharePictureOnFaceBook() {
       if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.PHOTOS)) {
            Bitmap bitmap = BitmapFactory.decodeFile(entry.getPicturePath());
            List<Bitmap> images = new ArrayList<Bitmap>();
            images.add(bitmap);
            FacebookDialog shareDialog = new FacebookDialog.PhotoShareDialogBuilder(this)
                    .addPhotos(images)
                    .build();
            uiLifecycleHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            Toast.makeText(this, "Você tem que instalar o aplicativo do " +
                    "Facebook para poder compartilhar imagens.", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener sharePictureEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePictureOnFaceBook();
            }
        };
    }

}
