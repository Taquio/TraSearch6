package com.example.taquio.trasearch6;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.SettingsApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
//    implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener

    private static final String TAG = MapsActivity.class.getSimpleName();
    private final int locationReqestCode = 1234;
    private GoogleMap mMap;
    private GoogleApiClient mApiClient;
    private SeekBar seekBar;
    //vars
    private Boolean mLocationGranted = false;
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = MapsActivity.this;
    private Circle circle;
    private Marker marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Log.d(TAG, "onCreate: Map Started");
        mapInit();
        setupBottomNavigationView();
    }
    private void mapInit() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap != null){
//            change info window values when dragged
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    Geocoder gc = new Geocoder(MapsActivity.this);
                    LatLng ll = marker.getPosition();
                    double lat = ll.latitude,
                        lng = ll.longitude;
                    List<Address> list = null;
                    try {
                        list = gc.getFromLocation(lat, lng, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = (Address) list.get(0);
                    String addressStr = "";
                    addressStr += address.getAddressLine(0) + ", ";
                    addressStr += address.getAddressLine(1) + ", ";
                    addressStr += address.getAddressLine(2);
                    marker.setTitle(""+addressStr);
                    marker.showInfoWindow();
                }
            });

//            adds info window to the marker
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.map_info_window, null);
//                    reference the fields
                    TextView address = v.findViewById(R.id.mapaddress);
                    TextView lat = v.findViewById(R.id.maplatitude);
                    TextView lng = v.findViewById(R.id.maplongitude);
                    TextView snip = v.findViewById(R.id.mapsnippet);
//                    set the values to the map_info_window
                    LatLng ll = marker.getPosition();
                    address.setText(marker.getTitle());
                    lat.setText("Latitude: " +ll.latitude);
                    lng.setText("Longitude: " +ll.longitude);
                    snip.setText(marker.getSnippet());

                    return v;
                }
            });
        }

        mMap.setMyLocationEnabled(true);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mApiClient.connect();
    }

    LocationRequest locationReq;
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationReq = LocationRequest.create();
        locationReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient,locationReq,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
// get your place through GPS and put marker
    @Override
    public void onLocationChanged(Location location) {
        LatLng ll;
        double lat = location.getLatitude(),
                lng = location.getLongitude();
        if(location == null)
        {
            Toast.makeText(this, "Can't get current location",Toast.LENGTH_LONG).show();
        }else{
            ll = new LatLng(lat,lng);
//            Know the place name
            Geocoder myLocation = new Geocoder(this, Locale.getDefault());
            List<Address> myList = null;
            try {
                myList = myLocation.getFromLocation(lat,lng,1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = (Address) myList.get(0);
            String addressStr = "";
            addressStr += address.getAddressLine(0) + ", ";
            addressStr += address.getAddressLine(1) + ", ";
            addressStr += address.getAddressLine(2);
//          Camera zoom
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
            mMap.animateCamera(update);
//          Name of Place
            Toast.makeText(this, ""+addressStr, Toast.LENGTH_LONG).show();
//            marker
            setMarker(lat, lng, addressStr);
        }

    }
//    creating marker
    private void setMarker(double lat, double lng, String addressStr) {
        if(marker != null)
        {
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .title(addressStr)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .snippet("I am here")
                .position(new LatLng(lat,lng));
        marker = mMap.addMarker(markerOptions);

        circle = drawCircle(new LatLng(lat,lng));
    }
//    creating circle
    private Circle drawCircle(LatLng latLng) {
        CircleOptions options = new CircleOptions()
                .center(latLng)
                .radius(5000)
                .fillColor(0x33FF0000)
                .strokeColor(Color.GREEN)
                .strokeWidth(3);
        return mMap.addCircle(options);
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
