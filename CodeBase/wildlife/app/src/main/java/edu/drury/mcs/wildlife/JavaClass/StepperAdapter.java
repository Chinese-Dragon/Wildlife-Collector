package edu.drury.mcs.wildlife.JavaClass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.drury.mcs.wildlife.Fragment.CollectionDate;
import edu.drury.mcs.wildlife.Fragment.CollectionLocation;
import edu.drury.mcs.wildlife.Fragment.CollectionSpecies;

/**
 * Created by mark93 on 12/11/2016.
 */

public class StepperAdapter extends FragmentPagerAdapter {
    static final int NUM_ITEMS = 3;

    public StepperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;

        if (position == 0) {
            frag = new CollectionDate();
        }

        if (position == 1) {
            frag = new CollectionLocation();
        }

        if (position == 2) {
            frag = new CollectionSpecies();
        }

        return frag;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
