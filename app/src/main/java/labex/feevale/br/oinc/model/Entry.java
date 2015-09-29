package labex.feevale.br.oinc.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 0126128 on 26/01/2015.
 */
@Table(name = "entry")
public class Entry extends Model implements Serializable{
    public static final char DEBIT = 'D';
    public static final char CREDIT = 'C';
    public static final char NONE = 'N';

    @Column
    private Float value;
    @Column
    private Character type;
    @Column
    private String picturePath;
    @Column
    private Date date;
    @Column(name = "goal")
    private Goal goal;

    private List<String> imagesPath;
    private List<String> comments;

    public Entry() {super();}

    public Entry(Float value, Character type, Date date) {
        this.value = value;
        this.type = type;
        this.date = date;
    }

    public Float getValue() {
        if(value == null)
            this.value = 0f;
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Character getType() {
        if(type == null)
            this.type = Entry.CREDIT;
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public List<String> getImagesPath() {
        if(this.imagesPath == null)
            this.imagesPath = new ArrayList<String>();
        return imagesPath;
    }

    public void setImagesPath(List<String> imagesPath) {
        this.imagesPath = imagesPath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }


}
