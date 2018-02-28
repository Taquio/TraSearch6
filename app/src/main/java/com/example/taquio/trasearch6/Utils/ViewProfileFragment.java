package com.example.taquio.trasearch6.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch6.MessageActivity;
import com.example.taquio.trasearch6.Models.Comment;
import com.example.taquio.trasearch6.Models.Like;
import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Models.User;
import com.example.taquio.trasearch6.Models.UserSettings;
import com.example.taquio.trasearch6.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



/**
 * Created by Edward 2018.
 */

public class ViewProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;
    OnGridImageSelectedListener mOnGridImageSelectedListener;
    TextView name,email,phoneNo;
    CircleImageView image;
    ProgressDialog mProgressDialog;
    Button vProfile_sendFriequest_btn,declineRequest;
    FirebaseUser mCurrent_user;
    Integer mfriend_status;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    //widgets
    private TextView mName, mEmail, mPhoneNumber;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private ImageView mBackArrow;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;
    private TextView editProfile;
    private Button message;
    //vars
    private User mUser;
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;
    private DatabaseReference mUsersDatabase
            ,mFriendRequestDatabase
            ,mFriendDatabase
            ,mNotificatioonDatabase
            ,mRootRef;
    private String user_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_viewprofile, container, false);


        mfriend_status = 0;
        mProgressDialog = new ProgressDialog(view.getContext());
        mProgressDialog.setTitle("Loading");
        mProgressDialog.setMessage("Please wait while we load User Data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        
        mName = view.findViewById(R.id.viewedName);
        mEmail = view.findViewById(R.id.viewedEmail);
        mPhoneNumber = view.findViewById(R.id.viewedNumber);
        mProfilePhoto = view.findViewById(R.id.viewedProfile);
        message = view.findViewById(R.id.btnMessage);
        vProfile_sendFriequest_btn = view.findViewById(R.id.vProfile_sendFriequest_btn);
        declineRequest = view.findViewById(R.id.declineRequest);

        gridView = view.findViewById(R.id.gridView);
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);

        mContext = getActivity();
        Log.d(TAG, "onCreateView: stared.");


        try{
            mUser = getUserFromBundle();
            init();
        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: "  + e.getMessage() );
            Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }
        setupBottomNavigationView();
        setupFirebaseAuth();
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUserID());
        mFriendRequestDatabase = FirebaseDatabase.getInstance().getReference().child("friend_request");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mNotificatioonDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mRootRef = FirebaseDatabase.getInstance().getReference();



        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Check Database");
                mFriendRequestDatabase
                        .child(mCurrent_user.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.hasChild(user_id))
                                {
                                    Log.d(TAG, "onDataChange: user found in FriendRequest Database");
                                    String req_type = dataSnapshot.
                                            child(user_id)
                                            .child("request_type")
                                            .getValue()
                                            .toString();

                                    if(req_type.equals("received"))
                                    {
                                        Log.d(TAG, "onDataChange: Request type: Received");
                                        mfriend_status = 2;
                                        vProfile_sendFriequest_btn.setText("Accept Friend Request");
                                        declineRequest.setVisibility(View.VISIBLE);
                                        declineRequest.setEnabled(true);
                                    }
                                    else if(req_type.equals("sent"))
                                    {
                                        Log.d(TAG, "onDataChange: Request type: Sent");
                                        mfriend_status = 1;
                                        vProfile_sendFriequest_btn.setText("Cancel Friend Request");
                                        declineRequest.setVisibility(View.INVISIBLE);
                                        declineRequest.setEnabled(false);
                                    }
                                    mProgressDialog.dismiss();

                                }else{
                                    mFriendDatabase.child(mCurrent_user.getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(user_id))
                                                    {
                                                        vProfile_sendFriequest_btn.setEnabled(true);
                                                        mfriend_status = 3;
                                                        vProfile_sendFriequest_btn.setText("Unfriend this Person");
                                                        declineRequest.setVisibility(View.INVISIBLE);
                                                        declineRequest.setEnabled(false);
                                                    }
                                                    mProgressDialog.dismiss();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    mProgressDialog.dismiss();

                                                }
                                            });
                                }
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        vProfile_sendFriequest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vProfile_sendFriequest_btn.setEnabled(false);
                //mFriend_status cheat codes:
                // 0 = not friends
                // 1 = req send
                // 2 = req recv
                // 3 = friends

                if(mfriend_status == 0)
                {
                    DatabaseReference newNotificationref = mRootRef.child("Notifications").child(user_id).push();
                    String newNotificationID = newNotificationref.getKey();

                    Map notificationData = new HashMap();
                    notificationData.put("from", mCurrent_user.getUid());
                    notificationData.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("friend_request/"+mCurrent_user.getUid()+"/"+user_id+"/request_type","sent");
                    requestMap.put("friend_request/"+user_id+"/"+mCurrent_user.getUid()+"/request_type","received");
                    requestMap.put("Notifications/"+user_id+"/"+newNotificationID,notificationData);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null)
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(view.getContext()
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }

                            vProfile_sendFriequest_btn.setEnabled(true);
                            mfriend_status = 1;
                            vProfile_sendFriequest_btn.setText("Cancel Friend Request");
                        }
                    });
                }
                else if (mfriend_status==1)
                {
                    Log.d(TAG, "onClick: Cancel friend request Started");
                    Map cancelReqMap = new HashMap();

                    cancelReqMap.put("friend_request/"+mCurrent_user.getUid()+"/"+user_id,null);
                    cancelReqMap.put("friend_request/"+user_id+"/"+mCurrent_user.getUid(),null);

                    mRootRef.updateChildren(cancelReqMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                vProfile_sendFriequest_btn.setText("Send Friend Request");
                                vProfile_sendFriequest_btn.setEnabled(true);
                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                                mfriend_status = 0;
                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(view.getContext()
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });


                }
                else if(mfriend_status==2)
                {
                    final String current_date = DateFormat.getDateInstance().format(new Date());

                    Map friendMap = new HashMap();

                    friendMap.put("Friends/"+mCurrent_user.getUid()+"/"+user_id+"/date",current_date);
                    friendMap.put("Friends/"+user_id+"/"+mCurrent_user.getUid()+"/date",current_date);

                    friendMap.put("friend_request/"+mCurrent_user.getUid()+"/"+user_id,null);
                    friendMap.put("friend_request/"+user_id+"/"+mCurrent_user.getUid(),null);

                    mRootRef.updateChildren(friendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                vProfile_sendFriequest_btn.setEnabled(true);
                                mfriend_status = 3;
                                vProfile_sendFriequest_btn.setText("Unfriend this Person");

                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(view.getContext()
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
                else if(mfriend_status==3)
                {
                    Map unfriendMap = new HashMap();

                    unfriendMap.put("Friends/"+mCurrent_user.getUid()+"/"+user_id,null);
                    unfriendMap.put("Friends/"+user_id+"/"+mCurrent_user.getUid(),null);

                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError==null)
                            {
                                vProfile_sendFriequest_btn.setEnabled(true);
                                mfriend_status = 0;
                                vProfile_sendFriequest_btn.setText("Send Friend Request");

                                declineRequest.setVisibility(View.INVISIBLE);
                                declineRequest.setEnabled(false);
                            }
                            else
                            {
                                String error = databaseError.getMessage();
                                Toast.makeText(view.getContext()
                                        ,error
                                        ,Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
                }
            }
        });

        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Decline Friend Request Clicked");

                Map declineMap = new HashMap();

                declineMap.put("friend_request/"+mCurrent_user.getUid()+user_id,null);
                declineMap.put("friend_request/"+user_id+mCurrent_user.getUid(),null);

                mRootRef.updateChildren(declineMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null)
                        {
                            vProfile_sendFriequest_btn.setText("Send Friend Request");
                            declineRequest.setVisibility(View.INVISIBLE);
                            declineRequest.setEnabled(false);
                            mfriend_status = 0;
                        }
                        else
                        {
                            String error = databaseError.getMessage();
                            Toast.makeText(view.getContext()
                                    ,error
                                    ,Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

            }
        });


//        isFollowing();
//        getFollowingCount();
//        getFollowersCount();
//        getPostsCount();

//
//
//        mFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: now following: " + mUser.getUsername());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_following))
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(mUser.getUser_id())
//                        .child(getString(R.string.field_user_id))
//                        .setValue(mUser.getUser_id());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_followers))
//                        .child(mUser.getUser_id())
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(getString(R.string.field_user_id))
//                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                setFollowing();
//            }
//        });
//
//
//        mUnfollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: now unfollowing: " + mUser.getUsername());
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_following))
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(mUser.getUser_id())
//                        .removeValue();
//
//                FirebaseDatabase.getInstance().getReference()
//                        .child(getString(R.string.dbname_followers))
//                        .child(mUser.getUser_id())
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .removeValue();
//                setUnfollowing();
//            }
//        });
//
//        //setupGridView();



        return view;
    }

    private User getUserFromBundle(){
        Log.d(TAG, "getUserFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.intent_user));
        }else{
            return null;
        }
    }

    private void init(){

        //set the profile widgets
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference1.child("Users")
                .orderByChild("userID").equalTo(mUser.getUserID());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found user:" + singleSnapshot.getValue(User.class).toString());

                    UserSettings settings = new UserSettings();
                    settings.setUser(mUser);
                    user_id = settings.getUser().getUserID();
                    setProfileWidgets(settings);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //get the users profile photos

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();

        /* GAKUHA SA DATABASE PHOTOS DATA ADTO SA USER_PHOTOS TABLE
         * NYA ANG UNIQUE ID KAY ANG USER_ID
          * NYA INSIDE KAY ANG FIELDS WITH VALUES*/
        Query query2 = reference2
                .child("Users_Photos")
                .child(mUser.getUserID());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Photo> photos = new ArrayList<Photo>();
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                    Log.d(TAG, "onDataChange: CHECKING >>>" + singleSnapshot.getValue().toString());
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    photo.setPhoto_description(objectMap.get(getString(R.string.field_caption)).toString());
                    photo.setQuantity(objectMap.get(getString(R.string.field_tags)).toString());
                    photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                    photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                    photo.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                    photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());


                    ArrayList<Comment> comments = new ArrayList<Comment>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child(getString(R.string.field_comments)).getChildren()){
                        Comment comment = new Comment();
                        comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                        comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                        comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                        comments.add(comment);
                    }

                    photo.setComments(comments);

                    List<Like> likesList = new ArrayList<Like>();
                    for (DataSnapshot dSnapshot : singleSnapshot
                            .child(getString(R.string.field_likes)).getChildren()){
                        Like like = new Like();
                        like.setUser_id(dSnapshot.getValue(Like.class).getUser_id());
                        likesList.add(like);
                    }
                    photo.setLikes(likesList);
                    photos.add(photo);
                }
                setupImageGrid(photos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setupImageGrid(final ArrayList<Photo> photos){
        //setup our image grid
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        ArrayList<String> imgUrls = new ArrayList<String>();
        for(int i = 0; i < photos.size(); i++){
            imgUrls.add(photos.get(i).getImage_path());
        }
        GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,
                "", imgUrls);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }

    private void setProfileWidgets(UserSettings userSettings){
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.toString());
        //Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + userSettings.getSettings().getUsername());


        //User user = userSettings.getUser();
        User user = userSettings.getUser();
        user_id = user.getUserID();
        if(user.getImage().equals("default"))
        {
            UniversalImageLoader.setImage(user.getImage(), mProfilePhoto, null, "drawable://" );
        }else {
            UniversalImageLoader.setImage(user.getImage(), mProfilePhoto, null, "");
        }
        final String nuser = user.getUserID();
        final String name = user.getUserName();
        mName.setText(user.getUserName());
        mEmail.setText(user.getEmail());
        mPhoneNumber.setText(user.getPhoneNumber());

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, MessageActivity.class);
                    i.putExtra("user_id",nuser );
                    i.putExtra("user_name", name);
                    mContext.startActivity(i);
                }
            });
//        mBackArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: navigating back");
//                getActivity().getSupportFragmentManager().popBackStack();
//                getActivity().finish();
//            }
//        });

    }

        /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext,getActivity() ,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /* GA HIMO UG INTERFACE INSIDE SA FRAGMENT PARA IG
        PARA MA GAMIT SA GRID IMAGE CLICK PERO ANG
        PAG INFLATE SA LAYOUT ADTO SA PROFILE ACTIVITY USING
        THE PARAMETERS SUPPLIED  ARI NGA GE CALL
    */
    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Photo photo, int activityNumber);
    }
}
