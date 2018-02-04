package com.example.taquio.trasearch6;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by Del Mar on 2/4/2018.
 */

public class ActivityLogin extends AppCompatActivity {
    EditText username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        username = (EditText) findViewById(R.id.loginEt2);
        //123
    }
}