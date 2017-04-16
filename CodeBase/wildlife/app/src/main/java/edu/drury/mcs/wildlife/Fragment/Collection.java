package edu.drury.mcs.wildlife.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Collection extends Fragment {
    public static final String EXTRA_MAIN_COLLECTION = "edu.drury.mcs.wildlife.EXTRA_MAIN_COLLECTION";
    private RecyclerView cRecyclerView;
    private collectionAdapter cAdapter;
    private View layout;
    FloatingActionMenu FAM;
    private FloatingActionButton addFab;
    private FloatingActionButton emailFab;
    private MainCollectionObj current_mainCollection;

    public static final Collection newInstance(MainCollectionObj _mainCollection) {
        Collection fragment = new Collection();
        final Bundle args = new Bundle(1);
        args.putParcelable(EXTRA_MAIN_COLLECTION, _mainCollection);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.current_mainCollection = getArguments().getParcelable(EXTRA_MAIN_COLLECTION);

        // check if current_mainCollection is empty
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_collection,container,false);

        FAM = (FloatingActionMenu) layout.findViewById(R.id.material_design_android_floating_action_menu);
        addFab = (FloatingActionButton) layout.findViewById(R.id.add_collection);
        emailFab = (FloatingActionButton) layout.findViewById(R.id.email_collection);
        //set up onclick events on Fabs
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialog dialog = new AddDialog(getActivity());
                dialog.show(getActivity().getSupportFragmentManager(), "Add an Entry");
                FAM.close(true);
            }
        });

        emailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setUpRecyclerView();

        return layout;
    }


    public void addNewCollectionToList(CollectionObj newC) {
        current_mainCollection.add_collectionObj(newC);
        cAdapter.addNewData(newC);
        //save new Entry to DB
        newC.saveToDB(getActivity(), current_mainCollection);
        Message.showMessage(getActivity(),"Successfully Saved Entry Data");
    }


    // update both our current maincollection ragarding to element inside change.
    // update adapter data to correctly display our updated data
    public void updateCollectionList(CollectionObj updatedC, int position) {
        current_mainCollection.update_collectionObj(updatedC, position);
        CollectionObj old = cAdapter.getSingleData(position);
        cAdapter.updateRow(updatedC, position);

        // update with the modified collection
        updatedC.updateToDB(getActivity(), old, current_mainCollection);
        Message.showMessage(getActivity(), "Successfuly Updated Entry Data");

        Message.showMessage(getActivity(), Integer.toString(current_mainCollection.getCollections().size()));
    }


    private void setUpRecyclerView() {
        // grab outlets from xml layout

        cRecyclerView = (RecyclerView) layout.findViewById(R.id.collection_recyclerview);

        //Initialize collection recycler view
        cRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cAdapter = new collectionAdapter(getActivity(), getAdapterData(), Collection.this);

        cRecyclerView.setAdapter(cAdapter);
    }

    private List<CollectionObj> getAdapterData() {
        List<CollectionObj> result = null;
        try {
            result = CollectionObj.readAllCollections(getActivity(), current_mainCollection);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        current_mainCollection.setCollections(result);
        return result;
    }

    public MainCollectionObj getCurrent_mainCollection() {
        return current_mainCollection;
    }
}
