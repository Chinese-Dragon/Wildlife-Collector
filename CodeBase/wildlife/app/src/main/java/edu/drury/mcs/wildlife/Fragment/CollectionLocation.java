package edu.drury.mcs.wildlife.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.drury.mcs.wildlife.Activity.CreateCollection;
import edu.drury.mcs.wildlife.JavaClass.Message;
import edu.drury.mcs.wildlife.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionLocation extends Fragment implements View.OnClickListener {
    private View layout;
    private Button back, cancel, next, getLocation;
    private LocationManager locationManager;
    private TextView location;

//    /**
//     * Instead of handing over potential parameters via constructor,
//     * use the newInstance(...) method and the Bundle for handing over parameters.
//     * This way if detached and re-attached the object state can be stored through the arguments.
//     * Much like Bundles attached to Intents
//     *
//     * @param indicator
//     * @param pager
//     * @return
//     */
//    public static CollectionLocation newInstance(myStepperIndicator indicator, NonSwipeableViewPager pager /* 3rd parameter will be collection data*/) {
//        CollectionLocation myFragment = new CollectionLocation();
//
//        Bundle args = new Bundle();
//        args.putSerializable(CollectionDate.EXTRA_INDICATOR,indicator);
//        args.putSerializable(CollectionDate.EXTRA_PAGER, pager);
//
//        myFragment.setArguments(args);
//        return myFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.collection_location_fragment, container, false);
        back = (Button) layout.findViewById(R.id.back);
        cancel = (Button) layout.findViewById(R.id.cancel);
        next = (Button) layout.findViewById(R.id.next);
        getLocation = (Button) layout.findViewById(R.id.getLocation);
        location = (TextView) layout.findViewById(R.id.location);

        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        next.setOnClickListener(this);

        return layout;
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            CreateCollection.pager.setCurrentItem(0);
        } else if (view == cancel) {
            getActivity().finish();
        } else if (view == next) {
            CreateCollection.pager.setCurrentItem(2);
        } else if (view == getLocation) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String latitude = Double.toString(location.getLatitude());
                    String longtitude = Double.toString(location.getLongitude());
                    Message.showMessage(getActivity(),"Latitude " + latitude + " | " + "Longtitude" + longtitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.print("Second pager resume");
    }
}
