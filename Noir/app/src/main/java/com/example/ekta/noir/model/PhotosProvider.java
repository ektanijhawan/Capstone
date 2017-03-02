package com.example.ekta.noir.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import com.example.ekta.noir.utils.UnsplashDbHelper;

public class PhotosProvider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "com.example.ekta.noir";
    public static final String URL = "content://" + CONTENT_AUTHORITY + "/photos";
    public static final Uri BASE_CONTENT_URI = Uri.parse(URL);

    private UnsplashDbHelper mDatabaseHelper;
    private static final int ALL_PHOTOS = 1;
    private static final int SINGLE_PHOTO = 2;
    private static HashMap<String, String> PROJECTION_MAP;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new UnsplashDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UnsplashDbHelper.UNSPLASH_TABLE);
        queryBuilder.setProjectionMap(PROJECTION_MAP);

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long id = db.insert(UnsplashDbHelper.UNSPLASH_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int deleteCount = db.delete(UnsplashDbHelper.UNSPLASH_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int updateCount = db.update(UnsplashDbHelper.UNSPLASH_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
