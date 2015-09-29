package labex.feevale.br.oinc.views.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.actions.Observer;
import labex.feevale.br.oinc.actions.Subject;

/**
 * Created by 0126128 on 04/02/2015.
 */
public class AlertDialogHelper implements Subject{

    private Observer observer;
    private Activity activity;
    private AlertDialog alertDialog;
    private String comment;

    public AlertDialogHelper(Activity activity) {
        this.activity = activity;
    }

    public AlertDialog showTagsDialog(List<String> comments, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(true);

        View dialog = activity.getLayoutInflater().inflate(R.layout.dialog_comment_picture, null);
        EditText commentText = (EditText)dialog.findViewById(R.id.messagePictureEditText);
        InputFilter[] filters = {new InputFilter.LengthFilter(60)};
        commentText.setFilters(filters);

        Button confirmAction = (Button) dialog.findViewById(R.id.confirmCommentButton);
        Button cancelAction = (Button) dialog.findViewById(R.id.cancelCommentButton);

        commentText.setText(comments.get(position));

        cancelAction.setOnClickListener(cancelActionListener());
        confirmAction.setOnClickListener(saveCommentListener(comments, position, commentText));

        builder.setView(dialog);
        alertDialog = builder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return alertDialog;
    }

    private View.OnClickListener saveCommentListener(final List<String> comments, final int position, final EditText commentText) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentText.getText().toString();
                comments.set(position,comment);
                notifyObservers();
                dismissDialog();
            }
        };
    }

    private View.OnClickListener cancelActionListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        };
    }

    private void dismissDialog(){
        if(alertDialog.isShowing())
            alertDialog.dismiss();
    }

    @Override
    public void register(Observer obj) {
        this.observer = obj;
    }

    @Override
    public void unregister(Observer obj) {
        this.observer = null;//TODO: Rever abordagem, seria uma lista, mas eu quero usar só um.
    }

    @Override
    public void notifyObservers() {
        if(observer != null)
            observer.update();
    }

    @Override
    public Object getUpdate(Observer obj) {
        return comment;
    }
}
