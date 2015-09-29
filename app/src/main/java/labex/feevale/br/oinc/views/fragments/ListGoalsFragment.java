package labex.feevale.br.oinc.views.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Goal;
import labex.feevale.br.oinc.utils.GoalAdapter;
import labex.feevale.br.oinc.views.activities.ShowTripsActivity;


public class ListGoalsFragment extends Fragment {

    private ListView listVGoals;

    public ListGoalsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GoalDao goalDao = new GoalDao();
        List<Goal> listGoal = goalDao.getAllGoals();
        Collections.reverse(listGoal);
        GoalAdapter goalAdapter = new GoalAdapter(listGoal, getActivity());
        listVGoals.setAdapter(goalAdapter);

        listVGoals.setClickable(true);
        listVGoals.setOnItemClickListener(opeTrips());
    }

    private AdapterView.OnItemClickListener opeTrips() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showTrips = new Intent(getActivity(), ShowTripsActivity.class);
                showTrips.putExtra("GOAL",id);
                startActivity(showTrips);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_goals, container, false);
        listVGoals = (ListView) view.findViewById(R.id.list_goals);
        return view;
    }


}
