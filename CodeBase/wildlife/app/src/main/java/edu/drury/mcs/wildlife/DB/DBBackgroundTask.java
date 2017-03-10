package edu.drury.mcs.wildlife.DB;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;

/**
 * Created by mark93 on 2/21/2017.
 */

public class DBBackgroundTask extends AsyncTask<String,Void,List<CollectionObj>> {
    private Context context;
    private MainCollectionObj curent_main_collection;
    private CollectionObj newCollection;
    private String collection_name;
    private wildlifeDB wildlifeDB;

    // READ ALL
    public DBBackgroundTask(Context context, MainCollectionObj _mainCollection) {
        this.context = context;
        this.curent_main_collection = _mainCollection;
        this.wildlifeDB = new wildlifeDB(context);
    }

    // CREATE (INSERT NEW) or UPDATE WITH NEW DATA
    public DBBackgroundTask(Context context, MainCollectionObj _mainCollection, CollectionObj collection) {
        this(context, _mainCollection);
        this.newCollection = collection;
    }

    // DELETE SINGLE
    public DBBackgroundTask(Context context, MainCollectionObj _mainCollection, String collectionName) {
        this(context,_mainCollection);
        this.collection_name = collectionName;
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

                }
                break;
            case "read":
                // call read all collection in wildlifeDB
                Log.i("readTask", " I am in about to read all collection");
                result = wildlifeDB.getAllCollections(curent_main_collection);
                break;
            case "delete":
                if(collection_name != null) {
                    // call delete in wildlifeDB
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
