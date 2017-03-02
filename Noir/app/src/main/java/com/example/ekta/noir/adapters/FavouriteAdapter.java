package com.example.ekta.noir.adapters;

/**
 * Created by Ekta on 24-02-2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.example.ekta.noir.R;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.utils.VolleySingleton;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;



public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private Context context;
    private ArrayList<UnsplashData> unsplashList = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private FavouriteAdapterOnClickHandler clickHandler;

    public FavouriteAdapter(Context context, FavouriteAdapterOnClickHandler vh){
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();

        this.context = context;
        this.clickHandler = vh;
    }

    public void setAppList(ArrayList<UnsplashData> unsplashList){
        this.unsplashList = unsplashList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_favourite, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        UnsplashData currentUnsplash = unsplashList.get(position);

        String photo = currentUnsplash.getUrlRegular();
        if (photo != null) {
//            imageLoader.get(photo, new ImageLoader.ImageListener() {
//                @Override
//                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
//                    Bitmap image = response.getBitmap();
//                    holder.imagePhoto.setImageBitmap(image);
//                }
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                }
//            });
//            imageLoader.get(photo, ImageLoader.getImageListener(holder.imagePhoto,
//                    0, android.R.drawable
//                            .ic_dialog_alert));
//            holder.imagePhoto.setImageUrl(photo, imageLoader);
            Uri imageUri = Uri.parse(photo);
            holder.imagePhoto.setImageURI(imageUri);
        }
    }

    @Override
    public int getItemCount() {
        return unsplashList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //        ImageView imagePhoto;
//        NetworkImageView imagePhoto;
        SimpleDraweeView imagePhoto;
        MaterialRippleLayout imageRipple;
        Animation anim;

        public ViewHolder(View itemView) {
            super(itemView);

//            imagePhoto = (NetworkImageView) itemView.findViewById(R.id.ivPhoto);
            imagePhoto = (SimpleDraweeView) itemView.findViewById(R.id.ivPhoto);
            imageRipple = (MaterialRippleLayout) itemView.findViewById(R.id.imageRipple);
            anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);

            imagePhoto.setAnimation(anim);

            imageRipple.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(unsplashList.get(getAdapterPosition()), this);
        }
    }

    public interface FavouriteAdapterOnClickHandler{
        void onClick(UnsplashData unsplashData, RecyclerView.ViewHolder vh);
    }
}

