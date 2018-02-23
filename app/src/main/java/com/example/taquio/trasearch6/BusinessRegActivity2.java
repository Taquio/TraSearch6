package com.example.taquio.trasearch6;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Del Mar on 2/20/2018.
 */

public class BusinessRegActivity2 extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    String bsnMail,
            bsnPass,
            bsnConPass,
            bsnBusinessName,
            bsnLocation,
            bsnPhone,
            bsnMobile;
    Button busRegister, busUpload;
    ImageView imagePermit;
    private Uri filePath;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_register2);

        Bundle bundle = getIntent().getExtras();
        bsnMail = bundle.getString("EMAIL");
        bsnPass = bundle.getString("PASS");
        bsnBusinessName = bundle.getString("BUSINESSNAME");
        bsnLocation = bundle.getString("LOCATION");
        bsnPhone = bundle.getString("PHONE");
        bsnMobile = bundle.getString("MOBILE");

        busRegister = findViewById(R.id.registerBtn1);
        busUpload = findViewById(R.id.busUpload);
        imagePermit = findViewById(R.id.imagePermit);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        busRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//comment
//                if(!bsnPass.equals(bsnConPass)){
//                    Toast.makeText(BusinessRegActivity2.this, "Password does not match!", Toast.LENGTH_SHORT).show();
//                }
                mAuth.createUserWithEmailAndPassword(bsnMail, bsnPass)
                        .addOnCompleteListener(BusinessRegActivity2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {

                        }else {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = databaseReference.child("Users");
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            Map userDetails = new HashMap();
                            userDetails.put("BusinessName", bsnBusinessName);
                            userDetails.put("BusinessLocation", bsnLocation);
                            userDetails.put("BusinessTelephoneNo.", bsnPhone);
                            userDetails.put("BusinessMobileNo.", bsnMobile);
                            userDetails.put("userID",user_id);
                            userDetails.put("device_token",deviceToken);

                            current_user_db.child(user_id).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //tawag ra dirig intent
                                    }else{
                                        //if error
                                    }
                                }
                            });


                        }
                    }
                });
            }
        });

        busUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePermit.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if(filePath != null) {
            StorageReference ref = storageReference.child("images/*"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(BusinessRegActivity2.this, "Sucess", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
