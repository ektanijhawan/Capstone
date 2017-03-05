package com.example.ekta.noir.model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;


import static com.example.ekta.noir.model.ResponseKeys.*;

public class UnsplashProviderMethods {

    public static boolean isPhotoInDatabase(Activity activity, String id){

        ArrayList<UnsplashData> list = new ArrayList<>(UnsplashProviderMethods.getPhotoList(activity.getApplicationContext()));
        for(UnsplashData listItem : list){
            if(listItem.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<UnsplashData> getPhotoList(Context context){

        ArrayList<UnsplashData> photoList = new ArrayList<>();
        Uri contentUri = PhotosProvider.BASE_CONTENT_URI;
        Cursor c = context.getContentResolver().query(contentUri, null, null, null, null);

        if(c != null) {
            if (c.moveToFirst()) {
                do {

                    UnsplashData photo = new UnsplashData(
                            c.getString(c.getColumnIndex(IMAGE_ID)),
                            c.getInt(c.getColumnIndex(IMAGE_WIDTH)),
                            c.getInt(c.getColumnIndex(IMAGE_HEIGHT)),
                            c.getString(c.getColumnIndex(IMAGE_CREATED_AT)),
                            c.getString(c.getColumnIndex(IMAGE_COLOR)),
                            c.getString(c.getColumnIndex(IMAGE_URLS_FULL)),
                            c.getString(c.getColumnIndex(IMAGE_URLS_REGULAR)),
                            c.getString(c.getColumnIndex(IMAGE_USER_NAME))
                    );
                    photoList.add(photo);
                } while (c.moveToNext());
            }
            c.close();
        }
        return photoList;
    }

    public static UnsplashData getPhotoFromDatabase(Activity activity, String id){

        UnsplashData unsplashData = null;
        Uri contentUri = PhotosProvider.BASE_CONTENT_URI;
        Cursor c = activity.getContentResolver().query(contentUri, null, null, null, null);

        if(c.moveToFirst()){
            do{
                if(id.equals(c.getString(c.getColumnIndex(IMAGE_ID)))){
                    unsplashData = new UnsplashData(
                            c.getString(c.getColumnIndex(IMAGE_ID)),
                            c.getInt(c.getColumnIndex(IMAGE_WIDTH)),
                            c.getInt(c.getColumnIndex(IMAGE_HEIGHT)),
                            c.getString(c.getColumnIndex(IMAGE_CREATED_AT)),
                            c.getString(c.getColumnIndex(IMAGE_COLOR)),
                            c.getString(c.getColumnIndex(IMAGE_URLS_FULL)),
                            c.getString(c.getColumnIndex(IMAGE_URLS_REGULAR)),
                            c.getString(c.getColumnIndex(IMAGE_USER_NAME))
                    );
                    break;
                }
            }while(c.moveToNext());
        }
        c.close();
        return unsplashData;
    }
}
