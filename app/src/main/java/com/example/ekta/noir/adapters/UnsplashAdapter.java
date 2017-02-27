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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ekta.noir.R;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.utils.VolleySingleton;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class UnsplashAdapter extends RecyclerView.Adapter<UnsplashAdapter.ViewHolder>{

    private Context context;
    private ArrayList<UnsplashData> unsplashList = new ArrayList<>();

    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    private UnsplashAdapterOnClickHandler clickHandler;

    public UnsplashAdapter(Context context, UnsplashAdapterOnClickHandler vh){
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();

//        imageLoader = VolleySingleton.getInstance(context).getImageLoader();

        this.context = context;
        this.clickHandler = vh;
    }

    public void setAppList(ArrayList<UnsplashData> unsplashList){
        this.unsplashList = unsplashList;
        notifyItemRangeChanged(0, unsplashList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.holder_unsplash, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final UnsplashData currentUnsplash = unsplashList.get(position);

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

//            Glide.with(context)
//                .load(photo)
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.imagePhoto);

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
        SimpleDraweeView imagePhoto;
        Animation anim;
        //        NetworkImageView imagePhoto;
        MaterialRippleLayout imageRipple;

        public ViewHolder(View itemView) {
            super(itemView);

            anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);

//            imagePhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
//            imagePhoto = (NetworkImageView) itemView.findViewById(R.id.ivPhoto);
            imagePhoto = (SimpleDraweeView) itemView.findViewById(R.id.ivPhoto);
            imageRipple = (MaterialRippleLayout) itemView.findViewById(R.id.imageRipple);

            imagePhoto.setAnimation(anim);

            imageRipple.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onClick(unsplashList.get(getAdapterPosition()), this);
        }
    }

    public interface UnsplashAdapterOnClickHandler{
        void onClick(UnsplashData unsplashData, RecyclerView.ViewHolder vh);
    }
}
