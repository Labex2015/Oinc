package labex.feevale.br.oinc.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.views.adapter.GalleryGoalPageAdapter;

/**
 * Created by 0126128 on 18/03/2015.
 */
public class GalleryGoalsActivity extends FragmentActivity{

    private List<Goal> goals;
    private Goal goal;
    private ViewPager viewPager;
    private GalleryGoalPageAdapter galleryGoalPageAdapter;
    private ImageButton closeGalleryImageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_pager);

        closeGalleryImageButton = (ImageButton) findViewById(R.id.closeGalleryImageButton);
        closeGalleryImageButton.setOnClickListener(closeGalleryEventClick());

        galleryGoalPageAdapter = new GalleryGoalPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.goal_pager_layout);
        viewPager.setAdapter(galleryGoalPageAdapter);
    }



    private View.OnClickListener closeGalleryEventClick() {
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        };
    }


    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }
}
