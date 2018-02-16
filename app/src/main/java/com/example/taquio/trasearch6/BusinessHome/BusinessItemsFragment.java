package com.example.taquio.trasearch6.BusinessHome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch6.R;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessItemsFragment extends Fragment {
    private static final String TAG = "BusinessItemsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.business_home_items_fragment, container, false);

        return view;
    }
}
