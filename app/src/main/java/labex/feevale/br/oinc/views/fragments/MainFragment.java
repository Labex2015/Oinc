package labex.feevale.br.oinc.views.fragments;


import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.text.DecimalFormat;

import labex.feevale.br.oinc.MainActivity;
import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.utils.AudioUtils;
import labex.feevale.br.oinc.utils.SharedPrefUtils;
import labex.feevale.br.oinc.views.actions.LayoutChanges;
import labex.feevale.br.oinc.vo.TourItem;

/**
 * Created by 0126128 on 19/01/2015.
 */
public class MainFragment extends Fragment {

    private ImageView imgFlag, mastImageView;
    private TextView accumulatedTextView, goalValueTextView;
    private RelativeLayout relativeLayout;
    private LayoutChanges layoutChanges;
    private View view;
    private int finalHeight, finalWidth;
    private Goal goal;
    private float have;
    private RelativeLayout.LayoutParams lps;
    private ViewTarget target;
    private ShowcaseView showcaseView;

    public MainFragment() {}

    public MainFragment(LayoutChanges layoutChanges) {
        this.layoutChanges = layoutChanges;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        imgFlag = (ImageView) view.findViewById(R.id.img_flag);
        mastImageView = (ImageView) view.findViewById(R.id.img_mast);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout_Progress);
        accumulatedTextView = (TextView) view.findViewById(R.id.accumulatedValueTextView);

        buildLayout(view);
        SharedPrefUtils prefUtils = new SharedPrefUtils();
        if(!prefUtils.alredyPlayedTour(getActivity())) {
            loadTour();
            prefUtils.saveTourExecuted(true, getActivity());
        }
        return view;
    }

    public void loadTour(){
        createTour(TourItem.loadItens());
    }

    public void createTour(final TourItem tourItem){
        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        target = new ViewTarget(tourItem.getComponent(), getActivity());
        showcaseView = new ShowcaseView.Builder(getActivity(), true)
                .setTarget(target)
                .setContentTitle(tourItem.getTitle())
                .setContentText(tourItem.getText())
                .setStyle(R.style.CustomShowcaseTheme2)
                .build();

        showcaseView.setButtonPosition(lps);
        System.gc();
        showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
            @Override
            public void onShowcaseViewHide(ShowcaseView showcaseView) {}

            @Override
            public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                lps = null;
                target = null;
                if(tourItem.getNextItem() != null){
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion > Build.VERSION_CODES.HONEYCOMB_MR1) {
                        createTour(tourItem.getNextItem());
                    }
                }else{
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                showcaseView = null;
            }

            @Override
            public void onShowcaseViewShow(ShowcaseView showcaseView) {}
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        this.goal = GoalDao.getUserActiveGoal();
        if(goal != null)
            moveFlag(goal.getTotalValue() >= 0 ? goal.getTotalValue() : 0,
                    goal.getValue() >= 0 ? goal.getValue() : 0);

            DecimalFormat df = new DecimalFormat(".00");
            df.setMinimumIntegerDigits(1);
            accumulatedTextView.setText("R$" + (goal != null ? df.format(goal.getTotalValue()): "00,00"));
            layoutChanges.changeLayout();
    }


    private void moveFlag( float have, float goal) {
        if(have > goal)
            have = goal;

        if(have <= 0)
           have = 0;
        int position = getPosition(goal, have, getScreenHeight());
        replace(0, -position, 1f, 1f);
    }

    /**
     * Faz regra de trez para identificar a posição
     * referente ao tamanho da tela
     **/
    private int getPosition(float goalValue, float have, float relativeHeight) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int size = 0;

        if(metrics.densityDpi >= 570)
            size = 900;//TODO: Testar com celular do Pretz
        else if(metrics.densityDpi >= 320)
            size = 450;
        else if(metrics.densityDpi >= 240)
            size = 360;
        else if(metrics.densityDpi >= 160)
            size = 320;

        float positionFinal =  Math.round(((relativeHeight * size) / metrics.heightPixels));
        return Math.round(((have * positionFinal) / goalValue));
    }

    // movimenta a bandeira
    private void replace(int xTo, int yTo, float xScale, float yScale) {

        AnimationSet replaceAnimation = new AnimationSet(false);
        replaceAnimation.setFillAfter(true);
        replaceAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPrefUtils myPrefs = new SharedPrefUtils();
                if((goal.getTotalValue() >= 0 ? goal.getTotalValue() : 0) >=
                   (goal.getValue() >= 0 ? goal.getValue() : 0) && !(myPrefs.alredyPlayedCongrats(getActivity()))){
                    new AudioUtils().playAudioFromInternalStorage(AudioUtils.CONGRATS_AUDIO, getActivity());
                    myPrefs.saveMusicPlayed(true, getActivity());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        TranslateAnimation trans = new TranslateAnimation(0, xTo, 0, yTo);
        trans.setDuration(2000);

        replaceAnimation.addAnimation(trans);
        imgFlag.startAnimation(replaceAnimation);
    }

    private float getScreenHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return (float) displaymetrics.heightPixels;
    }

    private void buildLayout(final View view){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = getResources().getDisplayMetrics();//TODO: Remover logs abaixo
        Log.e("METRICS", "SIZE density: "+metrics.density);
        Log.e("METRICS", "SIZE densityDpi: "+metrics.densityDpi);
        Log.e("METRICS", "SIZE heightPixels: "+metrics.heightPixels);
        Log.e("METRICS", "SIZE widthPixels: "+metrics.widthPixels);
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int padding = 0;
        if(metrics.densityDpi >= 320)
            padding = (int)((metrics.heightPixels)*0.15);
        else if(metrics.densityDpi >= 240)
            padding = (int)((metrics.heightPixels)*0.17);
        else if(metrics.densityDpi >= 160)
            padding = (int)((metrics.heightPixels)*0.19);
        Log.e("PADDING", "SIZE: "+(padding));
        this.mastImageView.setPadding(0,0,0,padding);
        this.imgFlag.setPadding((metrics.widthPixels/2),0,0,(int)((metrics.heightPixels)*0.15));
    }
}
