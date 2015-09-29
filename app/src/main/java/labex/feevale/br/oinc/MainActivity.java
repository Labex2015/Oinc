package labex.feevale.br.oinc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.dao.UserDao;
import labex.feevale.br.oinc.model.Balance;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.views.actions.ExtraAction;
import labex.feevale.br.oinc.views.actions.LayoutChanges;
import labex.feevale.br.oinc.views.activities.BaseActivity;
import labex.feevale.br.oinc.views.activities.EntryActivity;
import labex.feevale.br.oinc.views.activities.GalleryGoalsActivity;
import labex.feevale.br.oinc.views.custom.CustomToast;
import labex.feevale.br.oinc.views.dialogs.DialogActions;
import labex.feevale.br.oinc.views.dialogs.DialogMaker;
import labex.feevale.br.oinc.views.fragments.MainFragment;
import labex.feevale.br.oinc.views.fragments.NewGoalFragment;
import labex.feevale.br.oinc.views.fragments.RegisterFragment;


public class MainActivity extends BaseActivity implements LayoutChanges {

    private ImageButton galleryButton, homeButton, newEntryButton, buyButton;
    private TextView objectiveTextView, objectiveValueTextView, additionalValueTextView;
    private ImageView objectivesAdd;
    private RelativeLayout footer;
    private LinearLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        footer = (RelativeLayout) findViewById(R.id.footer);
        header = (LinearLayout) findViewById(R.id.top);
        objectivesAdd = (ImageView)findViewById(R.id.btn_reset);

        homeButton = (ImageButton)findViewById(R.id.goToMain);
        homeButton.setOnClickListener(openMainClickListener());

        galleryButton = (ImageButton)findViewById(R.id.btn_list);
        galleryButton.setOnClickListener(openListGoalClickListener());

        additionalValueTextView = (TextView)findViewById(R.id.additionalValueTextView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/porkys.regular.ttf");
        additionalValueTextView.setTypeface(typeface);
        additionalValueTextView.setTextColor(getResources().getColor(R.color.orange));
        additionalValueTextView.setRotationY(45);
        additionalValueTextView.setVisibility(View.INVISIBLE);

        newEntryButton = (ImageButton) findViewById(R.id.newEntryButton);
        newEntryButton.setOnClickListener(openNewEntryClickListener());

        objectivesAdd.setOnClickListener(createNewGolClickListener());

        objectiveTextView = (TextView) header.findViewById(R.id.tv_title_objetivo);
        objectiveValueTextView = (TextView) header.findViewById(R.id.tv_value_objetivo);

        buyButton =  (ImageButton) header.findViewById(R.id.buyButton);
        buyButton.setOnClickListener(buyEventClick());

        if(UserDao.getUser() == null){
            changeFragment(new RegisterFragment());
        }else {
            changeFragment(new MainFragment(this));
        }
    }

    private View.OnClickListener buyEventClick() {
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogMaker("Efetuar compra?",
                        "Legal, você ja tem o valor suficiente para adquirir o que deseja. Vai fazer isso agora?",new DialogActions() {
                    @Override
                    public void cancelAction() { }

                    @Override
                    public void confirmAction() {
                        closeGoal();
                    }
                }).createDialog(activity).show();
            }
        };
    }



    private View.OnClickListener createNewGolClickListener() {
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GoalDao.getUserActiveGoal() != null) {
                    new DialogMaker("Novo Objetivo", "Começar um novo objetivo fará com que o anterior" +
                            "seja desativo. Prosseguir? ", new DialogActions() {
                        @Override
                        public void cancelAction() {}

                        @Override
                        public void confirmAction() {
                            closeGoal();
                        }
                    }).createDialog(activity).show();
                } else {
                    changeFragment(new NewGoalFragment());
                }
            }
        };
    }

    private View.OnClickListener openNewEntryClickListener() {
        final Activity activity = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GoalDao.getUserActiveGoal() != null) {
                    Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    CustomToast.show("Você deve ter um objetivo para efetuar um lançamento.", activity, new ExtraAction() {
                        @Override
                        public void execute() {
                            changeFragment(new NewGoalFragment());
                        }
                    });
                }
            }
        };
    }

    private View.OnClickListener openMainClickListener(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    private View.OnClickListener openListGoalClickListener(){
        final Activity activity = this;
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(GoalDao.getAllGoals() != null && !GoalDao.getAllGoals().isEmpty()) {
                    Intent intent = new Intent(activity, GalleryGoalsActivity.class);
                    startActivity(intent);
                }else{
                   new DialogMaker("Sem objetivos =/", "Ops! Você não tem nenhum objetivo. Que tal fazer isso agora?",
                           new DialogActions() {
                               @Override
                               public void cancelAction() {}

                               @Override
                               public void confirmAction() {
                                   changeFragment(new NewGoalFragment());
                               }
                           }).createDialog(activity).show();
                }
            }
        };
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {return true;}
        return super.onOptionsItemSelected(item);
    }



    private void showHideBars(){
        if(getFragment() instanceof MainFragment) {
            footer.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
            additionalValueTextView.setVisibility(View.VISIBLE);
            homeButton.setImageResource(R.drawable.btn_door);
        }else{
            footer.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            additionalValueTextView.setVisibility(View.INVISIBLE);
            homeButton.setImageResource(R.drawable.btn_oinc);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if(getFragment() instanceof MainFragment || getFragment() instanceof RegisterFragment)
            super.finish();
        else
            changeFragment(new MainFragment(this));
    }

    @Override
    public void changeLayout() {
        Goal goal = GoalDao.getUserActiveGoal();
        this.objectiveTextView.setText(goal != null ? goal.getMeta() : "");

        DecimalFormat df = new DecimalFormat(".00");
        df.setMinimumIntegerDigits(1);
        this.objectiveValueTextView.setText("R$"+ (goal != null ? df.format(goal.getValue()) : "00,00"));
        this.additionalValueTextView.setText("Bônus! +R$"+ (goal != null ? df.format(goal.getTotalValue() - goal.getValue()) : "00,00"));
        if(goal != null && goal.getTotalValue() >= goal.getValue()) {
            this.buyButton.setVisibility(View.VISIBLE);
            this.additionalValueTextView.setVisibility(View.VISIBLE);
        }else {
            this.buyButton.setVisibility(View.INVISIBLE);
            this.additionalValueTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void closeGoal(){
        Goal goal = GoalDao.getUserActiveGoal();
        if (goal != null) {
            goal.setStatus(false);
            goal.save();
            if(goal.getTotalValue() > goal.getValue()){
                Balance balance = Balance.getBalance();
                balance.setBallanceOnAccount(goal.getTotalValue() - goal.getValue());
                balance.save();
            }
        }
        changeFragment(new NewGoalFragment());
    }

    @Override
    public void executeActionChangeFragment() {
        showHideBars();
    }
}
