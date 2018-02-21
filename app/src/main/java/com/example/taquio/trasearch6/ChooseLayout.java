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

    Button nonBusiness, business;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_layout);


        refIDs();
        nonBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseLayout.this,RegisterActivity.class));
                finish();

            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseLayout.this,BusinessRegActivity.class));
                finish();
            }
        });
    }

    public void refIDs()
    {
        nonBusiness = findViewById(R.id.btnNonBusiness);
        business = findViewById(R.id.btnBusiness);
    }
}

