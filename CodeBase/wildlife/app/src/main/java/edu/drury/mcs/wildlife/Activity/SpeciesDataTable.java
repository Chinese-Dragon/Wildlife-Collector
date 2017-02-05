package edu.drury.mcs.wildlife.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.R;

public class SpeciesDataTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_data_table);

        // get bundles
        int groupID = getIntent().getExtras().getInt(sAdapter.EXTRA_SPECIESID);
        String commonName = getIntent().getExtras().getString(sAdapter.EXTRA_SPECIESNAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(commonName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
