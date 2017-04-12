package edu.drury.mcs.wildlife.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.JavaClass.viewEntryDataAdapter;
import edu.drury.mcs.wildlife.R;

public class ViewCollectionEntry extends AppCompatActivity {
    private int adapterPosition;
    private CollectionObj currentCollection;
    private TextView entry_name, entry_date, entry_location;
    private RecyclerView salamander_recyclerview, turtle_recyclerview, lizard_recyclerview, frog_recyclerview, snake_recyclerview;
    private viewEntryDataAdapter salamander_adapter, turtle_adapter, lizard_adapter, frog_adapter, snake_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_collection_entry);

        //get extras
        currentCollection = getIntent().getParcelableExtra(collectionAdapter.EXTRA_VIEW);
        adapterPosition = getIntent().getIntExtra(collectionAdapter.EXTRA_POSITION, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentCollection.getCollection_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.delete_48);

        setUpUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Message.showMessage(this,"Closed");
                onBackPressed();
                return true;
            case R.id.edit:
                // go to edit page
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpUI() {
        entry_name = (TextView) findViewById(R.id.entry_name);
        entry_date = (TextView) findViewById(R.id.entry_date);
        entry_location = (TextView) findViewById(R.id.entry_location);

        entry_name.setText(currentCollection.getCollection_name());
        entry_date.setText(currentCollection.getDate());
        if(!Objects.equals(currentCollection.getLocationUTM().trim(), "")) {
            entry_location.setText(currentCollection.getLocationUTM());
        } else {
            entry_location.setText("Latitude: " + currentCollection.getLocation().getLatitude() + "\n"
                    + "Longitude: " + currentCollection.getLocation().getLongitude());
        }

        setUpRecyclerviews();
    }

    private void setUpRecyclerviews() {
        salamander_recyclerview = (RecyclerView) findViewById(R.id.salamander_recyclerview);
        turtle_recyclerview = (RecyclerView) findViewById(R.id.turtle_recyclerview);
        lizard_recyclerview = (RecyclerView) findViewById(R.id.lizard_recyclerview);
        frog_recyclerview = (RecyclerView) findViewById(R.id.frog_recyclerview);
        snake_recyclerview = (RecyclerView) findViewById(R.id.snake_recyclerview);

        salamander_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        turtle_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        lizard_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        frog_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        snake_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        for (Species s: currentCollection.getSpecies()) {
            switch (s.getGroup_ID()) {
                case 1:
                    salamander_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    break;
                case 2:
                    frog_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    break;
                case 3:
                    lizard_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    break;
                case 4:
                    snake_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    break;
                case 5:
                    turtle_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    break;
                default:
                    break;
            }
        }

        salamander_recyclerview.setAdapter(salamander_adapter);
        turtle_recyclerview.setAdapter(turtle_adapter);
        lizard_recyclerview.setAdapter(lizard_adapter);
        frog_recyclerview.setAdapter(frog_adapter);
        snake_recyclerview.setAdapter(snake_adapter);
    }

}
