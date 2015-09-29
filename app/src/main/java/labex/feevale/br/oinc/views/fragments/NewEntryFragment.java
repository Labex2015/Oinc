package labex.feevale.br.oinc.views.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import labex.feevale.br.oinc.MainActivity;
import labex.feevale.br.oinc.R;
import labex.feevale.br.oinc.actions.Observer;
import labex.feevale.br.oinc.dao.GoalDao;
import labex.feevale.br.oinc.model.Entry;
import labex.feevale.br.oinc.utils.CurrencyUtils;
import labex.feevale.br.oinc.utils.Masks;
import labex.feevale.br.oinc.views.actions.LayoutChanges;
import labex.feevale.br.oinc.views.actions.ProcessDatabaseActions;
import labex.feevale.br.oinc.views.actions.ProcessImageAction;
import labex.feevale.br.oinc.views.activities.EntryActivity;


public class NewEntryFragment extends Fragment{

    private Context context;
    private boolean mEntryMov = true;
    private Button btnNext, btnBack;
    private ImageButton btnTrip;
    private RadioButton radioSpend, radioWon, radioNo;
    private EditText editValue;
    private RadioGroup radioGroup;
    private Entry entry;
    private ProcessDatabaseActions databaseActions;
    private ProcessImageAction processImageAction;
    List<Observer> observers = new ArrayList<>();

    public NewEntryFragment() {}

    public NewEntryFragment(Context context, Entry entry, ProcessDatabaseActions databaseActions) {
        this.context = context;
        this.entry = entry;
        this.databaseActions = databaseActions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_new_entry, container, false);

            btnNext = (Button) view.findViewById(R.id.btn_entry_next);
            btnBack = (Button) view.findViewById(R.id.btn_entry_back);
            btnTrip = (ImageButton) view.findViewById(R.id.btn_trip);
            radioSpend = (RadioButton) view.findViewById(R.id.radio_spend);
            radioWon = (RadioButton) view.findViewById(R.id.radio_won);
            radioNo = (RadioButton) view.findViewById(R.id.radio_no);
            editValue = (EditText) view.findViewById(R.id.edit_value);
            radioGroup = (RadioGroup) view.findViewById(R.id.radio_entry);

            editValue.addTextChangedListener(new Masks(editValue));
            InputFilter[] filters = {new InputFilter.LengthFilter(10)};
            editValue.setFilters(filters);
            editValue.requestFocus();
            radioWon.setChecked(true);

            btnNext.setOnClickListener(saveEntry());
            btnBack.setOnClickListener(goBack());
            btnTrip.setOnClickListener(goTrip());

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            radioSpend.setOnClickListener(clickUpdateLayout());
            radioNo.setOnClickListener(clickUpdateLayout());
            radioWon.setOnClickListener(clickUpdateLayout());


            setRetainInstance(true);
            return view;
    }

    @Override
    public void onResume() {

        if (entry.getValue() > 0 || entry.getType() == Entry.NONE) {
            loadEntryValues();
        }

        loadTypeEntry();
        super.onResume();
    }

    private View.OnClickListener clickUpdateLayout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTypeEntry();
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            setRetainInstance(true);
        }
    }

    private View.OnClickListener goTrip() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadEntryValue(new LoadEntryAction() {
                    @Override
                    public void executeAction() {
                        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editValue.getWindowToken(), 0);
                        ((EntryActivity)getActivity()).changeFragment(new GalleryFragment("", "", (LayoutChanges) getActivity()));
                    }
                });
            }
        };
    }

    private View.OnClickListener goBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getActivity().onBackPressed();
            }
        };
    }

    private View.OnClickListener saveEntry() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEntryValue(new LoadEntryAction() {
                    @Override
                    public void executeAction() {
                        if(GoalDao.updateGoal(entry, getActivity()))
                           databaseActions.saveEntity(entry);
                    }
                });
            }
        };
    }

    private boolean checkFields() {
        loadTypeEntry();
        if (!radioNo.isChecked()) {
            if (editValue.getText().toString().equals("")) {
                editValue.setError(getResources().getString(R.string.required_field));
                editValue.requestFocus();
                return false;
            }
        }
        return true;
    }

    private void updateLayout(Boolean show) {
        if (show) {
            editValue.setVisibility(View.VISIBLE);
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editValue, 0);
            btnNext.setEnabled(true);
        } else {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editValue.getWindowToken(), 0);
            editValue.setVisibility(View.INVISIBLE);
            editValue.setText("");
            btnNext.setEnabled(false);
        }
    }

    private void loadTypeEntry(){
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_spend:
                entry.setType(entry.DEBIT);
                mEntryMov = true;
                updateLayout(true);
                break;
            case R.id.radio_won:
                entry.setType(entry.CREDIT);
                mEntryMov = true;
                updateLayout(true);
                break;
            case R.id.radio_no:
                mEntryMov = false;
                entry.setType(Entry.NONE);
                updateLayout(false);
                break;
        }
    }

    private void loadRadioGroupValues(){
        switch ((entry.getType())){
            case Entry.DEBIT:
                radioSpend.setChecked(true);
                mEntryMov = true;
                break;
            case Entry.CREDIT:
                radioWon.setChecked(true);
                mEntryMov = true;
                break;
            case Entry.NONE:
                radioNo.setChecked(true);
                mEntryMov = false;
                break;
        }
    }

    private void loadEntryValue(LoadEntryAction loadEntryAction){
        if (checkFields()) {

            if (mEntryMov) {
                String value = editValue.getText().toString();
                entry.setValue(CurrencyUtils.getValueFromString(value, Locale.getDefault().getLanguage()));
            }

                loadEntryAction.executeAction();

        }
    }

    private void loadMainActivity(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void loadEntryValues(){
        String value = Currency.getInstance(Locale.getDefault()).getSymbol()
                +entry.getValue().toString().replace(".", ",");
        this.editValue.setText(value.length() > 6 || entry.getValue() < 0.10 ? value : value+"0");
        loadRadioGroupValues();
    }

}

interface LoadEntryAction {
    void executeAction();
}
