package com.example.taquio.trasearch6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.eschao.android.widget.elasticlistview.ElasticListView;
import com.eschao.android.widget.elasticlistview.LoadFooter;
import com.eschao.android.widget.elasticlistview.OnLoadListener;
import com.eschao.android.widget.elasticlistview.OnUpdateListener;
import com.example.taquio.trasearch6.Models.Comment;
import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Utils.MainFeedListAdapter;
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

public class SaveItemActivity extends AppCompatActivity implements OnUpdateListener, OnLoadListener {
    private static final String TAG = "SaveItemActivity";

    private ElasticListView mListView;
    private SaveListAdapter adapter;
    private ArrayList<String> msaveUserId;
    private ArrayList<Photo> mPhotos;
    private ArrayList<Photo> mPaginatedPhotos;
    private int resultsCount = 0;

    @Override
    public void onUpdate() {
        getSaveItems();
    }
    @Override
    public void onLoad() {
        Log.d(TAG, "ElasticListView: loading...");

        // Notify load is done
        mListView.notifyLoaded();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_item);

        mListView = findViewById(R.id.listView);

        initListViewRefresh();
        getSaveItems();
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
    private void getSaveItems() {
        Log.d(TAG, "getKeys: searching for following");

        clearAll();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Bookmarks")
                .orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "getKeys: found user: " + singleSnapshot
                            .getChildren());

                    msaveUserId.add(singleSnapshot.getKey());
                }

                getPhotos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    private void clearAll(){
        mPhotos = new ArrayList<>();
        mPaginatedPhotos = new ArrayList<>();
        msaveUserId = new ArrayList<>();
    }
    private void getPhotos(){
        Log.d(TAG, "getPhotos: getting list of photos");

        for(int i = 0; i < msaveUserId.size(); i++){
            final int count = i;
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Users_Photos")
                    .child(msaveUserId.get(i))
                    .orderByChild("user_id")
                    .equalTo(msaveUserId.get(i));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                        Photo newPhoto = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        newPhoto.setPhoto_description(objectMap.get(getString(R.string.field_caption)).toString());
                        newPhoto.setQuantity(objectMap.get(getString(R.string.field_tags)).toString());
                        newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                        newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        newPhoto.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
                        newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());


                        mPhotos.add(newPhoto);
                    }
                    if(count >= msaveUserId.size() - 1){
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
                adapter = new SaveListAdapter(this, R.layout.layout_save_items, mPaginatedPhotos);
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
