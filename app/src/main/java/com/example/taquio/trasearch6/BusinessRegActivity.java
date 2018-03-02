package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Edward on 20/02/2018.
 */

public class BusinessRegActivity extends AppCompatActivity{

    private Context mContext = BusinessRegActivity.this;
    EditText bsnMail, bsnPass, bsnConPass, bsnBusinessName, bsnLocation, bsnPhone, bsnMobile;
    Button busContinue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_register);

        bsnMail = (EditText) findViewById(R.id.bsnMail);
        bsnPass = (EditText) findViewById(R.id.bsnPass);
        bsnConPass = (EditText) findViewById(R.id.bsnConPass);
        bsnBusinessName = (EditText) findViewById(R.id.bsnBusinessName);
        bsnLocation = (EditText) findViewById(R.id.bsnLocation);
        bsnPhone = (EditText) findViewById(R.id.bsnPhone);
        bsnMobile = (EditText) findViewById(R.id.bsnMobile);

        busContinue = findViewById(R.id.registerCont);
        busContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bsnMail.getText().toString();
                String pass = bsnPass.getText().toString();
                String conpass = bsnConPass.getText().toString();
                String businessname = bsnBusinessName.getText().toString();
                String location = bsnLocation.getText().toString();
                String phone = bsnPhone.getText().toString();
                String mobile = bsnMobile.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(conpass) && !TextUtils.isEmpty(businessname)
                        && !TextUtils.isEmpty(location) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(mobile)) {

                    Intent i = new Intent (mContext, BusinessRegActivity2.class);
                    i.putExtra("EMAIL",email);
                    i.putExtra("PASS",pass);
                    i.putExtra("BUSINESSNAME",businessname);
                    i.putExtra("LOCATION",location);
                    i.putExtra("PHONE",phone);
                    i.putExtra("MOBILE",mobile);
                    startActivity(i);
                }else {
                    Toast.makeText(mContext, "Please fill up all fields!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
