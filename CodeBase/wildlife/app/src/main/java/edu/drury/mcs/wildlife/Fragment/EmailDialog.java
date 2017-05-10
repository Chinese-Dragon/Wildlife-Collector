package edu.drury.mcs.wildlife.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.DB.wildlifeDBHandler;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmailDialog extends DialogFragment {
    public static final String EXTRA_CURRENTCOLLECTION = "edu.drury.mcs.wildlife.CURRENTCOLLECTION";;
    public final static int STATIC_INTEGER_VALUE = 100;
    private AlertDialog.Builder dialogBuilder;
    private LayoutInflater inflater;
    private View dialog_view;
    private Button cancel, send;
    private EditText editText;
    private Context context;

    public HashMap<String, Object> getANSWER() {
        return ANSWER;
    }

    public void setANSWER(HashMap<String, Object> ANSWER) {
        this.ANSWER = ANSWER;
    }

    private HashMap<String, Object> ANSWER;
    boolean isAlreadyExist = false;
    private File file = null;
    private static final String CSV_HEADER[] = {"EntryName"};

    public EmailDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogBuilder = new AlertDialog.Builder(getActivity());

        //get the layout inflater
        inflater = getActivity().getLayoutInflater();

        // inflate and set the our customized layout for the dialog
        dialog_view = inflater.inflate(R.layout.email_dialog_fragment, null);
        dialogBuilder.setView(dialog_view);

        // get hold of edittext, cancel and create button and set behavior
        editText = (EditText) dialog_view.findViewById(R.id.name_collection);
        send = (Button) dialog_view.findViewById(R.id.send_entry);
        cancel = (Button) dialog_view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                EmailDialog.this.getDialog().cancel();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Generate CSV and save on phone
                exportDBToCSV();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory()
                                + "/" + "Android/data/" + context.getPackageName().toString() + "/CSV.csv"));
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/csv");
                startActivity(Intent.createChooser(sendIntent, "Send mail..."));

                //Send intent with CSV file to send to another app (i.e. Outlook Email)

                /*CollectionObj currentCollection = new CollectionObj();
                String collection_name = editText.getText().toString().trim();
                Log.i(TAG,collection_name);
                if(!collection_name.equals("")) {
                    currentCollection.setCollection_name(editText.getText().toString());

                    // Segue to CreateCollection activity
                    Intent intent = new Intent(getActivity(), CreateCollection.class);
                    intent.putExtra(EXTRA_CURRENTCOLLECTION, currentCollection);

                    EmailDialog.this.getDialog().cancel();
                    ((Activity) context).startActivityForResult(intent, STATIC_INTEGER_VALUE);
                } else {
                    Message.showMessage(context,"Collection Name is required");
                }
*/
            }
        });

        return dialogBuilder.create();
    }

    private void exportDBToCSV() {

        //setANSWER("Entry1", CSV_HEADER[0]);
        //ANSWER.put(CSV_HEADER[0], "Entry1");
        fileHandler();
        writeDataOnCSV();

        /*File dbFile = context.getDatabasePath("wildlife.db");
        wildlifeDBHandler dbhelper = new wildlifeDBHandler(context);
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "collectionEntry.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM contacts", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }*/
    }

    private void fileHandler() {
        File mainDir = new File(Environment.getExternalStorageDirectory() + "/" + "Android/data/" +
                context.getPackageName().toString());

        if (!mainDir.exists()) {
            mainDir.mkdir();
        }

        file = new File(mainDir + "/" + "CSV.csv");

        if (!file.exists()) {
            isAlreadyExist = true;
        }
    }

    private void writeDataOnCSV() {
        ICsvMapWriter mapWriter = null;
        try {
            mapWriter = new CsvMapWriter(new FileWriter(file, true), CsvPreference.STANDARD_PREFERENCE);

            if (isAlreadyExist) {
                mapWriter.writeHeader(CSV_HEADER);
            }

            mapWriter.write(getANSWER(), CSV_HEADER);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mapWriter != null) {
                try {
                    mapWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
