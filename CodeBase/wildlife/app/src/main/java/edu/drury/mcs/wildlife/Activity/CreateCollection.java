package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.drury.mcs.wildlife.Fragment.AddDialog;
import edu.drury.mcs.wildlife.Fragment.CollectionLocation;
import edu.drury.mcs.wildlife.Fragment.CollectionSpecies;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.NonSwipeableViewPager;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.StepperAdapter;
import edu.drury.mcs.wildlife.JavaClass.ViewpagerFragmentLifecycle;
import edu.drury.mcs.wildlife.JavaClass.myStepperIndicator;
import edu.drury.mcs.wildlife.R;

public class CreateCollection extends AppCompatActivity implements OnDataPassListener {
    public static final int RESULT_OK = 202;
    public static NonSwipeableViewPager pager;
    private myStepperIndicator indicator;
    private StepperAdapter pagerAdapter;
    private CollectionObj currentCollection;

    private final boolean pagerScrollEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Build Collection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // received collection we are working on from AddDialog
        currentCollection = (CollectionObj) getIntent().getExtras().getParcelable(AddDialog.EXTRA_CURRENTCOLLECTION);

        // get references of pager and indicator from layout
        pager = (NonSwipeableViewPager) findViewById(R.id.pager);
        pager.setPagingEnabled(pagerScrollEnabled);
        indicator = (myStepperIndicator) findViewById(R.id.stepper);

        //initialize and setup pageradaper
        //NOTE: pagerAdapter: Controller; pager: View
        pagerAdapter = new StepperAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currrentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // dont need to use that right now
            }

            @Override
            public void onPageSelected(int newPosition) {
                ViewpagerFragmentLifecycle fragmentToShow = (ViewpagerFragmentLifecycle) pagerAdapter.getItem(newPosition);
                fragmentToShow.onResumeFragment();

                ViewpagerFragmentLifecycle fragmentToHide = (ViewpagerFragmentLifecycle) pagerAdapter.getItem(currrentPosition);
                fragmentToHide.onPauseFragment();

                currrentPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // dont need to use that right now
            }
        });
        indicator.setViewPager(pager);

    }

    @Override
    public void onBackPressed() {
        int currentPage = 0;
        currentPage = pager.getCurrentItem();

        if (currentPage > 0) {
            pager.setCurrentItem(--currentPage);
        }
    }

    public CollectionObj getCurrentCollection() {
        return currentCollection;
    }

    public void setCurrentCollection(CollectionObj currentCollection) {
        this.currentCollection = currentCollection;
    }

    /*
            callback function from fragments to pass data back to activity
         */
    @Override
    public void onDataPass(CollectionObj collection, int nextPosition) {
        setCurrentCollection(collection);
        if(nextPosition == 1) {
            CollectionLocation location = (CollectionLocation) pagerAdapter.getItem(nextPosition);
            location.setCurrentCollection(collection);
        } else if(nextPosition == 2) {
            CollectionSpecies species = (CollectionSpecies) pagerAdapter.getItem(nextPosition);
            species.setCurrentCollection(collection);
        }
        pager.setCurrentItem(nextPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
