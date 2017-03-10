package edu.drury.mcs.wildlife.DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by mark93 on 3/7/2017.
 */

public class syncDBTask extends AsyncTask<Void, Void, Boolean>{
    public static final String CHECK = "edu.drury.mcs.wildlife.CHECK_UPDATE";
    private Context context;
    private SQLiteDatabase db;
    private AlertDialog needInternet;
    private URL url;
    private HttpURLConnection httpURLConnection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private String JSON_STRING;
    private SharedPreferences sharedPref;



    public syncDBTask(Context _context) {
        this.context = _context;
        sharedPref = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        initializeSQLite();


        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netFo = conMan.getActiveNetworkInfo();

        // check internet state
        if (netFo != null && netFo.isConnected()) {

            //NOTE: CHECK FOR UPDATE FIRST *****************************
            boolean needUpdate = sharedPref.getBoolean(CHECK, true);
            if(needUpdate) {
                //update tables if needed
                try {
                    updateTables(retrieveData());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else {
            db.close();
            return false;
        }

        db.close();

        return true;
    }

    private void initializeSQLite() {
        db = new wildlifeDBHandler(context).getWritableDatabase();
        Log.i(TAG,"Inializing db");
    }

    private String retrieveData() {
        String target = "https://wildlife-expo-yma004.c9users.io/getGroupData";
        String result_json_string = "";

        try {
            bufferedReader = getBufferedReader(target);
            result_json_string = readJSONToString(bufferedReader);

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        }catch (IOException e){
            e.printStackTrace();
        }

        return result_json_string;
    }

    private void updateTables(String json_string) throws JSONException {
        JSONArray jsonArray = new JSONArray(json_string);
        String cName, sName = "";

        for(int i = 0; i < jsonArray.length(); i ++) {
            JSONObject object = jsonArray.getJSONObject(i);
            cName = object.getString("common_name");
            sName = object.getString("scientific_name");

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(GroupMappingTable.GM_CNAME, cName);
            values.put(GroupMappingTable.GM_SNAME, sName);
            db.insertWithOnConflict(GroupMappingTable.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(CHECK, false);
        editor.commit();
        Log.i("info", "finished updating group table");
    }

    private BufferedReader getBufferedReader(String request_url) {
        BufferedReader reader = null;

        try {
            url = new URL(request_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private String readJSONToString(BufferedReader reader) {
        String result = "";

        stringBuilder = new StringBuilder();
        try {
            while ((JSON_STRING = reader.readLine()) != null) {
                stringBuilder.append(JSON_STRING).append("\n");
            }
            result = stringBuilder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
