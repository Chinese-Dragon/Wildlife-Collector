package edu.drury.mcs.wildlife.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.ViewpagerFragmentLifecycle;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionDate extends Fragment implements View.OnClickListener,ViewpagerFragmentLifecycle {
    private View layout;
    private Button next,cancel,datePickerButton;
    private TextView currentDate;
    private RadioButton getCurrentDate,getCustomizedDate;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private CollectionObj currentCollection;
    private OnDataPassListener dataListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if(context instanceof Activity) {
            a = (Activity) context;

            try {
                dataListener = (OnDataPassListener) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(a.toString() + " must implement OnDataPassListener interface");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.collection_date_fragment,container,false);
        // Inflate the layout for this fragment

        // deal with buttons
        cancel = (Button) layout.findViewById(R.id.cancel);
        next = (Button) layout.findViewById(R.id.next);
        datePickerButton = (Button) layout.findViewById(R.id.datePickerButton);
        getCurrentDate = (RadioButton) layout.findViewById(R.id.currentDateChecked);
        getCustomizedDate = (RadioButton) layout.findViewById(R.id.customDateChecked);

        currentCollection = ((CreateCollection) getActivity()).getCurrentCollection();

        currentDate = (TextView) layout.findViewById(R.id.currentDate);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        datePickerButton.setOnClickListener(this);
        getCurrentDate.setOnClickListener(this);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == cancel) {
            getActivity().finish();
        } else if (view == next) {
            dataListener.onDataPass(currentCollection, 1);
        } else if (view == getCurrentDate) {

        } else if (view == datePickerButton) {
            if(getCustomizedDate.isChecked()) {
                Calendar calendar = Calendar.getInstance();
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year,month,day);
                        currentDate.setText(dateFormatter.format(newDate.getTime()));
                        currentCollection.setDate(currentDate.getText().toString());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"CollectionDate is Resumed");
    }

    @Override
    public void onPauseFragment() {
        Log.i(TAG,"Date -- onPauseFragment()");
    }

    @Override
    public void onResumeFragment() {
        Log.i(TAG,"Date -- onResumeFragment()");
    }
}
