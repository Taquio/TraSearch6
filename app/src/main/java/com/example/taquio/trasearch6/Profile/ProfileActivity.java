package com.example.taquio.trasearch6.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.ActivityLogin;
import com.example.taquio.trasearch6.HomeActivity2;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = ProfileActivity.this;
    private ProgressBar mProgressbar;

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private StorageReference mImageStorage;

    private Button logout2,chgn_name,chgn_image;
    private TextView txtusername,txtemail;
    private EditText field_changeName;
    private CircleImageView ivprofilepic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);refIDs();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        logout2 = findViewById(R.id.logout2);
        logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Logout Clicked");
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, ActivityLogin.class));
                finish();
            }
        });

//        mProgressbar = findViewById(R.id);
        setupBottomNavigationView();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Has data");

                txtusername.setText(dataSnapshot.child("Name").getValue().toString());
                txtemail.setText(dataSnapshot.child("Email").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chgn_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserDatabase.child("Name").setValue(field_changeName.getText().toString().toUpperCase());
            }
        });

        chgn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent galleryIntent = new Intent();
//                galleryIntent.setType("image/*");
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),1);
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .start(ProfileActivity.this);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                ivprofilepic.setImageURI(resultUri);

                StorageReference filePath = mImageStorage.child("image_profile").child(mCurrentUser.getUid()).child(random()+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this,"Upload Failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random()
    {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomeLength = generator.nextInt(12);
        char tempChar;
        for(int i = 0;i<randomeLength;i++)
        {
            tempChar = (char) (generator.nextInt(96)+32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void refIDs()
    {
        txtemail = findViewById(R.id.txtemail);
        txtusername = findViewById(R.id.txtusername);
        chgn_name = findViewById(R.id.chgn_name);
        field_changeName = findViewById(R.id.field_changeName);
        chgn_image = findViewById(R.id.chgn_image);
        ivprofilepic = findViewById(R.id.ivprofilepic);
    }
//        setupToolBar();
//    }
//
//    private void setupToolBar() {
//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.profileToolBar);
//        setSupportActionBar(toolbar);
//        ImageView profMenu = findViewById(R.id.profileMenu);
//        profMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, AccountSettingsActivity.class));
//            }
//        });
//    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
