package com.example.taquio.trasearch6;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminVerification extends AppCompatActivity {
    private static final String TAG = "AdminVerification";
    String user_id;
    DatabaseReference userDatabase;
    FirebaseAuth mAuth;
    private Button ver_IDbtn,ver_Selfiebtn,ver_Skipbtn,ver_UploadExec;
    private ImageView ver_ID,ver_Selfie;
    private Uri selfie,mID;
    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verification);
        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        ver_ID = findViewById(R.id.ver_ID);
        ver_Selfie = findViewById(R.id.ver_Selfie);
        ver_IDbtn = findViewById(R.id.ver_IDbtn);
        ver_Selfiebtn = findViewById(R.id.ver_Selfiebtn);
        ver_Skipbtn = findViewById(R.id.ver_Skipbtn);
        ver_UploadExec = findViewById(R.id.ver_UploadExec);
        user_id  = getIntent().getStringExtra("user_id");
        Log.d(TAG, "onCreate: UserID "+user_id);
        userDatabase.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("IDD")||dataSnapshot.hasChild("selfieURL"))
                {
                    Log.d(TAG, "onDataChange: ID URRL"+dataSnapshot
                            .child("IDURL").getValue().toString());
                    ver_Skipbtn.setText("Verify");
                    ver_UploadExec.setText("Revoke");
                    Picasso.with(AdminVerification.this)
                            .load(dataSnapshot
                                    .child("IDURL").getValue().toString())
                            .placeholder(R.drawable.man)
                            .into(ver_ID);
                    Log.d(TAG, "onDataChange: ID URRL"+dataSnapshot
                            .child("selfieURL").getValue().toString());

                    Picasso.with(AdminVerification.this)
                            .load(dataSnapshot
                                    .child("selfieURL").getValue().toString())
                            .placeholder(R.drawable.man)
                            .into(ver_Selfie);
                    ver_Skipbtn.setEnabled(true);
                    ver_UploadExec.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ver_Skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatabase.child(user_id).child("isVerify").setValue(true);
                startActivity(new Intent(AdminVerification.this,AdminActivity.class));
                finish();
            }
        });


    }
}
