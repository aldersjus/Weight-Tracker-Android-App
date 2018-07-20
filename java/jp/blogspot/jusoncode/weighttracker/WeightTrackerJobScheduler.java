package jp.blogspot.jusoncode.weighttracker;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Collections;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.webkit.WebViewDatabase.getInstance;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.kilograms;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.measurement;
import static jp.blogspot.jusoncode.weighttracker.R.attr.weight;


public class WeightTrackerJobScheduler extends JobService{

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler,1,jobParameters));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mJobHandler.removeMessages(1);
        return false;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        private RemoteViews remoteViews;
        @Override
        public boolean handleMessage(Message message) {

            DatabaseAccess db = new DatabaseAccess(getBaseContext());
            db.getWeights();


            if(!db.databaseList.isEmpty()) {
                Collections.sort(db.databaseList);
                Weight weight = db.databaseList.get(0);
                remoteViews = new RemoteViews(getPackageName(),R.layout.home_screen_widget);
                remoteViews.setTextViewText(R.id.widgetTextToChange,"Now:".concat(weight.weight).concat(weight.measurement));
            }else{
                remoteViews = new RemoteViews(getPackageName(),R.layout.home_screen_widget);
                remoteViews.setTextViewText(R.id.widgetTextToChange,"Weigh Now");
            }

            ///Tell the app widget manager to update this app widget....
            ComponentName myWidget = new ComponentName(getApplicationContext(),WeightTrackerWidget.class);
            int[] widgets = AppWidgetManager.getInstance(getApplicationContext()).getAppWidgetIds(myWidget);
            for(int widget : widgets) {
                AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(widget, remoteViews);
            }
            jobFinished((JobParameters)message.obj,false);

            return true;
        }
    });
}
