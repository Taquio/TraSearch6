package com.example.taquio.trasearch6;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch6.modal.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARVN 02-16-2018.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 13f;
    private static final int PLACE_PICKER_REQUEST = 1;
    //    south west / north east
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    //    for the bottom navigation
    private Context mContext = MapsActivity.this;
    private final int ACTIVITY_NUM = 3;
    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageButton mGps, mInfo, mPlacePicker;
    private RadioButton mRbRecyclingCenter, mRbJunkyard;
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;// for the autocomplete textview
    private GoogleApiClient mGoogleClient; // for loading all the recycling center
    private PlaceInfo mPlace;
    private Marker mMarker;
    //store lat lang in firebase and retrieve
    private DatabaseReference mDatabase;
    private DatabaseReference refDatabase;
    //for loading all recycling centers
    private LocationRequest locationRequest;
    private Location lastlocation;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;

    /*
    *   Necessary methods that is needed
    * */
    @Override
    public void onLocationChanged(Location location)
    {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
        lastlocation = location;
        if (mMarker != null)
        {
            mMarker.remove();
        }
        Log.d("lat = ", "" + latitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        mMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if (mGoogleClient != null)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, (com.google.android.gms.location.LocationListener) this);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");

        mMap = googleMap;

//        loadStaticMarker();//static location

        /*
        * create firebase for receiving latitude longitude values
        * */

        //load latitude longitude from database and put markers
//        refDatabase.addChildEventListener(new ChildEventListener()
//        {
//              @Override
//              public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey)
//              {
//                  LatLng newLocation = new LatLng(
//                          dataSnapshot.child("latitude").getValue(Long.class),
//                          dataSnapshot.child("longitude").getValue(Long.class)
//                  );
//                  mMap.addMarker(new MarkerOptions()
//                          .position(newLocation)
//                          .title(dataSnapshot.getKey()));
//              }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });//end refDatabase
//
//        //when map is clicked store latitude and longitude to firebase
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                Marker marker = mMap.addMarker(new MarkerOptions()
//                .position(latLng));
//                final LatLng latlng = marker.getPosition();
//
//                DatabaseReference newPost = mDatabase.push();
//                newPost.setValue(latlng);
//            }
//        });//end

        if (mLocationPermissionsGranted) {
//            loadStaticMarker();
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mMap.setMyLocationEnabled(true);

            init();
        }
    }//end onMapReady

    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleClient.connect();

    }

    /*
    *       start program flow
    * */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //buttom navigation method
        setupBottomNavigationView();

        refId();

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

//        firebase retrieval latitude and longitude
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Navigation");
//        refDatabase = FirebaseDatabase.getInstance().getReference().child("Location");

        getLocationPermission();

        initMap();
    }//end onCreate

    private void refId()
    {
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
//        mGps = (ImageButton) findViewById(R.id.ic_gps);
//        mInfo = (ImageButton) findViewById(R.id.place_info);
        mPlacePicker = (ImageButton) findViewById(R.id.place_picker);
    }

    private void loadStaticMarker() {
        //preset marker

        //Ma-Vill
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3540675, 123.9208625))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Ma-Vill")
//                .visible(false)
                .snippet("Recycling Center"));
        //E & J Scrap Buyer
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3744345, 123.9138125))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("E & J Scrap Buyer")
//                .visible(false)
                .snippet("Recycling Center"));
        //TSL Enterprises
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.33493, 123.9316495))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("TSL Enterprises")
//                .visible(false)
                .snippet("Recycling Center"));
        //Naturefress
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3540562, 123.9208624))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Naturefress")
//                .visible(false)
                .snippet("Bottle & Can Redemption Center"));
        //Bakilid Junk Shop Corner
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3469118, 123.9289209))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Bakilid Junk Shop Corner")
//                .visible(false)
                .snippet("Junkyard"));
        //Thes Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3532373, 123.9422024))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Thes Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Pables Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3532307, 123.9422023))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Pables Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Elica Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3830479, 123.9456331))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Elica Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Cindy's Enterprises
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3830158, 123.8408229))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Cindy's Enterprises")
//                .visible(false)
                .snippet("Junkyard"));
        //Roadside Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3396889, 123.9293173))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Roadside Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Pab Les Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3261964, 123.9522611))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Pab Les Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Meme Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3141685, 123.9235954))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Meme Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Kerjaspher Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3119518, 123.9091465))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Kerjaspher Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Ajj Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3164531, 123.9130684))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Ajj Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Montejo Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3216555, 123.9222994))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Montejo Junk Shop")
//                .visible(false)
                .snippet("Junkyard"));
        //Bakilid Junk Shop Corner
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3381261, 123.9354665))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Bakilid Junk Shop Corner")
//                .visible(false)
                .snippet("Junkyard"));
        //Jr Cabato Junk Shop
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10.3021436, 123.9037193))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Jr Cabato Junk Shopr")
//                .visible(false)
                .snippet("Junkyard"));

        //camera zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.3532373, 123.9422024)
                , DEFAULT_ZOOM));
    }

    private void init() {
        Log.d(TAG, "init: initializing");

        // filter the autocomplete suggestion to Philippines only
        AutocompleteFilter filter = new AutocompleteFilter
                .Builder()
                .setTypeFilter(Place.TYPE_ESTABLISHMENT)
                .setCountry("PH")
                .build();

        //set Autocompletelistener to AutocompleteTextView
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        //passing of necessary objects to AutocompleteAdaper
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter
                (this,
                        mGoogleApiClient,
                        LAT_LNG_BOUNDS,
                        filter);

        //set AutocompleteAdapter as adapter in AutocompleteTextView
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == KeyEvent.KEYCODE_SEARCH
                        || actionId == KeyEvent.KEYCODE_ENTER) {

                    hideSoftKeyboard();
                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

//        mGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked gps icon");
//
//                getDeviceLocation();
//            }
//        });

//        mInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked place info");
//                try{
//                    if(mMarker.isInfoWindowShown()){
//                        mMarker.hideInfoWindow();
//                    }else{
//                        Log.d(TAG, "onClick: place info: " + mPlace.toString());
//                        mMarker.showInfoWindow();
//                    }
//                }catch (NullPointerException e){
//                    Log.e(TAG, "onClick: NullPointerException: " + e.getMessage() );
//                }
//            }
//        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage());
                }
            }
        });

        hideSoftKeyboard();
    }
    //method to load Recycling Center
    private void loadRecyclingCenters()
    {
        Object dataTransfer[] = new Object[2];
        getNearbyPlacesData = new GetNearbyPlacesData();

        String recyclingCenter = "recycling+center+junkyard";
        String url = getUrl(latitude, longitude, recyclingCenter);
        Toast.makeText(this, "Check long: "+longitude+"lat: "+latitude, Toast.LENGTH_SHORT).show();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        getNearbyPlacesData.execute(dataTransfer);
        Toast.makeText(MapsActivity.this, "Showing Nearby Recycling Centers", Toast.LENGTH_SHORT).show();
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
        googlePlaceUrl.append("query="+nearbyPlace);
        googlePlaceUrl.append("&location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&key="+"AIzaSyDvsia0V9CUml-qj5BIhEiOtnMdT27EhMs");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                            //load all recycling centers in first load of map
                            loadRecyclingCenters();

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));

        if (placeInfo != null) {
            try {
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);

            } catch (NullPointerException e) {
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage());
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            if (mGoogleClient == null) {
                                buildGoogleApiClient();
                            }
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED)
                            {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        ---------------------------Autocomplete suggestions -----------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
                mPlace.setType(place.getAttributions().toString());
                Log.d(TAG, "onResult: attributions: " + place.getPlaceTypes());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatLng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };

    /*
    * buttom navigation
    * */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}