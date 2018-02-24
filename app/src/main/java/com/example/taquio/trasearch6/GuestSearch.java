package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Del Mar on 2/12/2018.
 */

public class GuestSearch extends AppCompatActivity {
    private static final String TAG = "GuestSearch";

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private Button signIn, reg,searchExec;
    private EditText searchText;
    private DatabaseReference searchItem;
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
                progressDialog = new ProgressDialog(GuestSearch.this);
                progressDialog.setTitle("Searching");
                progressDialog.setMessage("Please wait while we CRAWL");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                final String search = searchText.getText().toString();

                DatabaseReference mTraSearch;
                mTraSearch = FirebaseDatabase.getInstance().getReference().child("TraSearch");
                searchItem = FirebaseDatabase.getInstance().getReference().child("TraSearch").child(search).child("Videos");


                mTraSearch.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(search).exists())
                        {
                            Spider spider = new Spider();
                            Map<Integer, CrawledData> videoLinks;
                            Map tobeUpload = new HashMap();
                            videoLinks = spider.searchEngine(search);
                            for(Integer index: videoLinks.keySet()){
                                Integer key = index;
                                CrawledData value = videoLinks.get(key);
                                String[] newLink = value.getUrl().split("v=");


                                tobeUpload.put(newLink[1]+"/Title",value.getTitle());
                            }

                            searchItem.updateChildren(tobeUpload).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                    {
                                        Log.d(TAG, "onComplete: Search Completed");
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        Log.d(TAG, "onComplete: Search Failed");
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }else{
                            Log.d(TAG, "onDataChange: "+search+" already in the DAtabase");
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





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