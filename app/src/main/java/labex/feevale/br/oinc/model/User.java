package labex.feevale.br.oinc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 0126128 on 26/01/2015.
 */
@Table(name = "user")
public class User extends Model implements Serializable{

    @Column
    private String name;
    @Column
    private String email;
    @Column
    private Date birthDate;
    @Column
    private String photoPath;

    private List<Goal> goals;

    public User() { super();}

    public User(String name, String email, Date birthDate) {
        super();
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<Goal> getGoals() {
        return getMany(Goal.class, "user");
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
