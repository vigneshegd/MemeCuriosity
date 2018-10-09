package com.swcuriosity.memes.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.ui.DesignScreen;
import com.swcuriosity.memes.ui.MemeTemplates;
import com.swcuriosity.memes.ui.ViewPagerActivity;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky N on 11/5/2017.
 */

        import android.content.Context;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.swcuriosity.memes.R;
        import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

        import java.util.List;

import static com.swcuriosity.memes.main.BaseActivity.mActivity;

/**
 * Created by vigneswaran_467at17 on 06-11-2017.
 */

public class MemeImageAdapter extends RecyclerView.Adapter<MemeImageAdapter.ViewHolder> {

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;
//    HashMap<String,ImageUploadInfo> mMapCategoryList =new HashMap<>();

    public MemeImageAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        ImageUploadInfo UploadInfo = (ImageUploadInfo)MainImageUploadInfoList.get(position);

        holder.imageNameTextView.setText(UploadInfo.getImageName());
        holder.imageNameTextView.setVisibility(View.GONE);
        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getImageURL()).centerCrop().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=MainImageUploadInfoList.get(position).getId();
                Intent intent = new Intent(context, ViewPagerActivity.class);
                intent.putExtra("removeflag","1");
                AppConstants.CURRENT_POS=position;
                AppConstants.SELECTED_URL=MainImageUploadInfoList.get(position).getImageURL();
                intent.putExtra("image_id",id);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)MainImageUploadInfoList);
                intent.putExtra("BUNDLE",args);
//                intent.putParcelableArrayListExtra("img_list", (ArrayList<? extends Parcelable>) MainImageUploadInfoList);


                mActivity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
        }
    }
}