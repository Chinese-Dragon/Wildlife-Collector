package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.drury.mcs.wildlife.DB.wildlifeDB;
import edu.drury.mcs.wildlife.Fragment.AddDialog;
import edu.drury.mcs.wildlife.Fragment.Collection;
import edu.drury.mcs.wildlife.Fragment.CollectionSpecies;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String CURRENT_EMAIL = "edu.drury.mcs.wildlife.CURRENT_EMAIL";
    public static final int RESULT_OK = 202;
    private List<MainCollectionObj> mainCollectionList;
    private MainCollectionObj currentMainCollection;
    private Toolbar toolbar;

    // need a sharepreference to store which current main collection is displaying
    // store as mainCollection email
    // when user swtich anther main collection, we change shared perefence to the corresping email
    // when enter from splash screen, we initialize the fragment using the current main collection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainCollectionList = new ArrayList<>();
        currentMainCollection = new MainCollectionObj();
        // get extra to see if it comes from splash or main screen
        String caller = getIntent().getStringExtra("caller");

        if(Objects.equals(caller, "firstmaincollectionscreen")) {
            Message.showMessage(this, "I am from main screen");

            // get email and name from extra
            String email = getIntent().getStringExtra("email");
            String name = getIntent().getStringExtra("name");

            currentMainCollection = new MainCollectionObj(name, email);
            // create new main collection from intent and add to data list
            mainCollectionList.add(currentMainCollection);

            // change sharepreference so that main screen wont show up next time
            SharedPreferences shared = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putBoolean(SplashScreen.CHECK_IF_NEED_SCREEN, false);
            editor.apply();

        } else if (Objects.equals(caller, "splash")){
            Message.showMessage(this, "I am from splash");

            // get maincollection data from sqlite and assign to data list
            wildlifeDB wildlifeDB = new wildlifeDB(this);
            mainCollectionList = wildlifeDB.getMainCollectionList();
            wildlifeDB.closeDBConnection();

            // set current main collection according to preference (sort it)
            SharedPreferences sharedP = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            String current_email = sharedP.getString(CURRENT_EMAIL, "");

            for(MainCollectionObj mcObj: mainCollectionList) {
                if(Objects.equals(mcObj.getEmail(), current_email)) {
                    currentMainCollection = mcObj;
                }
            }
        }

        //get toolbar referene from layout and setup toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set up navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //initialize fragment
        currentMainCollection.saveToDB(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeholder, Collection.newInstance(currentMainCollection),"collection_fragment")
                .commit();

        getSupportActionBar().setTitle(currentMainCollection.getMain_collection_name());

        // set the preference to the current main collection
        SharedPreferences sharedP = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedP.edit();
        editor.putString(CURRENT_EMAIL, currentMainCollection.getEmail());
        editor.apply();
    }

    public List<MainCollectionObj> getMainCollectionList() {
        return mainCollectionList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Collection cFrag = (Collection) getSupportFragmentManager().findFragmentByTag("collection_fragment");

        if(requestCode == AddDialog.STATIC_INTEGER_VALUE && resultCode == RESULT_OK) {
            //ready to pass new collection data to Collection fragment to add to list
            Message.showMessage(this,"new added collection is here");

            CollectionObj newC = data.getParcelableExtra(CollectionSpecies.SAVEDCOLLECTIONDATA);
            cFrag.addNewCollectionToList(newC);
        } else if (requestCode == collectionAdapter.STATIC_INTEGER_VALUE && resultCode == RESULT_OK) {
            // receive updated collection(entry)
            Message.showMessage(this,"Updated collection is here");
            CollectionObj updateC = data.getParcelableExtra(ViewAndUpdateCollectionEntry.UPDATEDCOLLECTIONDATA);
            int adapterPosition = data.getIntExtra(ViewAndUpdateCollectionEntry.CURRENTADAPTERPOSITION, -1);
            cFrag.updateCollectionList(updateC, adapterPosition);
        }
    }


}
