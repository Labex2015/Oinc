package labex.feevale.br.oinc.actions;

/**
 * Created by 0126128 on 04/02/2015.
 */
public interface Observer {

    public void update();
    public void setSubject(Subject sub);
}
