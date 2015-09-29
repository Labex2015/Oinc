package labex.feevale.br.oinc.dao;

import android.app.Activity;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.utils.AudioUtils;

/**
 * Created by 0126128 on 28/01/2015.
 */
public class EntryDao {

    public static Entry findById(Long id){
        return new Select().from(Entry.class)
                .where("Id = ?", id).executeSingle();
    }

    public static List<Entry> getEntries(){
        return new Select().from(Entry.class).execute();
    }

    public static List<Entry> getEntriesByGoal(Long goalId){
        return new Select().from(Entry.class)
                .where("goal = ?", goalId).execute();
    }

    public static List<Entry> getEntriesByDate(Date date){
        return new Select().from(Entry.class)
                .where("date = ?", date).execute();
    }

    public static List<Entry> getEntriesByGoalAndType(Long goalId, Character type){
        return new Select().from(Entry.class)
                .where("goal = ?", goalId)
                .and(" type = ?", type).execute();
    }

    public static List<Entry> getEntriesByDateAndType(Date date, Character type){
        return new Select().from(Entry.class)
                .where("date = ?", date)
                .and(" type = ?", type).execute();
    }

    public static Entry saveWithSound(Entry entry, Activity activity){
        entry.save();
        if(entry.getType() == Entry.CREDIT){
            new AudioUtils().playAudioFromInternalStorage(AudioUtils.CASH_AUDIO, activity);
        }
        return entry;
    }
}
