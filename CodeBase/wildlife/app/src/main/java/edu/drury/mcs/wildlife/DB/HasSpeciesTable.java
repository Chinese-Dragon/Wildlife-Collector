package edu.drury.mcs.wildlife.DB;

/**
 * Created by mark93 on 2/22/2017.
 */

// this is the mapping table between species and collection
public class HasSpeciesTable {
    public static final String TABLE_NAME = "has_species";

    public static final String FK_C_ID = "collection_id";
    public static final String FK_S_ID = "species_id";

    //create table
    public static final String CREATE_TABLE_HAS_SPECIES = "CREATE TABLE " + TABLE_NAME
            + " ("
            + FK_C_ID + " INTEGER,"
            + FK_S_ID + " INTEGER,"
            + "PRIMARY KEY (" + FK_C_ID + ", " + FK_S_ID + "),"
            + "FOREIGN KEY (" + FK_C_ID + ") REFERENCES " + CollectionTable.TABLE_NAME + " (" + CollectionTable.C_ID + "),"
            + "FOREIGN KEY (" + FK_S_ID + ") REFERENCES " + SpeciesTable.TABLE_NAME + " (" + SpeciesTable.S_ID + ")"
            + ");";
}
