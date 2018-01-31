package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    EditText field_email,field_password,field_username,field_name;
    Button btn_submit;

    private static final String TAG = "RegisterActivity";

    String email;
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

                final String password = field_password.getText().toString();
                final String username = field_username.getText().toString();
                final String name = field_name.getText().toString();
                email = field_email.getText().toString();
                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Log.d(TAG, "onClick: No Error");
                    field_password.setError(null);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "onComplete: Success Registration");
                                        String user_id=mAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = databaseReference.child(user_id);
                                        current_user_db.child("Name").setValue(name);
                                        current_user_db.child("UserName").setValue(username);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

    }

    private boolean hasRegError()
    {
        final String email = field_email.getText().toString(),
                password = field_password.getText().toString(),
                username = field_username.getText().toString(),
                name = field_name.getText().toString();
        boolean flag = true;

        if(email.length()<=0)
        {
            field_email.setError("Please input a valid Email Address");
        }
        else if(name.length()<=0)
        {
            field_name.setError("Please input a valid Name");
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
            Toast.makeText(RegisterActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
            Intent startActivityIntent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(startActivityIntent);
            RegisterActivity.this.finish();
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
        field_name = findViewById(R.id.field_name);
    }
}
