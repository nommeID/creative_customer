package com.nomme.creative.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.nomme.creative.DTO.ArtistDetailsDTO;
import com.nomme.creative.DTO.GalleryDTO;
import com.nomme.creative.R;
import com.nomme.creative.interfacess.Consts;
import com.nomme.creative.ui.adapter.AdapterGallery;
import com.nomme.creative.utils.CustomTextViewBold;
import com.nomme.creative.utils.TouchImageView;

import java.util.ArrayList;

public class ImageGallery extends Fragment implements View.OnClickListener {
    private String TAG = ImageGallery.class.getSimpleName();
    private View view;
    private ArtistDetailsDTO artistDetailsDTO;
    private RecyclerView rvGallery;
    private ArrayList<GalleryDTO> galleryList;
    private AdapterGallery adapterGallery;
    private Bundle bundle;
    private GridLayoutManager gridLayoutManager;
    private LinearLayout llBack;
    private TouchImageView ivZoom;
    private RelativeLayout rlZoomImg, rlView;
    private ImageView ivClose;
    private CustomTextViewBold tvNotFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        bundle = this.getArguments();
        artistDetailsDTO = (ArtistDetailsDTO) bundle.getSerializable(Consts.ARTIST_DTO);
        showUiAction(view);
        return view;
    }

    public void showUiAction(View v) {
        rvGallery = (RecyclerView) v.findViewById(R.id.rvGallery);
        tvNotFound = (CustomTextViewBold) v.findViewById(R.id.tvNotFound);
        llBack = (LinearLayout) v.findViewById(R.id.llBack);
        ivZoom = (TouchImageView) v.findViewById(R.id.ivZoom);
        rlZoomImg = (RelativeLayout) v.findViewById(R.id.rlZoomImg);
        rlView = (RelativeLayout) v.findViewById(R.id.rlView);
        ivClose = (ImageView) v.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(this);
        showData();

    }


    public void showData() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        galleryList = new ArrayList<>();
        galleryList = artistDetailsDTO.getGallery();
        if (galleryList.size() > 0) {
            tvNotFound.setVisibility(View.GONE);
            rlView.setVisibility(View.VISIBLE);
            adapterGallery = new AdapterGallery(ImageGallery.this, galleryList);
            rvGallery.setLayoutManager(gridLayoutManager);
            rvGallery.setAdapter(adapterGallery);
        } else {
            tvNotFound.setVisibility(View.VISIBLE);
            rlView.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClose:
                rlZoomImg.setVisibility(View.GONE);
                break;
        }
    }

    public void showImg(String imgURL) {
        rlZoomImg.setVisibility(View.VISIBLE);
        Glide
                .with(getActivity())
                .load(imgURL)
                .placeholder(R.drawable.dummyuser_image)
                .into(ivZoom);
    }

}
