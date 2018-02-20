package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.taquio.trasearch6.BusinessHome.BusinessHome;

/**
 * Created by Edward on 20/02/2018.
 */

public class BusinessRegActivity extends AppCompatActivity{

    private Context mContext = BusinessRegActivity.this;
    Button busRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_register);

        busRegister = findViewById(R.id.registerBtn1);
        busRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, BusinessHome.class));
            }
        });

    }
}
