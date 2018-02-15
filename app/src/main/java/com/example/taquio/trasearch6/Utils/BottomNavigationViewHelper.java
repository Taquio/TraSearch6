package com.example.taquio.trasearch6.Utils;

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
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.ic_home:
                        Intent intent1 = new Intent(context, HomeActivity2.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_messages:
                        Intent intent2 = new Intent(context, MessagesActivity.class );
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_camera:
                        Intent intent3 = new Intent(context, CameraActivity.class);
                        context.startActivity(intent3);
                        break;
                    case R.id.ic_nearby:
                        Log.d(TAG, "onNavigationItemSelected: Nearby selected");
                        Intent intent4 = new Intent(context, MapsActivity.class);
                        context.startActivity(intent4);
                        break;
                    case R.id.ic_profile:
                        Intent intent5 = new Intent(context, MyProfileActivity.class);
                        context.startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }
}
