package edu.drury.mcs.wildlife.JavaClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.DB.MainCollectionTable;
import edu.drury.mcs.wildlife.DB.wildlifeDBHandler;

/**
 * Created by mark93 on 3/2/2017.
 */

public class MainCollectionObj implements Parcelable{
    private String main_collection_name;
    private String email;
    private List<CollectionObj> collections;

    public MainCollectionObj(){
        this.email = "";
        this.main_collection_name = "";
        this.collections = new ArrayList<>();
    }

    public MainCollectionObj(String _name) {
        this();
        this.main_collection_name = _name;
    }

    public MainCollectionObj(String _name, String _email) {
        this(_name);
        this.email = _email;
    }

    public MainCollectionObj(String _name, String _email, List<CollectionObj> _collections) {
        this(_name, _email);
        this.collections = _collections;
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

    public void saveToDB(Context context) {
        new MainCollecton2DB(context,this).execute("create");
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

    private class MainCollecton2DB extends AsyncTask<String, Void, MainCollectionObj> {
        private MainCollectionObj main_collection;
        private SQLiteDatabase db;
        private Context context;
        public MainCollecton2DB(Context context, MainCollectionObj _main) {
            this.main_collection = _main;
            this.context =context;
        }

        @Override
        protected MainCollectionObj doInBackground(String... params) {
            String method = params[0];

            switch (method) {
                case "create":
                    db = new wildlifeDBHandler(context).getWritableDatabase();

                    String[] projections = {
                            MainCollectionTable.MC_NAME
                    };

                    String selection = MainCollectionTable.MC_NAME + " = ?";
                    String[] selectionArgs = {main_collection.getMain_collection_name()};

                    Cursor cursor = db.query(
                            MainCollectionTable.TABLE_NAME,
                            projections,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null
                    );

                    if(cursor.getCount() == 0){
                        ContentValues values = new ContentValues();
                        values.put(MainCollectionTable.MC_NAME, main_collection.getMain_collection_name());
                        values.put(MainCollectionTable.MC_EMAIL, main_collection.getEmail());
                        db.insert(MainCollectionTable.TABLE_NAME, null, values);
                    }
                    cursor.close();
                    break;
                default:
                    break;
            }

            return null;
        }

    }
}
