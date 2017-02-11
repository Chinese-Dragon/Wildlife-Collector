package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.JavaClass.tAdapter;
import edu.drury.mcs.wildlife.R;

public class SpeciesDataTable extends AppCompatActivity implements View.OnClickListener {
    private static final String SAVEDSPECIESDATA = "edu.drury.mcs.wildlife.SAVEDSPECIESDATA";
    private RecyclerView tRecyclerView;
    private tAdapter tAdapter;
    private Species currentSpecies;
    private Button cancel,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_data_table);

        // get bundles
        currentSpecies = (Species) getIntent().getExtras().getParcelable(sAdapter.EXTRA_CURRENTSPECIES);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentSpecies.getCommonName());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initilze and setup recycler view
        tRecyclerView = (RecyclerView) findViewById(R.id.data_table_recyclerview);
        tRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tAdapter = new tAdapter(this, getData(currentSpecies.getGroup_ID()));
        tRecyclerView.setAdapter(tAdapter);

//        //buttons
        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.done);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Message.showMessage(this,"Pressed back");
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<SpeciesCollected> getData(int groupId) {
        List<SpeciesCollected> data = new ArrayList<SpeciesCollected>();
        switch (groupId) {
            case 0:
                data.add(new SpeciesCollected("Siren intermedia", "Western Lesser Siren"));
                data.add(new SpeciesCollected("Cryptobranchus", "Hellbender"));
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }

        return data;
    }

    @Override
    public void onClick(View view) {
        if (view == cancel) {
            onBackPressed();
        } else if (view == save) {
            List<SpeciesCollected> savedSpeciesData = tAdapter.getLastestItems();
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra(SAVEDSPECIESDATA, ((ArrayList) savedSpeciesData));
            setResult(CreateCollection.RESULT_OK, resultIntent);
            finish();
        }
    }
}
