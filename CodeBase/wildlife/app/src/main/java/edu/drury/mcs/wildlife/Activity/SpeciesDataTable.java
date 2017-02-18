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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.JavaClass.BackgroundTask;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.JavaClass.tAdapter;
import edu.drury.mcs.wildlife.R;

public class SpeciesDataTable extends AppCompatActivity implements View.OnClickListener {
    public static final String SAVEDSPECIESDATA = "edu.drury.mcs.wildlife.SAVEDSPECIESDATA";
    public static final String CURRENT_GROUP_ID = "edu.drury.mcs.wildlife.CURRENT_GROUP_ID";
    private RecyclerView tRecyclerView;
    private tAdapter tAdapter;
    private Species currentSpecies;
    private Button cancel,save;
    private List<SpeciesCollected> data;
    private BackgroundTask backgroundTask;
    private String JSON_STRING;
    private String method = "getSpecies";

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

        // send request and get json string data
        backgroundTask = new BackgroundTask(this);
        try{
            JSON_STRING = backgroundTask.execute(method,Integer.toString(currentSpecies.getGroup_ID())).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        data = getData(JSON_STRING);

        //initilze and setup recycler view
        tRecyclerView = (RecyclerView) findViewById(R.id.data_table_recyclerview);
        tRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tAdapter = new tAdapter(this, data);
        tRecyclerView.setAdapter(tAdapter);

        //buttons
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

    private List<SpeciesCollected> getData(String json_string) {
        List<SpeciesCollected> data = new ArrayList<SpeciesCollected>();
        String common_name;
        String scientific_name;
        JSONObject a;
        JSONArray jsonArray;

        try{
            jsonArray = new JSONArray(JSON_STRING);
            for(int i = 0; i < jsonArray.length(); i++) {
                a = jsonArray.getJSONObject(i);
                scientific_name = a.getString("scientific_name");
                common_name = a.getString("common_name");
                SpeciesCollected s = new SpeciesCollected(scientific_name,common_name);
                data.add(s);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return data;
    }


    @Override
    public void onClick(View view) {
        if (view == cancel) {
            onBackPressed();
        } else if (view == save) {
            List<SpeciesCollected> savedSpeciesData = tAdapter.getLatestItems();
            for(SpeciesCollected s: savedSpeciesData) {
                Message.showMessage(this,s.getCommonName());
            }
            Intent resultIntent = new Intent();
            Bundle resultBundle = new Bundle();
            resultBundle.putParcelableArrayList(SAVEDSPECIESDATA, ((ArrayList) savedSpeciesData));
            resultBundle.putInt(CURRENT_GROUP_ID,currentSpecies.getGroup_ID());
            resultIntent.putExtras(resultBundle);
            setResult(CreateCollection.RESULT_OK, resultIntent);
            finish();
        }
    }
}
