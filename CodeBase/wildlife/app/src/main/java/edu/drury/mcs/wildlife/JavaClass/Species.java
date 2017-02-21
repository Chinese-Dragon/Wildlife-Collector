package edu.drury.mcs.wildlife.JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yma004 on 12/11/16.
 */

public class Species implements Parcelable{

    private String commonName;
    private String scientificName;
    private int group_ID;
    private List<SpeciesCollected> species_Data;

    public Species() {
        this.commonName = "";
        this.scientificName = "";
        this.group_ID = 0;
        this.species_Data = new ArrayList<>();
    }

    public Species(String common, String science, int ID) {
        this.commonName = common;
        this.scientificName = science;
        this.group_ID = ID;
        this.species_Data = new ArrayList<>();
    }

    public Species(Parcel input) {
        this();
        this.commonName = input.readString();
        this.scientificName = input.readString();
        this.group_ID = input.readInt();
//        this.species_Data = new ArrayList<>();
        input.readTypedList(species_Data, SpeciesCollected.CREATOR);
    }

    public String getCommonName() {

        return commonName;
    }

    public String getScientificName() {

        return scientificName;
    }


    public int getGroup_ID() {
        return group_ID;
    }

    public void setSpecies_Data(List<SpeciesCollected> species_Data) {
        this.species_Data = species_Data;
    }

    public List<SpeciesCollected> getSpecies_Data() {
        return species_Data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(commonName);
        parcel.writeString(scientificName);
        parcel.writeInt(group_ID);
        parcel.writeTypedList(species_Data);
    }


    public static final Parcelable.Creator<Species> CREATOR
            = new Parcelable.Creator<Species>() {
        public Species createFromParcel(Parcel in) {
            return new Species(in);
        }

        public Species[] newArray(int size) {
            return new Species[size];
        }
    };

}
