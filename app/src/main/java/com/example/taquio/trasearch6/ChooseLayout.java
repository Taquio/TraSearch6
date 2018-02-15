package com.example.taquio.trasearch6;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by Del Mar on 2/12/2018.
 */

public class ChooseLayout extends AppCompatActivity {

    Button btn, btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_layout);

        btn = (Button) findViewById(R.id.btnNonB);
        btn2 = (Button) findViewById(R.id.btnYesB);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChooseLayout.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }
}

