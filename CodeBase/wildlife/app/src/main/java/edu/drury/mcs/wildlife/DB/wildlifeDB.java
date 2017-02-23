package edu.drury.mcs.wildlife.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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


    //UPDATE certain wildlife Collection


    //DELETE certain wildlife Collection


    /*
        Helper functions (CRUD) for collection Table
     */
    public long insert_collection(SQLiteDatabase db, String name, String date, Double lat, Double lng) {
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
    public long insert_species(SQLiteDatabase db, String common, String scientific, int group_id) {
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


    /*
        Helper functions (CRUD) for speciesCollection Table
     */
    public void insert_speciesCollected(SQLiteDatabase db, String common, String scientific, int quantity, long s_id ) {
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


    /*
        Helper functions for hasSpecies Table
     */
    public void insert_hasSpecies(SQLiteDatabase db, long c_id, long s_id) {
        has_species_table_values.clear();

        has_species_table_values.put(HasSpeciesTable.FK_C_ID, c_id);
        has_species_table_values.put(HasSpeciesTable.FK_S_ID, s_id);
        try{
            db.insert(HasSpeciesTable.TABLE_NAME, null, has_species_table_values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
