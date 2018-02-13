package com.example.taquio.trasearch6;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
// declarations
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mGoogleMap;
    private EditText mSearchInput;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap =  googleMap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        refId();

        final String search = mSearchInput.getText().toString();
//        edittext perform search when enter key is pressed
        try {
            mSearchInput.setOnEditorActionListener(
                    new EditText.OnEditorActionListener(){
                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        List<Address> list = geocoder.getFromLocationName(search, 1);
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            // Identifier of the action. This will be either the identifier you supplied,
                            // or EditorInfo.IME_NULL if being called due to the enter key being pressed.
                            if (actionId == EditorInfo.IME_ACTION_SEARCH
                                    || actionId == EditorInfo.IME_ACTION_DONE
                                    || event.getAction() == KeyEvent.ACTION_DOWN
                                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                            {
                                Address address = list.get(0);
                                String locality = address.getLocality();

                                double lat = address.getLatitude();
                                double lng = address.getLongitude();

                                goToLocation(lat, lng, DEFAULT_ZOOM);

                                return true;
                            }
                            // Return true if you have consumed the action, else false.
                            return false;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

//            mSearchInput.setOnKeyListener(new View.OnKeyListener() {
//                Geocoder geocoder = new Geocoder(MapsActivity.this);
//                List<Address> list = geocoder.getFromLocationName(search, 1);
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_SEARCH
//                            || keyCode == KeyEvent.ACTION_DOWN
//                            || keyCode == KeyEvent.KEYCODE_ENTER)
//                    {
//                        Address address = list.get(0);
//                        String locality = address.getLocality();
//
//                        double lat = address.getLatitude();
//                        double lng = address.getLongitude();
//
//                        goToLocation(lat, lng, DEFAULT_ZOOM);
//                    }
//                    return false;
//                }
//            });

    }

    private void goToLocation(double lat, double lng, float defaultZoom)
    {
        LatLng latLng = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }

    private void refId()
    {
        mSearchInput = (EditText) findViewById(R.id.input_search);

    }
//    initialize google map
    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
