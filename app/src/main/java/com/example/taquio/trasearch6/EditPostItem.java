package com.example.taquio.trasearch6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Models.User;
import com.example.taquio.trasearch6.Utils.SquareImageView;
import com.example.taquio.trasearch6.Utils.UniversalImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Edward on 26/02/2018.
 */

public class EditPostItem extends AppCompatActivity {

    private DatabaseReference mReference;
    private ImageView close;
    private TextView save, username;
    private EditText caption;
    private String captionHolder;
    private CircleImageView profphoto;
    private SquareImageView postedphoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_post);

        mReference = FirebaseDatabase.getInstance().getReference();
        profphoto = findViewById(R.id.profile_photo);
        postedphoto = findViewById(R.id.post_image);
        username = findViewById(R.id.username);

        caption = (EditText) findViewById(R.id.image_caption);
        close = findViewById(R.id.ivCloseShare);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save = findViewById(R.id.tvSave);

        Bundle b =  getIntent().getExtras();

        final User muser = (User) b.get("user");
       final Photo mphoto = (Photo) b.get("photo");


                            UniversalImageLoader.setImage(muser.getImage(),profphoto, null,"");
                            UniversalImageLoader.setImage(mphoto.getImage_path(),postedphoto, null,"");
                            username.setText(muser.getUserName());
                            caption.setText(mphoto.getPhoto_description());
                            captionHolder = caption.getText().toString();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!caption.equals(mphoto.getPhoto_description()))
                {
                    mReference.child("Users_Photos")
                            .child(mphoto.getUser_id())
                            .child(mphoto.getPhoto_id())
                            .child("caption")
                            .setValue( caption.getText().toString());

                    mReference.child("Photos")
                            .child(mphoto.getPhoto_id())
                            .child("caption")
                            .setValue( caption.getText().toString());
                    Toast.makeText(getApplicationContext(),"Successfully Edited!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
