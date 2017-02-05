package edu.drury.mcs.wildlife.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionSpecies extends Fragment implements View.OnClickListener {
    private View layout;
    private Button back,cancel,done;
    private RecyclerView sRecyclerView;
    private RecyclerView.Adapter sAdapter;


//    /**
//     * Instead of handing over potential parameters via constructor,
//     * use the newInstance(...) method and the Bundle for handing over parameters.
//     * This way if detached and re-attached the object state can be stored through the arguments.
//     * Much like Bundles attached to Intents
//     *
//     * @param indicator
//     * @param pager
//     * @return
//     */
//    public static CollectionSpecies newInstance(myStepperIndicator indicator, NonSwipeableViewPager pager /* 3rd parameter will be collection data*/) {
//        CollectionSpecies myFragment = new CollectionSpecies();
//
//        Bundle args = new Bundle();
//        args.putSerializable(CollectionDate.EXTRA_INDICATOR,indicator);
//        args.putSerializable(CollectionDate.EXTRA_PAGER, pager);
//
//        myFragment.setArguments(args);
//        return myFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.collection_species_fragment, container, false);

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
                    //break;
                //case 4:
                    //data.add(new Species("Turtles","Testudines",i));
            }
        }

        return data;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.print("Third pager resume");
    }
}
