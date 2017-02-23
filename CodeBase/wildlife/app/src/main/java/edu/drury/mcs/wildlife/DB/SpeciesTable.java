package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 2/21/2017.
 */

public class SpeciesTable {
    // table name
    public static final String TABLE_NAME = "species";

    // colmn info
    // pk
    public static final String S_ID = "species_id";

    public static final String S_CNAME = "common_name";
    public static final String S_SNAME = "scientific_name";
    public static final String S_GROUPID = "group_id";

    // create table
    public static final String CREATE_TABLE_SPECIES = "CREATE TABLE " + TABLE_NAME
            + " ("
            + S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + S_CNAME + " VARCHAR(255) NOT NULL,"
            + S_SNAME + " VARCHAR(255) NOT NULL,"
            + S_GROUPID + " INTEGER NOT NULL"
            + ");";
}
