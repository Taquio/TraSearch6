package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Edward on 20/02/2018.
 */

public class BusinessRegActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener{

    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private Context mContext = BusinessRegActivity.this;
    private GoogleApiClient mGoogleClient;
    EditText bsnMail, bsnPass, bsnConPass, bsnBusinessName, bsnLocation, bsnPhone, bsnMobile;
    Button busContinue, btnLoc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_register1);

        bsnMail = (EditText) findViewById(R.id.bsnMail);
        bsnPass = (EditText) findViewById(R.id.bsnPass);
        bsnConPass = (EditText) findViewById(R.id.bsnConPass);
        bsnBusinessName = (EditText) findViewById(R.id.bsnBusinessName);
        bsnLocation = (EditText) findViewById(R.id.bsnLocation);
        bsnPhone = (EditText) findViewById(R.id.bsnPhone);
        bsnMobile = (EditText) findViewById(R.id.bsnMobile);
        //comment
        busContinue = findViewById(R.id.registerCont);
        busContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bsnMail.getText().toString();
                String pass = bsnPass.getText().toString();
                String conpass = bsnConPass.getText().toString();
                String businessname = bsnBusinessName.getText().toString();
                String location = bsnLocation.getText().toString();
                String phone = bsnPhone.getText().toString();
                String mobile = bsnMobile.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(conpass) && !TextUtils.isEmpty(businessname)
                        && !TextUtils.isEmpty(location) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(mobile)) {

                    if(pass.equals(conpass)) {
                        Intent i = new Intent (mContext, BusinessRegActivity2.class);
                        i.putExtra("EMAIL",email);
                        i.putExtra("PASS",pass);
                        i.putExtra("BUSINESSNAME",businessname);
                        i.putExtra("LOCATION",location);
                        i.putExtra("PHONE",phone);
                        i.putExtra("MOBILE",mobile);
                        startActivity(i);
                        BusinessRegActivity.this.finish();
                    }else {
                        Toast.makeText(mContext, "Password does not match.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(mContext, "Please fill up all fields.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLoc = (Button) findViewById(R.id.btnLoc);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(BusinessRegActivity.this);

                try {
                    if (mLocationPermissionsGranted) {

                        final Task location = mFusedLocationProviderClient.getLastLocation();
                        location.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Location currentLocation = (Location) task.getResult();
                                    double latitude = currentLocation.getLatitude();
                                    double longitude = currentLocation.getLongitude();

                                    bsnLocation.setText(Double.toString(longitude) + " " + Double.toString(latitude));
                                    //textView.setText(""+currentLocation.getLatitude+""+currentLocation.getLongitude);

                                } else {
                                    Toast.makeText(BusinessRegActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (SecurityException e) {

                }
            }
        });

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

    private void getLocationPermission() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;

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
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            if (mGoogleClient == null) {
                                buildGoogleApiClient();
                            }
                            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED)
                            {
                                return;
                            }
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

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
}
