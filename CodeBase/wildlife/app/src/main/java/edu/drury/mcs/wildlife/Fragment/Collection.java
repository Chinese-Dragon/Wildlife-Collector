package edu.drury.mcs.wildlife.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Collection extends Fragment {

    private RecyclerView cRecyclerView;
    private RecyclerView.Adapter cAdapter;
    private View layout;
    private FloatingActionButton addFab;
    private FloatingActionButton emailFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_collection,container,false);

        // grab outlets from xml layout
        addFab = (FloatingActionButton) layout.findViewById(R.id.add_collection);
        emailFab = (FloatingActionButton) layout.findViewById(R.id.email_collection);
        cRecyclerView = (RecyclerView) layout.findViewById(R.id.collection_recyclerview);

        //set up onclick events on Fabs
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog dialog = new addDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Add Collection");
            }
        });

        emailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Initialize collection recycler view
        cRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cAdapter = new collectionAdapter(getActivity(),getData(),Collection.this);
        cRecyclerView.setAdapter(cAdapter);

        return layout;
    }

    //get testdata
    private List<String> getData() {
        List<String> data = new ArrayList<>();
        int dataSize = 20;

        for (int i = 0; i < dataSize; i++) {
            data.add("Collection" + Integer.toString(i));
        }

        return data;
    }

}
