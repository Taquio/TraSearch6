package com.example.taquio.trasearch6;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class JunkShopsFragment extends Fragment {
    private static final String TAG = "JunkShopsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_junkshops, container, false);

        return view;
    }
}
