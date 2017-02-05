package edu.drury.mcs.wildlife.JavaClass;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * Created by yma004 on 2/4/17.
 */

public class CollectionObj implements Serializable{
    private String collection_name;
    private String date;
    private Location location;
    private List<Species> species = new ArrayList<Species>();

    public CollectionObj() {
    }

    public List<Species> getSpecies() {
        return species;
    }

    public Location getLocation() {
        return location;
    }

    public String getCollection_name() {
        return collection_name;
    }

    public String getDate() {
        return date;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setSpecies(List<Species> species) {
        this.species = species;
    }
}
