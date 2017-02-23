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
import edu.drury.mcs.wildlife.DB.DBBackgroundTask;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionSpecies extends Fragment implements View.OnClickListener {
    public static final String TASK_CREATE = "create";
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
        Log.i(TAG,"CollectionSpecies onCreateVIew");
        currentCollection = ((CreateCollection) getActivity()).getCurrentCollection();

        // set collection initial species data
        currentCollection.setSpecies(getData());
        ((CreateCollection) getActivity()).setCurrentCollection(currentCollection);

        back = (Button) layout.findViewById(R.id.back);
        cancel = (Button) layout.findViewById(R.id.cancel);
        done = (Button) layout.findViewById(R.id.done);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        done.setOnClickListener(this);

        //initilize and setup recycler view
        sRecyclerView = (RecyclerView) layout.findViewById(R.id.species_recyclerview);
        sRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sAdapter = new sAdapter(getActivity(), getData(), this);
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
            Log.i("OnSave", currentCollection.getCollection_name());
            Log.i("OnSave", currentCollection.getDate());
            Log.i("OnSave", currentCollection.getLocation().toString());

            for(Species s : currentCollection.getSpecies()) {
                Log.i("OnSave", s.getCommonName() + " has " + s.getSpecies_Data().size() +" species collected");
            }

            new DBBackgroundTask(getActivity(),currentCollection).execute(TASK_CREATE);
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
        Log.i(TAG,"CurrentCollection Location" + Double.toString(currentCollection.getLocation().getLatitude()));
        for(Species s: currentCollection.getSpecies()){
            Log.i(TAG,s.getCommonName()+" has "+ Integer.toString(s.getSpecies_Data().size()) + "species collected");
        }
    }


    public CollectionObj getCurrentCollection() {
        return currentCollection;
    }
}
