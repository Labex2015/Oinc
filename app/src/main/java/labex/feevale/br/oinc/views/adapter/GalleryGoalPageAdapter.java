package labex.feevale.br.oinc.views.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Collections;
import java.util.List;

import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.views.fragments.DisplayGoalFragment;

/**
 * Created by 0126128 on 18/03/2015.
 */
public class GalleryGoalPageAdapter extends FragmentStatePagerAdapter{

    private List<Goal> goals;

    public GalleryGoalPageAdapter(FragmentManager fm) {
        super(fm);
        goals = GoalDao.getAllGoals();
        Collections.sort(goals);
    }

    @Override
    public Fragment getItem(int position) {
        DisplayGoalFragment fragment = new DisplayGoalFragment(goals.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return goals != null ? goals.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return goals.get(position).getMeta();
    }

}
