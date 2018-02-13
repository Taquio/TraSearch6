package com.example.taquio.trasearch6;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by taquio on 2/13/18.
 */

public class BaseApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
