package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import edu.drury.mcs.wildlife.Fragment.AddDialog;
import edu.drury.mcs.wildlife.Fragment.CollectionLocation;
import edu.drury.mcs.wildlife.Fragment.CollectionSpecies;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.NonSwipeableViewPager;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.StepperAdapter;
import edu.drury.mcs.wildlife.JavaClass.myStepperIndicator;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // received collection we are working on from AddDialog.java
        currentCollection = (CollectionObj) getIntent().getParcelableExtra(AddDialog.EXTRA_CURRENTCOLLECTION);

        // get references of pager and indicator from layout
        pager = (NonSwipeableViewPager) findViewById(R.id.pager);
        pager.setPagingEnabled(pagerScrollEnabled);
        indicator = (myStepperIndicator) findViewById(R.id.stepper);

        //initialize and setup pageradaper
        //NOTE: pagerAdapter: Controller; pager: View
        pagerAdapter = new StepperAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
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
            CollectionLocation locationFrag = (CollectionLocation) pagerAdapter.getItem(nextPosition);
            locationFrag.setCurrentCollection(collection);
        } else if(nextPosition == 2) {
            CollectionSpecies speciesFrag = (CollectionSpecies) pagerAdapter.getItem(nextPosition);
            speciesFrag.setCurrentCollection(collection);
        }
        pager.setCurrentItem(nextPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case sAdapter.STATIC_INTEGER_VALUE:
                if(resultCode == RESULT_OK){
                    List<SpeciesCollected> newData = data.getExtras().getParcelableArrayList(SpeciesDataTable.SAVEDSPECIESDATA);
                    int group_id = data.getExtras().getInt(SpeciesDataTable.CURRENT_GROUP_ID);
                    updateCurrentCollection(newData,group_id);
                    CollectionSpecies speciesFrag = (CollectionSpecies) pagerAdapter.getItem(2);
                    speciesFrag.setCurrentCollection(currentCollection);
                }
                break;
            default:
                break;
        }
    }

    private void updateCurrentCollection(List<SpeciesCollected> newSpeciesData, int group_id) {
        for(Species s : currentCollection.getSpecies()) {
            if (s.getGroup_ID() == group_id) {
                // then we just update this species data
                s.setSpecies_Data(newSpeciesData);
            }
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
