package edu.drury.mcs.wildlife.JavaClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mark93 on 2/16/2017.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {
    private URL url;
    private HttpURLConnection httpURLConnection;
    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private String JSON_STRING;
    private Context context;
    private String get_species_url = "https://wildlife-expo-yma004.c9users.io/species/";;
    private String get_groups_url = "https://wildlife-expo-yma004.c9users.io/species/getGroups";
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
    protected String doInBackground(String... params) {
        String goal = params[0];
        String result_json_string = "";

        if(goal.equals("getSpecies")) {
            String group_id = params[1];
            try {
                bufferedReader = getBufferedReader(get_species_url + group_id);
                result_json_string = readJSONToString(bufferedReader);

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result_json_string;
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        // better to return a empty string than null
        return result_json_string;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result_json_string) {
        // this method will be running on UI thread
        progressDialog.dismiss();
        taskCompleteListener.onTaskComplete(result_json_string);
    }

    private BufferedReader getBufferedReader(String request_url) {
        BufferedReader reader = null;

        try {
            url = new URL(request_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    public String readJSONToString(BufferedReader reader) {
        String result = "";

        stringBuilder = new StringBuilder();
        try {
            while ((JSON_STRING = reader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
            }
            result = stringBuilder.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
