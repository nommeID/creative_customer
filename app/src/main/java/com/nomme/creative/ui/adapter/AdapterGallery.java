package com.nomme.creative.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nomme.creative.DTO.GalleryDTO;
import com.nomme.creative.R;
import com.nomme.creative.ui.fragment.ImageGallery;

import java.util.ArrayList;

/**
 * Created by VARUN on 01/01/19.
 */

public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.MyViewHolder> {
    private ImageGallery imageGallery;
    LayoutInflater mLayoutInflater;
    private ArrayList<GalleryDTO> gallery;
    private Context context;

    public AdapterGallery(ImageGallery imageGallery, ArrayList<GalleryDTO> gallery) {
        this.imageGallery = imageGallery;
        context = imageGallery.getActivity();
        this.gallery = gallery;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_gallery, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide
                .with(context)
                .load(gallery.get(position).getImage())
                .placeholder(R.drawable.dummyuser_image)
                .into(holder.iv_bottom_foster);

    /*    holder.iv_bottom_foster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageGallery.showImg(gallery.get(position).getImage());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_bottom_foster;

        public MyViewHolder(View view) {
            super(view);

            iv_bottom_foster = (ImageView) itemView.findViewById(R.id.iv_bottom_foster);


        }
    }
}
