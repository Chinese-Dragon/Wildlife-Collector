package edu.drury.mcs.wildlife.JavaClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.DB.AnimalTable;
import edu.drury.mcs.wildlife.DB.wildlifeDBHandler;

/**
 * Created by mark93 on 2/16/2017.
 */

public class BackgroundTask extends AsyncTask<String, Void, List<SpeciesCollected>> {
    private Context context;
    private AsyncTaskCompleteListener<String> taskCompleteListener;
    private ProgressDialog progressDialog;

    public BackgroundTask(Context context, AsyncTaskCompleteListener<String> listener){
        this.context = context;
        this.taskCompleteListener = listener;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        // do not need to call super.onPreExecute() because it does nothing
        // this method will be running on UI thread
        progressDialog.setMessage("\tLoading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected List<SpeciesCollected> doInBackground(String... params) {
        String goal = params[0];
        List<SpeciesCollected> animalData = new ArrayList<>();

        if(goal.equals("getSpecies")) {
            String group_id = params[1];
            SQLiteDatabase db = new wildlifeDBHandler(context).getReadableDatabase();
            String[] projection = {
                    AnimalTable.A_ID,
                    AnimalTable.AGM_ID,
                    AnimalTable.A_SNAME,
                    AnimalTable.A_CNAME
            };

            String selecton = AnimalTable.AGM_ID + " = ?";
            String[] selectionArgs = {group_id};

            Cursor cursor = db.query(AnimalTable.TABLE_NAME,
                    projection,
                    selecton,
                    selectionArgs,
                    null,
                    null,
                    null);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(AnimalTable.A_ID));
                String c_name = cursor.getString(cursor.getColumnIndexOrThrow(AnimalTable.A_CNAME));
                String s_name = cursor.getString(cursor.getColumnIndexOrThrow(AnimalTable.A_SNAME));
                animalData.add(new SpeciesCollected(id, s_name, c_name));
            }

            cursor.close();
            db.close();
        }

        // better to return a empty string than null
        return animalData;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<SpeciesCollected> data) {
        // this method will be running on UI thread
        progressDialog.dismiss();
        taskCompleteListener.onTaskComplete(data);
    }
}
