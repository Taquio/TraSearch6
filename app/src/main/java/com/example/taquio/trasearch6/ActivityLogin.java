package com.example.taquio.trasearch6;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Del Mar on 2/4/2018.
 */

public class ActivityLogin extends AppCompatActivity {

    private static final String TAG = "ActivityLogin";
    ImageView btn_search;
    Button btn_register,btn_login;
    EditText Lfield_email,Lfield_password,traSearch_bar;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,mUserType;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Log.d(TAG, "onCreate: Started");


        refIDs();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: login clicked");

                String email = Lfield_email.getText().toString(),
                        password = Lfield_password.getText().toString();

                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(ActivityLogin.this,"Error",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: SignIN Sucess");
                                        String current_userID = mAuth.getCurrentUser().getUid();
                                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                        databaseReference
                                                .child(current_userID)
                                                .child("device_token")
                                                .setValue(deviceToken)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Log.d(TAG, "onComplete: Device Token Success");
                                                    DatabaseReference mUserType = FirebaseDatabase.getInstance().getReference().child("Users")
                                                            .child(mAuth.getCurrentUser().getUid());

                                                    mUserType.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String userType = dataSnapshot.child("userType").getValue().toString();
                                                            Log.d(TAG, "onDataChange: UserType: "+userType);
                                                            if(dataSnapshot.child("userType").getValue().toString().equals("free"))
                                                            {
                                                                Log.d(TAG, "signInWithEmail:success");
                                                                mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//
                                                                Intent startActivityIntent = new Intent(ActivityLogin.this, HomeActivity2.class);
                                                                startActivity(startActivityIntent);
                                                                ActivityLogin.this.finish();
                                                            }
                                                            else if(dataSnapshot.child("userType").getValue().toString().equals("admin"))
                                                            {
                                                                Log.d(TAG, "signInWithEmail:success");
                                                                mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
//
                                                                Intent startActivityIntent = new Intent(ActivityLogin.this, AdminActivity.class);
                                                                startActivity(startActivityIntent);
                                                                ActivityLogin.this.finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }else{

                                                }
                                            }
                                        });
                                        // Sign in success, update UI with the signed-in user's information

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    //..sadasdasd
                                }
                            });
                }

            }
        });
    }

    private boolean hasRegError()
    {
        final String email = Lfield_email.getText().toString(),
                password = Lfield_password.getText().toString();
        boolean flag = true;

        if(email.length()<=0)
        {
            Lfield_email.setError("Please input a valid Email Address");
        }
        else if(password.length()<8)
        {
            Lfield_password.setError("Password must be more than 8 characters");
        }
        else
        {
            flag=false;
        }

        return flag;

    }

//    public void checkUserExists()
//    {
//        Log.d(TAG, "checkUserExists: Started");
//        final String user_id = mAuth.getCurrentUser().getUid();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(user_id))
//                {
//                    Log.d(TAG, "onDataChange: Auth Success");
//                    Toast.makeText(ActivityLogin.this, "Authentication success.",
//                            Toast.LENGTH_SHORT).show();
////                    mUserRef.child("online").setValue(true);
//                    Intent startActivityIntent = new Intent(ActivityLogin.this, HomeActivity2.class);
//                    startActivity(startActivityIntent);
//                    ActivityLogin.this.finish();
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

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
            DatabaseReference userType = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(mAuth.getCurrentUser().getUid());

            userType.child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userType = dataSnapshot.getValue().toString();
                    if(userType.equals("free"))
                    {
                        startActivity(new Intent(ActivityLogin.this,HomeActivity2.class));
                        finish();
                    }
                    else if(userType.equals("admin"))
                    {
                        startActivity(new Intent(ActivityLogin  .this,AdminActivity.class));
                        finish();
                    }
                    else if(userType.equals("business"))
                    {
                        startActivity(new Intent(ActivityLogin.this,BusinessProfileActivity.class));
                        finish();
                    }else{
                        Toast.makeText(ActivityLogin.this,"UserType is null",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void refIDs()
    {
        btn_login = findViewById(R.id.btn_login);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        Lfield_email = findViewById(R.id.Lfield_email);
        Lfield_password = findViewById(R.id.Lfield_password);
    }

}