package com.example.taquio.trasearch6;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppStart extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText traSearch;
    Button btn_search,btn_register,btn_login;

    private static final String TAG = AppStart.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        refIDs();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        mAuth = FirebaseAuth.getInstance();

        

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivityIntent = new Intent(AppStart.this, RegisterActivity.class);
                startActivity(startActivityIntent);
                AppStart.this.finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startActivityIntent = new Intent(AppStart.this, LoginActivity.class);
                startActivity(startActivityIntent);
                AppStart.this.finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user !=null)
        {
            Toast.makeText(AppStart.this,"Welcome",Toast.LENGTH_SHORT).show();
            Intent startActivityIntent = new Intent(AppStart.this, HomeActivity.class);
            startActivity(startActivityIntent);
            AppStart.this.finish();
        }
    }

    private void refIDs()
    {
        traSearch = findViewById(R.id.trasearch);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_search = findViewById(R.id.btn_search);
    }
}
