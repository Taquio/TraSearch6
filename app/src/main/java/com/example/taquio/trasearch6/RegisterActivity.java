package com.example.taquio.trasearch6;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    String email;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText field_email,field_password,field_username,field_cPassword;
    private Button btn_submit;
//    private ImageButton chooseImage;
    private ImageView userProfileImage;
    private Uri filePath;
    private StorageReference storageReference;

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

//        chooseImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                takePicture.setType("userImage/*");
//                takePicture.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(takePicture,"Select Picture"),0);
//            }
//        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit button clicked");

                final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Registering...");
                progressDialog.show();
                
                String pass = field_password.getText().toString(),
                        cPass = field_cPassword.getText().toString();

                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(RegisterActivity.this,"Please check your Registration Details",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                
                else if (!(pass.equals(cPass)))
                {
                    field_password.setError("Password didn't match");
                    field_cPassword.setError("Password didn't match");
                    progressDialog.dismiss();
                }
                else
                {
                    email = field_email.getText().toString();
                    Log.d(TAG, "onClick: Registering User");
                    progressDialog.setTitle("Registering");
                    addUser(email,pass);
                    progressDialog.dismiss();
                    startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                }
//                else
//                {
//
//                    Log.d(TAG, "onClick: No Error");
//                    field_password.setError(null);
//
//
//                    if(filePath==null)
//                    {
//                        progressDialog.dismiss();
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                        // Add the buttons
//                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User clicked OK button
//                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                takePicture.setType("userImage/*");
//                                takePicture.setAction(Intent.ACTION_GET_CONTENT);
//                                startActivityForResult(Intent.createChooser(takePicture,"Select Picture"),0);
//
//                            }
//                        });
//                        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                                email = field_email.getText().toString();
//                                final String password = field_password.getText().toString();
//
//                                addUser(email,password);
//
//                            }
//                        }).setTitle("No Image Detected")
//                                .setMessage("Do you want to add your profile picture?");
//                        // Set other dialog properties
//
//
//                        // Create the AlertDialog
//                        AlertDialog dialog = builder.create();
//                        dialog.show();
//
//                    }
//                    else{
//                        Log.d(TAG, "onComplete: Adding User Details");
//
//                        progressDialog.setTitle("Image is Uploading...");
//                        StorageReference storageReference2nd = storageReference
//                                .child("userImage/" + System.currentTimeMillis() + "." + GetFileExtension(filePath));
//                        storageReference2nd.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                                // Getting image name from EditText and store into string variable.
//
//                                // Hiding the progressDialog after done uploading.
//                                progressDialog.dismiss();
//                                email = field_email.getText().toString();
//                                final String password = field_password.getText().toString();
//
//                                addUser(email,password);
//
//                                final String user_id=mAuth.getCurrentUser().getUid();
//                                final DatabaseReference current_user_db = databaseReference.child(user_id);
//
//                                final String username = field_username.getText().toString();
//                                final String name = field_username.getText().toString();
//
//
//
//
//                                current_user_db.child("Email").setValue(email);
//                                current_user_db.child("Name").setValue(name);
//                                current_user_db.child("UserName").setValue(username);
//                                current_user_db.child("userImage").setValue(filePath+"");
//
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
//                                // Showing toast message after done uploading.
//                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
//
//                                Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Hiding the progressDialog.
//                                progressDialog.dismiss();
//
//                                // Showing exception error message.
//                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                // Setting progressDialog Title.
//                                progressDialog.setTitle("Image is Uploading...");
//                            }
//                        });
//                        progressDialog.dismiss();
//                    }
//
//                }
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

    public void addUser(String email,String password)
    {
        Log.d(TAG, "addUser: Started");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "onComplete: Success Registration");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
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
                name = field_username.getText().toString();
        boolean flag = true;

        if(email.length()<=0)
        {
            field_email.setError("Please input a valid Email Address");
        }
        else if(name.length()<=0)
        {
            field_username.setError("Please input a valid Name");
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
            Log.d(TAG, "updateUI: Adding User Details to Database");


            final String username = field_username.getText().toString();
            final String name = field_username.getText().toString();
            email = field_email.getText().toString();

            final String user_id=mAuth.getCurrentUser().getUid();
            final DatabaseReference current_user_db = databaseReference.child(user_id);


            current_user_db.child("Email").setValue(email);
            current_user_db.child("Name").setValue(name);
            current_user_db.child("UserName").setValue(username);
//                                current_user_db.child("userImage").setValue(filePath+"");

            Log.d(TAG, "updateUI: Done Adding details, staring Home");


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
//        field_name = findViewById(R.id.field_name);
//        chooseImage = findViewById(R.id.register_chooseImage);
//        userProfileImage = findViewById(R.id.register_image);
        field_cPassword = findViewById(R.id.field_cPassword);

    }
}
