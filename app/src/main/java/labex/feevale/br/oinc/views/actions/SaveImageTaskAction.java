package labex.feevale.br.oinc.views.actions;

/**
 * Created by 0126128 on 03/03/2015.
 */
public interface SaveImageTaskAction<T> {

    public Boolean doInBackground(T entity);
    public void postExecuteSuccess();
    public void postExecuteFail();
}
