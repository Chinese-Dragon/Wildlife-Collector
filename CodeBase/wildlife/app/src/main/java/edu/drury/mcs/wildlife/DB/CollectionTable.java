package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 2/21/2017.
 */

public class CollectionTable {

   // table name
   public static final String TABLE_NAME = "collection";

   // colomn info
   public static final String C_ID = "collection_id";
   public static final String C_NAME = "name";
   public static final String C_DATE = "date";
   public static final String C_LAT = "lat";
   public static final String C_LNG = "lng";
   public static final String CMC_ID = "main_collection_id";


    // create table
   public static final String CREATE_TABLE_COLLECTION = "CREATE TABLE " + TABLE_NAME
            + " ("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " VARCHAR(255) NOT NULL,"
            + C_DATE + " VARCHAR(255) NOT NULL,"
            + C_LAT + " REAL,"
            + C_LNG + " REAL,"
            + CMC_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + CMC_ID + ") REFERENCES " + MainCollectionTable.TABLE_NAME + " (" + MainCollectionTable.MC_ID + ")"
            + ");";
}
