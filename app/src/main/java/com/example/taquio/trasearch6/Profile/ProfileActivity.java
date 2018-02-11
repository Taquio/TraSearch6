package com.example.taquio.trasearch6.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.ActivityLogin;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.Users;
import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch6.ViewProfile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = ProfileActivity.this;
    private ProgressDialog progressDialog;

    private DatabaseReference mUserDatabase,mAllUsersDatabase;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;
    private StorageReference mImageStorage;

    private Button logout2,chgn_name,chgn_image;
    private TextView txtusername,txtemail;
    private EditText field_changeName;
    private CircleImageView ivprofilepic;
    private RecyclerView allUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        refIDs();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mAllUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

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
                Picasso.with(ProfileActivity.this).load(dataSnapshot.child("Image").getValue().toString())
                        .placeholder(R.drawable.man)
                        .into(ivprofilepic);
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
        try {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setTitle("Uploading Image...");
                progressDialog.setMessage("Please wait while we upload the your beautiful image");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                File image_path = new File(resultUri.getPath());
                final Bitmap thumbBitmap;

                    thumbBitmap = new Compressor(ProfileActivity.this)
                                .setMaxWidth(200).setMaxHeight(200).setQuality(75)
                                .compressToBitmap(image_path);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumbBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    final byte [] byteData = baos.toByteArray();

                StorageReference filePath = mImageStorage.
                        child("image_profile").
                        child(mCurrentUser.getUid()).
                        child("profile_image.jpg");
                final StorageReference thumbFilePath = mImageStorage.
                        child("image_profile").
                        child(mCurrentUser.getUid()).
                        child("progile_thumb.jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbFilePath.putBytes(byteData);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    final String thumb_downloadURL = thumb_task.getResult().getDownloadUrl().toString();
                                    Map update_hashmap = new HashMap();
                                    update_hashmap.put("Image",download_url);
                                    update_hashmap.put("Image_thumb",thumb_downloadURL);

                                    mUserDatabase.updateChildren(update_hashmap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful())
                                            {
                                                progressDialog.dismiss();

                                            }else{
                                                Toast.makeText(ProfileActivity.this,"Failed to retrive image",Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this,"Error Uploading Profile Picture",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.user_single_layout,
                UsersViewHolder.class,
                mAllUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, final int position) {
                viewHolder.setName(model.getName());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setProfileImage(model.getImage(),ProfileActivity.this);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    String User_id = getRef(position).getKey();
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProfileActivity.this, ViewProfile.class)
                        .putExtra("user_id",User_id));

                    }
                });
            }
        };
        allUser.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName(String Name)
        {
            TextView mUserNameHolder = mView.findViewById(R.id.allUsersName);
            mUserNameHolder.setText(Name);
        }
        public void setEmail(String Email)
        {
            TextView mUserEmailHolder = mView.findViewById(R.id.allUsersEmail);
            mUserEmailHolder.setText(Email);
        }
        public void setProfileImage (String ImageURL, Context cTHis)
        {
            CircleImageView mImageHolder = mView.findViewById(R.id.allUsersImg);
            Picasso.with(cTHis).load(ImageURL)
                    .into(mImageHolder);

        }
    }

    private void refIDs()
    {
        txtemail = findViewById(R.id.txtemail);
        txtusername = findViewById(R.id.txtusername);
        chgn_name = findViewById(R.id.chgn_name);
        field_changeName = findViewById(R.id.field_changeName);
        chgn_image = findViewById(R.id.chgn_image);
        ivprofilepic = findViewById(R.id.ivprofilepic);
        allUser = findViewById(R.id.allUsers);
            allUser.setHasFixedSize(true);
            allUser.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
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
