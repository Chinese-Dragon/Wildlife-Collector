package edu.drury.mcs.wildlife.JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yma004 on 2/4/17.
 */

public class SpeciesCollected implements Parcelable{
    private int quantity;
    private String scientificName;
    private String commonName;

    public SpeciesCollected() {
        this.commonName = "";
        this.scientificName = "";
        this.quantity = 0;
    }

    public SpeciesCollected(String scientificName, String commonName) {
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.quantity = 0;
    }

    public SpeciesCollected(Parcel input) {
        this.quantity = input.readInt();
        this.scientificName = input.readString();
        this.commonName = input.readString();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(quantity);
        parcel.writeString(scientificName);
        parcel.writeString(commonName);
    }

    public static final Parcelable.Creator<SpeciesCollected> CREATOR
            = new Parcelable.Creator<SpeciesCollected>() {
        public SpeciesCollected createFromParcel(Parcel in) {
            return new SpeciesCollected(in);
        }

        public SpeciesCollected[] newArray(int size) {
            return new SpeciesCollected[size];
        }
    };

}
