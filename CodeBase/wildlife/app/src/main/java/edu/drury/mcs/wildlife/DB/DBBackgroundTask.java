package edu.drury.mcs.wildlife.DB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;

import static edu.drury.mcs.wildlife.R.id.collection_name;

/**
 * Created by mark93 on 2/21/2017.
 */

public class DBBackgroundTask extends AsyncTask<String,Void,List<CollectionObj>> {
    private Context context;
    private MainCollectionObj curent_main_collection;
    private CollectionObj newCollection;
    private wildlifeDB wildlifeDB;

    // READ ALL
    public DBBackgroundTask(Context context, MainCollectionObj _mainCollection) {
        this.context = context;
        this.curent_main_collection = _mainCollection;
        this.wildlifeDB = new wildlifeDB(context);
    }

    // CREATE (INSERT NEW)
    public DBBackgroundTask(Context context, MainCollectionObj _mainCollection, CollectionObj collection) {
        this(context, _mainCollection);
        this.newCollection = collection;
    }

    // UPDATE or DELETE
    public DBBackgroundTask(Context context, CollectionObj collection) {
        this.context = context;
        this.newCollection = collection;
        this.wildlifeDB = new wildlifeDB(context);
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<CollectionObj> doInBackground(String... params) {
        String method = params[0];
        List<CollectionObj> result = new ArrayList<>();

        switch (method) {
            case "create":
                if(newCollection != null) {
                    // call create method in wildlifeDB
                    wildlifeDB.createNewCollection(newCollection, curent_main_collection);
                }

                break;
            case "update":
                if(newCollection != null) {
                    // call update methid in wildlifeDB
                    wildlifeDB.updateCollection(newCollection);
                }
                break;
            case "read":
                // call read all collection in wildlifeDB
                Log.i("readTask", " I am in about to read all collection");
                result = wildlifeDB.getAllCollections(curent_main_collection);
                break;
            case "delete":
                if(newCollection != null) {
                    // call delete in wildlifeDB
                    wildlifeDB.deleteCollection(newCollection);
                    Log.i("deleteTask", collection_name + " is deleted");
                }
                break;
            case "group":
                break;
            default:
                break;
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Void... params) {
        super.onProgressUpdate(params);
    }

    @Override
    protected void onPostExecute(List<CollectionObj> params) {
        super.onPostExecute(params);
    }
}
