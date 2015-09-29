package labex.feevale.br.oinc.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import labex.feevale.br.oinc.MainActivity;
import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.dao.UserDao;
import labex.feevale.br.oinc.model.Balance;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.model.User;
import labex.feevale.br.oinc.tasks.CartoonTask;
import labex.feevale.br.oinc.tasks.CopyImageTask;
import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.utils.CurrencyUtils;
import labex.feevale.br.oinc.utils.Masks;
import labex.feevale.br.oinc.utils.SharedPrefUtils;
import labex.feevale.br.oinc.views.actions.LayoutChanges;
import labex.feevale.br.oinc.views.actions.SaveImageTaskAction;

/**
 * Created by 0126128 on 27/01/2015.
 */
public class NewGoalFragment extends Fragment implements SaveImageTaskAction<String>{

    private final int SELECT_PICTURE = 1;

    private ImageView imageView;
    private User user;
    private Goal goal;
    private Boolean hasImage;
    private Button buttonSave, buttonCancel;
    private EditText goalEditText, valueEditText;

    public NewGoalFragment() {
        this.user = UserDao.getUser();
        this.goal = new Goal();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_choose_image_new_goal, container, false);
        if(view != null){
            imageView = (ImageView) view.findViewById(R.id.choosePictureImageView);
            buttonSave = (Button)view.findViewById(R.id.saveNewGoalButton);
            buttonCancel = (Button)view.findViewById(R.id.backGalleryItemButton);
            goalEditText = (EditText) view.findViewById(R.id.goalNameEditText);

            InputFilter[] filters = {new InputFilter.LengthFilter(10)};
            valueEditText = (EditText) view.findViewById(R.id.goalValueEditText);
            valueEditText.addTextChangedListener(new Masks(valueEditText));
            valueEditText.setFilters(filters);

            imageView.setOnClickListener(choosePictureEventClick());
            buttonSave.setOnClickListener(saveNewGoalEventClick());
            buttonCancel.setOnClickListener(cancelNewGoalEventClick());
        }
        return view;
    }

    private View.OnClickListener cancelNewGoalEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).changeFragment(new MainFragment((LayoutChanges)getActivity()));
            }
        };
    }

    private View.OnClickListener saveNewGoalEventClick() {
        final SaveImageTaskAction taskAction = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {
                    imageView.setImageBitmap(null);
                    new CopyImageTask(getActivity(), goal.getMeta()+(new SimpleDateFormat("ddMMyyyy_HHmm")
                            .format(Calendar.getInstance().getTime()))+".jpg",taskAction).execute();
                }
            }
        };
    }

    private View.OnClickListener choosePictureEventClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(photoPickerIntent,"Selecione uma imagem"), SELECT_PICTURE);
                }
            };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    //CartoonTask.invalidateBitmap();
                    hasImage = true;
                    ArchiveUtility.decodeUri(data.getData(), getActivity());
                    imageView.setImageBitmap(CartoonTask.bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    hasImage = false;
                }
            }
        }
    }

    private Boolean validateFields(){
        if(goalEditText.getText().toString().length() == 0){
            goalEditText.setError(getResources().getString(R.string.required_field));
            goalEditText.requestFocus();
            return false;
        }else if(valueEditText.getText().toString().length() == 0){
            valueEditText.setError(getResources().getString(R.string.required_field));
            valueEditText.requestFocus();
            return false;
        }else{
            goal.setMeta(goalEditText.getText().toString());
            String value = valueEditText.getText().toString();
            try {
                goal.setValue(CurrencyUtils.getValueFromString(value, Locale.getDefault().getLanguage()));
                return true;
            }catch (Exception e){
                valueEditText.setError(getResources().getString(R.string.invalid_value));
                valueEditText.requestFocus();
                return false;
            }
        }
    }

    @Override
    public Boolean doInBackground(String imagePath) {
        Balance balance = Balance.getBalance();
        if(balance.getBallanceOnAccount() > 0){
            goal.investment(balance.getBallanceOnAccount());
            balance.setBallanceOnAccount(0f);
            balance.save();
        }
        goal.setPathImage((imagePath != null && imagePath.length() > 4) ? imagePath : "");
        goal.setStatus(true);
        goal.setUser(UserDao.getUser());
        goal.save();


        SharedPrefUtils myPrefs = new SharedPrefUtils();
        myPrefs.saveMusicPlayed(false, getActivity());
        return (goal.getId() != null && goal.getId() > 0);
    }

    @Override
    public void postExecuteSuccess() {
        ((MainActivity) getActivity()).changeFragment(new MainFragment((LayoutChanges)getActivity()));
    }

    @Override
    public void postExecuteFail() {
        Toast.makeText(getActivity(), "Ops! Problemas ao tentar salvar o novo objetivo.", Toast.LENGTH_LONG).show();
    }
}
