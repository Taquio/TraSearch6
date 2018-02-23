package com.example.taquio.trasearch6.Utils;

import android.os.Environment;

/**
 * Created by Edward 2018.
 */

public class FilePaths {

    //"storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";
    public String FIREBASE_IMAGE_STORAGE = "Photos/";

}
