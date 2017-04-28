package edu.drury.mcs.wildlife.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.FixSwipeableItemClickListener;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.OnDataReturnListener;
import edu.drury.mcs.wildlife.JavaClass.Species;
import edu.drury.mcs.wildlife.JavaClass.SpeciesCollected;
import edu.drury.mcs.wildlife.JavaClass.collectionAdapter;
import edu.drury.mcs.wildlife.JavaClass.viewEntryDataAdapter;
import edu.drury.mcs.wildlife.R;

public class ViewAndUpdateCollectionEntry extends AppCompatActivity implements OnDataReturnListener, View.OnClickListener {
    public static final String UPDATEDCOLLECTIONDATA = "edu.drury.mcs.wildlife.UPDATEDCOLLECTIONDATA";
    public static final String CURRENTADAPTERPOSITION = "edu.drury.mcs.wildlife.CURRENTADAPTERPOSITION";
    public final static String EXTRA_CURRENTSPECIES = "edu.drury.mcs.wildlife.CURRENTSPECIES";
    public final static int STATIC_INTEGER_VALUE = 826;
    public static final int RESULT_OK = 202;
    private int adapterPosition;
    private CollectionObj currentCollection;
    private EditText entry_name, entry_date;
    private TextView entry_location;
    private RecyclerView salamander_recyclerview, turtle_recyclerview, lizard_recyclerview, frog_recyclerview, snake_recyclerview;
    private viewEntryDataAdapter salamander_adapter, turtle_adapter, lizard_adapter, frog_adapter, snake_adapter;
//    private ItemTouchHelper sa_touchHelper, tu_touchHelper, li_touchHelper, fr_touchHelper, sn_touchHelper;
    private ImageButton addFrog, addSalamander, addLizard, addTurtle, addSnake;
    private View salamander_divider, lizard_divider, frog_divider, turtle_divider, snake_divider;
    private SwipeToDismissTouchListener<RecyclerViewAdapter> salamander_touchListenser, lizard_touchListenser, frog_touchListenser, turtle_touchListenser, snake_touchListenser;


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
        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Message.showMessage(this,"Closed");
                onBackPressed();
                return true;
            case R.id.update:
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
        salamander_divider  = findViewById(R.id.salamander_divider);
        frog_divider = findViewById(R.id.frog_divider);
        snake_divider = findViewById(R.id.snake_divider);
        turtle_divider = findViewById(R.id.turtle_divider);
        lizard_divider = findViewById(R.id.lizard_divider);

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
        setUpButtons();
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
                    salamander_recyclerview.setAdapter(salamander_adapter);
//                    sa_touchHelper = new ItemTouchHelper(getTouchHelper(salamander_adapter));
                    salamander_touchListenser = getTouchListener(salamander_recyclerview, salamander_adapter);
                    salamander_recyclerview.setOnTouchListener(salamander_touchListenser);
                    salamander_recyclerview.setOnScrollListener((RecyclerView.OnScrollListener) salamander_touchListenser.makeScrollListener());
                    salamander_recyclerview.addOnItemTouchListener(getSwipeableItemClickListener(salamander_touchListenser));

                    if(s.getSpecies_Data().isEmpty()) {
                        salamander_divider.setVisibility(View.GONE);
                    }

                    break;
                case 2:
                    frog_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    frog_recyclerview.setAdapter(frog_adapter);
//                    fr_touchHelper = new ItemTouchHelper(getTouchHelper(frog_adapter));
                    frog_touchListenser = getTouchListener(frog_recyclerview, frog_adapter);
                    frog_recyclerview.setOnTouchListener(frog_touchListenser);
                    frog_recyclerview.setOnScrollListener((RecyclerView.OnScrollListener) frog_touchListenser.makeScrollListener());
                    frog_recyclerview.addOnItemTouchListener(getSwipeableItemClickListener(frog_touchListenser));

                    if(s.getSpecies_Data().isEmpty()) {
                        frog_divider.setVisibility(View.GONE);
                    }

                    break;
                case 3:
                    lizard_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    lizard_recyclerview.setAdapter(lizard_adapter);
//                    li_touchHelper = new ItemTouchHelper(getTouchHelper(lizard_adapter));
                    lizard_touchListenser = getTouchListener(lizard_recyclerview, lizard_adapter);
                    lizard_recyclerview.setOnTouchListener(lizard_touchListenser);
                    lizard_recyclerview.setOnScrollListener((RecyclerView.OnScrollListener) lizard_touchListenser.makeScrollListener());
                    lizard_recyclerview.addOnItemTouchListener(getSwipeableItemClickListener(lizard_touchListenser));

                    if(s.getSpecies_Data().isEmpty()) {
                        lizard_divider.setVisibility(View.GONE);
                    }

                    break;
                case 4:
                    snake_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
                    snake_recyclerview.setAdapter(snake_adapter);
//                    sn_touchHelper = new ItemTouchHelper(getTouchHelper(snake_adapter));
                    snake_touchListenser = getTouchListener(snake_recyclerview, snake_adapter);
                    snake_recyclerview.setOnTouchListener(snake_touchListenser);
                    snake_recyclerview.setOnScrollListener((RecyclerView.OnScrollListener) snake_touchListenser.makeScrollListener());
                    snake_recyclerview.addOnItemTouchListener(getSwipeableItemClickListener(snake_touchListenser));

                    if(s.getSpecies_Data().isEmpty()) {
                        snake_divider.setVisibility(View.GONE);
                    }
                    break;
                case 5:
                    turtle_adapter = new viewEntryDataAdapter(this, s.getSpecies_Data(), s.getGroup_ID());
//                    tu_touchHelper = new ItemTouchHelper(getTouchHelper(turtle_adapter));
                    turtle_recyclerview.setAdapter(turtle_adapter);
                    turtle_touchListenser = getTouchListener(turtle_recyclerview, turtle_adapter);
                    turtle_recyclerview.setOnTouchListener(turtle_touchListenser);
                    turtle_recyclerview.setOnScrollListener((RecyclerView.OnScrollListener) turtle_touchListenser.makeScrollListener());
                    turtle_recyclerview.addOnItemTouchListener(getSwipeableItemClickListener(turtle_touchListenser));

                    if(s.getSpecies_Data().isEmpty()) {
                        turtle_divider.setVisibility(View.GONE);
                    }

                    break;
                default:
                    break;
            }
        }

        //attach itemtouchhelper
//        sa_touchHelper.attachToRecyclerView(salamander_recyclerview);
//        tu_touchHelper.attachToRecyclerView(turtle_recyclerview);
//        li_touchHelper.attachToRecyclerView(lizard_recyclerview);
//        fr_touchHelper.attachToRecyclerView(frog_recyclerview);
//        sn_touchHelper.attachToRecyclerView(snake_recyclerview);

    }

    private FixSwipeableItemClickListener getSwipeableItemClickListener(final SwipeToDismissTouchListener touchListener) {
        Message.showMessage(this, "I am in getSwipeableItemClickListener");
        return new FixSwipeableItemClickListener(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(view.getId() == R.id.txt_delete) {
                    touchListener.processPendingDismisses();
                } else if(view.getId() == R.id.txt_undo) {
                    touchListener.undoPendingDismiss();
                }
            }
        });
    }

    private SwipeToDismissTouchListener<RecyclerViewAdapter> getTouchListener(RecyclerView recyclerView, final viewEntryDataAdapter adapter) {
        return new SwipeToDismissTouchListener<>(
                new RecyclerViewAdapter(recyclerView), new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerViewAdapter recyclerView, int position) {
                Log.i("swipe", "I am swiped at " + Integer.toString(position));
                adapter.removeItem(position);
            }
        });
    }

//    private ItemTouchHelper.SimpleCallback getTouchHelper(final viewEntryDataAdapter adapter) {
//        Log.i("swipe", "I am in getTouchHelper");
//      ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//
//                adapter.removeItem(position);
//            }
//        };
//
//        return callback;
//    }

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

    public void setUpButtons() {
        addFrog = (ImageButton) findViewById(R.id.add_frog);
        addLizard = (ImageButton) findViewById(R.id.add_lizard);
        addSalamander = (ImageButton) findViewById(R.id.add_salamander);
        addTurtle = (ImageButton) findViewById(R.id.add_turtle);
        addSnake = (ImageButton) findViewById(R.id.add_snake);

        addFrog.setOnClickListener(this);
        addLizard.setOnClickListener(this);
        addSalamander.setOnClickListener(this);
        addTurtle.setOnClickListener(this);
        addSnake.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Species s = null;
        if(view == addFrog) {
            s = getSpecies(2);
        } else if(view == addLizard) {
            s = getSpecies(3);
        } else if(view == addSalamander) {
            s = getSpecies(1);
        } else if (view == addTurtle) {
            s = getSpecies(5);
        } else if (view == addSnake) {
            s = getSpecies(4);
        }

        Intent intent = new Intent(this, UpdateSpeciesDataTable.class);
        intent.putExtra(EXTRA_CURRENTSPECIES, s);
        startActivityForResult(intent, STATIC_INTEGER_VALUE);
    }

    public Species getSpecies(int group_id) {
        for(Species s : currentCollection.getSpecies()) {
            if(s.getGroup_ID() == group_id) {
                return s;
            }
        }

        return new Species();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == STATIC_INTEGER_VALUE && resultCode == RESULT_OK) {
            //ready to pass new collection data to Collection fragment to add to list
            Message.showMessage(this,"new added animals are here");

            List<SpeciesCollected> newAddedSCs = data.getParcelableArrayListExtra(UpdateSpeciesDataTable.SAVEDSPECIESDATA);
            int group_id = data.getIntExtra(UpdateSpeciesDataTable.CURRENT_GROUP_ID, -1);

            Message.showMessage(this, "Added " + Integer.toString(newAddedSCs.size()));
            Message.showMessage(this, "Group Id " + Integer.toString(group_id));
            for (Species s : currentCollection.getSpecies()) {
                if(s.getGroup_ID() == group_id) {
                    //it should change all the reference in collection and in adapter
                    int old_size = s.getSpecies_Data().size();
                    s.getSpecies_Data().addAll(newAddedSCs);

                    //need to update corresponding recyclerview
                    switch (group_id) {
                        case 1:
                            salamander_adapter.notifyItemRangeInserted(old_size, newAddedSCs.size());
                            break;
                        case 2:
                            frog_adapter.notifyItemRangeInserted(old_size, newAddedSCs.size());
                            break;
                        case 3:
                            lizard_adapter.notifyItemRangeInserted(old_size, newAddedSCs.size());
                            break;
                        case 4:
                            snake_adapter.notifyItemRangeInserted(old_size, newAddedSCs.size());
                            break;
                        case 5:
                            turtle_adapter.notifyItemRangeInserted(old_size, newAddedSCs.size());
                            break;
                        default:break;
                    }
                }
            }
        }
    }
}
