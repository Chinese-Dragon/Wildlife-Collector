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
    private int selected = 0;

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

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getCommonName() {
        return commonName;
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

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
    }
}
