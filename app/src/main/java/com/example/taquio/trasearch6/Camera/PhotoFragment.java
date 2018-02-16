package com.example.taquio.trasearch6.Camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch6.EditProfileActivity;
import com.example.taquio.trasearch6.R;
import com.example.taquio.trasearch6.Utils.Permissions;


/**
 * Created by Edward
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int  CAMERA_REQUEST_CODE = 5;



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");


                if(((CameraActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM){
                    if(((CameraActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])){
                        Log.d(TAG, "onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

        return view;
    }

    private boolean isRootTask(){
        if(((CameraActivity)getActivity()).getTask() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");

            Bitmap bitmap;


            if(isRootTask()){
                try{
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                    startActivity(intent);
                }catch (NullPointerException e){
                    Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
                }
            }else{
               try{
                   bitmap = (Bitmap) data.getExtras().get("data");
                   Log.d(TAG, "onActivityResult: received  new bitmap from camera: " + bitmap);
                   Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                   intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                   intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                   startActivity(intent);
                   getActivity().finish();
               }catch (NullPointerException e){
                   Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
               }
            }

        }
    }
}

































