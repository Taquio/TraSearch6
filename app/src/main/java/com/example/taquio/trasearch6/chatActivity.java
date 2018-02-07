package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class chatActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUM = 1;
    private static final String TAG = "chatActivity";
    private EditText editMessage;
    private DatabaseReference databaseReference;
    private RecyclerView mMessageList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext = chatActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setupBottomNavigationView();

        refIDs();
        mAuth = FirebaseAuth.getInstance();

        mMessageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mMessageList.setLayoutManager(linearLayoutManager);




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(chatActivity.this,RegisterActivity.class));
                    chatActivity.this.finish();
                }
            }
        };


    }

    public void sendButtonClicked (View view)
    {
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        DatabaseReference databaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        final String messageValue = editMessage.getText().toString().trim();
        if(!TextUtils.isEmpty(messageValue))
        {
            final DatabaseReference newPost = databaseReference.push();
            databaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("content").setValue(messageValue);
                    newPost.child("userName").setValue(dataSnapshot.child("UserName").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mMessageList.scrollToPosition(mMessageList.getAdapter().getItemCount());
        }
    }

    private void refIDs()
    {
        editMessage = findViewById(R.id.editMessage);
        mMessageList = findViewById(R.id.messageRec);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Messages");


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter <message,MessageHolder> FBRA = new FirebaseRecyclerAdapter<message, MessageHolder>(
                message.class,
                R.layout.singlemessagelayout,
                MessageHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(MessageHolder viewHolder, message model, int position) {
                viewHolder.setContent(model.getContent());
                viewHolder.setUserName(model.getUserName());
            }
        };
        mMessageList.setAdapter(FBRA);
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    public static class MessageHolder extends RecyclerView.ViewHolder{
        View mView ;
        public MessageHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setContent(String content)
        {
            TextView messageContent = mView.findViewById(R.id.messagetext);
            messageContent.setText(content);
        }
        public void setUserName(String userName)
        {
            TextView messageUserName = mView.findViewById(R.id.usernamaetext);
            messageUserName.setText(userName);
        }
    }
}
