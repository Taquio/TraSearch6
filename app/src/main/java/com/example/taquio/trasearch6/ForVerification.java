package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ForVerification extends AppCompatActivity {

    FirebaseAuth mAuth;
    StorageReference mImageStorage;
    DatabaseReference mUserDatabase;
    private Button ver_IDbtn,ver_Selfiebtn,ver_Skipbtn,ver_UploadExec;
    private ImageView ver_ID,ver_Selfie;
    private Uri selfie,mID;
    private boolean isID=false,isSelfie=false;
    private String mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_verification);
        refIDs();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        ver_Skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForVerification.this,HomeActivity2.class));
                finish();
            }
        });

        if (isID||isSelfie)
        {
            ver_UploadExec.setEnabled(true);
        }

        ver_IDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = CropImage.activity(mID)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .getIntent(ForVerification.this);
                startActivityForResult(intent, 0);
            }
        });

        ver_Selfiebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity(selfie)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .getIntent(ForVerification.this);
                startActivityForResult(intent, 1);
            }
        });

        ver_UploadExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            upLoadExec(isID,isSelfie);

            }
        });
    }

    private void upLoadExec(boolean IDD,boolean Selfiee)
    {
        final ProgressDialog progressDialog = new ProgressDialog(ForVerification.this);
        if(IDD&&Selfiee)
        {
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Please wait while we upload your Images");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        if (Selfiee)
        {
            StorageReference filePath = mImageStorage.
                    child("forVerification").
                    child(mCurrentUser).
                    child("Selfie");

            filePath.putFile(selfie).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful())
                    {
                        ver_Selfiebtn.setEnabled(false);
                        String selfURL = task.getResult().getDownloadUrl().toString();
                        mUserDatabase.child(mCurrentUser).child("selfieURL").setValue(selfURL);
                        ver_Skipbtn.setText("Finish");
                        progressDialog.dismiss();
                    }
                }
            });
        }
        if (IDD)
        {
            StorageReference filePath = mImageStorage.
                    child("forVerification").
                    child(mCurrentUser).
                    child("ID");

            filePath.putFile(mID).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        ver_Selfiebtn.setEnabled(false);
                        String IDURL = task.getResult().getDownloadUrl().toString();
                        mUserDatabase.child(mCurrentUser).child("IDURL").setValue(IDURL);
                        ver_Skipbtn.setText("Finish");
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void refIDs()
    {
        ver_ID = findViewById(R.id.ver_ID);
        ver_Selfie = findViewById(R.id.ver_Selfie);
        ver_IDbtn = findViewById(R.id.ver_IDbtn);
        ver_Selfiebtn = findViewById(R.id.ver_Selfiebtn);
        ver_Skipbtn = findViewById(R.id.ver_Skipbtn);
        ver_UploadExec = findViewById(R.id.ver_UploadExec);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mID = result.getUri();
                ver_ID.setImageURI(mID);
                isID=true;
                ver_UploadExec.setEnabled(true);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        else if (requestCode == 1) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selfie = result.getUri();
                ver_Selfie.setImageURI(selfie);
                isSelfie=true;
                ver_UploadExec.setEnabled(true);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
}
