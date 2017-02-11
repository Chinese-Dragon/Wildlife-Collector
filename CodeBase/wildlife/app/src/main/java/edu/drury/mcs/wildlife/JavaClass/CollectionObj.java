package edu.drury.mcs.wildlife.JavaClass;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yma004 on 2/4/17.
 */

public class CollectionObj implements Parcelable{
    private String collection_name;
    private String date;
    private Location location;
    private List<Species> species;

    public CollectionObj() {
        this.collection_name = "";
        this.date = "";
        this.location = new Location("");
        this.species = new ArrayList<>();
    }

    public CollectionObj(Parcel input) {
        this.collection_name = input.readString();
        this.date = input.readString();
        this.location = Location.CREATOR.createFromParcel(input);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(collection_name);
        parcel.writeString(date);
        location.writeToParcel(parcel, i);
        parcel.writeList(species);
    }

    public static final Parcelable.Creator<CollectionObj> CREATOR
            = new Parcelable.Creator<CollectionObj>() {
        public CollectionObj createFromParcel(Parcel in) {
            return new CollectionObj(in);
        }

        public CollectionObj[] newArray(int size) {
            return new CollectionObj[size];
        }
    };
}
