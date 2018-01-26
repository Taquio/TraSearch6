package com.example.taquio.trasearch6;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ARVN on 1/17/2018.
 */

public class SliderAdapter extends PagerAdapter{
    Context context;
    LayoutInflater inflater;

    //list of images
    public int[] list_images = {
            0,
            R.drawable.search,
            R.drawable.camera,
            R.drawable.navigation,
            R.drawable.chat
    };
    //list of titles
    public String[] list_titles = {
            "WELCOME",
            "SEARCH",
            "VIDEO",
            "MAP",
            "CHAT",
    };
    //list of description
    public String[] list_desc = {
            "We are TraSearch.",
            "Searching is now an inch away.",
            "Videos now are more and less.",
            "Maps lends a hand.",
            "Chat to communicate.",
    };
    //list of background
    public int[] list_backgroundcolor = {
            R.drawable.white
//            Color.rgb(252,252,252),
//            Color.rgb(252,252,252),
//            Color.rgb(252,252,252),
//            Color.rgb(252,252,252),
//            Color.rgb(252,252,252)
    };

    public SliderAdapter (Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_titles.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide, container,false);
        LinearLayout layout = view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);
        TextView txttitle = (TextView) view.findViewById(R.id.txttitle);
        TextView txtdesc = (TextView) view.findViewById(R.id.txtdesc);
        layout.setBackgroundColor(list_backgroundcolor[position]);
        imgslide.setImageResource(list_images[position]);
        txttitle.setText(list_titles[position]);
        txtdesc.setText(list_desc[position]);
        container.addView(view);
        return  view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout)object);
    }
}
