package labex.feevale.br.oinc.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.views.activities.ShowTripsActivity;

/**
 * Created by Jeferson on 17/03/2015.
 */
public class ScreenSlideTripFragment extends Fragment {

    private int mPageNumber;

    public static ScreenSlideTripFragment create(int pageNumber) {
        ScreenSlideTripFragment fragment = new ScreenSlideTripFragment();
        Bundle args = new Bundle();
        args.putInt(ShowTripsActivity.EXTRA_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mPageNumber = getArguments().getInt(ShowTripsActivity.EXTRA_PAGE);
            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_show_trips, container, false);
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
