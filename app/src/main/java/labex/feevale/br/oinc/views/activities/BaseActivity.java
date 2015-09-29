package labex.feevale.br.oinc.views.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.views.actions.ActivityAction;

/**
 * Created by 0126128 on 20/03/2015.
 */
public abstract class BaseActivity extends FragmentActivity implements ActivityAction{

    private Fragment fragment;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void changeFragment(Fragment fragment){
        super.onPostResume();
        setFragment(fragment);
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
        executeActionChangeFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
