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

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        refIDs();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = field_email.getText().toString(),
                        password = field_password.getText().toString(),
                        username = field_username.getText().toString(),
                        name = field_name.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
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
        });

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
