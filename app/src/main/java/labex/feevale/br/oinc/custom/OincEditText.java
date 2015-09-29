package labex.feevale.br.oinc.custom;

import android.content.Context;
import android.util.AttributeSet;

import labex.feevale.br.oinc.actions.Observer;
import labex.feevale.br.oinc.actions.Subject;
import labex.feevale.br.oinc.actions.ValidateAction;

/**
 * Created by 0126128 on 03/03/2015.
 */
public class OincEditText extends android.widget.EditText implements Observer{

    private String messageValidate;
    private String paramsToSearch;
    private String messageValidateParams;
    private Subject subject;
    private ValidateAction validateAction;

    public OincEditText(Context context) {
        super(context);
    }

    public OincEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OincEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAttributes(String messageValidate, ValidateAction validateAction, Subject subject){
        this.messageValidate = messageValidate;
        this.validateAction = validateAction;
        setSubject(subject);
    }

    public void setAttributes(String messageValidate, String paramsToSearch, String messageValidateParams, ValidateAction validateAction, Subject subject){
        this.messageValidate = messageValidate;
        this.paramsToSearch = paramsToSearch;
        this.messageValidateParams = messageValidateParams;
        this.validateAction = validateAction;
        setSubject(subject);
    }

    private Boolean validateMe(){
        if(this.messageValidate != null && this.getText().length() == 0){
            this.setError(messageValidate);
        }else if(this.messageValidateParams != null && !this.getText().toString().contains(paramsToSearch)){
            this.setError(messageValidateParams);
        }else{
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        if(validateAction != null) {
            if (validateMe())
                validateAction.setPositiveValidation();
            else
                validateAction.setFailValidation();
        }
    }

    @Override
    public void setSubject(Subject sub) {
        this.subject = sub;
        this.subject.register(this);
    }
}
