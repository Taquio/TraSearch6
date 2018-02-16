package com.example.taquio.trasearch6.Messages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch6.R;

/**
 * Created by Del Mar on 2/15/2018.
 */

public class InboxFragment extends Fragment {
    private static final String TAG = "InboxFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        return view;
    }
}
