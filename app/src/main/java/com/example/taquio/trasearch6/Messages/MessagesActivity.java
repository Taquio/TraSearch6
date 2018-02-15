package com.example.taquio.trasearch6.Messages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.taquio.trasearch6.R;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class MessagesActivity extends AppCompatActivity {
    private static final String TAG = "MessagesActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = MessagesActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

    }


}
