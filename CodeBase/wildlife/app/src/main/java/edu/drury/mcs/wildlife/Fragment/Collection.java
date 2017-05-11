package edu.drury.mcs.wildlife.Fragment;


import android.content.Context;
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

import edu.drury.mcs.wildlife.Activity.MainActivity;
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
    private FloatingActionButton showMainList;
    private MainCollectionObj current_mainCollection;
    private List<CollectionObj> adapterData;
    private List<MainCollectionObj> main_list;
    private MainActivity parent;

    public static final Collection newInstance(MainCollectionObj _mainCollection) {
        Collection fragment = new Collection();
        final Bundle args = new Bundle(1);
        args.putParcelable(EXTRA_MAIN_COLLECTION, _mainCollection);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity) {
            parent = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.current_mainCollection = getArguments().getParcelable(EXTRA_MAIN_COLLECTION);
        assert current_mainCollection != null;
        adapterData = current_mainCollection.getCollections();
//        this.current_mainCollection.setCollections(adapterData);
        // check if current_mainCollection is empty
        main_list = parent.getMainCollectionList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_collection,container,false);

        FAM = (FloatingActionMenu) layout.findViewById(R.id.material_design_android_floating_action_menu);
        addFab = (FloatingActionButton) layout.findViewById(R.id.add_collection);
        emailFab = (FloatingActionButton) layout.findViewById(R.id.email_collection);
        showMainList = (FloatingActionButton) layout.findViewById(R.id.show_main_collections);

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
                EmailDialog emailDialog = new EmailDialog(getActivity());
                emailDialog.show(getActivity().getSupportFragmentManager(), "Email an Entry");
                FAM.close(true);
            }
        });

        showMainList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchMainCollectionDialog dialog = new SwitchMainCollectionDialog(getActivity(), main_list);
                dialog.show(getActivity().getSupportFragmentManager(), "Show All Main Collections");
                FAM.close(true);
            }
        });

        setUpRecyclerView();

        return layout;
    }


    public void addNewCollectionToList(CollectionObj newC) {

        // add new data to internal data instance (which updates adapter data and maincollection data as well because they reference the same data list (adapterData))
        adapterData.add(newC);
        cAdapter.notifyItemInserted(adapterData.size() - 1);

        //save new Entry to DB
        newC.saveToDB(getActivity(), current_mainCollection);
        Message.showMessage(getActivity(),"Successfully Saved Entry Data");
    }


    // update both our current maincollection ragarding to element inside change.
    // update adapter data to correctly display our updated data
    public void updateCollectionList(CollectionObj updatedC, int position) {
        CollectionObj old = adapterData.get(position);

        // update internal data (which updates adapter data and maincollection data as well because they reference the same data list (adapterData))
        adapterData.set(position, updatedC);
        cAdapter.notifyDataSetChanged();

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

        cAdapter = new collectionAdapter(getActivity(), adapterData, Collection.this);

        cRecyclerView.setAdapter(cAdapter);

    }

//    private List<CollectionObj> getAdapterData() {
//        List<CollectionObj> result = null;
//        try {
//            result = CollectionObj.readAllCollections(getActivity(), current_mainCollection);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    public MainCollectionObj getCurrent_mainCollection() {
        return current_mainCollection;
    }
}
