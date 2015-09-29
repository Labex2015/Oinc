package labex.feevale.br.oinc.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.Date;

import labex.feevale.br.oinc.MainActivity;
import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.actions.ValidateAction;
import labex.feevale.br.oinc.custom.OincButton;
import labex.feevale.br.oinc.custom.OincEditText;
import labex.feevale.br.oinc.model.User;
import labex.feevale.br.oinc.tasks.CartoonTask;
import labex.feevale.br.oinc.tasks.CopyImageTask;
import labex.feevale.br.oinc.utils.ArchiveUtility;
import labex.feevale.br.oinc.views.actions.LayoutChanges;
import labex.feevale.br.oinc.views.actions.SaveImageTaskAction;

/**
 * Created by 0126128 on 03/03/2015.
 */
public class RegisterFragment extends Fragment implements ValidateAction, SaveImageTaskAction<String>{

    private static final int SELECT_PICTURE = 1;

    private OincButton buttonRegister;
    private OincEditText emailEditText, nameEditText;
    private Boolean hasErrors = false, hasPassed = false;
    private ImageView imageView;
    private Boolean hasPicture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        if(view != null){

            /**
             * @desc OincButton é um botão personalizado que implementa o padrão Observer(Subject), assim notificando a
             *       todos os observers quando o OnClick é realizado. Se o observer não implementar o set register
             *       então o mesmo deve registrar cada um dos observers que estão observando o componente
             */
            buttonRegister = (OincButton)view.findViewById(R.id.saveUserButton);
            buttonRegister.setOnClickListener(registerClickListener());

            /**
            * @desc OincEditText é um editText personalizado que implementa o padrão de projeto Observer(Observer), para assim
            *       validar ele mesmo, sem a necessidade de uma outra funcionalidade, o mesmo só executa a valdiação
            *       quando é passado através de setAttributes um ValidateAction, interface que determina as ações que ocorrem ao
            *       validar os dados e também se é informado um "subject", o componente que notifica os observers.
             *      Esse componente já registra no Subject ele mesmo como um Observer;
            */
            emailEditText = (OincEditText)view.findViewById(R.id.userEmailEditText);
            emailEditText.setAttributes("Campo obrigatório", "@", "e-Mail inválido",this, buttonRegister);

            nameEditText = (OincEditText)view.findViewById(R.id.userNameEditText);
            nameEditText.setAttributes("Campo obrigatório", this, buttonRegister);

            imageView = (ImageView) view.findViewById(R.id.choosePhotoUserView);
            imageView.setOnClickListener(chooserImageClickListener());

        }

        return view;
    }

    private View.OnClickListener chooserImageClickListener() {
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

    private View.OnClickListener registerClickListener() {
        final SaveImageTaskAction saveImageTaskAction = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasErrors && hasPassed){
                    if(hasPicture) {
                        imageView.setImageBitmap(null);
                        new CopyImageTask(getActivity(), nameEditText.getText().toString() + ".jpg", saveImageTaskAction).execute();
                    }else{
                        saveUser();
                        postExecuteSuccess();
                    }
                }else{
                    hasErrors = false;
                    hasPassed = true;
                }
            }
        };
    }

    @Override
    public void setFailValidation() {
        hasErrors = true;
    }

    @Override
    public void setPositiveValidation() {
        hasPassed = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                try {
                    hasPicture = true;
                    ArchiveUtility.decodeUri(data.getData(), getActivity());
                    this.imageView.setImageBitmap(CartoonTask.bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    hasPicture = false;
                }
            }
        }
    }

    @Override
    public Boolean doInBackground(String entity) {
        if(entity != null) {
            saveUser(entity);
            return true;
        }
        return false;
    }

    @Override
    public void postExecuteSuccess() {
        if(getActivity() instanceof MainActivity)
            ((MainActivity)getActivity()).changeFragment(new MainFragment((LayoutChanges)getActivity()));
        else
            throw new ClassCastException("Você deve instanciar essa fragment em uma activity que implemente " +
                    "LayoutChanges e/ou extenda MainActivity");
    }

    @Override
    public void postExecuteFail() {
        Toast.makeText(getActivity(), "Problemas ao tentar obter a imagem de seu dispositivo", Toast.LENGTH_LONG).show();
    }


    private void saveUser(String... imagePath){
        User user = new User(nameEditText.getText().toString(), emailEditText.getText().toString(), new Date());
        user.setPhotoPath((imagePath != null && imagePath.length > 0) ? imagePath[0]:"");
        user.save();
    }
}
