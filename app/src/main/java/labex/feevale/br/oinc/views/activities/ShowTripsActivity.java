package labex.feevale.br.oinc.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.utils.ScreenSlidePagerAdapter;


public class ShowTripsActivity extends FragmentActivity {

    public static String EXTRA_PAGE = "number";
    private ViewPager mPager;
    private static int NUM_PAGES = 0;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trips);

        Intent intent = getIntent();
        Long id  = intent.getLongExtra("GOAL", 0);
        GoalDao goalDao = new GoalDao();
        Goal goal = goalDao.findById(id);

        NUM_PAGES = goal.getEntries().size();

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), NUM_PAGES);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
}
