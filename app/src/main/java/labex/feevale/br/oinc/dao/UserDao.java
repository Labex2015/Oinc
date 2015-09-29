package labex.feevale.br.oinc.dao;

import com.activeandroid.query.Select;

import labex.feevale.br.oinc.model.User;

/**
 * Created by 0126128 on 28/01/2015.
 */
public class UserDao {

    public static User getUser(){
        return new Select().from(User.class).executeSingle();
    }
}
