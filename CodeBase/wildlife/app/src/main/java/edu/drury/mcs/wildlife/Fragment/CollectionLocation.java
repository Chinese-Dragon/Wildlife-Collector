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
public class CollectionLocation extends Fragment implements View.OnClickListener {
    private View layout;
    private Button back,cancel,next;

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
//    public static CollectionLocation newInstance(myStepperIndicator indicator, NonSwipeableViewPager pager /* 3rd parameter will be collection data*/) {
//        CollectionLocation myFragment = new CollectionLocation();
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
        layout = inflater.inflate(R.layout.collection_location_fragment, container, false);
        back = (Button) layout.findViewById(R.id.back);
        cancel = (Button) layout.findViewById(R.id.cancel);
        next = (Button) layout.findViewById(R.id.next);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            CreateCollection.pager.setCurrentItem(0);
        } else if (view == cancel) {
            getActivity().finish();
        } else if (view == next) {
            CreateCollection.pager.setCurrentItem(2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.print("Second pager resume");
    }
}
