package com.example.taquio.trasearch6;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

/**
 * Created by Del Mar on 2/12/2018.
 */

public class GuestSearch extends AppCompatActivity {
    private static final String TAG = "GuestSearch";

    private FirebaseAuth mAuth;

    private Button signIn, reg,searchExec;
    private EditText searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_search);
        refIDs();
        mAuth = FirebaseAuth.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

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
        searchExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString();
                Spider spider = new Spider();
                HashMap<Integer, CrawledData> videoLinks = new HashMap<Integer, CrawledData>();
                videoLinks = spider.searchEngine(search);
                CrawledData value = videoLinks.get(0);


                Log.d(TAG, "onClick: Search Result: "+value.getUrl());

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
        searchExec = findViewById(R.id.searchExec);
        searchText = findViewById(R.id.searchText);
    }
}