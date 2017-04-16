package edu.drury.mcs.wildlife.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.OnDataReturnListener;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.JavaClass.viewEntryDataAdapter;
import edu.drury.mcs.wildlife.R;

public class ViewAndUpdateCollectionEntry extends AppCompatActivity implements OnDataReturnListener {
    public static final String UPDATEDCOLLECTIONDATA = "edu.drury.mcs.wildlife.UPDATEDCOLLECTIONDATA";
    public static final String CURRENTADAPTERPOSITION = "edu.drury.mcs.wildlife.CURRENTADAPTERPOSITION";
    private int adapterPosition;
    private CollectionObj currentCollection;
    private EditText entry_name, entry_date;
    private TextView entry_location;
    private RecyclerView salamander_recyclerview, turtle_recyclerview, lizard_recyclerview, frog_recyclerview, snake_recyclerview;
    private viewEntryDataAdapter salamander_adapter, turtle_adapter, lizard_adapter, frog_adapter, snake_adapter;
    private ItemTouchHelper sa_touchHelper, tu_touchHelper, li_touchHelper, fr_touchHelper, sn_touchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_update_collection_entry);

        //get extras
        currentCollection = getIntent().getParcelableExtra(collectionAdapter.EXTRA_VIEW);
        adapterPosition = getIntent().getIntExtra(collectionAdapter.EXTRA_POSITION, -1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentCollection.getCollection_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.delete_48);

        setUpUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Message.showMessage(this,"Closed");
                onBackPressed();
                return true;
            case R.id.save:
                // call save data and finish activity
                saveUpdateData();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // override this to get user popupasking if they wanna save it or just leave when they accidently touch the back button
        super.onBackPressed();
    }

    private void setUpUI() {
        entry_name = (EditText) findViewById(R.id.entry_name);
        entry_date = (EditText) findViewById(R.id.entry_date);
        entry_location = (TextView) findViewById(R.id.entry_location);

        entry_name.setText(currentCollection.getCollection_name());
        entry_date.setText(currentCollection.getDate());
        if(!Objects.equals(currentCollection.getLocationUTM().trim(), "")) {
            entry_location.setText(currentCollection.getLocationUTM());
        } else {
            entry_location.setText("Latitude: " + currentCollection.getLocation().getLatitude() + "\n"
                    + "Longitude: " + currentCollection.getLocation().getLongitude());
        }

        entry_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                View view = getCurrentFocus();
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    entry_name.clearFocus();

                    // hide soft keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert view != null;
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });

        setUpDatePicker();
        setUpRecyclerviews();
    }

    private void setUpRecyclerviews() {
        salamander_recyclerview = (RecyclerView) findViewById(R.id.salamander_recyclerview);
        turtle_recyclerview = (RecyclerView) findViewById(R.id.turtle_recyclerview);
        lizard_recyclerview = (RecyclerView) findViewById(R.id.lizard_recyclerview);
        frog_recyclerview = (RecyclerView) findViewById(R.id.frog_recyclerview);
        snake_recyclerview = (RecyclerView) findViewById(R.id.snake_recyclerview);

        salamander_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        turtle_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        lizard_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        frog_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        snake_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        // attach adapter and assign itemtouch helper
        for (Species s: currentCollection.getSpecies()) {
            switch (s.getGroup_ID()) {
                case 1:
                    salamander_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    sa_touchHelper = new ItemTouchHelper(getTouchHelper(salamander_adapter));
                    break;
                case 2:
                    frog_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    fr_touchHelper = new ItemTouchHelper(getTouchHelper(frog_adapter));
                    break;
                case 3:
                    lizard_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    li_touchHelper = new ItemTouchHelper(getTouchHelper(lizard_adapter));
                    break;
                case 4:
                    snake_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    sn_touchHelper = new ItemTouchHelper(getTouchHelper(snake_adapter));
                    break;
                case 5:
                    turtle_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    tu_touchHelper = new ItemTouchHelper(getTouchHelper(turtle_adapter));
                    break;
                default:
                    break;
            }
        }

        salamander_recyclerview.setAdapter(salamander_adapter);
        turtle_recyclerview.setAdapter(turtle_adapter);
        lizard_recyclerview.setAdapter(lizard_adapter);
        frog_recyclerview.setAdapter(frog_adapter);
        snake_recyclerview.setAdapter(snake_adapter);

        //attach itemtouchhelper
        sa_touchHelper.attachToRecyclerView(salamander_recyclerview);
        tu_touchHelper.attachToRecyclerView(turtle_recyclerview);
        li_touchHelper.attachToRecyclerView(lizard_recyclerview);
        fr_touchHelper.attachToRecyclerView(frog_recyclerview);
        sn_touchHelper.attachToRecyclerView(snake_recyclerview);

    }

    private ItemTouchHelper.SimpleCallback getTouchHelper(final viewEntryDataAdapter adapter) {
        Log.i("swipe", "I am in getTouchHelper");
      ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.i("swipe", "I am swiped at " + Integer.toString(position));
                adapter.removeItem(position);
            }
        };

        return callback;
    }

    private void saveUpdateData() {
        // get curret data for name, date, location
        currentCollection.setCollection_name(entry_name.getText().toString());
        currentCollection.setDate(entry_date.getText().toString());

        // get lists of speciesCollected data from each adapter
        for(Species s: currentCollection.getSpecies()) {
            switch (s.getGroup_ID()) {
                case 1:
                    s.setSpecies_Data(salamander_adapter.getCurrentAdapterData());
                    break;
                case 2:
                    s.setSpecies_Data(frog_adapter.getCurrentAdapterData());
                    break;
                case 3:
                    s.setSpecies_Data(lizard_adapter.getCurrentAdapterData());
                    break;
                case 4:
                    s.setSpecies_Data(snake_adapter.getCurrentAdapterData());
                    break;
                case 5:
                    s.setSpecies_Data(turtle_adapter.getCurrentAdapterData());
                    break;
                default:
                    break;
            }
        }

        // set retrn result data
        Intent returnIntent = new Intent();
        returnIntent.putExtra(UPDATEDCOLLECTIONDATA, currentCollection);
        returnIntent.putExtra(CURRENTADAPTERPOSITION, adapterPosition);
        // finish
        setResult(MainActivity.RESULT_OK, returnIntent);
        finish();
    }

    private void setUpDatePicker() {
        final Calendar mCalander = new GregorianCalendar();
        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        final DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                mCalander.set(Calendar.YEAR, year);
                mCalander.set(Calendar.MONTH, month);
                mCalander.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //update display in EditText
                entry_date.setText(formatter.format(mCalander.getTime()));
            }
        };

        entry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ViewAndUpdateCollectionEntry.this,
                        listener, mCalander.get(Calendar.YEAR),
                        mCalander.get(Calendar.MONTH),
                        mCalander.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void onDataReturn(SpeciesCollected updateSC, int group_id, int adapterPosition) {
        // return from update SC dialog
        switch (group_id) {
            case 1:
                salamander_adapter.updateItem(updateSC, adapterPosition);
                break;
            case 2:
                frog_adapter.updateItem(updateSC, adapterPosition);
                break;
            case 3:
                lizard_adapter.updateItem(updateSC, adapterPosition);
                break;
            case 4:
                snake_adapter.updateItem(updateSC, adapterPosition);
                break;
            case 5:
                turtle_adapter.updateItem(updateSC, adapterPosition);
                break;
            default:
                break;
        }
    }
}
