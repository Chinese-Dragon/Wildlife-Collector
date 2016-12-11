package edu.drury.mcs.wildlife.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionDate extends Fragment implements View.OnClickListener {
//    public final static String EXTRA_INDICATOR = "edu.drury.mcs.wildlife.INDICATOR";
//    public final static String EXTRA_PAGER = "edu.drury.mcs.wildlife.PAGER";
    private View layout;
    private Button next,cancel;
//
//    /**
//     * Instead of handing over potential parameters via constructor,
//     * use the newInstance(...) method and the Bundle for handing over parameters.
//     * This way if detached and re-attached the object state can be stored through the arguments.
//     * Much like Bundles attached to Intents
//     */
//    public static CollectionDate newInstance(myStepperIndicator indicator, NonSwipeableViewPager pager /* 3rd parameter will be collection data*/) {
//        CollectionDate myFragment = new CollectionDate();
//
//        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_INDICATOR,indicator);
//        args.putSerializable(EXTRA_PAGER, pager);
//
//        myFragment.setArguments(args);
//        return myFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.collection_date_fragment,container,false);
        // Inflate the layout for this fragment

        // deal with buttons
        cancel = (Button) layout.findViewById(R.id.cancel);
        next = (Button) layout.findViewById(R.id.next);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == cancel) {
            getActivity().finish();
        } else if (view == next) {
            CreateCollection.pager.setCurrentItem(1);
        }
    }
}
