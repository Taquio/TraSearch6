package com.example.taquio.trasearch6;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by ARVN on 1/26/2018.
 */

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "StaggeredRecyclerViewAd";

    private ArrayList<String> mItemName = new ArrayList<>();
    private ArrayList<String> mItemImage = new ArrayList<>();
    private Context mContext;

    public StaggeredRecyclerViewAdapter(Context mContext, ArrayList<String> mItemName, ArrayList<String> mItemImage) {
        this.mItemName = mItemName;
        this.mItemImage = mItemImage;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(mItemImage.get(position))
                .apply(requestOptions)
                .into(holder.itemimage);

        holder.itemname.setText(mItemName.get(position));

        holder.itemimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mItemName.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView itemimage;
        TextView itemname;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemimage = itemView.findViewById(R.id.itemimage);
            this.itemname = itemView.findViewById(R.id.itemname);

        }
    }
}
