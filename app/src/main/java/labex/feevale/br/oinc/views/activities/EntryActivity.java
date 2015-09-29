package labex.feevale.br.oinc.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import labex.feevale.br.oinc.MainActivity;
import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.actions.Observer;
import labex.feevale.br.oinc.dao.EntryDao;
import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.tasks.CreateTrip;
import labex.feevale.br.oinc.utils.AppVariables;
import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.views.actions.CameraActions;
import labex.feevale.br.oinc.views.actions.ExtraAction;
import labex.feevale.br.oinc.views.actions.LayoutChanges;
import labex.feevale.br.oinc.views.actions.ProcessDatabaseActions;
import labex.feevale.br.oinc.views.actions.ProcessImageAction;
import labex.feevale.br.oinc.views.dialogs.AlertDialogHelper;
import labex.feevale.br.oinc.views.dialogs.DialogActions;
import labex.feevale.br.oinc.views.dialogs.DialogMaker;
import labex.feevale.br.oinc.views.fragments.GalleryFragment;
import labex.feevale.br.oinc.views.fragments.NewEntryFragment;

/**
 * Créditos para http://www.androidzeitgeist.com/
 * Post: http://www.androidzeitgeist.com/2012/10/using-fragment-for-camera-preview.html
 * Git: https://github.com/pocmo/Instant-Mustache/blob/master/src/com/androidzeitgeist/mustache/activity/CameraActivity.java
 * @author Sebastian Kaspari <sebastian@androidzeitgeist.com>
 */
public class EntryActivity extends BaseActivity implements LayoutChanges, DialogActions, ProcessImageAction, ProcessDatabaseActions<Entry> {

    private static int POSITION_GALLERY = 0;

    private View footerButtons, headerButtons;
    private ImageButton buttonSave, addPictureButton, nextGalleryItemButton, previousGalleryItemButton,
                        removeButton, commentButton, backButton;
    private Entry entry;
    private DialogActions dialogActions;
    private CameraActions cameraActions;

    private List<String> comments = new ArrayList<>(Arrays.asList(new String[]{"","","",""}));

    public EntryActivity() {
        //this.entry = new Entry(45.8f,Entry.CREDIT, new Date());//TO TEST
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_entry);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.entry = (Entry) bundle.getSerializable(AppVariables.ENTRY_KEY);
            this.comments = this.entry.getComments();
            POSITION_GALLERY = this.entry.getImagesPath().size() - 1;
        }

        footerButtons = findViewById(R.id.buttons_footer_entry_layout);
        headerButtons = findViewById(R.id.buttons_header_entry);

        addPictureButton = (ImageButton) headerButtons.findViewById(R.id.addPictureButton);
        addPictureButton.setOnClickListener(cameraButtonEventClick());

        nextGalleryItemButton = (ImageButton) headerButtons.findViewById(R.id.saveUserButton);
        nextGalleryItemButton.setOnClickListener(changeGalleryNextEventClick());

        previousGalleryItemButton = (ImageButton) headerButtons.findViewById(R.id.backGalleryItemButton);
        previousGalleryItemButton.setOnClickListener(changeGalleryPreviousEventClick());

        buttonSave = (ImageButton) findViewById(R.id.btn_save);
        buttonSave.setOnClickListener(finalizeProcess());

        removeButton = (ImageButton) headerButtons.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(removePictureEventClick());

        commentButton = (ImageButton) footerButtons.findViewById(R.id.commentPictureButton);
        commentButton.setOnClickListener(postCommentClickListener());

        backButton = (ImageButton) footerButtons.findViewById(R.id.goBackButton);
        backButton.setOnClickListener(backButtonClickEvent());

        if (this.entry == null) {
            POSITION_GALLERY = 0;
            loadNewEntry();
            changeFragment(new NewEntryFragment(this, entry, this));
        } else {
            if (entry.getImagesPath().size() > 0)
                changeFragment(new GalleryFragment(entry.getImagesPath().get(POSITION_GALLERY),
                        entry.getComments().get(POSITION_GALLERY), this));
            else
                changeFragment(new GalleryFragment("","", this));
        }
        updateLayout();
    }

    private View.OnClickListener backButtonClickEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseDialog(new ExtraAction() {
                    @Override
                    public void execute() {
                        if(getFragment() instanceof NewEntryFragment)
                            loadMainScreen();
                        else
                            onBackPressed();
                    }
                });
            }
        };
    }

    private void showCloseDialog(ExtraAction action){
        if(entry.getImagesPath().size() > 0)
            new DialogMaker("Descatar imagens?",
                    "Você realmente deseja descartar essas imagens?", new CancelPicturesAction(entry))
                    .createDialog(this).show();
        else
            action.execute();
    }

    private View.OnClickListener finalizeProcess() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entry.setComments(comments);
                CreateTrip createTrip = new CreateTrip(entry,EntryActivity.this);
                createTrip.execute();
            }
        };
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(AppVariables.ENTRY_KEY, entry);
        super.onSaveInstanceState(savedInstanceState);
    }

    private View.OnClickListener postCommentClickListener() {
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogHelper dialogHelper = new AlertDialogHelper(activity);
                if(getFragment() instanceof Observer) {
                    ((Observer) getFragment()).setSubject(dialogHelper);
                    dialogHelper.register((Observer) getFragment());
                }
                dialogHelper.showTagsDialog(comments, POSITION_GALLERY).show();
            }
        };
    }

    private View.OnClickListener removePictureEventClick() {
        dialogActions = this;
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogMaker(getBaseContext().getResources().getString(R.string.dialog_delete_image_title),
                        getBaseContext().getResources().getString(R.string.dialog_delete_image_message), dialogActions)
                        .createDialog(activity).show();
            }
        };
    }

    private View.OnClickListener changeGalleryPreviousEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGallery(getPreviousPicture());
            }
        };
    }

    private View.OnClickListener changeGalleryNextEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGallery(getNextPicture());
            }
        };
    }

    private void changeGallery(String image){
        if(cameraActions != null)
            cameraActions.changePicture(image, (comments.isEmpty() || comments.size() <= POSITION_GALLERY) ? "" :
                    comments.get(POSITION_GALLERY));
        else
            Toast.makeText(this, "Problemas ao carregar imagem", Toast.LENGTH_LONG);
    }

    private View.OnClickListener cameraButtonEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCameraActivity();
            }
        };
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        updateLayout();
    }

    private void updateLayout() {
        if(getFragment() instanceof GalleryFragment) {
            footerButtons.setVisibility(View.VISIBLE);
            headerButtons.setVisibility(View.VISIBLE);
            this.cameraActions = (CameraActions) getFragment();
            loadGalleryButtons();
            this.addPictureButton.setVisibility(entry.getImagesPath().size() < 4 ? View.VISIBLE : View.INVISIBLE);
            this.commentButton.setVisibility(!entry.getImagesPath().isEmpty() ? View.VISIBLE : View.INVISIBLE);
            this.removeButton.setVisibility(!entry.getImagesPath().isEmpty() ? View.VISIBLE : View.INVISIBLE);
            this.buttonSave.setVisibility(!entry.getImagesPath().isEmpty() ? View.VISIBLE : View.INVISIBLE);
        }else{
            footerButtons.setVisibility(View.INVISIBLE);
            headerButtons.setVisibility(View.INVISIBLE);
        }
    }

    private void loadGalleryButtons(){
        if(((POSITION_GALLERY+1) < entry.getImagesPath().size()&& entry.getImagesPath().size() > 0))
            this.nextGalleryItemButton.setVisibility(View.VISIBLE);
        else
            this.nextGalleryItemButton.setVisibility(View.INVISIBLE);

        if(POSITION_GALLERY > 0)
            this.previousGalleryItemButton.setVisibility(View.VISIBLE);
        else
            this.previousGalleryItemButton.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {
        if(getFragment() instanceof NewEntryFragment) {
            loadMainScreen();
        }else if(getFragment() instanceof GalleryFragment) {
            final Activity activity = this;
            showCloseDialog(new ExtraAction() {
                @Override
                public void execute() {
                    changeFragment(new NewEntryFragment(activity, entry, (ProcessDatabaseActions)activity));
                }
            });
        }
    }

    private String getNextPicture(){
        if((POSITION_GALLERY+1) < entry.getImagesPath().size())
            return entry.getImagesPath().get(++POSITION_GALLERY);
        if(entry.getImagesPath().isEmpty())
            return "";
        return entry.getImagesPath().get(POSITION_GALLERY);
    }

    private String getPreviousPicture(){
        if(POSITION_GALLERY > 0)
            return entry.getImagesPath().get(--POSITION_GALLERY);
        if(entry.getImagesPath().isEmpty())
            return "";
        return entry.getImagesPath().get(POSITION_GALLERY);
    }

    private void performPictureDeletions(){
       if(!entry.getImagesPath().isEmpty() && entry.getImagesPath().size() >  POSITION_GALLERY) {
            String absolutePath = entry.getImagesPath().get(POSITION_GALLERY);
            if(absolutePath!= null) {
                if (removePicture(absolutePath)) {
                    entry.getImagesPath().remove(POSITION_GALLERY);

                    actionOnDelete();
                }
                else
                    Toast.makeText(this, "Problemas ao tentar remover foto.", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(this, "Problemas ao tentar localizar foto.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean removePicture(String imagePath){
        return new ArchiveUtility(this).removeFile(imagePath);
    }


    @Override
    public void changeLayout() {
        updateLayout();
    }

    @Override
    public void cancelAction() {}

    @Override
    public void confirmAction() {
        performPictureDeletions();
    }

    @Override
    public void executeAction(String imagePath) {
        if(imagePath != null){
            this.entry.getImagesPath().add(imagePath);
            POSITION_GALLERY = this.entry.getImagesPath().size() - 1;
            changeFragment(new GalleryFragment(imagePath, comments.get(POSITION_GALLERY), this));
        }else{
            Toast.makeText(this, "Problemas ao salvar imagem.", Toast.LENGTH_LONG).show();
        }
    }

    public String changePictureGallery(){
        String changeTOImage = getPreviousPicture();
        if(changeTOImage.length() > 4)
            return changeTOImage;
        else
            return getNextPicture();
    }

    private void actionOnDelete(){
        if(comments.size() > POSITION_GALLERY)
            comments.remove(POSITION_GALLERY);

        if(entry.getImagesPath().size() > 0) {
            String imageToChange = changePictureGallery();
            changeGallery(imageToChange);
        }else{
            loadCameraActivity();
        }
    }

    private void loadNewEntry(){
        this.entry = new Entry();
        this.entry.setDate(Calendar.getInstance().getTime());
        this.entry.setGoal(GoalDao.getUserActiveGoal());
    }

    private void loadCameraActivity(){
        Intent intentOpenCamera = new Intent(getApplicationContext(), CameraActivity.class);
        Bundle bundle = new Bundle();
        entry.setComments(comments);
        bundle.putSerializable(AppVariables.ENTRY_KEY, entry);
        intentOpenCamera.putExtras(bundle);
        startActivity(intentOpenCamera);
    }

    @Override
    public void saveEntity(Entry entity) {
        EntryDao.saveWithSound(entity, this);
        loadMainScreen();
    }

    private void loadMainScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void executeActionChangeFragment() {

    }

    private class CancelPicturesAction implements DialogActions {

        private Entry entry;

        private CancelPicturesAction(Entry entry) {
            this.entry = entry;
        }

        @Override
        public void cancelAction() {}

        @Override
        public void confirmAction() {
            for(String path :entry.getImagesPath()) {
                removePicture(path);
            }
            entry.setImagesPath(new ArrayList<String>());
            POSITION_GALLERY = 0;
            onBackPressed();
        }
    }
}

