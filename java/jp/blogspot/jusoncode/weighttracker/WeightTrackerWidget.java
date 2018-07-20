package jp.blogspot.jusoncode.weighttracker;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WeightTrackerWidget extends AppWidgetProvider{

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        final int numberOfWidgets = appWidgetIds.length;

        ///Loop through all instances of app widgets for my app..
        for(int i = 0; i < numberOfWidgets; i++){

            int appWidgetId = appWidgetIds[i];

            ////Create intent to launch my app...
            Intent intent = new Intent(context,HomeScreen.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

            ///Get the layout for app Widget and attach on click listener..
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.home_screen_widget);
            remoteViews.setOnClickPendingIntent(R.id.widgetImageButton,pendingIntent);

            ///Tell the app widget manager to update this app widget....
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);

        }
    }
}
