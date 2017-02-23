package edu.drury.mcs.wildlife.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mark93 on 2/21/2017.
 */

public class wildlifeDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "wildlife.db";
    private Context context;

    public wildlifeDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CollectionTable.CREATE_TABLE_COLLECTION);
        }catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            db.execSQL(SpeciesTable.CREATE_TABLE_SPECIES);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(HasSpeciesTable.CREATE_TABLE_HAS_SPECIES);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(SpeciesCollectedTable.CREATE_TABLE_SPECIES_COLLECTED);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        db.execSQL("DROP TABLE IF EXISTS " + CollectionTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SpeciesCollectedTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SpeciesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HasSpeciesTable.TABLE_NAME);
        onCreate(db);
        Log.i("onUpgrade", " onUpgrade is called");
    }
}
