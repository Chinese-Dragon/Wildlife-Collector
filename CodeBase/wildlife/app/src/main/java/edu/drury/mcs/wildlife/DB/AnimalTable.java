package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 5/10/2017.
 */

public class AnimalTable {
    public static final String TABLE_NAME = "species";

    public static final String A_ID = "s_id";
    public static final String A_CNAME = "common_name";
    public static final String A_SNAME = "scientific_name";
    public static final String AGM_ID = "group_id";

    public static final String CREATE_TABLE_GROUP_MAPPING_COLLECTION = "CREATE TABLE " + TABLE_NAME
            + " ("
            + A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + A_CNAME + " VARCHAR(255) NOT NULL,"
            + A_SNAME + " VARCHAR(255) NOT NULL,"
            + AGM_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + AGM_ID + ") REFERENCES " + GroupMappingTable.TABLE_NAME + " (" + GroupMappingTable.GM_ID + ")"
            + ");";
}
