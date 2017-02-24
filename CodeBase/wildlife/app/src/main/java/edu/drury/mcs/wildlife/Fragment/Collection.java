package edu.drury.mcs.wildlife.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Collection extends Fragment {

    private RecyclerView cRecyclerView;
    private collectionAdapter cAdapter;
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
                AddDialog dialog = new AddDialog(getActivity());
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
        try {
            cAdapter = new collectionAdapter(getActivity(),CollectionObj.readAllCollections(getActivity()),Collection.this);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cRecyclerView.setAdapter(cAdapter);


        return layout;
    }


    public void addNewCollectionToList(CollectionObj newC) {
        cAdapter.addNewData(newC);
    }

}
