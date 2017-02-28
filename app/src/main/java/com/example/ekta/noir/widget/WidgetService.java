package com.example.ekta.noir.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.ekta.noir.R;
import com.example.ekta.noir.activities.BaseActivity;
import com.example.ekta.noir.model.UnsplashData;
import com.example.ekta.noir.model.UnsplashProviderMethods;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ekta on 26-02-2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    UnsplashData photo = new UnsplashData();

    /**
     * Equivalent to a CursorAdapter/ArrayAdapter with ListView.
     */
    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private final String TAG = StackRemoteViewsFactory.class.getSimpleName();
        private Context context;
        private Cursor cursor;
        private int appWidgetId;
        private ArrayList<UnsplashData> unsplashList;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        @Override
        public void onCreate() {
            Log.d("onCreateWidgetService","called");
            unsplashList = new ArrayList<>();
            getPhotos();

        }

        private void getPhotos() {

            ArrayList<UnsplashData> list = new ArrayList<>(UnsplashProviderMethods.getPhotoList(getApplicationContext()));
            //
            // unsplashList.clear();
            for (UnsplashData photo : list) {
                unsplashList.add(photo);
            }
            Log.d("count", String.valueOf(unsplashList.size()));
            Collections.reverse(unsplashList);
        }

        /**
         * Called when notifyDataSetChanged() is called.
         * Hence we can update the widget with new data!
         */
        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            //Meta-function for the AppWidgetManager
            Log.d("widget count", String.valueOf(unsplashList.size()));

            return unsplashList.size();
        }
        public AppWidgetTarget appWidgetTarget;


        @Override
        public RemoteViews getViewAt(int position) {


            RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(), R.layout.widget_item);
            appWidgetTarget = new AppWidgetTarget( context, remoteViews, R.id.iv_widget );

            Glide
                    .with( context.getApplicationContext() ) // safer!
                    .load(unsplashList.get(position).getUrlRegular())
                    .asBitmap()
                    .into( appWidgetTarget );

            Log.d("listsize", String.valueOf(unsplashList.size()));
            // if (this.cursor.moveToPosition(position)) {
//            Uri imageUri = Uri.parse(unsplashList.get(position).getUrlRegular());
//            remoteViews.setImageViewUri(R.id.iv_widget, imageUri);

//            Glide.with(context)
//                    .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
//                    .into(R.id.iv_widget);

//            Bundle extras = new Bundle();
//            extras.putString("symbol", imageUri.toString());
//
//
//            Intent fillInIntent = new Intent();
//            fillInIntent.putExtras(extras);
//            remoteViews.setOnClickFillInIntent(R.id.ll, fillInIntent);

            //   }

            pushWidgetUpdate(context, remoteViews);

            return remoteViews;
        }

        public  void pushWidgetUpdate(Context context, RemoteViews rv) {
            ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(myWidget, rv);
        }



        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {

            return unsplashList.size();
        }

        @Override
        public long getItemId(int position) {

            return position;

            //this.cursor.getInt(0);
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }
    }
}