package com.example.taquio.trasearch6.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.taquio.trasearch6.Camera.CameraActivity;
import com.example.taquio.trasearch6.HomeActivity2;
import com.example.taquio.trasearch6.MapsActivity;
import com.example.taquio.trasearch6.MessageActivity;
import com.example.taquio.trasearch6.Messages.MessagesActivity;
import com.example.taquio.trasearch6.MyProfileActivity;
import com.example.taquio.trasearch6.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView (Main)");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity2.class);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_messages:
                        Intent intent2 = new Intent(context, MessagesActivity.class );
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_camera:
                        Intent intent3 = new Intent(context, CameraActivity.class);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_nearby:
                        //Log.d(TAG, "onNavigationItemSelected: Nearby selected");
                        Intent intent4 = new Intent(context, MapsActivity.class);
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.ic_profile:
                        Intent intent5 = new Intent(context, MyProfileActivity.class);
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }

                return false;
            }
        });
    }
}
