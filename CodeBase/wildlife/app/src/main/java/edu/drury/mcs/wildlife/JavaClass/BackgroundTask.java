package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    private OutputStream outputStream;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private StringBuilder stringBuilder;
    private String JSON_STRING;
    private Context context;
    private String get_species_url;
    private String get_groups_url;
    private String result_json_string;

    public BackgroundTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        get_species_url = "https://wildlife-expo-yma004.c9users.io/species/";
        get_groups_url = "https://wildlife-expo-yma004.c9users.io/species/getGroups";
    }

    @Override
    protected String doInBackground(String... params) {
        String goal = params[0];

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

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
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
