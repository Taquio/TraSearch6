package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    String email;
    ProgressDialog regProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText field_email
            ,field_password
            ,field_username
            ,field_cPassword
            ,field_name
            ,field_phonenumber;
    private Button btn_submit;
//    private ImageButton chooseImage;
    private ImageView userProfileImage;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        refIDs();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        if(getIntent().hasExtra("emailPass"))
        {
            email = getIntent().getStringExtra("emailPass");
            field_email.setText(email);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit button clicked");

                regProgress = new ProgressDialog(RegisterActivity.this);
                regProgress.setTitle("Registering");
                regProgress.setMessage("Please wait while we verify your data");
                regProgress.show();
                
                final String pass = field_password.getText().toString(),
                        cPass = field_cPassword.getText().toString();

                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(RegisterActivity.this,"Please check your Registration Details",Toast.LENGTH_SHORT).show();
                    regProgress.dismiss();
                }
                
                else if (!(pass.equals(cPass)))
                {
                    field_password.setError("Password didn't match");
                    field_cPassword.setError("Password didn't match");
                    regProgress.dismiss();
                }
                else
                {
                    email = field_email.getText().toString();
                    Log.d(TAG, "onClick: Registering User");
                    DatabaseReference checkUserName = FirebaseDatabase.getInstance().getReference();

                    checkUserName.child("Users").child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(field_username
                                    .getText()
                                    .toString()))
                            {
                                field_username.setError("Username already exist");
                                regProgress.dismiss();
                            }
                            else
                            {
                                addUser(field_email
                                .getText()
                                .toString(),field_password
                                .getText()
                                .toString());
                                regProgress.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void addUser(String email,String password)
    {
        Log.d(TAG, "addUser: Started");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "onComplete: Success Registration");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            regProgress.dismiss();
                            updateUI(null);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            userProfileImage.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean hasRegError()
    {
        final String email = field_email.getText().toString(),
                password = field_password.getText().toString(),
                username = field_username.getText().toString(),
                name = field_username.getText().toString();
        boolean flag = true;

        if(email.length()<=0)
        {
            field_email.setError("Please input a valid Email Address");
        }
        else if(name.length()<=0)
        {
            field_username.setError("Please input a valid Name");
        }
        else if (username.length()<=0)
        {
            field_username.setError("Please input a valid Username");
        }
        else if(password.length()<8)
        {
            field_password.setError("Password must be more than 8 characters");
        }
        else
        {
            flag=false;
        }

        return flag;

    }

    private void updateUI(FirebaseUser user)
    {
        if(user !=null)
        {
            Log.d(TAG, "updateUI: Adding User Details to Database");


            final String username = field_username.getText().toString();
            final String name = field_name.getText().toString().toUpperCase();
            email = field_email.getText().toString();
            final String phonenumber = field_phonenumber.getText().toString();
            final String user_id=mAuth.getCurrentUser().getUid();
            final DatabaseReference current_user_db = databaseReference.child(user_id);

            String deviceToken = FirebaseInstanceId.getInstance().getToken();
            Map userDetails = new HashMap();
            userDetails.put("Email",email);
            userDetails.put("Name",name);
            userDetails.put("UserName",username);
            userDetails.put("Image","default");
            userDetails.put("device_token",deviceToken);
            userDetails.put("PhoneNumber",phonenumber);


            current_user_db.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        regProgress.dismiss();
                        Toast.makeText(RegisterActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                        Intent startActivityIntent = new Intent(RegisterActivity.this, HomeActivity2.class);
                        startActivity(startActivityIntent);
                        finish();
                    }
                }
            });

        }
       else
        {
            Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void refIDs()
    {
        field_email = findViewById(R.id.field_email);
        field_password = findViewById(R.id.field_password);
        btn_submit = findViewById(R.id.btn_submit);
        field_username = findViewById(R.id.field_username);
//        field_name = findViewById(R.id.field_name);
//        chooseImage = findViewById(R.id.register_chooseImage);
//        userProfileImage = findViewById(R.id.register_image);
        field_cPassword = findViewById(R.id.field_cPassword);
        field_name = findViewById(R.id.field_name);
        field_phonenumber = findViewById(R.id.field_phonenumber);

    }
}
