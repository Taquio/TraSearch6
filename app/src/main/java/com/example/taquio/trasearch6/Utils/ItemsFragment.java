package com.example.taquio.trasearch6.Utils;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.eschao.android.widget.elasticlistview.ElasticListView;
import com.example.taquio.trasearch6.Models.Photo;
import com.example.taquio.trasearch6.Models.User;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * Created by Del Mar on 2/7/2018.
 */

public class ItemsFragment extends Fragment
        //implements OnUpdateListener, OnLoadListener
{

    private static final String TAG = "HomeFragment";
    public StoriesRecyclerViewAdapter mStoriesAdapter;
    //vars
    private ArrayList<Photo> mPhotos;
    private ArrayList<Photo> mPaginatedPhotos;
    private ArrayList<String> mFollowing;
    private ArrayList<String> mAllUsers;
    private int recursionIterator = 0;
    //    private ListView mListView;
    private ElasticListView mListView;
    private int resultsCount = 0;
    private ArrayList<User> mUserAccountSettings;
    //    private ArrayList<UserStories> mAllUserStories = new ArrayList<>();
    private JSONArray mMasterStoriesArray;
    private RecyclerView mRecyclerView;

//    @Override
//    public void onUpdate() {
//        Log.d(TAG, "ElasticListView: updating list view...");
//
//        getFollowing();
//    }

//    @Override
//    public void onLoad() {
//        Log.d(TAG, "ElasticListView: loading...");
//
//        // Notify load is done
//        mListView.notifyLoaded();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_items, container, false);
////        mListView = (ListView) view.findViewById(R.id.listView);
//        mListView = view.findViewById(R.id.listView);
//
//        initListViewRefresh();
//        getFollowing();
//
//        return view;
//    }
//
//    private void initListViewRefresh(){
//        mListView.setHorizontalFadingEdgeEnabled(true);
//        mListView.setAdapter(adapter);
//        mListView.enableLoadFooter(true)
//                .getLoadFooter().setLoadAction(LoadFooter.LoadAction.RELEASE_TO_LOAD);
//        mListView.setOnUpdateListener(this)
//                .setOnLoadListener(this);
////        mListView.requestUpdate();
//    }
//
//
//    /**
//     //     * Retrieve all user id's that current user is following
//     //     */
//    private void getFollowing() {
//        Log.d(TAG, "getFollowing: searching for following");
//
//        clearAll();
//        //also add your own id to the list
////        mFollowing.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
////        Query query = FirebaseDatabase.getInstance().getReference()
////                .child(getActivity().getString(R.string.dbname_following))
////                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
////                ;
//        Query query = FirebaseDatabase.getInstance().getReference()
//                .child(getActivity().getString(R.string.dbname_user_photos))
//                .orderByKey();
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "getFollowing: found user: " + singleSnapshot
//                            .getChildren());
//
////                    mFollowing.add(singleSnapshot
////                            .child(getString(R.string.field_user_id)).getValue().toString());
//             mAllUsers.add(singleSnapshot.getKey().toString());
//                }
//
//                getPhotos();
////                getMyUserAccountSettings();
//                getFriendsAccountSettings();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//        });
//
//    }
//    private void getFriendsAccountSettings(){
//        Log.d(TAG, "getFriendsAccountSettings: getting friends account settings.");
//
////        for(int i = 0; i < mFollowing.size(); i++) {
//        for(int i = 0; i < mAllUsers.size(); i++) {
//            Log.d(TAG, "getFriendsAccountSettings: user: " + mAllUsers.get(i));
//            final int count = i;
//            Query query = FirebaseDatabase.getInstance().getReference()
//                    .child("Users")
//                    .orderByKey();
////                    .equalTo(mFollowing.get(i));
//
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Log.d(TAG, "getFriendsAccountSettings: got a user: " + snapshot.getValue(User.class).getName());
//                        mUserAccountSettings.add(snapshot.getValue(User.class));
//
//                        if(count == 0){
//                            JSONObject userObject = new JSONObject();
//                            try {
//                                userObject.put("Name", mUserAccountSettings.get(count).getName());
//                                userObject.put("UserName", mUserAccountSettings.get(count).getUserName());
//                                userObject.put("Image", mUserAccountSettings.get(count).getImage());
//                                userObject.put("userID", mUserAccountSettings.get(count).getUserID());
//                                JSONObject userSettingsStoryObject = new JSONObject();
//                                userSettingsStoryObject.put("Users", userObject);
//                                mMasterStoriesArray.put(0, userSettingsStoryObject);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                    }
////                    if (count == mFollowing.size() - 1) {
////                        getFriendsStories();
////                    }
//                    if (count == mAllUsers.size() - 1) {
//                        getFriendsStories();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }
//
//
//    private void getFriendsStories(){
//        Log.d(TAG, "getFriendsStories: getting stories of following.");
//
//        for(int i = 0; i < mUserAccountSettings.size(); i++){
//            Log.d(TAG, "getFriendsStories: checking user for stories: " + mUserAccountSettings.get(i));
//            final int count = i;
//            Query query = FirebaseDatabase.getInstance().getReference()
//                    .child(getString(R.string.dbname_stories))
//                    .child(mUserAccountSettings.get(i).getUserID());
//
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    JSONArray storiesArray = new JSONArray();
//                    JSONObject userObject = new JSONObject();
//
//                    Log.d(TAG, "getFriendsStories: count: " + count);
//                    Log.d(TAG, "getFriendsStories: user: " + mUserAccountSettings.get(count).getName());
//                    try{
//                        if(count != 0){
//                            userObject.put("Name", mUserAccountSettings.get(count).getName());
//                            userObject.put("UserName", mUserAccountSettings.get(count).getUserName());
//                            userObject.put("Image", mUserAccountSettings.get(count).getImage());
//                            userObject.put("userID", mUserAccountSettings.get(count).getUserID());
//                        }
//
//                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                            JSONObject story = new JSONObject();
//                            story.put("userID", snapshot.getValue(Story.class).getUser_id());
//                            story.put(getString(R.string.field_timestamp), snapshot.getValue(Story.class).getTimestamp());
//                            story.put(getString(R.string.field_image_uri), snapshot.getValue(Story.class).getImage_url());
//                            story.put(getString(R.string.field_video_uri), snapshot.getValue(Story.class).getVideo_url());
//                            story.put(getString(R.string.field_story_id), snapshot.getValue(Story.class).getStory_id());
//                            story.put(getString(R.string.field_views), snapshot.getValue(Story.class).getViews());
//                            story.put(getString(R.string.field_duration), snapshot.getValue(Story.class).getDuration());
//
//
//                            Log.d(TAG, "getFriendsStories: got a story: " + story.get(getString(R.string.field_user_id)));
////                            Log.d(TAG, "getFriendsStories: story: " + story.toString());
//                            storiesArray.put(story);
//                        }
//
//                        JSONObject userSettingsStoryObject = new JSONObject();
//                        if(count != 0){
//                            userSettingsStoryObject.put("Users", userObject);
//                            if(storiesArray.length() > 0){
//                                userSettingsStoryObject.put(getString(R.string.user_stories), storiesArray);
//                                int position = mMasterStoriesArray.length();
//                                mMasterStoriesArray.put(position, userSettingsStoryObject);
//                                Log.d(TAG, "onDataChange: adding list of stories to position #" + position);
//                            }
//                        }
//                        else {
//                            userObject = mMasterStoriesArray.getJSONObject(0).getJSONObject("Users");
//                            userSettingsStoryObject.put("Users", userObject);
//                            userSettingsStoryObject.put(getString(R.string.user_stories), storiesArray);
////                            int position = mMasterStoriesArray.length() - 1;
//                            int position = 0;
//                            mMasterStoriesArray.put(position, userSettingsStoryObject);
//                            Log.d(TAG, "onDataChange: adding list of stories to position #" + position);
//                        }
//
//
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//                    if(!dataSnapshot.exists()){
//                        Log.d(TAG, "getFriendsStories: no stories could be found.");
////                        Log.d(TAG, "getFriendsStories: " + mMasterStoriesArray.toString());
//
//                    }
////                    if(count == mFollowing.size() - 1){
////                        initRecyclerView();
////                    }
//                    if(count == mAllUsers.size() - 1){
//                        initRecyclerView();
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//    }
//
//
//
//    private void initRecyclerView(){
//        Log.d(TAG, "initRecyclerView: init recyclerview.");
//        if(mRecyclerView == null){
//            TextView textView = new TextView(getActivity());
//            textView.setText("Stories");
//            textView.setTextColor(getResources().getColor(R.color.black));
//            textView.setTextSize(14);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT
//            );
//            textView.setLayoutParams(params);
//            mListView.addHeaderView(textView);
//
//            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//            mRecyclerView = new RecyclerView(getActivity());
//            mRecyclerView.setLayoutManager(layoutManager);
//            mListView.addHeaderView(mRecyclerView);
//        }
//
////        mStoriesAdapter = new StoriesRecyclerViewAdapter(mMasterStoriesArray, getActivity());
////        mRecyclerView.setAdapter(mStoriesAdapter);
//    }
//
//    private void clearAll(){
//        if(mFollowing != null){
//            mFollowing.clear();
//        }
////        if(mPhotos != null){
////            mPhotos.clear();
////            if(adapter != null){
////                adapter.clear();
////                adapter.notifyDataSetChanged();
////            }
////        }
//        if(mUserAccountSettings != null){
//            mUserAccountSettings.clear();
//        }
//        if(mPaginatedPhotos != null){
//            mPaginatedPhotos.clear();
//        }
//        mMasterStoriesArray = new JSONArray(new ArrayList<String>());
////        if(mStoriesAdapter != null){
////            mStoriesAdapter.notifyDataSetChanged();
////        }
//        if(mRecyclerView != null){
//            mRecyclerView.setAdapter(null);
//        }
//        mFollowing = new ArrayList<>();
//        mAllUsers = new ArrayList<>();
//        mPhotos = new ArrayList<>();
//        mPaginatedPhotos = new ArrayList<>();
//        mUserAccountSettings = new ArrayList<>();
//    }
//
//
////    private void getPhotos(){
////        Log.d(TAG, "getPhotos: getting list of photos");
////
////        for(int i = 0; i < mFollowing.size(); i++){
////            final int count = i;
////            Query query = FirebaseDatabase.getInstance().getReference()
////                    .child(getActivity().getString(R.string.dbname_user_photos))
////                    .child(mFollowing.get(i))
////                    .orderByChild(getString(R.string.field_user_id))
////                    .equalTo(mFollowing.get(i))
////                    ;
////            query.addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(DataSnapshot dataSnapshot) {
////                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
////
////                        Photo newPhoto = new Photo();
////                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
////
////                        newPhoto.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
////                        newPhoto.setTags(objectMap.get(getString(R.string.field_tags)).toString());
////                        newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
////                        newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
////                        newPhoto.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
////                        newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
////
////                        Log.d(TAG, "getPhotos: photo: " + newPhoto.getPhoto_id());
////                        List<Comment> commentsList = new ArrayList<Comment>();
////                        for (DataSnapshot dSnapshot : singleSnapshot
////                                .child(getString(R.string.field_comments)).getChildren()){
////                            Map<String, Object> object_map = (HashMap<String, Object>) dSnapshot.getValue();
////                            Comment comment = new Comment();
////                            comment.setUser_id(object_map.get(getString(R.string.field_user_id)).toString());
////                            comment.setComment(object_map.get(getString(R.string.field_comment)).toString());
////                            comment.setDate_created(object_map.get(getString(R.string.field_date_created)).toString());
////                            commentsList.add(comment);
////                        }
////                        newPhoto.setComments(commentsList);
////                        mPhotos.add(newPhoto);
////                    }
////                    if(count >= mFollowing.size() - 1){
////                        //display the photos
////                        displayPhotos();
////                    }
////
////                }
////
////                @Override
////                public void onCancelled(DatabaseError databaseError) {
////                    Log.d(TAG, "onCancelled: query cancelled.");
////                }
////            });
////
////        }
////    }
//private void getPhotos(){
//    Log.d(TAG, "getPhotos: getting list of photos");
//
//    for(int i = 0; i < mAllUsers.size(); i++){
//        final int count = i;
//        Query query = FirebaseDatabase.getInstance().getReference()
//                .child(getActivity().getString(R.string.dbname_user_photos))
//                .child(mAllUsers.get(i))
//                .orderByChild(getString(R.string.field_user_id))
//                .equalTo(mAllUsers.get(i))
//                ;
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//
//                    Photo newPhoto = new Photo();
//                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
//
//                    newPhoto.setCaption(objectMap.get(getString(R.string.field_caption)).toString());
//                    newPhoto.setTags(objectMap.get(getString(R.string.field_tags)).toString());
//                    newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
//                    newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
//                    newPhoto.setDate_created(objectMap.get(getString(R.string.field_date_created)).toString());
//                    newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
//
//                    Log.d(TAG, "getPhotos: photo: " + newPhoto.getPhoto_id());
//                    List<Comment> commentsList = new ArrayList<Comment>();
//                    for (DataSnapshot dSnapshot : singleSnapshot
//                            .child(getString(R.string.field_comments)).getChildren()){
//                        Map<String, Object> object_map = (HashMap<String, Object>) dSnapshot.getValue();
//                        Comment comment = new Comment();
//                        comment.setUser_id(object_map.get(getString(R.string.field_user_id)).toString());
//                        comment.setComment(object_map.get(getString(R.string.field_comment)).toString());
//                        comment.setDate_created(object_map.get(getString(R.string.field_date_created)).toString());
//                        commentsList.add(comment);
//                    }
//                    newPhoto.setComments(commentsList);
//                    mPhotos.add(newPhoto);
//                }
//                if(count >= mAllUsers.size() - 1){
//                    //display the photos
//                    displayPhotos();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: query cancelled.");
//            }
//        });
//
//    }
//}
//
//    private void displayPhotos(){
////        mPaginatedPhotos = new ArrayList<>();
//        if(mPhotos != null){
//
//            try{
//
//                //sort for newest to oldest
//                Collections.sort(mPhotos, new Comparator<Photo>() {
//                    public int compare(Photo o1, Photo o2) {
//                        return o2.getDate_created().compareTo(o1.getDate_created());
//                    }
//                });
//
//                //we want to load 10 at a time. So if there is more than 10, just load 10 to start
//                int iterations = mPhotos.size();
//                if(iterations > 10){
//                    iterations = 10;
//                }
////
//                resultsCount = 0;
//                for(int i = 0; i < iterations; i++){
//                    mPaginatedPhotos.add(mPhotos.get(i));
//                    resultsCount++;
//                    Log.d(TAG, "displayPhotos: adding a photo to paginated list: " + mPhotos.get(i).getPhoto_id());
//                }
//
//                adapter = new MainFeedListAdapter(getActivity(), R.layout.layout_mainfeed_listitem, mPaginatedPhotos);
//                mListView.setAdapter(adapter);
//
//                // Notify update is done
//                mListView.notifyUpdated();
//
//            }catch (IndexOutOfBoundsException e){
//                Log.e(TAG, "displayPhotos: IndexOutOfBoundsException:" + e.getMessage() );
//            }catch (NullPointerException e){
//                Log.e(TAG, "displayPhotos: NullPointerException:" + e.getMessage() );
//            }
//        }
//    }
//
//    public void displayMorePhotos(){
//        Log.d(TAG, "displayMorePhotos: displaying more photos");
//
//        try{
//
//            if(mPhotos.size() > resultsCount && mPhotos.size() > 0){
//
//                int iterations;
//                if(mPhotos.size() > (resultsCount + 10)){
//                    Log.d(TAG, "displayMorePhotos: there are greater than 10 more photos");
//                    iterations = 10;
//                }else{
//                    Log.d(TAG, "displayMorePhotos: there is less than 10 more photos");
//                    iterations = mPhotos.size() - resultsCount;
//                }
//
//                //add the new photos to the paginated list
//                for(int i = resultsCount; i < resultsCount + iterations; i++){
//                    mPaginatedPhotos.add(mPhotos.get(i));
//                }
//
//                resultsCount = resultsCount + iterations;
//                adapter.notifyDataSetChanged();
//            }
//        }catch (IndexOutOfBoundsException e){
//            Log.e(TAG, "displayPhotos: IndexOutOfBoundsException:" + e.getMessage() );
//        }catch (NullPointerException e){
//            Log.e(TAG, "displayPhotos: NullPointerException:" + e.getMessage() );
//        }
//    }


}