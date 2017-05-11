package edu.drury.mcs.wildlife.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainListAdapter;
import edu.drury.mcs.wildlife.R;

/**
 * Created by mark93 on 5/11/2017.
 */

public class SwitchMainCollectionDialog extends DialogFragment{
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialog_view;
    private ListView listView;
    private RecyclerView recyclerView;
    private MainListAdapter adapter;
    private Context context;
    private List<MainCollectionObj> main_list;

    public SwitchMainCollectionDialog(Context context, List<MainCollectionObj> main_list) {
        this.context = context;
        this.main_list = main_list;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        inflater = getActivity().getLayoutInflater();

        // inflate and set the our customized layout for the dialog
        dialog_view = inflater.inflate(R.layout.switch_main_collection_dialog, null);
        dialogBuilder.setView(dialog_view);

        // get hold of recycerview
        recyclerView = (RecyclerView) dialog_view.findViewById(R.id.main_collection_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new MainListAdapter(context, main_list);
        recyclerView.setAdapter(adapter);

        return dialogBuilder.create();
    }
}
