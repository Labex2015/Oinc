package labex.feevale.br.oinc.utils;

/**
 * Created by 0126128 on 13/02/2015.
 */
public class CurrencyUtils {

    public static final String BR = "pt";
    public static final String US = "en";

    public static float getValueFromString(String value, String locale){
        switch (locale){
            case BR:
                value = value.replace(".", "");
                value = value.replace(",", ".");
                value = value.replace("R", "");
                value = value.replace("$", "");
                break;
            case US:
                value = value.replace(",", "");
                value = value.replace("R", "");
                value = value.replace("$", "");
                break;
        }
        try{
            return Float.parseFloat(value);
        }catch(IllegalArgumentException e){
            return 0f;
        }
    }
}
