package edu.drury.mcs.wildlife.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import edu.drury.mcs.wildlife.JavaClass.myStepperIndicator;

import edu.drury.mcs.wildlife.JavaClass.NonSwipeableViewPager;
import edu.drury.mcs.wildlife.JavaClass.StepperAdapter;
import edu.drury.mcs.wildlife.R;

public class CreateCollection extends AppCompatActivity {
    public static NonSwipeableViewPager pager;
    private myStepperIndicator indicator;
    private StepperAdapter pagerAdapter;

    private final boolean pagerScrollEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Build Collection");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


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


}
