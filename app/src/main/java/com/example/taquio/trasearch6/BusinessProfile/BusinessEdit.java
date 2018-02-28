package com.example.taquio.trasearch6.BusinessProfile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by Del Mar on 2/25/2018.
 */

public class BusinessEdit extends AppCompatActivity {


    private static final String TAG = "BusinessEdit";
    String newEmail, newPassword, mAuthEmail , mAuthPassword;
    ImageView confirm;
    TextView editPhoto;
    private DatabaseReference mUserDatabase;
    EditText etName, etMail, etLocation, etPassword, etMobile, etPhone;
    CircleImageView profImage;
    private StorageReference mImageStorage;
    private ProgressDialog progressDialog;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private boolean name,email,location,password,mobile,phone,image;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_profile_edit);

        etName = (EditText) findViewById(R.id.bsnEditName);
        etMail = (EditText) findViewById(R.id.bsnEdit_email);
        etLocation = (EditText) findViewById(R.id.bsnEditLoc);
        etPassword = (EditText) findViewById(R.id.ediProfile_password);
        etMobile = (EditText) findViewById(R.id.bsnEditMobile);
        etPhone = (EditText) findViewById(R.id.bsnEditTele);
        confirm = (ImageView) findViewById(R.id.businessSaveChanges);
        editPhoto = (TextView) findViewById(R.id.ediProfile_changeImage);
        profImage = (CircleImageView) findViewById(R.id.ediProfile_image);

        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = mCurrentUser.getUid();
        
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                etName.setText(dataSnapshot.child("bsnBusinessName").getValue().toString());
                etLocation.setText(dataSnapshot.child("bsnLocation").getValue().toString());
                etMobile.setText(dataSnapshot.child("bsnMobile").getValue().toString());
                etPhone.setText(dataSnapshot.child("bsnPhone").getValue().toString());
                mAuthEmail = dataSnapshot.child("bsnEmail").getValue().toString();

                Picasso.with(BusinessEdit.this).load(dataSnapshot.child("image").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.man)
                        .into(profImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(BusinessEdit.this)
                                        .load(dataSnapshot.child("image").getValue().toString())
                                        .placeholder(R.drawable.man)
                                        .into(profImage);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .start(BusinessEdit.this);
            }
        });


        name = false; email = false; location = false; password = false; mobile = false; phone = false; image = false;

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BusinessEdit.this);
                alertDialog.setTitle("PASSWORD");
                alertDialog.setMessage("Enter Password");
                String itsName = etName.getText().toString(),
                        itsPass = etPassword.getText().toString(),
                        itsEmail = etMail.getText().toString(),
                        itsLocation = etLocation.getText().toString(),
                        itsMobile = etMobile.getText().toString(),
                        itsPhone = etPhone.getText().toString();

                if(!TextUtils.isEmpty(itsName)) {
                    Log.d(TAG, "onClick: Name True");name = true; }
                if(!TextUtils.isEmpty(itsPass)) { password = true; }
                if(!TextUtils.isEmpty(itsEmail)) { email = true; }
                if(!TextUtils.isEmpty(itsLocation)) { location = true; }
                if(!TextUtils.isEmpty(itsMobile)) { mobile = true; }
                if(!TextUtils.isEmpty(itsPhone)) { phone = true; }
                if(resultUri != null) { image = true; }

                final EditText input = new EditText(BusinessEdit.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.key);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuthPassword = input.getText().toString();
                                if(TextUtils.isEmpty(mAuthPassword)) {
                                    Toast.makeText(BusinessEdit.this, "Verify your old password", Toast.LENGTH_SHORT).show();
                                } else {
                                    AuthCredential credential = EmailAuthProvider.getCredential(mAuthEmail, mAuthPassword);
                                    mCurrentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                updateProfile(name, email, password, location, mobile, phone, image);
                                            } else {
                                                Toast.makeText(BusinessEdit.this, "Invalid Password" + mAuthPassword + "\n" + mAuthEmail, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void updateProfile(boolean Thisname,
                               boolean Thisemail,
                               boolean Thispassword,
                               boolean Thislocation,
                               boolean Thismobile,
                               boolean Thisphone,
                               boolean Thisimage) {
        Map updateDB = new HashMap();
        boolean flag = false, emailFlag = false;

        if(Thisname) {
            flag = true;
            updateDB.put("bsnBusinessName", etName.getText().toString());
        }
        if(Thisemail) {
            flag = true;
            updateDB.put("bsnEmail", etMail.getText().toString());
        }
        if(Thislocation) {
            flag = true;
            updateDB.put("bsnLocation", etLocation.getText().toString());
        }

        if(Thismobile) {
            flag = true;
            updateDB.put("bsnMobile", etMobile.getText().toString());
        }
        if(Thisphone) {
            flag = true;
            updateDB.put("bsnPhone", etPhone.getText().toString());
        }
        if(Thispassword)
        {
            flag = true;
            mCurrentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //
                    } else {
                        //
                    }
                }
            });
        }
        if(Thisemail) {
            flag = true;
            emailFlag = true;
            mCurrentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        //
                    } else {
                        //
                    }
                }
            });
        }
        if(flag||Thisimage) {
            if(emailFlag) {
                updateDB.put("bsnEmail", etMail.getText().toString());
            }
            mUserDatabase.updateChildren(updateDB).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(BusinessEdit.this, "Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BusinessEdit.this,BusinessProfile.class));
                        finish();

                    }
                }
            });
            if(Thisimage) {
                progressDialog = new ProgressDialog(BusinessEdit.this);
                progressDialog.setTitle("Uploading image...");
                progressDialog.setMessage("Please wait.");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                File image_path = new File(resultUri.getPath());
                final Bitmap thumbBitmap;
                try {
                    thumbBitmap = new Compressor(BusinessEdit.this)
                            .setMaxWidth(200).setMaxHeight(200).setQuality(75)
                            .compressToBitmap(image_path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] byteDate = baos.toByteArray();

                    StorageReference filePath = mImageStorage.child("Photos").
                            child(mCurrentUser.getUid()).
                            child("ProfilePhoto").
                            child("profile_image");
                    final StorageReference thumbFilePath = mImageStorage.
                            child("Photos").
                            child(mCurrentUser.getUid()).
                            child("ProfilePhoto").
                            child("profile_thumb");
                    filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                final String download_url = task.getResult().getDownloadUrl().toString();

                                UploadTask uploadTask = thumbFilePath.putBytes(byteDate);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                        final String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();
                                        Map update_hashmap = new HashMap();
                                        update_hashmap.put("image", download_url);
                                        update_hashmap.put("image_thumb", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashmap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                } else {
                                                    Toast.makeText(BusinessEdit.this,"Failed to retrieve image.",Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                                    }
                                });
                            }else {
                                Toast.makeText(BusinessEdit.this,"Error Uploading Profile Picture",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(BusinessEdit.this,"Not updated",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profImage.setImageURI(resultUri);
                image = true;
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
