package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

public class SpeciesDataTable extends AppCompatActivity implements AsyncTaskCompleteListener<String>, SearchView.OnQueryTextListener {
    public static final String SAVEDSPECIESDATA = "edu.drury.mcs.wildlife.SAVEDSPECIESDATA";
    public static final String CURRENT_GROUP_ID = "edu.drury.mcs.wildlife.CURRENT_GROUP_ID";
    private static int REQUEST = 100;
    private RecyclerView tRecyclerView;
    private tAdapter tAdapter;
    private Species currentSpecies;
    private List<SpeciesCollected> data = new ArrayList<>();
    private String method = "getSpecies";
    private FloatingActionButton voice_search;
    private SearchView searchView;
    private Hashtable<String, SpeciesCollected> storedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_species_data_table);

        // get bundles
        currentSpecies = getIntent().getParcelableExtra(sAdapter.EXTRA_CURRENTSPECIES);
        // store existing sc data in to table with common name as key
        storedData = storeInHash(currentSpecies);

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

        voice_search = (FloatingActionButton) findViewById(R.id.voice_search);
        voice_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please Speak Up");
                startActivityForResult(i, REQUEST);
//                need to check if voice to text is available first
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
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

//    private List<SpeciesCollected> getSpeciesData(String json_string, Species currentSpecies) {
//        List<SpeciesCollected> data = new ArrayList<SpeciesCollected>();
//        String common_name;
//        String scientific_name;
//        JSONObject a;
//        JSONArray jsonArray;
//
//        try{
//            jsonArray = new JSONArray(json_string);
//            for(int i = 0; i < jsonArray.length(); i++) {
//                a = jsonArray.getJSONObject(i);
//                scientific_name = a.getString("scientific_name");
//                common_name = a.getString("common_name");
//                SpeciesCollected s = new SpeciesCollected(scientific_name, common_name);
//
//                if(storedData.get(s.getCommonName()) != null) {
//                    // s is already is already collected and switch with ours
//                    s = storedData.get(s.getCommonName());
//                }
//
//                data.add(s);
//            }
//
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//
//        return data;
//    }

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
    public void onTaskComplete(List<SpeciesCollected> animalData) {
        data = filteredData(animalData, storedData);
        //initilze and setup recycler view
        tAdapter.swap(data);
    }

    private List<SpeciesCollected> filteredData(List<SpeciesCollected> animalData, Hashtable<String, SpeciesCollected> storedData) {
        List<SpeciesCollected> filtered = new ArrayList<>();

        for (SpeciesCollected sc : animalData) {
            if(storedData.get(sc.getCommonName()) != null) {
                filtered.add(storedData.get(sc.getCommonName()));
            } else {
                filtered.add(sc);
            }
        }

        return filtered;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        List<SpeciesCollected> newList = new ArrayList<>();
        for(SpeciesCollected sc: data) {
            String s_name = sc.getScientificName().toLowerCase();
            String c_name = sc.getCommonName().toLowerCase();
            if(s_name.contains(newText) || c_name.contains(newText)) {
                newList.add(sc);
            }
        }

        tAdapter.setFilter(newList);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            searchView.setQuery(spokenText, false);
        }
    }
}
