package labex.feevale.br.oinc.views.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.Toast;

import java.util.List;

import labex.feevale.br.oinc.dao.EntryDao;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.views.activities.GoalsAndEntryDataActivity;
import labex.feevale.br.oinc.views.adapter.EntryListAdapter;

/**
 * Created by 0126128 on 19/03/2015.
 */
public class EntryListFragment extends ListFragment {

    private Goal goal;
    private List<Entry> entries;
    private EntryListAdapter adapter;

    public EntryListFragment() {}

    public EntryListFragment(Goal goal) {
        this.goal = goal;
        this.entries = EntryDao.getEntriesByGoal(goal.getId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            if(this.entries.size() > 0) {
                adapter = new EntryListAdapter(this.entries, getActivity());
                setListAdapter(adapter);
                setRetainInstance(true);
            }else{
                Toast.makeText(getActivity(), "Ops, esse objetivo não possui lançamentos.", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((GoalsAndEntryDataActivity)getActivity()).setFragment(this);
    }
}
