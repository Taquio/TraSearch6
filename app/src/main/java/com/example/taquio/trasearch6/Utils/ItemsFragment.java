package com.example.taquio.trasearch6.Utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.eschao.android.widget.elasticlistview.ElasticListView;
import com.eschao.android.widget.elasticlistview.LoadFooter;
import com.eschao.android.widget.elasticlistview.OnLoadListener;
import com.eschao.android.widget.elasticlistview.OnUpdateListener;
import com.example.taquio.trasearch6.Models.Comment;
import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Models.User;
import com.example.taquio.trasearch6.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Edward on 2/7/2018.
 */

public class ItemsFragment extends Fragment implements  OnUpdateListener, OnLoadListener {

    private static final String TAG = "HomeFragment";
    //vars
    private ArrayList<Photo> mPhotos;
    private ArrayList<Photo> mPaginatedPhotos;
    private ArrayList<String> mFollowing;
    private ArrayList<String> mAllUsers;
    private ArrayList<User> mUserAccountSettings;
    private ElasticListView mListView;
    private int resultsCount = 0;
    private MainFeedListAdapter adapter;
    private int ACTIVITY_NUM = 0;
    private EditText openSearch;
    private Context mContext = getActivity();
    private Toolbar toolbar;
    private GridView mgridView;

    @Override
    public void onUpdate() {
        Log.d(TAG, "ElasticListView: updating list view...");

        getKeys();
    }

    @Override
    public void onLoad() {
        Log.d(TAG, "ElasticListView: loading...");

        // Notify load is done
        mListView.notifyLoaded();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items, container, false);
        mListView = view.findViewById(R.id.listView);

        toolbar = view.findViewById(R.id.profileToolBar);

//        openSearch = view.findViewById(R.id.opensearch);
//        openSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(mContext, SearchActivity.class));
//            }
//        });
        initListViewRefresh();
        getKeys();

        return view;
    }

    private void initListViewRefresh(){
        mListView.setHorizontalFadingEdgeEnabled(true);
        mListView.setAdapter(adapter);
        mListView.enableLoadFooter(true)
                .getLoadFooter().setLoadAction(LoadFooter.LoadAction.RELEASE_TO_LOAD);
        mListView.setOnUpdateListener(this)
                .setOnLoadListener(this);
//        mListView.requestUpdate();
    }
    private void getKeys() {
        Log.d(TAG, "getKeys: searching for following");

        clearAll();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Users_Photos")
                .orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "getKeys: found user: " + singleSnapshot
                            .getChildren());

//                    mFollowing.add(singleSnapshot
//                            .child(getString(R.string.field_user_id)).getValue().toString());
                    mAllUsers.add(singleSnapshot.getKey());
                }

                getPhotos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    private void getPhotos(){
        Log.d(TAG, "getPhotos: getting list of photos");

        for(int i = 0; i < mAllUsers.size(); i++){
            final int count = i;
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Users_Photos")
                    .child(mAllUsers.get(i))
                    .orderByChild("user_id")
                    .equalTo(mAllUsers.get(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                        Photo newPhoto = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        newPhoto.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
                        newPhoto.setTags(objectMap.get(getString(R.string.field_tags)).toString());
                        newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                        newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        newPhoto.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());

                        Log.d(TAG, "getPhotos: photo: " + newPhoto.getPhoto_id());
                        List<Comment> commentsList = new ArrayList<Comment>();
                        for (DataSnapshot dSnapshot : singleSnapshot
                                .child(getString(R.string.field_comments)).getChildren()){
                            Map<String, Object> object_map = (HashMap<String, Object>) dSnapshot.getValue();
                            Comment comment = new Comment();
                            comment.setUser_id(object_map.get(getString(R.string.field_user_id)).toString());
                            comment.setComment(object_map.get(getString(R.string.field_comment)).toString());
                            comment.setDate_created(object_map.get(getString(R.string.field_date_created)).toString());
                            commentsList.add(comment);
                        }
                        newPhoto.setComments(commentsList);
                        mPhotos.add(newPhoto);
                    }
                    if(count >= mAllUsers.size() - 1){
                        //display the photos
                        displayPhotos();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });

        }
    }

    private void clearAll(){
        if(mFollowing != null){
            mFollowing.clear();
        }
        if(mPhotos != null){
            mPhotos.clear();
            if(adapter != null){
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
        if(mUserAccountSettings != null){
            mUserAccountSettings.clear();
        }
        if(mPaginatedPhotos != null){
            mPaginatedPhotos.clear();
        }
        mFollowing = new ArrayList<>();
        mPhotos = new ArrayList<>();
        mPaginatedPhotos = new ArrayList<>();
        mUserAccountSettings = new ArrayList<>();
        mAllUsers = new ArrayList<>();
    }


    private void displayPhotos(){
        if(mPhotos != null){

            try{

                //sort for newest to oldest
                Collections.sort(mPhotos, new Comparator<Photo>() {
                    public int compare(Photo o1, Photo o2) {
                        return o2.getDate_created().compareTo(o1.getDate_created());
                    }
                });

                //we want to load 10 at a time. So if there is more than 10, just load 10 to start
                int iterations = mPhotos.size();
                if(iterations > 10){
                    iterations = 10;
                }
//
                resultsCount = 0;
                for(int i = 0; i < iterations; i++){
                    mPaginatedPhotos.add(mPhotos.get(i));
                    resultsCount++;
                    Log.d(TAG, "displayPhotos: adding a photo to paginated list: " + mPhotos.get(i).getPhoto_id());
                }
                adapter = new MainFeedListAdapter(getActivity(), R.layout.layout_mainfeed_listitem, mPaginatedPhotos);
                mListView.setAdapter(adapter);

                // Notify update is done
                mListView.notifyUpdated();


            }catch (IndexOutOfBoundsException e){
                Log.e(TAG, "displayPhotos: IndexOutOfBoundsException:" + e.getMessage() );
            }catch (NullPointerException e){
                Log.e(TAG, "displayPhotos: NullPointerException:" + e.getMessage() );
            }
        }
    }

    public void displayMorePhotos(){
        Log.d(TAG, "displayMorePhotos: displaying more photos");

        try{

            if(mPhotos.size() > resultsCount && mPhotos.size() > 0){

                int iterations;
                if(mPhotos.size() > (resultsCount + 10)){
                    Log.d(TAG, "displayMorePhotos: there are greater than 10 more photos");
                    iterations = 10;
                }else{
                    Log.d(TAG, "displayMorePhotos: there is less than 10 more photos");
                    iterations = mPhotos.size() - resultsCount;
                }

                //add the new photos to the paginated list
                for(int i = resultsCount; i < resultsCount + iterations; i++){
                    mPaginatedPhotos.add(mPhotos.get(i));
                }

                resultsCount = resultsCount + iterations;
                adapter.notifyDataSetChanged();
            }
        }catch (IndexOutOfBoundsException e){
            Log.e(TAG, "displayPhotos: IndexOutOfBoundsException:" + e.getMessage() );
        }catch (NullPointerException e){
            Log.e(TAG, "displayPhotos: NullPointerException:" + e.getMessage() );
        }
    }

}