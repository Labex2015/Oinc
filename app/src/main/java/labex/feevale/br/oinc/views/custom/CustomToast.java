package labex.feevale.br.oinc.views.custom;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.views.actions.ExtraAction;

/**
 * Created by 0126128 on 24/03/2015.
 */
public class CustomToast{

    private static ExtraAction action;
    private static Toast toast;

    public static void show(String message, Activity activity, ExtraAction action) {
        CustomToast.action = action;
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        Button button = (Button)layout.findViewById(R.id.buttonToast);
        button.setOnClickListener(toastConfirmClickEvent());
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);
        toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }

    private static View.OnClickListener toastConfirmClickEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICKADO", "Click, click, click, click...............");
                if(action != null)
                    action.execute();
                toast.cancel();
            }
        };
    }
}
