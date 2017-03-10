package edu.drury.mcs.wildlife.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.DB.MainCollectionTable;
import edu.drury.mcs.wildlife.DB.wildlifeDBHandler;
import edu.drury.mcs.wildlife.Fragment.AddDialog;
import edu.drury.mcs.wildlife.Fragment.Collection;
import edu.drury.mcs.wildlife.Fragment.CollectionSpecies;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int RESULT_OK = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get toolbar referene from layout and setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        MainCollectionObj currentMainColellection = new MainCollectionObj("2017 Collection","yma004@drury.edu");
        try {
            new MainCollecton2DB(this,currentMainColellection).execute("create").get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeholder, Collection.newInstance(currentMainColellection),"collection_fragment")
                .commit();

    }

    private class MainCollecton2DB extends AsyncTask<String, Void, MainCollectionObj> {
        private MainCollectionObj main_collection;
        private SQLiteDatabase db;
        private Context context;
        public MainCollecton2DB(Context context, MainCollectionObj _main) {
            this.main_collection = _main;
            this.context =context;
        }

        @Override
        protected MainCollectionObj doInBackground(String... params) {
            String method = params[0];

            switch (method) {
                case "create":
                    db = new wildlifeDBHandler(context).getWritableDatabase();

                    String[] projections = {
                            MainCollectionTable.MC_NAME
                    };

                    String selection = MainCollectionTable.MC_NAME + " = ?";
                    String[] selectionArgs = {main_collection.getMain_collection_name()};

                    Cursor cursor = db.query(
                            MainCollectionTable.TABLE_NAME,
                            projections,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );

                    if(cursor.getCount() == 0){
                        ContentValues values = new ContentValues();
                        values.put(MainCollectionTable.MC_NAME, main_collection.getMain_collection_name());
                        values.put(MainCollectionTable.MC_EMAIL, main_collection.getEmail());
                        db.insert(MainCollectionTable.TABLE_NAME, null, values);
                    }
                    cursor.close();

                    break;
                default:
                    break;
            }

            return null;
        }

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

        if(requestCode == AddDialog.STATIC_INTEGER_VALUE && resultCode == RESULT_OK) {
            //ready to pass new collection data to Collection fragment to add to list
            Message.showMessage(this,"new added collection is here");
            Collection cFrag = (Collection) getSupportFragmentManager().findFragmentByTag("collection_fragment");
            CollectionObj newC = data.getParcelableExtra(CollectionSpecies.SAVEDCOLLECTIONDATA);
            cFrag.addNewCollectionToList(newC);
        }
    }


}
