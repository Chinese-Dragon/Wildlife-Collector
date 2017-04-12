package edu.drury.mcs.wildlife.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.MainCollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;

import static android.content.ContentValues.TAG;

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
    public void createNewCollection(CollectionObj newC, MainCollectionObj current_mainC) {
        this.db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CollectionTable.C_NAME, newC.getCollection_name());
        values.put(CollectionTable.C_DATE, newC.getDate());
        values.put(CollectionTable.C_LAT, newC.getLocation().getLatitude());
        values.put(CollectionTable.C_LNG, newC.getLocation().getLongitude());
        values.put(CollectionTable.C_UTM, newC.getLocationUTM());

        //find and add foreign key
        String[] projection = {MainCollectionTable.MC_ID};
        String selection = MainCollectionTable.MC_NAME + " = ?";
        String[] selectionArgs = {current_mainC.getMain_collection_name()};
        Cursor cursor = db.query(MainCollectionTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null,null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MainCollectionTable.MC_ID));
            values.put(CollectionTable.CMC_ID, id);
        }
        cursor.close();
        long collection_id = db.insert(CollectionTable.TABLE_NAME, null, values);

        // insert speciesCollectedTable
        for(Species s: newC.getSpecies()) {
            int group_id = s.getGroup_ID();
            for(SpeciesCollected sc: s.getSpecies_Data()) {
                ContentValues sc_values = new ContentValues();
                sc_values.put(SpeciesCollectedTable.SC_CNAME, sc.getCommonName());
                sc_values.put(SpeciesCollectedTable.SC_SNAME, sc.getScientificName());
                sc_values.put(SpeciesCollectedTable.SC_QUANTITY, sc.getQuantity());
                sc_values.put(SpeciesCollectedTable.SC_NUM_REMOVED, sc.getNum_removed());
                sc_values.put(SpeciesCollectedTable.SC_NUM_RELEASED, sc.getNum_released());
                sc_values.put(SpeciesCollectedTable.SC_BAND_NUM , sc.getBand_num());
                sc_values.put(SpeciesCollectedTable.SC_VS_RETAINED, sc.getVoucher_specimen_retained() ? 1:0);
                sc_values.put(SpeciesCollectedTable.SC_BLOOD_TAKEN, sc.getIs_blood_taken() ? 1:0);
                sc_values.put(SpeciesCollectedTable.SC_STATUS, SpeciesCollected.disposition_map.get(sc.getStatus()));
                sc_values.put(SpeciesCollectedTable.SCC_ID, collection_id);
                sc_values.put(SpeciesCollectedTable.SCGROUP_ID, group_id);
                db.insert(SpeciesCollectedTable.TABLE_NAME, null, sc_values);
            }
        }
        Log.i(TAG," Finish inserting single collection data");
        db.close();
    }

    // READ all collections related to the main collection
    public List<CollectionObj> getAllCollections(MainCollectionObj current_mainC) {
        List<CollectionObj> results = new LinkedList<>();
        this.db = dbHandler.getReadableDatabase();
        long main_collection_id = 0;

        //find main collection primary key
        String[] projection = {MainCollectionTable.MC_ID};
        String selection = MainCollectionTable.MC_NAME + " = ?";
        String[] selectionArgs = {current_mainC.getMain_collection_name()};
        Cursor cursor = db.query(MainCollectionTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null,null);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            main_collection_id = cursor.getLong(cursor.getColumnIndexOrThrow(MainCollectionTable.MC_ID));
        }
        cursor.close();

        //handle collection table using main_collection_id
        String[] c_proj = {CollectionTable.C_ID, CollectionTable.C_NAME, CollectionTable.C_DATE, CollectionTable.C_LAT, CollectionTable.C_LNG, CollectionTable.C_UTM};
        String c_selection = CollectionTable.CMC_ID + " = ?";
        String[] c_selectionArgs = {Long.toString(main_collection_id)};
        Cursor c_cursor = db.query(CollectionTable.TABLE_NAME, c_proj, c_selection, c_selectionArgs, null, null, null);

        //start assemble each row to a collectionObj
        while(c_cursor.moveToNext()) {
            long c_id = c_cursor.getLong(c_cursor.getColumnIndexOrThrow(CollectionTable.C_ID));
            String name = c_cursor.getString(c_cursor.getColumnIndexOrThrow(CollectionTable.C_NAME));
            String c_date = c_cursor.getString(c_cursor.getColumnIndexOrThrow(CollectionTable.C_DATE));
            Double lat = c_cursor.getDouble(c_cursor.getColumnIndexOrThrow(CollectionTable.C_LAT));
            Double lng = c_cursor.getDouble(c_cursor.getColumnIndexOrThrow(CollectionTable.C_LNG));
            Location location = new Location("");
            location.setLatitude(lat);
            location.setLongitude(lng);
            String utmLocation = c_cursor.getString(c_cursor.getColumnIndexOrThrow(CollectionTable.C_UTM));

            //need list of species data
            results.add(new CollectionObj(c_id , name, c_date, location, utmLocation, getSpeciesList(db, c_id)));
        }

        db.close();
        return results;
    }

    // DELETE
    public void deleteCollection(CollectionObj collection2Delete) {
        Log.i("info", "I am in delete collection process");

        this.db = dbHandler.getWritableDatabase();
        long target_C_id = collection2Delete.getId();

        //delete collection
        String deleteCollection_selection = CollectionTable.C_ID + " LIKE ?";
        String[] deleteC_selectionArgs = {Long.toString(target_C_id)};
        db.delete(CollectionTable.TABLE_NAME, deleteCollection_selection, deleteC_selectionArgs);

        //delete collection species of this collection
        String deleteSpeciesCollected_selection = SpeciesCollectedTable.SCC_ID + " LIKE ?";
        String[] deleteSC_selectionArgs = {Long.toString(target_C_id)};
        db.delete(SpeciesCollectedTable.TABLE_NAME, deleteSpeciesCollected_selection, deleteSC_selectionArgs);

        db.close();

    }

    // UPDATE
    public void updateCollection(CollectionObj updatedC) {
        Log.i("info", "I am in update collection process");
        this.db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();

        // new values for columns
        values.put(CollectionTable.C_NAME, updatedC.getCollection_name());
        values.put(CollectionTable.C_DATE, updatedC.getDate());
        values.put(CollectionTable.C_LAT, updatedC.getLocation().getLatitude());
        values.put(CollectionTable.C_LNG, updatedC.getLocation().getLongitude());
        values.put(CollectionTable.C_UTM, updatedC.getLocationUTM());

        // select
        String selection = CollectionTable.C_ID + " LIKE ?";
        String[] selectionArgs = {Long.toString(updatedC.getId())};

        // update
        db.update(CollectionTable.TABLE_NAME, values, selection, selectionArgs);

        // update collected_species by different group
        for (Species s: updatedC.getSpecies()) {
            int group_id = s.getGroup_ID();
            for(SpeciesCollected sc: s.getSpecies_Data()) {
                ContentValues sc_values = new ContentValues();
                sc_values.put(SpeciesCollectedTable.SC_CNAME, sc.getCommonName());
                sc_values.put(SpeciesCollectedTable.SC_SNAME, sc.getScientificName());
                sc_values.put(SpeciesCollectedTable.SC_QUANTITY, sc.getQuantity());
                sc_values.put(SpeciesCollectedTable.SC_NUM_REMOVED, sc.getNum_removed());
                sc_values.put(SpeciesCollectedTable.SC_NUM_RELEASED, sc.getNum_released());
                sc_values.put(SpeciesCollectedTable.SC_BAND_NUM , sc.getBand_num());
                sc_values.put(SpeciesCollectedTable.SC_VS_RETAINED, sc.getVoucher_specimen_retained() ? 1:0);
                sc_values.put(SpeciesCollectedTable.SC_BLOOD_TAKEN, sc.getIs_blood_taken() ? 1:0);
                sc_values.put(SpeciesCollectedTable.SC_STATUS, SpeciesCollected.disposition_map.get(sc.getStatus()));

                String selection_sc = SpeciesCollectedTable.SCC_ID + " LIKE ? AND "
                        + SpeciesCollectedTable.SCGROUP_ID + " LIKE ?";
                String[] selection_scArgs = {Long.toString(updatedC.getId()), Integer.toString(group_id)};
                db.update(SpeciesCollectedTable.TABLE_NAME, sc_values, selection_sc, selection_scArgs);
            }
        }
        db.close();
    }

    private List<Species> getSpeciesList(SQLiteDatabase db, Long c_id) {
        List<Species> data = new LinkedList<>();

        String[] g_projection = {GroupMappingTable.GM_ID, GroupMappingTable.GM_CNAME, GroupMappingTable.GM_SNAME};

        Cursor g_cursor = db.query(GroupMappingTable.TABLE_NAME, g_projection, null,null,null,null,null,null);
        while (g_cursor.moveToNext()) {
            int group_id = g_cursor.getInt(g_cursor.getColumnIndexOrThrow(GroupMappingTable.GM_ID));
            String c_name = g_cursor.getString(g_cursor.getColumnIndexOrThrow(GroupMappingTable.GM_CNAME));
            String s_name = g_cursor.getString(g_cursor.getColumnIndexOrThrow(GroupMappingTable.GM_SNAME));

            //need list of speciesCollected
            data.add(new Species(s_name, c_name, group_id, getSpeciesCollectedList(db, c_id, group_id)));
        }

        return data;
    }

    private List<SpeciesCollected> getSpeciesCollectedList(SQLiteDatabase db, Long c_id, int group_id) {
        List<SpeciesCollected> sc_data = new LinkedList<>();

        String[] sc_projection = {
                SpeciesCollectedTable.SC_CNAME,
                SpeciesCollectedTable.SC_SNAME,
                SpeciesCollectedTable.SC_QUANTITY,
                SpeciesCollectedTable.SC_NUM_REMOVED,
                SpeciesCollectedTable.SC_NUM_RELEASED,
                SpeciesCollectedTable.SC_BAND_NUM,
                SpeciesCollectedTable.SC_VS_RETAINED,
                SpeciesCollectedTable.SC_BLOOD_TAKEN,
                SpeciesCollectedTable.SC_STATUS
        };

        String sc_selection = SpeciesCollectedTable.SCC_ID + " = ? AND " + SpeciesCollectedTable.SCGROUP_ID + " = ?";
        String[] sc_selectionArgs = {Long.toString(c_id),Integer.toString(group_id)};

        Cursor sc_cursor = db.query(SpeciesCollectedTable.TABLE_NAME,
                sc_projection,
                sc_selection,
                sc_selectionArgs,
                null,
                null,
                null);
        while (sc_cursor.moveToNext()) {
            String c_name = sc_cursor.getString(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_CNAME));
            String s_name = sc_cursor.getString(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_SNAME));
            int quantity = sc_cursor.getInt(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_QUANTITY));
            int num_removed = sc_cursor.getInt(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_NUM_REMOVED));
            int num_released = sc_cursor.getInt(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_NUM_RELEASED));
            String band_num = sc_cursor.getString(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_BAND_NUM));
            boolean vs_retained = ((sc_cursor.getInt(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_VS_RETAINED))) == 1);
            boolean blood_taken = ((sc_cursor.getInt(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_BLOOD_TAKEN))) == 1);
            SpeciesCollected.Disposition status = SpeciesCollected.disposition_map_reverse.get(
                    sc_cursor.getInt(sc_cursor.getColumnIndexOrThrow(SpeciesCollectedTable.SC_STATUS))
            );

            sc_data.add(new SpeciesCollected(c_name,s_name,quantity, num_removed, num_released, band_num, vs_retained, blood_taken, status));
        }
        sc_cursor.close();

        return sc_data;
    }


}
