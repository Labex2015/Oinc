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
@Table(name = "goal")
public class Goal extends Model implements Serializable, Comparable<Goal>{

    @Column
    private String meta;
    @Column
    private Float value;
    @Column
    private Float totalValue;
    @Column
    private String pathImage;
    @Column
    private Boolean status;
    @Column
    private Date term;
    @Column(name = "user")
    private User user;

    private List<Entry> entries;


    public Goal() {super();}

    public Goal(String meta, Float value, Float totalValue, Date term, User user) {
        super();
        this.meta = meta;
        this.value = value;
        this.totalValue = totalValue;
        this.term = term;
        this.user = user;
    }

    public void investment(Float value){
        this.totalValue = getTotalValue() + value;
    }

    public void draft(Float value){
        this.totalValue = getTotalValue() - value;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Float getValue() {
        if(value == null)
            this.value = 0f;
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getTotalValue() {
        if(totalValue == null)
            this.totalValue = 0f;
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public Date getTerm() {
        return term;
    }

    public void setTerm(Date term) {
        this.term = term;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Entry> getEntries() {
        return getMany(Entry.class, "goal");
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int compareTo(Goal another) {
        int lastCmp = status ? -1 : getId().compareTo(another.getId());
        return lastCmp;
    }
}
