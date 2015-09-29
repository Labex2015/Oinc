package labex.feevale.br.oinc.views.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.tasks.CartoonTask;
import labex.feevale.br.oinc.utils.AppVariables;
import labex.feevale.br.oinc.views.fragments.EntryListFragment;

/**
 * Created by 0126128 on 18/03/2015.
 */
public class GoalsAndEntryDataActivity extends BaseActivity {

    private Goal goal;
    private List<Entry> entries;
    private View footer;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        goal = Goal.load(Goal.class, (Long)bundle.getSerializable(AppVariables.GOAL_KEY_ACTIVITY));
        setContentView(R.layout.activity_goals_entry);
        footer = (View) findViewById(R.id.includeFooterButtonsGoalEntry);
        backButton = (ImageButton) footer.findViewById(R.id.goBackGoalEntryButton);
        backButton.setOnClickListener(backButtonEventClick());


        if(getFragment() == null){
            changeFragment(new EntryListFragment(goal));
        }
    }

    private View.OnClickListener backButtonEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    @Override
    public void onBackPressed() {
        CartoonTask.invalidateBitmap();
        if(getFragment() instanceof EntryListFragment) {
            super.onBackPressed();//Gambiarra, mas fazer o que né......
            super.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        setFragment(fragment);
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    @Override
    public void executeActionChangeFragment() {}
}
