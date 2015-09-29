package labex.feevale.br.oinc.dao;

import android.app.Activity;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.List;

import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.model.Goal;

/**
 * Created by 0126128 on 28/01/2015.
 */
public class GoalDao {

    public static Goal findById(Long id){
        return new Select().from(Goal.class)
                .where("Id = ?", id).executeSingle();
    }

    public static List<Goal> findGoalsByStatus(Boolean status){
        return new Select().from(Goal.class)
                   .where("status = ?", status).execute();
    }

    public static Goal findByMeta(String meta){
        return new Select().from(Goal.class)
                .where("meta like ?", meta.trim()).executeSingle();
    }

    public static List<Goal> getUserGoals(Boolean status){
        return new Select().from(Goal.class)
                .where("user = ?", UserDao.getUser().getId())
                .and(" status = ?", status).execute();
    }

    public static Goal getUserActiveGoal(){
        return new Select().from(Goal.class)
                .where("user = ?", UserDao.getUser().getId())
                .and(" status = ?", true).executeSingle();
    }

    public static List<Goal> getAllGoals(){
        return new Select().from(Goal.class).execute();
    }


    public static Boolean updateGoal(Entry entry, Activity activity){
        Goal goal = GoalDao.getUserActiveGoal();

        if (goal != null) {
            switch (entry.getType()) {
                case Entry.CREDIT:
                    goal.investment(entry.getValue());
                    break;
                case Entry.DEBIT:
                    goal.draft(entry.getValue());
                default:
                    ;
            }

            goal.save();
            entry.setGoal(goal);
            return true;
        } else {
            Toast.makeText(activity, "Problemas ao tentar encontrar um objetivo.", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
