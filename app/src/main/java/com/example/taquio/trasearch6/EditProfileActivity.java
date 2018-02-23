package com.example.taquio.trasearch6;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.ServerValue;
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

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";
    private DatabaseReference mUserDatabase,mAllUsersDatabase;
    private FirebaseUser mCurrentUser;
    private EditText ediProfile_mobile
            ,ediProfile_name
            ,ediProfile_email,
            ediProfile_password;
    private TextView ediProfile_changeImage;
    private CircleImageView ediProfile_image;
    private StorageReference mImageStorage;
    private ProgressDialog progressDialog;
    private Uri resultUri;
    private String newName,newEmail,newPassword,newMobile,mauthEmail,mauthPassword;
    private ImageView ediProfile_saveChanges;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Boolean name,email,password,mobile,image;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        refIDs();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ediProfile_name.setHint(dataSnapshot.child("Name").getValue().toString());
                ediProfile_email.setHint(dataSnapshot.child("Email").getValue().toString());
                ediProfile_mobile.setHint(dataSnapshot.child("PhoneNumber").getValue().toString());
                mauthEmail = dataSnapshot.child("Email").getValue().toString();

                Picasso.with(EditProfileActivity.this).load(dataSnapshot.child("Image").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.man)
                        .into(ediProfile_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(EditProfileActivity.this)
                                        .load(dataSnapshot
                                                .child("Image").getValue().toString())
                                        .placeholder(R.drawable.man)
                                        .into(ediProfile_image);
                            }
                        });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ediProfile_changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .start(EditProfileActivity.this);
            }
        });



        name=false;email=false;password=false;mobile=false;image=false;


        ediProfile_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);
                alertDialog.setTitle("PASSWORD");
                alertDialog.setMessage("Enter Password");
                String itsName = ediProfile_name.getText().toString(),
                        itsPass = ediProfile_password.getText().toString(),
                        itsEmail = ediProfile_email.getText().toString(),
                        itsMobile = ediProfile_mobile.getText().toString();

                if(!TextUtils.isEmpty(itsName))
                {name = true;}
                if(!TextUtils.isEmpty(itsPass))
                {password = true;}
                if(!TextUtils.isEmpty(itsEmail))
                {email = true;}
                if(!TextUtils.isEmpty(itsMobile))
                {mobile = true;}
                if(resultUri!=null)
                {image=true;}

                final EditText input = new EditText(EditProfileActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.key);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mauthPassword = input.getText().toString();
                                if(TextUtils.isEmpty(mauthPassword))
                                {
                                    Toast
                                            .makeText(EditProfileActivity.this
                                                    , " Verify your old password"
                                                    , Toast.LENGTH_SHORT)
                                            .show();
                                }
                                else
                                {

                                    AuthCredential credential = EmailAuthProvider
                                            .getCredential(mauthEmail, mauthPassword);
                                    mCurrentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                uploadData(password, image, name, email, mobile);
                                            }
                                            else
                                            {
                                                Toast
                                                        .makeText(EditProfileActivity.this
                                                                , "Invalid Password: "+mauthPassword+"\n"+mauthEmail
                                                                , Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });


    }

    public void uploadData(boolean Thispass
            , boolean Thisimage
            , boolean Thisname
            , boolean Thisemail
            , boolean Thismobile) {
        Map updateDB = new HashMap();
       boolean flag =false
               ,emailFlag=false;
        if (Thisname) {
            Log.d(TAG, "uploadData: Will Update Name");
            updateDB.put("Name", ediProfile_name.getText().toString());
            flag =true;
        }
        if (Thisemail) {
            Log.d(TAG, "uploadData: Will Update Email");
            flag =true;
            updateDB.put("Email", ediProfile_email.getText().toString());
        }
        if (Thismobile) {
            Log.d(TAG, "uploadData: Will Update Mobile");
            flag =true;
            updateDB.put("PhoneNumber", ediProfile_mobile.getText().toString());
        }
        if (Thispass) {
            Log.d(TAG, "uploadData: Will Update Password");
            flag =true;
            mCurrentUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Password updated");
                    } else {
                        Log.d(TAG, "Error password not updated");
                    }
                }
            });
        }
        if (Thisemail) {
            Log.d(TAG, "uploadData: Will Update Email");
            flag =true;
            emailFlag = true;
            mCurrentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email updated");
                    } else {
                        Log.d(TAG, "Error Email not updated");
                    }
                }
            });
        }
        if(flag||Thisimage)
        {
            if(emailFlag)
            {
                updateDB.put("Email", ediProfile_email.getText().toString());
            }
            mUserDatabase.updateChildren(updateDB).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(EditProfileActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProfileActivity.this,EditProfileActivity.class));
                        progressDialog.dismiss();
                        finish();
                    }
                }
            });
            if (Thisimage) {
                Log.d(TAG, "uploadData: Will Update Image");

                progressDialog = new ProgressDialog(EditProfileActivity.this);
                progressDialog.setTitle("Uploading Image...");
                progressDialog.setMessage("Please wait while we upload the your beautiful image");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                File image_path = new File(resultUri.getPath());
                final Bitmap thumbBitmap;
                try {
                    thumbBitmap = new Compressor(EditProfileActivity.this)
                            .setMaxWidth(200).setMaxHeight(200).setQuality(75)
                            .compressToBitmap(image_path);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] byteData = baos.toByteArray();

                    StorageReference filePath = mImageStorage.
                            child("Photos").
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
                            if (task.isSuccessful()) {
                                final String download_url = task.getResult().getDownloadUrl().toString();

                                UploadTask uploadTask = thumbFilePath.putBytes(byteData);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                        final String thumb_downloadURL = thumb_task.getResult().getDownloadUrl().toString();
                                        Map update_hashmap = new HashMap();
                                        update_hashmap.put("Image", download_url);
                                        update_hashmap.put("Image_thumb", thumb_downloadURL);

                                        mUserDatabase.updateChildren(update_hashmap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();

                                                } else {
                                                    Toast.makeText(EditProfileActivity.this, "Failed to retrive image", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        });
                                    }
                                });
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Error Uploading Profile Picture", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(EditProfileActivity.this,"Not Updated",Toast.LENGTH_SHORT).show();
        }
    }
        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    ediProfile_image.setImageURI(resultUri);
                    image=true;
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }



    private void refIDs()
    {
        ediProfile_mobile = findViewById(R.id.ediProfile_mobile);
        ediProfile_name = findViewById(R.id.ediProfile_name);
        ediProfile_email = findViewById(R.id.ediProfile_email);
        ediProfile_password = findViewById(R.id.ediProfile_password);
        ediProfile_changeImage = findViewById(R.id.ediProfile_changeImage);
        ediProfile_image = findViewById(R.id.ediProfile_image);
        ediProfile_saveChanges = findViewById(R.id.ediProfile_saveChanges);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

}
