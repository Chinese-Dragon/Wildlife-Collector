package edu.drury.mcs.wildlife.JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark93 on 3/2/2017.
 */

public class MainCollectionObj implements Parcelable{
    private String main_collection_name;
    private String email;
    private List<CollectionObj> collections;

    public MainCollectionObj(String _name) {
        this.main_collection_name = _name;
        this.collections = new ArrayList<>();
        this.email = "";
    }

    public MainCollectionObj(String _name, String _email) {
        this.main_collection_name = _name;
        this.email = _email;
        this.collections = new ArrayList<>();
    }

    public MainCollectionObj(String _name, String _email, List<CollectionObj> _collections) {
        this.main_collection_name = _name;
        this.collections = _collections;
        this.email = _email;
    }

    protected MainCollectionObj(Parcel in) {
        this.main_collection_name = in.readString();
        this.email = in.readString();
        this.collections = in.createTypedArrayList(CollectionObj.CREATOR);
    }

    public void setCollections(List<CollectionObj> collections) {
        this.collections = collections;
    }

    public void setMain_collection_name(String main_collection_name) {
        this.main_collection_name = main_collection_name;
    }

    public List<CollectionObj> getCollections() {
        return collections;
    }

    public String getMain_collection_name() {
        return main_collection_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void add_collectionObj(CollectionObj newEntry) {
        this.collections.add(newEntry);
    }

    public void update_collectionObj(CollectionObj updatedC, int position) {
        this.collections.set(position, updatedC);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.main_collection_name);
        parcel.writeString(this.email);
        parcel.writeTypedList(this.collections);

    }

    public static final Creator<MainCollectionObj> CREATOR = new Creator<MainCollectionObj>() {
        @Override
        public MainCollectionObj createFromParcel(Parcel in) {
            return new MainCollectionObj(in);
        }

        @Override
        public MainCollectionObj[] newArray(int size) {
            return new MainCollectionObj[size];
        }
    };
}
