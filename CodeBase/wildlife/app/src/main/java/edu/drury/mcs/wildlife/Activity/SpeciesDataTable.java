package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.AsyncTaskCompleteListener;
import edu.drury.mcs.wildlife.JavaClass.BackgroundTask;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.sAdapter;
import edu.drury.mcs.wildlife.JavaClass.tAdapter;
import edu.drury.mcs.wildlife.R;

public class SpeciesDataTable extends AppCompatActivity implements AsyncTaskCompleteListener<String> {
    public static final String SAVEDSPECIESDATA = "edu.drury.mcs.wildlife.SAVEDSPECIESDATA";
    public static final String CURRENT_GROUP_ID = "edu.drury.mcs.wildlife.CURRENT_GROUP_ID";
    private RecyclerView tRecyclerView;
    private tAdapter tAdapter;
    private Species currentSpecies;
    private List<SpeciesCollected> data = new ArrayList<>();
    private String method = "getSpecies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_data_table);

        // get bundles
        currentSpecies = getIntent().getParcelableExtra(sAdapter.EXTRA_CURRENTSPECIES);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentSpecies.getCommonName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.delete_48);

        // send request and get callback through onTaskComplete
        new BackgroundTask(this, this).execute(method, Integer.toString(currentSpecies.getGroup_ID()));

        tRecyclerView = (RecyclerView) findViewById(R.id.data_table_recyclerview);
        tRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tAdapter = new tAdapter(this, data);
        tRecyclerView.setAdapter(tAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            Message.showMessage(this,"Closed");
            onBackPressed();
            return true;
        } else if(id == R.id.save){
            saveData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<SpeciesCollected> getSpeciesData(String json_string, Species currentSpecies) {
        List<SpeciesCollected> data = new ArrayList<SpeciesCollected>();
        String common_name;
        String scientific_name;
        JSONObject a;
        JSONArray jsonArray;
        Hashtable<String, SpeciesCollected> storedData = storeInHash(currentSpecies);

        try{
            jsonArray = new JSONArray(json_string);
            for(int i = 0; i < jsonArray.length(); i++) {
                a = jsonArray.getJSONObject(i);
                scientific_name = a.getString("scientific_name");
                common_name = a.getString("common_name");
                SpeciesCollected s = new SpeciesCollected(scientific_name, common_name);

                if(storedData.get(s.getCommonName()) != null) {
                    // s is already is already collected and switch with ours
                    s = storedData.get(s.getCommonName());
                }

                data.add(s);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return data;
    }

    // store collected species data into hashtable with common name as key
    private Hashtable<String, SpeciesCollected> storeInHash(Species currentSpecies) {
        Hashtable<String, SpeciesCollected> table = new Hashtable<>();

        for(SpeciesCollected sc: currentSpecies.getSpecies_Data()) {
            table.put(sc.getCommonName(), sc);
        }

        return table;
    }

    private void saveData() {
        List<SpeciesCollected> savedSpeciesData = tAdapter.getLatestItems();
//        tAdapter.notifyDataSetChanged();
//        for(SpeciesCollected s: savedSpeciesData) {
//            Message.showMessage(this,"common name: " + s.getCommonName());
//            Message.showMessage(this,"band number: " + s.getBand_num());
//            Message.showMessage(this, "number captured: "+ Integer.toString(s.getQuantity()));
//            Message.showMessage(this, "number removied: "+ Integer.toString(s.getNum_removed()));
//            Message.showMessage(this, "disposition status: " + s.getStatus().toString());
//            Message.showMessage(this,"is blood taken: " + Boolean.toString(s.getIs_blood_taken()));
//            Message.showMessage(this,"is specimen retained: " + Boolean.toString(s.getVoucher_specimen_retained()));
//
//        }
        Intent resultIntent = new Intent();
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(SAVEDSPECIESDATA, ((ArrayList) savedSpeciesData));
        resultBundle.putInt(CURRENT_GROUP_ID,currentSpecies.getGroup_ID());
        resultIntent.putExtras(resultBundle);
        setResult(CreateCollection.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onTaskComplete(String jsonm_result_string) {
        data = getSpeciesData(jsonm_result_string, currentSpecies);
        //initilze and setup recycler view
        tAdapter.swap(data);
    }

}
