package com.example.taquio.trasearch6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    Button Lbtn_submit;
    EditText Lfield_email,Lfield_password;
    TextView noAccountYet;
    FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: start login");
        refIDs();

        noAccountYet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = Lfield_email.getText().toString();

                Intent startActivityIntent = new Intent(LoginActivity.this, RegisterActivity.class);

                if(email.length()<=0)
                {
                    startActivity(startActivityIntent);
                    LoginActivity.this.finish();
                }
                else
                {
                    Log.d(TAG, "onClick: Passing: "+email+" to Reg Act");
                    startActivityIntent.putExtra("emailPass",email);
                    startActivity(startActivityIntent);
                    LoginActivity.this.finish();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        Lbtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Lfield_email.getText().toString(),password = Lfield_password.getText().toString();

                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        checkUserExists();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    //..sadasdasd
                                }
                            });
                }
            }
        });
    }

    public void checkUserExists()
    {
        final String user_id = mAuth.getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id))
                {
                    Toast.makeText(LoginActivity.this, "Authentication success.",
                            Toast.LENGTH_SHORT).show();
                    Intent startActivityIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(startActivityIntent);
                    LoginActivity.this.finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

    private void refIDs(){
        Lbtn_submit = findViewById(R.id.Lbtn_submit);
        Lfield_email = findViewById(R.id.Lfield_email);
        Lfield_password = findViewById(R.id.Lfield_password);
        noAccountYet = findViewById(R.id.noAccountYet);

    }
}
