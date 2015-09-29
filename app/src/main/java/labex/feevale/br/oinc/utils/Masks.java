package labex.feevale.br.oinc.utils;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.NumberFormat;

/**
 * Created by Jeferson on 02/02/2015.
 */
public class Masks implements TextWatcher {

    final EditText field;
    private boolean isUpdating = false;
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    public Masks(EditText field) {
        super();
        this.field = field;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
// Evita que o método seja executado varias vezes.
// Se tirar ele entre em loop
        if (isUpdating) {
            isUpdating = false;
            return;
        }

        isUpdating = true;
        String str = charSequence.toString();
// Verifica se já existe a máscara no texto, se existir é retirada
        boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) &&
                (str.indexOf(".") > -1 || str.indexOf(",") > -1));

        if (hasMask) {
            str = str.replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
        }

        try {
            str = numberFormat.format(Double.parseDouble(str) / 100);
            field.setText(str);
            field.setSelection(field.getText().length());
        } catch (NumberFormatException e) {
            charSequence = "";
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO identificar função
    }

    @Override
    public void afterTextChanged(Editable s) {
    // TODO identificar função
    }


}
