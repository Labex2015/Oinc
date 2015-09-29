package labex.feevale.br.oinc.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import labex.feevale.br.oinc.views.fragments.ScreenSlideTripFragment;


/**
 * Created by Jeferson on 17/03/2015.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private int mNumpages;

    public ScreenSlidePagerAdapter(FragmentManager fragmentManager, int mNumPages) {
        super(fragmentManager);
        this.mNumpages = mNumPages;
    }

    @Override
    public Fragment getItem(int position) {
        return ScreenSlideTripFragment.create(position);
    }

    @Override
    public int getCount() {
        return mNumpages;
    }
}