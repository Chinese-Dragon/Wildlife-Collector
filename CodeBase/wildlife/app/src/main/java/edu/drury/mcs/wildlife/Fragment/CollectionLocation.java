package edu.drury.mcs.wildlife.Fragment;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.CollectionObj;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.JavaClass.MyLocation;
import edu.drury.mcs.wildlife.JavaClass.OnDataPassListener;
import edu.drury.mcs.wildlife.JavaClass.UTM;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionLocation extends Fragment implements View.OnClickListener {
    private View layout;
    private Button back, cancel, next, getLocation, getUTMLocation;
    private LocationManager locationManager;
    private EditText coordinates;
    private EditText latitude;
    private EditText longitude;
    private EditText utmEasting;
    private EditText utmNorthing;
    private EditText utmZoneAndLetter;
    private CollectionObj currentCollection;
    private OnDataPassListener dataListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;

        if(context instanceof Activity) {
            a = (Activity) context;

            try {
                dataListener = (OnDataPassListener) a;
            } catch (ClassCastException e) {
                throw new ClassCastException(a.toString() + " must implement OnDataPassListener interface");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.collection_location_fragment, container, false);

        currentCollection = ((CreateCollection) getActivity()).getCurrentCollection();

        back = (Button) layout.findViewById(R.id.back);
        cancel = (Button) layout.findViewById(R.id.cancel);
        next = (Button) layout.findViewById(R.id.next);
        getLocation = (Button) layout.findViewById(R.id.getLocation);
        getUTMLocation = (Button) layout.findViewById(R.id.getUTMLocation);
        latitude = (EditText) layout.findViewById(R.id.latitude);
        longitude = (EditText) layout.findViewById(R.id.longitude);
        utmEasting = (EditText) layout.findViewById(R.id.UTMEast);
        utmNorthing = (EditText) layout.findViewById(R.id.UTMNorth);
        utmZoneAndLetter = (EditText) layout.findViewById(R.id.UTMZoneAndLetter);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);
        getLocation.setOnClickListener(this);
        getUTMLocation.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            CreateCollection.pager.setCurrentItem(0);
        } else if (view == cancel) {
            getActivity().finish();
        } else if (view == next) {
            String lt = latitude.getText().toString().trim();
            String lnt = longitude.getText().toString().trim();
            String utmE = utmEasting.getText().toString().trim();
            String utmN = utmNorthing.getText().toString().trim();
            String utmZ = utmZoneAndLetter.getText().toString().trim();


            if(!lt.equals("") && !lnt.equals("")) {
                Double lat = Double.parseDouble(lt);
                Double lng = Double.parseDouble(lnt);
                Location loc = new Location("");
                loc.setLatitude(lat);
                loc.setLongitude(lng);

                currentCollection.setLocation(loc);
                dataListener.onDataPass(currentCollection, 2);

            } else if(!utmE.equals("") && (!utmN.equals("")) && !utmZ.equals("")) {


            } else {
                Message.showMessage(getActivity(),"Location Info is required");
            }

        } else if (view == getLocation) {


            MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                @Override
                public void gotLocation(Location location){
                    //Got the location!
                    //coordinates.setText(Double.toString(location.getLatitude()) + Double.toString(location.getLongitude()));
                    latitude.setText(Double.toString(location.getLatitude()));
                    longitude.setText(Double.toString(location.getLongitude()));

                }
            };
            MyLocation myLocation = new MyLocation(getActivity());
            myLocation.getLocation(getActivity(), locationResult);

        } else if (view == getUTMLocation) {

            MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                @Override
                public void gotLocation(Location location) {
                    UTM utmCoord = new UTM(location.getLatitude(), location.getLongitude());

                    utmEasting.setText(Double.toString(utmCoord.getEasting()));
                    utmNorthing.setText(Double.toString(utmCoord.getNorthing()));
                    utmZoneAndLetter.setText(Integer.toString(utmCoord.getZone()) + Character.toString(utmCoord.getLetter()));
                }
            };
            MyLocation myLocation = new MyLocation(getActivity());
            myLocation.getLocation(getActivity(), locationResult);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"CollectionLocation is resumed");
    }

    public void setCurrentCollection(CollectionObj collection) {
        this.currentCollection = collection;
        Log.i(TAG,"CurrentCollection Date" + currentCollection.getDate());
    }
}
