package edu.drury.mcs.wildlife.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailDialog extends DialogFragment {
    public static final String EXTRA_CURRENTCOLLECTION = "edu.drury.mcs.wildlife.CURRENTCOLLECTION";;
    public final static int STATIC_INTEGER_VALUE = 100;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialog_view;
    private Button cancel, create;
    private EditText editText;
    private Context context;

    public EmailDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        inflater = getActivity().getLayoutInflater();

        // inflate and set the our customized layout for the dialog
        dialog_view = inflater.inflate(R.layout.email_dialog_fragment, null);
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
                EmailDialog.this.getDialog().cancel();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionObj currentCollection = new CollectionObj();
                String collection_name = editText.getText().toString().trim();
                Log.i(TAG,collection_name);
                if(!collection_name.equals("")) {
                    currentCollection.setCollection_name(editText.getText().toString());

                    // Segue to CreateCollection activity
                    Intent intent = new Intent(getActivity(), CreateCollection.class);
                    intent.putExtra(EXTRA_CURRENTCOLLECTION, currentCollection);

                    EmailDialog.this.getDialog().cancel();
                    ((Activity) context).startActivityForResult(intent, STATIC_INTEGER_VALUE);
                } else {
                    Message.showMessage(context,"Collection Name is required");
                }

            }
        });

        return dialogBuilder.create();
    }
}
