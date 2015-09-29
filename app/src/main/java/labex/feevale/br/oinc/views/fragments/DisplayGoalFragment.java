package labex.feevale.br.oinc.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.tasks.CartoonTask;
import labex.feevale.br.oinc.utils.AppVariables;
import labex.feevale.br.oinc.views.activities.GalleryGoalsActivity;
import labex.feevale.br.oinc.views.activities.GoalsAndEntryDataActivity;

/**
 * Created by 0126128 on 18/03/2015.
 */
public class DisplayGoalFragment extends Fragment{

    private Goal goal;
    private ImageView imageView;
    private TextView textView, metaTextView;
    private Activity activity;
    private ImageButton  openGoalImageButton;

    public DisplayGoalFragment() {}

    public DisplayGoalFragment(Goal goal) {
        this.goal = goal;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
            setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CartoonTask.invalidateBitmap();
        View view = inflater.inflate(R.layout.fragment_goal_pager_item, container, false);
        if(view != null){
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/porkys.regular.ttf");

            textView = (TextView) view.findViewById(R.id.goalPagerItemTextView);
            imageView = (ImageView) view.findViewById(R.id.goalPagerItemImageView);
            metaTextView = (TextView) view.findViewById(R.id.metaPagerItemTextView);

            metaTextView.setTypeface(typeface);
            metaTextView.setTextColor(getResources().getColor(R.color.orange));
            metaTextView.setRotationY(45);

            textView.setTypeface(typeface);
            textView.setTextColor(getResources().getColor(R.color.white));

            metaTextView.setText(goal.getMeta());

            openGoalImageButton = (ImageButton) view.findViewById(R.id.showGoalImageButton);
            openGoalImageButton.setVisibility(goal.getEntries() != null && !goal.getEntries().isEmpty() ? View.VISIBLE : View.INVISIBLE);
            openGoalImageButton.setOnClickListener(openEntriesEventClick());

            if(goal.getPathImage() != null && !goal.getPathImage().isEmpty() && goal.getPathImage().length() > 4)
                imageView.setImageURI(Uri.fromFile(new File(goal.getPathImage())));

            DecimalFormat df = new DecimalFormat(".00");
            df.setMinimumIntegerDigits(1);
            textView.setText("R$" + (goal != null ? df.format(goal.getValue()): "00,00"));

            ((GalleryGoalsActivity)getActivity()).setGoal(goal);
            this.activity = getActivity();
        }
        return view;
    }

    private View.OnClickListener openEntriesEventClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEntrysFromGoal();
            }
        };
    }

    private void showEntrysFromGoal(){
        Intent intentOpenCamera = new Intent(getActivity(), GoalsAndEntryDataActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppVariables.GOAL_KEY_ACTIVITY, goal.getId());
        intentOpenCamera.putExtras(bundle);
        startActivity(intentOpenCamera);
    }


}
