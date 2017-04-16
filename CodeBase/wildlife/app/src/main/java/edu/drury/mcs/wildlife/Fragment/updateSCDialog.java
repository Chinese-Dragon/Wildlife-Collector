package edu.drury.mcs.wildlife.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import edu.drury.mcs.wildlife.Activity.ViewAndUpdateCollectionEntry;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.OnDataReturnListener;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class updateSCDialog extends DialogFragment implements View.OnClickListener{

    private SpeciesCollected data;
    private OnDataReturnListener returnListener;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialog_view;
    private Context context;
    private ImageButton increase,decrease, rm_increase, rm_decrease;
    private EditText quantity, quantity_rm, band_number;
    private RadioGroup radioGroup, radioGroup2;
    private RadioButton specimen_yes, specimen_no, blood_yes, blood_no;
    private Spinner disposition_spinner;
    private int quantity_removed = 0;
    private int quantity_captured = 0;
    private ArrayAdapter<CharSequence> adapter;
    private Button update, cancel;
    private int group_id, adapterPosition;

    public updateSCDialog(Context context, SpeciesCollected data, int group_id, int adapterPosition) {
        this.data = data;
        // get hold of callback target
        ViewAndUpdateCollectionEntry viewAndUpdate;
        if(context instanceof ViewAndUpdateCollectionEntry) {
            viewAndUpdate = (ViewAndUpdateCollectionEntry) context;
            try {
                returnListener = viewAndUpdate;
            }catch (ClassCastException e) {
                throw new ClassCastException(viewAndUpdate.toString() + " must implement OnDataPassListener interface");
            }
        }

        this.context = context;
        this.group_id = group_id;
        this.adapterPosition = adapterPosition;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        dialogBuilder = new AlertDialog.Builder(getActivity());

        dialog_view = inflater.inflate(R.layout.updatesc_dialog_fragment, null);
        dialogBuilder.setView(dialog_view);

        setUpUI();

        return dialogBuilder.create();
    }

    private void setUpUI() {
        increase = (ImageButton) dialog_view.findViewById(R.id.increase);
        decrease = (ImageButton) dialog_view.findViewById(R.id.decrease);
        quantity = (EditText) dialog_view.findViewById(R.id.quantity_captured);
        rm_increase = (ImageButton) dialog_view.findViewById(R.id.rm_increase);
        rm_decrease = (ImageButton) dialog_view.findViewById(R.id.rm_decrease);
        quantity_rm = (EditText) dialog_view.findViewById(R.id.quantity_removed);
        band_number = (EditText) dialog_view.findViewById(R.id.band_num);
        update = (Button) dialog_view.findViewById(R.id.update);
        cancel = (Button) dialog_view.findViewById(R.id.cancel);

        // detect edittext change and then change corresponding data
        band_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    data.setBand_num(band_number.getText().toString());
                }
            }
        });

        radioGroup = (RadioGroup) dialog_view.findViewById(R.id.radio_group);
        radioGroup2 = (RadioGroup) dialog_view.findViewById(R.id.radio_group_2);
        specimen_yes = (RadioButton) dialog_view.findViewById(R.id.specimen_yes);
        specimen_no = (RadioButton) dialog_view.findViewById(R.id.specimen_no);
        blood_yes = (RadioButton) dialog_view.findViewById(R.id.blood_yes);
        blood_no = (RadioButton) dialog_view.findViewById(R.id.blood_no);
        disposition_spinner = (Spinner) dialog_view.findViewById(R.id.disposition);
        adapter = ArrayAdapter.createFromResource(context,R.array.disposition_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        disposition_spinner.setAdapter(adapter);

        update.setOnClickListener(this);
        cancel.setOnClickListener(this);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);
        rm_increase.setOnClickListener(this);
        rm_decrease.setOnClickListener(this);

        fillInData();
    }

    private void fillInData() {
        quantity.setText(Integer.toString(data.getQuantity()));
        quantity_rm.setText(Integer.toString(data.getNum_removed()));
        quantity_captured = data.getQuantity();
        quantity_removed = data.getNum_removed();

        if(data.getVoucher_specimen_retained()) {
            Message.showMessage(context, "update specimen: is specimen");
            specimen_yes.setChecked(true);
        } else {
            Message.showMessage(context, "update specimen: no specimen");
            specimen_no.setChecked(true);
        }

        if(data.getIs_blood_taken()) {
            Message.showMessage(context, "update blood radio button: is taken");
            blood_yes.setChecked(true);
        } else {
            Message.showMessage(context, "update blood radio button: not taken");
            blood_no.setChecked(true);
        }

        Log.i("FIllData status", data.getStatus().toString());
        Log.i("FIllData position", Integer.toString(adapter.getPosition(data.getStatus().toString())));
        disposition_spinner.setSelection(SpeciesCollected.disposition_map.get(data.getStatus()) -1);
        band_number.setText(data.getBand_num());
    }

    @Override
    public void onClick(View view) {
        if (view == update) {
            // set updated value to currentSC
            data.setQuantity(quantity_captured);
            data.setNum_removed(quantity_removed);
            data.setNum_released(quantity_captured - quantity_removed);
            data.setBand_num(band_number.getText().toString());

            switch (disposition_spinner.getSelectedItem().toString()) {
                case "Released":
                    data.setStatus(SpeciesCollected.Disposition.RELEASED);
                    break;
                case "Held In Captivity":
                    data.setStatus(SpeciesCollected.Disposition.HELD);
                    break;
                case "Killed For Study Purpose":
                    data.setStatus(SpeciesCollected.Disposition.KILLED);
                    break;
                default:
                    break;
            }

            if(specimen_yes.isChecked()) {
                data.setVoucher_specimen_retained(true);
            } else {
                data.setVoucher_specimen_retained(false);
            }

            if(blood_yes.isChecked()) {
                data.setIs_blood_taken(true);
            } else {
                data.setIs_blood_taken(false);
            }

            //pass our updated DATA back with group_id, and adapter position to update the view and data
            returnListener.onDataReturn(data, group_id, adapterPosition);
            updateSCDialog.this.getDialog().cancel();
        } else if (view == cancel) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            updateSCDialog.this.getDialog().cancel();

        } else if (view == increase){
            quantity_captured ++;
            quantity.setText(Integer.toString(quantity_captured));

        } else  if (view == decrease){
            if(quantity_captured > 0 && quantity_captured > quantity_removed) {
                quantity_captured --;
                quantity.setText(Integer.toString(quantity_captured));
            }

        } else if (view == rm_increase) {
            if (quantity_removed < quantity_captured) {
                quantity_removed ++;
                quantity_rm.setText(Integer.toString(quantity_removed));
            } else {
                Message.showMessage(context, "Cannot remove more than captured");
            }

        } else if (view == rm_decrease) {
            if (quantity_removed > 0) {
                quantity_removed --;
                quantity_rm.setText(Integer.toString(quantity_removed));
            }
        }
    }

}
