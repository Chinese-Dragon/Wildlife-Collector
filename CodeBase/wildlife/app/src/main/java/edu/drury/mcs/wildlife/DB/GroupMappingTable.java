package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 3/1/2017.
 */

public class GroupMappingTable {
    public static final String TABLE_NAME = "group_mapping";

    public static final String GM_ID = "group_id";
    public static final String GM_CNAME = "common_name";
    public static final String GM_SNAME = "scientific_name";

    public static final String CREATE_TABLE_GROUP_MAPPING_COLLECTION = "CREATE TABLE " + TABLE_NAME
            + " ("
            + GM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + GM_CNAME + " VARCHAR(255) NOT NULL,"
            + GM_SNAME + " VARCHAR(255) NOT NULL"
            + ");";
}
