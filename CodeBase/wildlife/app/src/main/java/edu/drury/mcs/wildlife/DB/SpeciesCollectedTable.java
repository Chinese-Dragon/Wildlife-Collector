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
    public static final String SCSPECIES_ID = "species_id";

    public static final String CREATE_TABLE_SPECIES_COLLECTED = "CREATE TABLE " + TABLE_NAME
            + " ("
            + SC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + SC_CNAME + " VARCHAR(255) NOT NULL,"
            + SC_SNAME + " VARCHAR(255) NOT NULL,"
            + SC_QUANTITY + " INTEGER NOT NULL,"
            + SCSPECIES_ID + " INTEGER NOT NULL,"
            + "FOREIGN KEY (" + SCSPECIES_ID + ") REFERENCES " + SpeciesTable.TABLE_NAME + " (" + SpeciesTable.S_ID + ")"
            + ");";
}
