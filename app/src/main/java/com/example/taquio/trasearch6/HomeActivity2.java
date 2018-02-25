package com.example.taquio.trasearch6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch6.Utils.ItemsFragment;
import com.example.taquio.trasearch6.Utils.MainFeedListAdapter;
import com.example.taquio.trasearch6.Utils.OtherUserViewPost;
import com.example.taquio.trasearch6.Utils.UniversalImageLoader;
import com.example.taquio.trasearch6.Utils.ViewCommentsFragment;
import com.example.taquio.trasearch6.Utils.ViewPostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity2 extends AppCompatActivity implements
        MainFeedListAdapter.OnLoadMoreItemsListener{

    private static final String TAG = "HomeActivity2";
    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 1;
    private Context mContext = HomeActivity2.this;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;
    private DatabaseReference mUserDatabase;
  private  TextView notifications_badgeText;


    @Override
    public void onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: displaying more photos");

        ItemsFragment fragment = (ItemsFragment)getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());
        if(fragment != null){
            fragment.displayMorePhotos();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
        mViewPager = findViewById(R.id.container);
        mFrameLayout = findViewById(R.id.frame_container);
        mRelativeLayout = findViewById(R.id.relLayoutParent);

        setUpFirebaseAuth();
        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();

    }
    public void onImageSelected( Photo item,  int i, final String user_id) {


        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(user_id.equals(currentUser.getUid())) {

            ViewPostFragment fragment = new ViewPostFragment();
            Bundle args = new Bundle();
            args.putParcelable(getString(R.string.photo), item);
            args.putInt(getString(R.string.activity_number), i);

            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack("View Post");
            transaction.commit();
        }else{
            OtherUserViewPost fragment = new OtherUserViewPost();
            Bundle args = new Bundle();
            args.putParcelable(getString(R.string.photo), item);
            args.putInt(getString(R.string.activity_number), i);

            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack("View Post");
            transaction.commit();
        }



    }
    public void onCommentThreadSelected(Photo photo, String callingActivity){
        Log.d(TAG, "onCommentThreadSelected: selected a coemment thread");

        ViewCommentsFragment fragment  = new ViewCommentsFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putString(getString(R.string.home_activity), getString(R.string.home_activity));
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_comments_fragment));
        transaction.commit();

    }
    public void hideLayout(){
        Log.d(TAG, "hideLayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
    }


    public void showLayout(){
        Log.d(TAG, "hideLayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mFrameLayout.getVisibility() == View.VISIBLE){
            showLayout();
        }
    }
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause: OnPause Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();

//        if(currentUser!=null) {
//            Log.d(TAG, "onPause: User Offline");
//            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
//        }
    }
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideosFragment());
        adapter.addFragment(new ArticlesFragment());
        adapter.addFragment(new ItemsFragment());
        adapter.addFragment(new JunkShopsFragment());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Videos");
        tabLayout.getTabAt(1).setText("Articles");
        tabLayout.getTabAt(2).setText("Items");
        tabLayout.getTabAt(3).setText("Shops");

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        Log.d(TAG, "setupBottomNavigationView: Start Notification Badge");
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationViewEx.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notlayout, bottomNavigationMenuView, false);
        DatabaseReference mSeen = FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid());
        Log.d(TAG, "setupBottomNavigationView: Start Counting....");

        mSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int resultCount =0;
                notifications_badgeText = findViewById(R.id.notifications_badgeText);

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "" + childDataSnapshot.getValue()); //displays the key for the node
                    Log.d(TAG, "" + childDataSnapshot.child("seen").getValue());//gives the value for given keyname
                    if (childDataSnapshot.child("seen").getValue().toString().equals("false"))
                    {
                        Log.d(TAG, "onDataChange: RESULT IS FALSE");
                        resultCount++;
                    }
                }
                if(resultCount>0)
                {
                    notifications_badgeText.setText((resultCount++)+"");
                    notifications_badgeText.setVisibility(View.VISIBLE);
                }
                else
                {
                    notifications_badgeText.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        itemView.addView(badge);


    }



    //  ---------------------- F I R E B A S E -------------------------
    private void checkCurrentUser(FirebaseUser user){

        if(user == null){
            Intent intent = new Intent(mContext, ActivityLogin.class);
            startActivity(intent);
        }
    }
    private void setUpFirebaseAuth(){
        Log.d(TAG, "setUpFirebase: FIREBASE SETTING UP....");
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: SIGNED IN!"+ user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: SIGNED OUT!");
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser==null)
//        {
//            Log.d(TAG, "onStart: Calling back to start method");
//            sendToStart();
//        }
//        else
//        {
//            Log.d(TAG, "onStart: User Online");
//            mUserRef.child("online").setValue("true");
//        }
        mAuth.addAuthStateListener(mAuthStateListener);
        if(mAuth.getCurrentUser()!=null)
        {
            mUserDatabase.child("online").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
            mUserDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }

}
