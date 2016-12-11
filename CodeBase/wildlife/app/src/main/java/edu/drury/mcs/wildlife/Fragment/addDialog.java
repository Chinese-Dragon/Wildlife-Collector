package edu.drury.mcs.wildlife.Fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class addDialog extends DialogFragment {
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialog_view;
    private Button cancel, create;
    private EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        inflater = getActivity().getLayoutInflater();

        // inflate and set the our customized layout for the dialog
        dialog_view = inflater.inflate(R.layout.add_dialog_fragment, null);
        dialogBuilder.setView(dialog_view);

        // get hold of edittext, cancel and create button and set behavior
        editText = (EditText) dialog_view.findViewById(R.id.name_collection);
        create = (Button) dialog_view.findViewById(R.id.create);
        cancel = (Button) dialog_view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                addDialog.this.getDialog().cancel();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Segue to CreateCollection activity
                Intent intent = new Intent(getActivity(), CreateCollection.class);
                addDialog.this.getDialog().cancel();
                startActivity(intent);
            }
        });

        return dialogBuilder.create();
    }
}
