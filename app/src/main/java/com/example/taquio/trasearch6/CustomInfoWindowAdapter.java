package com.example.taquio.trasearch6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ARVN on 2/14/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        this.mWindow = LayoutInflater.from(context).inflate(R.layout.map_info_window, null);
        this.mContext = context;
    }

    private void renderWindowText(Marker marker, View view)
    {
//        for the title of the place
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);

        if(!title.equals(""))
        {
            tvTitle.setText(title);
        }
//        for the details of the place
        String snippet = marker.getTitle();
        TextView detail = (TextView) view.findViewById(R.id.details);

        if(!detail.equals(""))
        {
            detail.setText(title);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return null;
    }
}
