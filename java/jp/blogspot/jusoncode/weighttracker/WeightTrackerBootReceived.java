package jp.blogspot.jusoncode.weighttracker;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/////IF THE DEVICE IS REBOOTED(TURNED ON/OFF) USE THIS TO RESET ALARM......

public class WeightTrackerBootReceived  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            HomeScreen.setAlarm(context);
        }
    }
}
