package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase,mAllUsersDatabase;
    private FirebaseUser mCurrentUser;
    private EditText ediProfile_mobile
            ,ediProfile_name
            ,ediProfile_email,
            ediProfile_password;
    private TextView ediProfile_changeImage;
    private CircleImageView ediProfile_image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        refIDs();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ediProfile_name.setHint(dataSnapshot.child("Name").getValue().toString());
                ediProfile_email.setHint(dataSnapshot.child("Email").getValue().toString());
                ediProfile_password.setText(dataSnapshot.child("Email").getValue().toString());
                ediProfile_mobile.setText(dataSnapshot.child("PhoneNumber").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void refIDs()
    {
        ediProfile_mobile = findViewById(R.id.ediProfile_mobile);
        ediProfile_name = findViewById(R.id.ediProfile_name);
        ediProfile_email = findViewById(R.id.ediProfile_email);
        ediProfile_password = findViewById(R.id.ediProfile_password);
        ediProfile_changeImage = findViewById(R.id.ediProfile_changeImage);
        ediProfile_image = findViewById(R.id.ediProfile_image);


    }
}
