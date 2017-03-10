package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 3/1/2017.
 */

public class MainCollectionTable {

    //table name
    public static final String TABLE_NAME = "main_collection";

    //column info
    public static final String MC_ID = "main_collection_id";
    public static final String MC_NAME = "name";
    public static final String MC_EMAIL= "email";

    //create table
    public static final String CREATE_TABLE_MAIN_COLLECTION = "CREATE TABLE " + TABLE_NAME
            + " ("
            + MC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MC_NAME + " VARCHAR(255) NOT NULL,"
            + MC_EMAIL + " VARCHAR(320) NOT NULL"
            + ");";
}
