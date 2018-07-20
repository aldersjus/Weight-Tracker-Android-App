package jp.blogspot.jusoncode.weighttracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class WeightTrackerAlarmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final int notificationId = 1;
        Intent intentTwo = new Intent(context,HomeScreen.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intentTwo,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context).setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.weighToday)).setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,builder.build());

    }
}
