package labex.feevale.br.oinc.views.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import labex.feevale.br.oinc.R;

/**
 * Created by 0126128 on 26/01/2015.
 */
public class DialogMaker{

    private AlertDialog dialog;
    private String title;
    private String message;
    private DialogActions dialogActions;
    AlertDialog alertDialog;

    public DialogMaker(String title, String message, DialogActions dialogActions) {
        this.title = title;
        this.message = message;
        this.dialogActions = dialogActions;
    }

    public AlertDialog createDialog(Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialog = activity.getLayoutInflater().inflate(R.layout.dialog_maker,null);

        TextView descricaoTitle = (TextView) dialog.findViewById(R.id.textTitleDialog);
        descricaoTitle.setText(title);

        TextView descricaoMessage = (TextView) dialog.findViewById(R.id.messageTextViewDialog);
        descricaoMessage.setText(message);

        builder.setView(dialog);
        alertDialog = builder.create();

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel_Dialog);
        btnCancel.setText("Cancelar");
        btnCancel.setOnClickListener(closeDialogEventClick());

        Button btnConfirm = (Button) dialog.findViewById(R.id.btn_confirm_Dialog);
        btnConfirm.setText("OK");
        btnConfirm.setOnClickListener(confirmDialogAction());
        return alertDialog;
    }

    private View.OnClickListener confirmDialogAction() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogActions.confirmAction();
                if(alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        };
    }

    private View.OnClickListener closeDialogEventClick() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialogActions.cancelAction();
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        };
    }


}