package edu.drury.mcs.wildlife.Fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionSpecies extends Fragment implements View.OnClickListener {
    private View layout;
    private Button back,cancel,done;
    private RecyclerView sRecyclerView;
    private RecyclerView.Adapter sAdapter;
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
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.collection_species_fragment, container, false);

        Message.showMessage(getActivity(),"COllectionSPecies view is created");
        currentCollection = ((CreateCollection) getActivity()).getCurrentCollection();

        // get initial data
        currentCollection.setSpecies(getData());

        back = (Button) layout.findViewById(R.id.back);
        cancel = (Button) layout.findViewById(R.id.cancel);
        done = (Button) layout.findViewById(R.id.done);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        done.setOnClickListener(this);

        //initilize and setup recycler view
        sRecyclerView = (RecyclerView) layout.findViewById(R.id.species_recyclerview);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sAdapter = new sAdapter(getActivity(), getData());

        sRecyclerView.setAdapter(sAdapter);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            CreateCollection.pager.setCurrentItem(1);
        } else if (view == cancel) {
            getActivity().finish();
        } else if (view == done) {
            // Save collection data
            Message.showMessage(getActivity(),"Successfully Saved Collection Data");
        }
    }

    public List<Species> getData() {
        List<Species> data = new ArrayList<>();
        for (int i = 0; i < 5;i++) {
            switch (i) {
                case 0:
                    data.add(new Species("Salamanders","Caudata", i));
                    break;
                case 1:
                    data.add(new Species("Frogs","Anura",i));
                    break;
                case 2:
                    data.add(new Species("Lizards","Lacertilia",i));
                    break;
                case 3:
                    data.add(new Species("Snakes","Serpentes",i));
                    break;
                case 4:
                    data.add(new Species("Turtles","Testudines",i));
            }
        }

        return data;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"CollectionSpecies is Resumed");
    }

    public void setCurrentCollection(CollectionObj collection) {
        this.currentCollection = collection;
        Log.i(TAG,"CurrentCollection Date" + Double.toString(currentCollection.getLocation().getLatitude()));

    }

    /*
        This method only be called after we comeback from speciesTable with some data returned
        caller will be onActivityResult in parent activity
        so we gurentee that currentCollection has a list of species already
     */
    public void updateCollection(List<SpeciesCollected> newSpeciesData, int speciesGroupID) {
        for(SpeciesCollected s : newSpeciesData) {
            Log.i(TAG,s.getCommonName());
            Log.i(TAG,Integer.toString(s.getQuantity()));
        }

//        for(Species s : currentCollection.getSpecies()) {
//            if (s.getGroup_ID() == speciesGroupID) {
//                // then we just update this species data
//                s.setSpecies_Data(newSpeciesData);
//            }
//        }
    }

}
