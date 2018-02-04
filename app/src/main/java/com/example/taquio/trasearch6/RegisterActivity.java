package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText field_email,field_password,field_username,field_name;
    private Button btn_submit;
    private ImageButton chooseImage;
    private ImageView userProfileImage;
    private Uri filePath;

    private StorageReference storageReference;

    private static final String TAG = "RegisterActivity";

    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        refIDs();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        if(getIntent().hasExtra("emailPass"))
        {
            email = getIntent().getStringExtra("emailPass");
            field_email.setText(email);
        }

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.setType("userImage/*");
                takePicture.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(takePicture,"Select Picture"),0);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit button clicked");

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Registering...");
                progressDialog.show();

                final String password = field_password.getText().toString();
                final String username = field_username.getText().toString();
                final String name = field_name.getText().toString();
                email = field_email.getText().toString();
                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Log.d(TAG, "onClick: No Error");
                    field_password.setError(null);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "onComplete: Success Registration");
                                        final String user_id=mAuth.getCurrentUser().getUid();
                                        final DatabaseReference current_user_db = databaseReference.child(user_id);

                                        if(filePath==null)
                                        {
                                            progressDialog.dismiss();

                                        }
                                        else{
                                            Log.d(TAG, "onComplete: Adding User Details");



                                            progressDialog.setTitle("Image is Uploading...");
                                            StorageReference storageReference2nd = storageReference
                                                    .child("userImage/" + System.currentTimeMillis() + "." + GetFileExtension(filePath));
                                            storageReference2nd.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    // Getting image name from EditText and store into string variable.

                                                    // Hiding the progressDialog after done uploading.
                                                    progressDialog.dismiss();

                                                    // Showing toast message after done uploading.
                                                    Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                                                    current_user_db.child("Email").setValue(email);
                                                    current_user_db.child("Name").setValue(name);
                                                    current_user_db.child("UserName").setValue(username);
                                                    current_user_db.child("userImage").setValue(filePath+"");

                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    updateUI(user);
                                                    Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Hiding the progressDialog.
                                                    progressDialog.dismiss();

                                                    // Showing exception erro message.
                                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // Setting progressDialog Title.
                                                    progressDialog.setTitle("Image is Uploading...");
                                                }
                                            });




                                            progressDialog.dismiss();
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == 0) {
//
//            if (resultCode == RESULT_OK) {
//                if (data != null) {
//                    // Get the URI of the selected file
//                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                    userProfileImage.setImageBitmap(photo);
//                }
//                else
//                {
//                    Toast.makeText(RegisterActivity.this,"No Picture",Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

        filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            userProfileImage.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private boolean hasRegError()
    {
        final String email = field_email.getText().toString(),
                password = field_password.getText().toString(),
                username = field_username.getText().toString(),
                name = field_name.getText().toString();
        boolean flag = true;

        if(email.length()<=0)
        {
            field_email.setError("Please input a valid Email Address");
        }
        else if(name.length()<=0)
        {
            field_name.setError("Please input a valid Name");
        }
        else if (username.length()<=0)
        {
            field_username.setError("Please input a valid Username");
        }
        else if(password.length()<8)
        {
            field_password.setError("Password must be more than 8 characters");
        }
        else
        {
            flag=false;
        }

        return flag;

    }

    private void updateUI(FirebaseUser user)
    {
        if(user !=null)
        {
            Toast.makeText(RegisterActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
            Intent startActivityIntent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(startActivityIntent);
            RegisterActivity.this.finish();
        }
       else
        {
            Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void refIDs()
    {
        field_email = findViewById(R.id.field_email);
        field_password = findViewById(R.id.field_password);
        btn_submit = findViewById(R.id.btn_submit);
        field_username = findViewById(R.id.field_username);
        field_name = findViewById(R.id.field_name);
        chooseImage = findViewById(R.id.register_chooseImage);
        userProfileImage = findViewById(R.id.register_image);
    }
}
