package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 2/21/2017.
 */

public class SpeciesCollectedTable {
    public static final String TABLE_NAME = "species_collected";
    // pk
    public static final String SC_ID = "species_collected_id";

    public static final String SC_CNAME = "common_name";
    public static final String SC_SNAME = "scientific_name";
    public static final String SC_QUANTITY = "quantity";

    // foreign key
    public static final String SCGROUP_ID = "group_id";
    public static final String SCC_ID = "collection_id";

    public static final String CREATE_TABLE_SPECIES_COLLECTED = "CREATE TABLE " + TABLE_NAME
            + " ("
            + SC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SC_CNAME + " VARCHAR(255) NOT NULL,"
            + SC_SNAME + " VARCHAR(255) NOT NULL,"
            + SC_QUANTITY + " INTEGER NOT NULL,"
            + SCGROUP_ID + " INTEGER NOT NULL,"
            + SCC_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + SCGROUP_ID + ") REFERENCES " + GroupMappingTable.TABLE_NAME + " (" + GroupMappingTable.GM_ID + "),"
            + "FOREIGN KEY (" + SCC_ID + ") REFERENCES " + CollectionTable.TABLE_NAME + " (" + CollectionTable.C_ID + ")"
            + ");";
}
