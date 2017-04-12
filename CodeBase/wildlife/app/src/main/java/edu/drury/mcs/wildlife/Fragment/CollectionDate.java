package edu.drury.mcs.wildlife.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class    CollectionDate extends Fragment implements View.OnClickListener, CalendarView.OnDateChangeListener {
    private View layout;
    private Button next,cancel,datePickerButton;
    private TextView currentDate;
    private RadioButton getCurrentDate,getCustomizedDate;
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private CollectionObj currentCollection;
    private OnDataPassListener dataListener;
    private CalendarView calendarView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CreateCollection a;

        if(context instanceof CreateCollection) {
            a = (CreateCollection) context;
            a.setActionBarTitle("Set Date");
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

        currentCollection = ((CreateCollection) getActivity()).getCurrentCollection();

        calendarView = (CalendarView) layout.findViewById(R.id.calendarView);
        currentDate = (TextView) layout.findViewById(R.id.currentDate);
        dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        currentDate.setText(dateFormatter.format(new GregorianCalendar().getTime()));


        calendarView.setOnDateChangeListener(this);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == cancel) {
            getActivity().finish();
        } else if (view == next) {
            String date = currentDate.getText().toString().trim();
            if(!date.equals("")) {
                currentCollection.setDate(date);
                dataListener.onDataPass(currentCollection, 1);
            }else {
                Message.showMessage(getActivity(),"Date is required");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"CollectionDate is Resumed");
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDate.setText(dateFormatter.format(calendar.getTime()));
    }
}
