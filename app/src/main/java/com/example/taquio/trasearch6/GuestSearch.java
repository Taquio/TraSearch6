package com.example.taquio.trasearch6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImageView;

/**
 * Created by Del Mar on 2/12/2018.
 */

public class GuestSearch extends AppCompatActivity {
    private static final String TAG = "GuestSearch";

    private FirebaseAuth mAuth;

    private Button signIn, reg;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_search);
        refIDs();
        mAuth = FirebaseAuth.getInstance();
        MobileAds.initialize(GuestSearch.this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuestSearch.this,ActivityLogin.class));
                finish();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuestSearch.this,ChooseLayout.class));
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user !=null)
        {
            Toast.makeText(GuestSearch.this,"Welcome",Toast.LENGTH_SHORT).show();
            Intent startActivityIntent = new Intent(GuestSearch.this, HomeActivity2.class);
            startActivity(startActivityIntent);
            GuestSearch.this.finish();
        }
    }

    public void refIDs()
    {
        signIn = findViewById(R.id.guest_SignIn);
        reg = findViewById(R.id.guest_Reg);
    }
}