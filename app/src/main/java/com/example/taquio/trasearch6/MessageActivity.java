package com.example.taquio.trasearch6;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = MessageActivity.this;

    private String user_id;

    private DatabaseReference mRootRef;

    private Toolbar mChatToolBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        user_id = getIntent().getStringExtra("user_id");
        String name = getIntent().getStringExtra("user_Name");
        setupBottomNavigationView();

        mChatToolBar =  findViewById(R.id.messageAppbar);

        setSupportActionBar(mChatToolBar);
        ActionBar actionBar =  getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        getSupportActionBar().setTitle(name);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_chatappbar,null);

        actionBar.setCustomView(action_bar_view);






        mRootRef = FirebaseDatabase.getInstance().getReference();




    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView (Chat)");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
