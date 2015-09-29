package labex.feevale.br.oinc.views.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.views.activities.GoalsAndEntryDataActivity;
import labex.feevale.br.oinc.views.fragments.PreviewImageEntryFragment;

/**
 * Created by 0126128 on 19/03/2015.
 */
public class EntryListAdapter extends BaseAdapter{

    private List<Entry> entries;
    private LayoutInflater inflater;
    private Activity activity;

    public EntryListAdapter(List<Entry> entries, Activity activity) {
        this.entries = entries;
        this.activity = activity;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        if(entries.get(position).getPicturePath() != null &&
                !entries.get(position).getPicturePath().isEmpty())
            return true;
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}//TODO: Criar evento para deletar item da lista

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}//TODO: Criar evento para deletar item da lista

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_list_goal_entry, parent, false);
        if(view != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat df = new DecimalFormat(".00");
            df.setMinimumIntegerDigits(1);

            ImageView imageOinc = (ImageView)view.findViewById(R.id.goalEntryImageView);
            TextView valueEntryTextView = (TextView)view.findViewById(R.id.entryValueTextView);
            TextView dateEntryTextView = (TextView)view.findViewById(R.id.entryDateTextView);

            valueEntryTextView.setText("R$" + (entries.get(position).getValue() != null ?
                                        df.format(entries.get(position).getValue()): "00,00"));
            dateEntryTextView.setText(simpleDateFormat.format(entries.get(position).getDate()));

            switch (entries.get(position).getType()){
                case Entry.CREDIT:
                    imageOinc.setBackgroundResource(R.drawable.ring_green);
                    break;
                case Entry.NONE:
                    imageOinc.setBackgroundResource(R.drawable.ring_gray);
                    break;
                case Entry.DEBIT:
                    imageOinc.setBackgroundResource(R.drawable.ring_red);
                    break;
            }

            view.setOnClickListener(showPreviewPictureEventClick(position));
        }
        return view;
    }

    private View.OnClickListener showPreviewPictureEventClick(int position) {
        final int positionAux = position;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((Entry)getItem(positionAux)).getPicturePath() != null && !((Entry)getItem(positionAux)).getPicturePath().isEmpty())
                    ((GoalsAndEntryDataActivity)activity).changeFragment(new PreviewImageEntryFragment((Entry)getItem(positionAux)));
                else
                    Toast.makeText(activity, "Você não possuí fotos vinculadas a esse lançamento.", Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return entries.size();
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }
}
