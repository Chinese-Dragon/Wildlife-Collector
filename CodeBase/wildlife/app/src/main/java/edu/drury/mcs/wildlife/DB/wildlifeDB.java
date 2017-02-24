package edu.drury.mcs.wildlife.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;

/**
 * Created by mark93 on 2/21/2017.
 */

public class wildlifeDB {
    private wildlifeDBHandler dbHandler;
    private SQLiteDatabase db;
    private Context context;
    ContentValues collection_table_values, species_table_values,has_species_table_values,speciesCollected_table_values;

    public wildlifeDB(Context context) {
        this.context = context;
        this.dbHandler = new wildlifeDBHandler(context);
        this.collection_table_values = new ContentValues();
        this.species_table_values = new ContentValues();
        this.has_species_table_values = new ContentValues();
        this.speciesCollected_table_values = new ContentValues();
    }


    //CREATE a new wildlife collection
    public void createNewCollection(CollectionObj newC) {
        this.db = dbHandler.getWritableDatabase();
        long c_id;
        long s_id;

        c_id = insert_collection(db,newC.getCollection_name(), newC.getDate(), newC.getLocation().getLatitude(), newC.getLocation().getLongitude());

        // insert data to speciesTable, get all species_ids, and associate with hasSpecies Table
        for (Species s : newC.getSpecies()) {
            s_id = insert_species(db,s.getCommonName(),s.getScientificName(),s.getGroup_ID());
            insert_hasSpecies(db, c_id, s_id);

            for (SpeciesCollected sc : s.getSpecies_Data()) {
                insert_speciesCollected(db, sc.getCommonName(), sc.getScientificName(), sc.getQuantity(), s_id);
            }
        }

        db.close();
    }

    //READ ALl wildlife Collection
    public List<CollectionObj> readAllCollection() {
        List<CollectionObj> collectionList = new ArrayList<>();
        this.db = dbHandler.getReadableDatabase();

        // define which colmn in table we want
        String[] collectionProjection = {
                CollectionTable.C_ID,
                CollectionTable.C_NAME,
                CollectionTable.C_DATE,
                CollectionTable.C_LAT,
                CollectionTable.C_LNG,
        };

        //setup cursor to hold query results
        Cursor cursor = db.query(
            CollectionTable.TABLE_NAME,
                collectionProjection,
                null,
                null,
                null,
                null,
                null
        );

        // using cursor to check each row in collection
        while (cursor.moveToNext()) {
            // start contructing new collection from db
            CollectionObj collection = new CollectionObj();
            Location loc = new Location("");

            long c_id = cursor.getLong( cursor.getColumnIndex(CollectionTable.C_ID));
            collection.setCollection_name( cursor.getString(cursor.getColumnIndex(CollectionTable.C_NAME)) );
            collection.setDate( cursor.getString(cursor.getColumnIndex(CollectionTable.C_DATE)) );

            loc.setLatitude( cursor.getDouble(cursor.getColumnIndex(CollectionTable.C_LAT)) );
            loc.setLongitude( cursor.getDouble(cursor.getColumnIndex(CollectionTable.C_LNG)) );
            collection.setLocation(loc);

            // to contruct list of species with known c_id
            collection.setSpecies(constructSpeciesList(db, c_id));

            collectionList.add(collection);
        }

        cursor.close();
        db.close();

        return collectionList;
    }

    //UPDATE certain wildlife Collection


    //DELETE certain wildlife Collection


    /*
        Helper functions (CRUD) for collection Table
     */
    private long insert_collection(SQLiteDatabase db, String name, String date, Double lat, Double lng) {
        long id = 0;
        collection_table_values.clear();

        // insert info to collection table first
        collection_table_values.put(CollectionTable.C_NAME, name);
        collection_table_values.put(CollectionTable.C_DATE, date);
        collection_table_values.put(CollectionTable.C_LAT, lat);
        collection_table_values.put(CollectionTable.C_LNG, lng);
        try {
            id = db.insert(CollectionTable.TABLE_NAME, null, collection_table_values);
        } catch (SQLException e ) {
            e.printStackTrace();
        }

        return id;
    }


    /*
        Helper functions (CRUD) for species Table
     */
    private long insert_species(SQLiteDatabase db, String common, String scientific, int group_id) {
        long id = 0;
        species_table_values.clear();

        // insert info to species
        species_table_values.put(SpeciesTable.S_CNAME, common);
        species_table_values.put(SpeciesTable.S_SNAME, scientific);
        species_table_values.put(SpeciesTable.S_GROUPID, group_id);
        try{
            id = db.insert(SpeciesTable.TABLE_NAME, null, species_table_values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    private Species constructSingleSpecies(SQLiteDatabase db, long s_id) {
        Species s = new Species();

        String[] projection = {
                SpeciesTable.S_ID,
                SpeciesTable.S_CNAME,
                SpeciesTable.S_SNAME,
                SpeciesTable.S_GROUPID
        };

        String selection = SpeciesTable.S_ID + " = ?";
        String[] selectionArg = {Long.toString(s_id)};

        Cursor cursor = db.query(
                SpeciesTable.TABLE_NAME,
                projection,
                selection,
                selectionArg,
                null,
                null,
                null
        );

        // should just retrn one row
        while (cursor.moveToNext()) {
            s.setCommonName( cursor.getString(cursor.getColumnIndex(SpeciesTable.S_CNAME)) );
            s.setScientificName( cursor.getString(cursor.getColumnIndex(SpeciesTable.S_SNAME)) );
            s.setGroup_ID( cursor.getInt(cursor.getColumnIndex(SpeciesTable.S_GROUPID)));
            s.setSpecies_Data(constructSpeciesCollectedList(db, s_id));
        }
        cursor.close();

        return s;
    }


    /*
        Helper functions (CRUD) for speciesCollection Table
     */
    private void insert_speciesCollected(SQLiteDatabase db, String common, String scientific, int quantity, long s_id ) {
        speciesCollected_table_values.clear();

        speciesCollected_table_values.put(SpeciesCollectedTable.SC_CNAME, common);
        speciesCollected_table_values.put(SpeciesCollectedTable.SC_SNAME, scientific);
        speciesCollected_table_values.put(SpeciesCollectedTable.SC_QUANTITY, quantity);
        speciesCollected_table_values.put(SpeciesCollectedTable.SCSPECIES_ID, s_id);
        try {
            db.insertOrThrow(SpeciesCollectedTable.TABLE_NAME, null, speciesCollected_table_values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<SpeciesCollected> constructSpeciesCollectedList(SQLiteDatabase db, long s_id) {
        List<SpeciesCollected> scList = new ArrayList<>();

        String[] projection = {
            SpeciesCollectedTable.SC_CNAME,
                SpeciesCollectedTable.SC_SNAME,
                SpeciesCollectedTable.SC_QUANTITY
        };

        String selection = SpeciesCollectedTable.SCSPECIES_ID + " = ?";
        String[] selectionArgs = {Long.toString(s_id)};

        Cursor cursor = db.query(
                SpeciesCollectedTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // this should return 0 - N rows of data since normally species will have many collected species data
        while (cursor.moveToNext()) {
            SpeciesCollected sc = new SpeciesCollected();
            sc.setCommonName( cursor.getString(cursor.getColumnIndex(SpeciesCollectedTable.SC_CNAME)) );
            sc.setScientificName( cursor.getString(cursor.getColumnIndex(SpeciesCollectedTable.SC_SNAME)) );
            sc.setQuantity( cursor.getInt(cursor.getColumnIndex(SpeciesCollectedTable.SC_QUANTITY)) );

            scList.add(sc);
        }
        cursor.close();

        return scList;
    }


    /*
        Helper functions for hasSpecies Table
     */
    private void insert_hasSpecies(SQLiteDatabase db, long c_id, long s_id) {
        has_species_table_values.clear();

        has_species_table_values.put(HasSpeciesTable.FK_C_ID, c_id);
        has_species_table_values.put(HasSpeciesTable.FK_S_ID, s_id);
        try{
            db.insert(HasSpeciesTable.TABLE_NAME, null, has_species_table_values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Species> constructSpeciesList(SQLiteDatabase db, long c_id) {
        List<Species> sList = new ArrayList<>();

        String[] hasSpeciesProjection = {
                HasSpeciesTable.FK_S_ID
        };

        // filter -- where clause
        String selection = HasSpeciesTable.FK_C_ID + " = ?";
        String[] selectionArgs = {Long.toString(c_id)};

        Cursor cursor = db.query(
                HasSpeciesTable.TABLE_NAME,
                hasSpeciesProjection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // now we should have result of all s_ids associate with c_id
        while (cursor.moveToNext()) {
            long s_id = cursor.getLong(cursor.getColumnIndex(HasSpeciesTable.FK_S_ID));
            Species single_species = constructSingleSpecies(db, s_id);
            sList.add(single_species);
        }
        cursor.close();

        return sList;
    }
}
