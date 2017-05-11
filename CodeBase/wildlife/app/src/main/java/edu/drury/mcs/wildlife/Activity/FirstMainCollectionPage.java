package edu.drury.mcs.wildlife.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.R;

public class FirstMainCollectionPage extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener{
    private EditText main_collection_name, main_collection_email;
    private Button main_collection_create;
    private String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_main_collection_page);

        main_collection_email = (EditText) findViewById(R.id.main_collection_email);
        main_collection_name = (EditText) findViewById(R.id.main_collection_name);
        main_collection_create = (Button) findViewById(R.id.main_collection_create);
        main_collection_create.setOnClickListener(this);
        main_collection_name.setOnEditorActionListener(this);
        main_collection_email.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == main_collection_create.getId()) {
            name = main_collection_name.getText().toString();
            email = main_collection_email.getText().toString();

            Log.i("messagae", " I am in click");
            if(!Objects.equals(name, "") && !Objects.equals(email, "")) {
                Log.i("messagae", " I am in ready to go");

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("caller", "firstmaincollectionscreen");
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();

            } else {
                Message.showMessage(this, "Email or Collection Name is not given");
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int action_id, KeyEvent keyEvent) {
        if (action_id == EditorInfo.IME_ACTION_DONE) {
            // hide soft keyboard
            textView.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
        }
        return false;
    }
}
