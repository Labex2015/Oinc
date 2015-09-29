package labex.feevale.br.oinc.actions;


/**
 * Created by 0126128 on 04/02/2015.
 */
public interface Subject {

    public void register(Observer obj);
    public void unregister(Observer obj);
    public void notifyObservers();
    public Object getUpdate(Observer obj);
}
