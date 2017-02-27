package com.example.ekta.noir.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.ekta.noir.R;
import com.example.ekta.noir.activities.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {
    public static final String ACTION = "com.example.ekta.noir.ACTION";
    public static final String EXTRA_ITEM = "com.example.ekta.noir.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent detail_intent = new Intent(context, MainActivity.class);
            context.startActivity(detail_intent);
        }
        super.onReceive(context, intent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the app widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.lv_widget_layout, intent);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            //Update the widget
            views.setRemoteAdapter(R.id.lv_widget_layout, new Intent(context, WidgetService.class));

            Intent toastIntent = new Intent(context, WidgetProvider.class);
            toastIntent.setAction(WidgetProvider.ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.lv_widget_layout, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }
}