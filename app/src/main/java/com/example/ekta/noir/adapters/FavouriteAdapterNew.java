package com.example.ekta.noir.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.toolbox.ImageLoader;
import com.balysv.materialripple.MaterialRippleLayout;
import com.example.ekta.noir.R;
import com.example.ekta.noir.model.ResponseKeys;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.utils.VolleySingleton;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Ekta on 05-03-2017.
 */

public class FavouriteAdapterNew extends RecyclerView.Adapter<FavouriteAdapterNew.ViewHolder>{
        private Context context;
        private ArrayList<UnsplashData> unsplashList = new ArrayList<>();
    Cursor dataCursor;

        private LayoutInflater layoutInflater;
        private VolleySingleton volleySingleton;
        private ImageLoader imageLoader;

        private FavouriteAdapterNew.FavouriteAdapterNewOnClickHandler clickHandler;

        public FavouriteAdapterNew(Context context,Cursor cursor, FavouriteAdapterNew.FavouriteAdapterNewOnClickHandler vh) {
            layoutInflater = LayoutInflater.from(context);
            volleySingleton = VolleySingleton.getsInstance();
            imageLoader = volleySingleton.getImageLoader();
            dataCursor = cursor;

            this.context = context;
            this.clickHandler = vh;
        }
    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
        public void setAppList(ArrayList<UnsplashData> unsplashList) {
            this.unsplashList = unsplashList;
        }

        @Override
        public FavouriteAdapterNew.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.adapter_favouritr_new, parent, false);
            FavouriteAdapterNew.ViewHolder viewHolder = new FavouriteAdapterNew.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final FavouriteAdapterNew.ViewHolder holder, int position) {
//            UnsplashData currentUnsplash = unsplashList.get(position);
            dataCursor.moveToPosition(position);

            String photo = dataCursor.getString(dataCursor.getColumnIndex(ResponseKeys.IMAGE_ID));
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
//            return unsplashList.size();
         return    dataCursor.getCount();
        }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //        ImageView imagePhoto;
//        NetworkImageView imagePhoto;
    SimpleDraweeView imagePhoto;
    MaterialRippleLayout imageRipple;
    Animation anim;

    public ViewHolder(View itemView) {
        super(itemView);

//            imagePhoto = (NetworkImageView) itemView.findViewById(R.id.ivPhoto);
        imagePhoto = (SimpleDraweeView) itemView.findViewById(R.id.ivPhotoNew);
       imageRipple = (MaterialRippleLayout) itemView.findViewById(R.id.imageRippleNew);
        anim = AnimationUtils.loadAnimation(context, R.anim.fade_in);

        imagePhoto.setAnimation(anim);

       imageRipple.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickHandler.onClick(unsplashList.get(getAdapterPosition()), this);
    }
}

public interface FavouriteAdapterNewOnClickHandler {
    void onClick(UnsplashData unsplashData, RecyclerView.ViewHolder vh);
}
}

