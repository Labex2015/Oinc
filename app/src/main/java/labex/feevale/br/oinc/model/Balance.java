package labex.feevale.br.oinc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;

/**
 * Created by 0126128 on 09/03/2015.
 */
@Table(name = "balance")
public class Balance extends Model implements Serializable {

    @Column
    private Float ballanceOnAccount;

    public Balance() {super();}

    public Balance(Float ballanceOnAccount) {
        super();
        this.ballanceOnAccount = ballanceOnAccount;
    }

    public Float getBallanceOnAccount() {
        if(ballanceOnAccount == null)
            ballanceOnAccount = 0f;
        return ballanceOnAccount;
    }

    public void setBallanceOnAccount(Float ballanceOnAccount) {
        this.ballanceOnAccount = ballanceOnAccount;
    }

    public static Balance getBalance(){
        Balance balance = new Select().from(Balance.class).executeSingle();
        if(balance == null)
            return new Balance(0f);
        return balance;
    }
}
