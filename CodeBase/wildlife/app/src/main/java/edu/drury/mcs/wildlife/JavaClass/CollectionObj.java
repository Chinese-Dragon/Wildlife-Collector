package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.drury.mcs.wildlife.DB.DBBackgroundTask;

/**
 * Created by yma004 on 2/4/17.
 */

public class CollectionObj implements Parcelable{
    private static final String TASK_CREATE = "create";
    private static final String TASK_READALL = "read";
    private static final String TASK_DELETE = "delete";
    private static final String TASK_UPDATE = "update";
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

    public CollectionObj(String _name, String _date, Location _location, List<Species> _list_species) {
        this.collection_name = _name;
        this.date = _date;
        this.location = _location;
        this.species = _list_species;
    }

    public CollectionObj(Parcel input) {
        // have to initilize list<> before copy value to it or we can call empty constructor to initialize all variables
        this(); // call CollectionObj()
        this.collection_name = input.readString();
        this.date = input.readString();
        this.location = Location.CREATOR.createFromParcel(input);
        // this.species = new ArrayList<>();
        input.readTypedList(species, Species.CREATOR);
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
        parcel.writeTypedList(species);
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

    // methods for interacting with db
    public void saveToDB(Context context, MainCollectionObj current_main_collection) {
        new DBBackgroundTask(context, current_main_collection, this).execute(TASK_CREATE);
    }

    //DELETE
    public void deleteFromDB(Context context, MainCollectionObj current_main_collection) {
        new DBBackgroundTask(context, current_main_collection, this.getCollection_name()).execute(TASK_DELETE);
    }

    //READALL
    public static List<CollectionObj> readAllCollections(Context context, MainCollectionObj current_main_collection) throws ExecutionException, InterruptedException {
        return new DBBackgroundTask(context, current_main_collection).execute(TASK_READALL).get();
    }

    //UPDATE
    public void updateToDB(Context context) {

    }

}
