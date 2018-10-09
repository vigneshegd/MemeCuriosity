package com.swcuriosity.memes.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.ui.MemeTemplates;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.util.List;

import static com.swcuriosity.memes.main.BaseActivity.mActivity;

/**
 * Created by vigneswaran_467at17 on 06-11-2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    Context mContext;
    List<ImageUploadInfo> MainImageUploadInfoList;
//    HashMap<String,ImageUploadInfo> mMapCategoryList =new HashMap<>();

    public CategoryAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.mContext = context;
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

        //Loading image from Glide library.
         Glide.with(mContext).load(UploadInfo.getImageURL()).centerCrop().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=MainImageUploadInfoList.get(position).getId();
                Intent intent = new Intent(mContext, MemeTemplates.class);
                intent.putExtra("image_id",id);
                intent.putExtra("ImageName",MainImageUploadInfoList.get(position).getImageName());
                intent.putExtra("editable",MainImageUploadInfoList.get(position).getIsExisting());
                intent.putExtra("notify","0");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
                AppConstants.EDITABLE= MainImageUploadInfoList.get(position).getIsExisting();
                AppConstants.SelectedCategory= MainImageUploadInfoList.get(position);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.
                startActivity(intent);
//                mActivity.finish();

            }
        });
        holder.editableImg.setVisibility(View.VISIBLE);
        if((MainImageUploadInfoList.get(position).getIsExisting()!=null)&&(MainImageUploadInfoList.get(position).getIsExisting().equalsIgnoreCase("0"))){
            holder.editableImg.setImageResource(R.drawable.ic_share_white);
        }else {
            holder.editableImg.setImageResource(R.drawable.ic_edit_white_24dp);
        }
        holder.editableImg.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView,editableImg;
        public TextView imageNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            editableImg= (ImageView) itemView.findViewById(R.id.edit_share);
            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);
        }
    }
}