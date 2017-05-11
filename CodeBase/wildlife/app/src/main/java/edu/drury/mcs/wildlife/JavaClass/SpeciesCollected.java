package edu.drury.mcs.wildlife.JavaClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yma004 on 2/4/17.
 */

public class SpeciesCollected implements Parcelable{

    private static final String TASK_CREATE = "create";
    private static final String TASK_DELETE = "delete";

    public static enum Disposition {
        RELEASED, HELD, KILLED
    }

    public static final Map<Disposition, Integer> disposition_map = createMap();
    private static Map<Disposition, Integer> createMap() {
        Map<Disposition, Integer> map = new HashMap<>();
        map.put(Disposition.RELEASED, 1);
        map.put(Disposition.HELD, 2);
        map.put(Disposition.KILLED, 3);
        return map;
    }

    public static final Map<Integer, Disposition> disposition_map_reverse = createMapReverse();
    private static Map<Integer, Disposition> createMapReverse() {
        Map<Integer, Disposition> map = new HashMap<>();
        map.put(1, Disposition.RELEASED);
        map.put(2, Disposition.HELD);
        map.put(3 ,Disposition.KILLED);
        return map;
    }

    private int quantity;
    private String scientificName;
    private String commonName;
    private int num_removed;
    private int num_released;
    private String band_num;
    private boolean voucher_specimen_retained;
    private boolean is_blood_taken;
    private Disposition status;
    private long id;

    public SpeciesCollected() {
        this.commonName = "";
        this.scientificName = "";
        this.quantity = 0;
        this.num_removed = 0;
        this.band_num = "";
        this.voucher_specimen_retained = false;
        this.is_blood_taken = false;
        this.status = Disposition.HELD;
    }

    public SpeciesCollected(long id, String scientificName, String commonName) {
        this();
        this.id = id;
        this.scientificName = scientificName;
        this.commonName = commonName;
    }

    public SpeciesCollected(String scientificName, String commonName, int quantity) {
        this();
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.quantity = quantity;
    }

    public SpeciesCollected(long _id, String s_name, String c_name, int quantity, int num_rm, int num_rl, String band_num, boolean vs_retained, boolean blood_taken, Disposition status) {
        this(s_name, c_name, quantity);
        this.id = _id;
        this.num_removed = num_rm;
        this.num_released = num_rl;
        this.band_num = band_num;
        this.voucher_specimen_retained = vs_retained;
        this.is_blood_taken = blood_taken;
        this.status = status;

    }

    public SpeciesCollected(Parcel input) {
        this();
        this.quantity = input.readInt();
        this.num_removed = input.readInt();
        this.num_released = input.readInt();
        this.scientificName = input.readString();
        this.commonName = input.readString();
        this.band_num = input.readString();
        this.voucher_specimen_retained = input.readByte() != 0;
        this.is_blood_taken = input.readByte() != 0;

        this.status = (Disposition) input.readSerializable();

        this.id = input.readLong();

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(quantity);
        parcel.writeInt(num_removed);
        parcel.writeInt(num_released);
        parcel.writeString(scientificName);
        parcel.writeString(commonName);
        parcel.writeString(band_num);
        parcel.writeByte((byte) (voucher_specimen_retained ? 1 : 0));
        parcel.writeByte((byte) (is_blood_taken ? 1:0));
        parcel.writeSerializable(status);
        parcel.writeLong(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public void setBand_num(String band_num) {
        this.band_num = band_num;
    }

    public void setIs_blood_taken(boolean is_blood_taken) {
        this.is_blood_taken = is_blood_taken;
    }

    public void setNum_removed(int num_removed) {
        this.num_removed = num_removed;
    }

    public void setStatus(Disposition status) {
        this.status = status;
    }

    public void setVoucher_specimen_retained(boolean voucher_specimen_retained) {
        this.voucher_specimen_retained = voucher_specimen_retained;
    }

    public Disposition getStatus() {
        return status;
    }

    public int getNum_removed() {
        return num_removed;
    }

    public String getBand_num() {
        return band_num;
    }

    public boolean getIs_blood_taken() {
        return is_blood_taken;
    }

    public boolean getVoucher_specimen_retained() {
        return voucher_specimen_retained;
    }

    public void setNum_released(int num_released) {
        this.num_released = num_released;
    }

    public int getNum_released() {
        return num_released;
    }

    @Override
    public int describeContents() {
        return 0;
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
