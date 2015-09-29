package labex.feevale.br.oinc.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import labex.feevale.br.oinc.actions.Observer;
import labex.feevale.br.oinc.actions.Subject;

/**
 * Created by 0126128 on 03/03/2015.
 */
public class OincButton extends Button implements Subject{

    private List<Observer> observers = new ArrayList<>();;

    public OincButton(Context context) {
        super(context);
    }

    public OincButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OincButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean performClick() {
        notifyObservers();
        return super.performClick();
    }

    @Override
    public void register(Observer obj) {
        this.observers.add(obj);
    }

    @Override
    public void unregister(Observer obj) {
        this.observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers)
            o.update();
    }

    @Override
    public Object getUpdate(Observer obj) {
        return null;
    }
}
