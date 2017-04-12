package edu.drury.mcs.wildlife.Fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.Activity.MainActivity;
import edu.drury.mcs.wildlife.DB.GroupMappingTable;
import edu.drury.mcs.wildlife.DB.wildlifeDBHandler;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionSpecies extends Fragment implements View.OnClickListener {
    public static final String SAVEDCOLLECTIONDATA = "edu.drury.mcs.wildlife.SAVEDCOLLECTIONDATA";
    private View layout;
    private Button back,cancel,done;
    private RecyclerView sRecyclerView;
    private RecyclerView.Adapter sAdapter;
    private CollectionObj currentCollection;
    private OnDataPassListener dataListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CreateCollection a;

        if(context instanceof CreateCollection) {
            a = (CreateCollection) context;
            a.setActionBarTitle("Collect data");
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

        Log.i("Info","COllectionSPecies view is created");
        Log.i(TAG,"CollectionSpecies onCreateVIew");
        currentCollection = ((CreateCollection) getActivity()).getCurrentCollection();

        // set collection initial species data
        try {
            currentCollection.setSpecies(getData());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        sAdapter = new sAdapter(getActivity(), currentCollection.getSpecies(), this);

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
            Log.i("onSave", Integer.toString(currentCollection.getSpecies().size()));

            for(Species s : currentCollection.getSpecies()) {
                Log.i("OnSave", s.getCommonName() + " has " + s.getSpecies_Data().size() +" species collected");
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(SAVEDCOLLECTIONDATA, currentCollection);
            getActivity().setResult(MainActivity.RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"CollectionSpecies is Resumed");
    }

    public List<Species> getData() throws ExecutionException, InterruptedException {
        return new getGroupInfoTask().execute().get();
    }

    private class getGroupInfoTask extends AsyncTask<Void, Void, List<Species>> {

        @Override
        protected List<Species> doInBackground(Void... voids) {
            List<Species> data = new ArrayList<>();
            SQLiteDatabase db = new wildlifeDBHandler(getActivity()).getReadableDatabase();

            String[] projection = {
                    GroupMappingTable.GM_ID,
                    GroupMappingTable.GM_CNAME,
                    GroupMappingTable.GM_SNAME
            };

            Cursor cursor = db.query(GroupMappingTable.TABLE_NAME,projection,null,null,null,null,null);
            while (cursor.moveToNext()) {
                int group_id = cursor.getInt(cursor.getColumnIndexOrThrow(GroupMappingTable.GM_ID));
                String cName = cursor.getString(cursor.getColumnIndexOrThrow(GroupMappingTable.GM_CNAME));
                String sName = cursor.getString(cursor.getColumnIndexOrThrow(GroupMappingTable.GM_SNAME));
                data.add(new Species(cName, sName, group_id));
                Log.i("Species", cName);
            }

            cursor.close();
            return data;
        }
    }

    public void setCurrentCollection(CollectionObj collection) {
        this.currentCollection = collection;
        Log.i(TAG,"CurrentCollection Location " + Double.toString(currentCollection.getLocation().getLatitude())
                + " , " + Double.toString(currentCollection.getLocation().getLongitude()));
        for(Species s: currentCollection.getSpecies()){
            Log.i(TAG,s.getCommonName()+" has "+ Integer.toString(s.getSpecies_Data().size()) + " species collected");
            if(s.getSpecies_Data().size() > 0) {
                Log.i("Test","common name: " + s.getSpecies_Data().get(0).getCommonName());
                Log.i("Test","band number: " + s.getSpecies_Data().get(0).getBand_num());
                Log.i("Test", "number captured: "+ Integer.toString(s.getSpecies_Data().get(0).getQuantity()));
                Log.i("Test", "number removied: "+ Integer.toString(s.getSpecies_Data().get(0).getNum_removed()));
                Log.i("Test", "disposition status: " + s.getSpecies_Data().get(0).getStatus().toString());
                Log.i("Test","is blood taken: " + Boolean.toString(s.getSpecies_Data().get(0).getIs_blood_taken()));
                Log.i("Test","is specimen retained: " + Boolean.toString(s.getSpecies_Data().get(0).getVoucher_specimen_retained()));

            }
        }

    }

    public CollectionObj getCurrentCollection() {
        return currentCollection;
    }
}
