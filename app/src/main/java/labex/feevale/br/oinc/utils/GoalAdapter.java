package labex.feevale.br.oinc.utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Goal;

/**
 * Created by Jeferson Klaus on 16/03/2015.
 */
public class GoalAdapter extends BaseAdapter {

    private List<Goal> listGoal;
    private Context context;
    private ImageView imgGoal;
    private TextView textTitle, textValue, textDate;


    public GoalAdapter(List<Goal> listGoal, Context context){
        this.listGoal = listGoal;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listGoal.size();
    }

    @Override
    public Object getItem(int position) {
        return listGoal.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listGoal.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Goal goal = (Goal) getItem(position);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.item_list_goal,null);

        imgGoal = (ImageView) layout.findViewById(R.id.image_goal);
        textTitle = (TextView) layout.findViewById(R.id.text_title_goal);
        textValue = (TextView) layout.findViewById(R.id.text_value_goal);
        textDate = (TextView) layout.findViewById(R.id.text_status_goal);


        textTitle.setText(goal.getMeta());
        textValue.setText(goal.getValue().toString());
        textDate.setText((goal.getStatus() == true? "Atual": ""));

        imgGoal.setImageURI(Uri.fromFile(new File(goal.getPathImage())));


        return  layout;
    }
}
