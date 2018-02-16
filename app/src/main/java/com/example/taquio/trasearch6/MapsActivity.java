//package com.example.taquio.trasearch6;

/**
 * Created by User on 10/2/2017.
 */

//public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
//        GoogleApiClient.OnConnectionFailedListener{
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onMapReady: map is ready");
//        mMap = googleMap;
//
//        if (mLocationPermissionsGranted) {
//            getDeviceLocation();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//
//            init();
//        }
//    }
//
//    private static final String TAG = "MapActivity";
//    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//    private static final float DEFAULT_ZOOM = 15f;
//    private static final int PLACE_PICKER_REQUEST = 1;
//    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
//            new LatLng(-40, -168), new LatLng(71, 136));
//    private final int ACTIVITY_NUM = 3;
//    private Context mContext = MapsActivity.this;
//    //widgets
//    private AutoCompleteTextView mSearchText;
//    private ImageView mGps, mInfo, mPlacePicker;
//    //vars
//    private Boolean mLocationPermissionsGranted = false;
//    private GoogleMap mMap;
//    private FusedLocationProviderClient mFusedLocationProviderClient;
//    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
//    private GoogleApiClient mGoogleApiClient;
//    private PlaceInfo mPlace;
//    private Marker mMarker;
//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(@NonNull PlaceBuffer places) {
//            if(!places.getStatus().isSuccess()){
//                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
//                places.release();
//                return;
//            }
//            final Place place = places.get(0);
//
//            try{
//                mPlace = new PlaceInfo();
//                mPlace.setName(place.getName().toString());
//                Log.d(TAG, "onResult: name: " + place.getName());
//                mPlace.setAddress(place.getAddress().toString());
//                Log.d(TAG, "onResult: address: " + place.getAddress());
////                mPlace.setAttributions(place.getAttributions().toString());
////                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
//                mPlace.setId(place.getId());
//                Log.d(TAG, "onResult: id:" + place.getId());
//                mPlace.setLatLng(place.getLatLng());
//                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
//                mPlace.setRating(place.getRating());
//                Log.d(TAG, "onResult: rating: " + place.getRating());
//                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
//                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
//                mPlace.setWebsiteUri(place.getWebsiteUri());
//                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
//
//                Log.d(TAG, "onResult: place: " + mPlace.toString());
//            }catch (NullPointerException e){
//                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
//            }
//
//            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
//                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
//
//            places.release();
//        }
//    };
//    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();
//
//            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
//            final String placeId = item.getPlaceId();
//
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//        }
//    };
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onMapReady: map is ready");
//        mMap = googleMap;
//
//        if (mLocationPermissionsGranted) {
//            getDeviceLocation();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//
//            init();
//        }
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        setupBottomNavigationView();
//
//        mSearchText = findViewById(R.id.input_search);
//        mGps = findViewById(R.id.ic_gps);
//        mInfo = findViewById(R.id.place_info);
//        mPlacePicker = findViewById(R.id.place_picker);
//
//        getLocationPermission();
//
//        initMap();
//
//    }
//
//    private void init(){
//        Log.d(TAG, "init: initializing");
//
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();
//
//        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
//
//        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
//                LAT_LNG_BOUNDS, null);
//
//        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
//
//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
//
//                    //execute our method for searching
//                    geoLocate();
//                }
//
//                return false;
//            }
//        });
//
//        mGps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked gps icon");
//                getDeviceLocation();
//            }
//        });
//
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
//
//        mPlacePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                try {
//                    startActivityForResult(builder.build(MapsActivity.this), PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    Log.e(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage() );
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    Log.e(TAG, "onClick: GooglePlayServicesNotAvailableException: " + e.getMessage() );
//                }
//            }
//        });
//
//        hideSoftKeyboard();
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(this, data);
//
//                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                        .getPlaceById(mGoogleApiClient, place.getId());
//                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//            }
//        }
//    }
//
//    private void geoLocate(){
//        Log.d(TAG, "geoLocate: geolocating");
//
//        String searchString = mSearchText.getText().toString();
//
//        Geocoder geocoder = new Geocoder(MapsActivity.this);
//        List<Address> list = new ArrayList<>();
//        try{
//            list = geocoder.getFromLocationName(searchString, 1);
//        }catch (IOException e){
//            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
//        }
//
//        if(list.size() > 0){
//            Address address = list.get(0);
//
//            Log.d(TAG, "geoLocate: found a location: " + address.toString());
//            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
//
//            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
//                    address.getAddressLine(0));
//        }
//    }
//
//    private void getDeviceLocation(){
//        Log.d(TAG, "getDeviceLocation: getting the devices current location");
//
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        try{
//            if(mLocationPermissionsGranted){
//
//                final Task location = mFusedLocationProviderClient.getLastLocation();
//                location.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if(task.isSuccessful()){
//                            Log.d(TAG, "onComplete: found location!");
//                            Location currentLocation = (Location) task.getResult();
//
//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
//                                    DEFAULT_ZOOM,
//                                    "My Location");
//
//                        }else{
//                            Log.d(TAG, "onComplete: current location is null");
//                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }catch (SecurityException e){
//            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
//        }
//    }
//
//    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//        mMap.clear();
//
//        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MapsActivity.this));
//
//        if(placeInfo != null){
//            try{
//                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
//                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
//                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
//                        "Price Rating: " + placeInfo.getRating() + "\n";
//
//                MarkerOptions options = new MarkerOptions()
//                        .position(latLng)
//                        .title(placeInfo.getName())
//                        .snippet(snippet);
//                mMarker = mMap.addMarker(options);
//
//            }catch (NullPointerException e){
//                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
//            }
//        }else{
//            mMap.addMarker(new MarkerOptions().position(latLng));
//        }
//
//        hideSoftKeyboard();
//    }
//
//    private void moveCamera(LatLng latLng, float zoom, String title){
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//
//        if(!title.equals("My Location")){
//            MarkerOptions options = new MarkerOptions()
//                    .position(latLng)
//                    .title(title);
//            mMap.addMarker(options);
//        }
//
//        hideSoftKeyboard();
//    }
//
//    private void initMap(){
//        Log.d(TAG, "initMap: initializing map");
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(MapsActivity.this);
//    }
//
//    private void getLocationPermission(){
//        Log.d(TAG, "getLocationPermission: getting location permissions");
//        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION};
//
//        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                mLocationPermissionsGranted = true;
//                initMap();
//            }else{
//                ActivityCompat.requestPermissions(this,
//                        permissions,
//                        LOCATION_PERMISSION_REQUEST_CODE);
//            }
//        }else{
//            ActivityCompat.requestPermissions(this,
//                    permissions,
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    /*
//        --------------------------- google places API autocomplete suggestions -----------------
//     */
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult: called.");
//        mLocationPermissionsGranted = false;
//
//        switch(requestCode){
//            case LOCATION_PERMISSION_REQUEST_CODE:{
//                if(grantResults.length > 0){
//                    for(int i = 0; i < grantResults.length; i++){
//                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
//                            mLocationPermissionsGranted = false;
//                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
//                            return;
//                        }
//                    }
//                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
//                    mLocationPermissionsGranted = true;
//                    //initialize our map
//                    initMap();
//                }
//            }
//        }
//    }
//
//    private void hideSoftKeyboard(){
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }
//
//    /*
//        --------------------------- google places API autocomplete suggestions -----------------
//     */
//
//    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();
//
//            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
//            final String placeId = item.getPlaceId();
//
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//        }
//    };
//
//    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
//        @Override
//        public void onResult(@NonNull PlaceBuffer places) {
//            if(!places.getStatus().isSuccess()){
//                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
//                places.release();
//                return;
//            }
//            final Place place = places.get(0);
//
//            try{
//                mPlace = new PlaceInfo();
//                mPlace.setName(place.getName().toString());
//                Log.d(TAG, "onResult: name: " + place.getName());
//                mPlace.setAddress(place.getAddress().toString());
//                Log.d(TAG, "onResult: address: " + place.getAddress());
////                mPlace.setAttributions(place.getAttributions().toString());
////                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
//                mPlace.setId(place.getId());
//                Log.d(TAG, "onResult: id:" + place.getId());
//                mPlace.setLatLng(place.getLatLng());
//                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
//                mPlace.setRating(place.getRating());
//                Log.d(TAG, "onResult: rating: " + place.getRating());
//                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
//                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
//                mPlace.setWebsiteUri(place.getWebsiteUri());
//                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
//
//                Log.d(TAG, "onResult: place: " + mPlace.toString());
//            }catch (NullPointerException e){
//                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
//            }
//
//            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
//                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);
//
//            places.release();
//        }
//    };
//
//    /*
//    * buttom navigation
//    * */
//    private void setupBottomNavigationView() {
//        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
//        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }

//}