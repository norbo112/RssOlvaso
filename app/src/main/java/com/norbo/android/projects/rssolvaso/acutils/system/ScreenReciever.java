package com.norbo.android.projects.rssolvaso.acutils.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Képernyő bekapcsolását fogadó objektum
 * Ha a képernyő bekapcsol és az rssactivity aktív akkor betölti a híreket
 */
public class ScreenReciever extends BroadcastReceiver {
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
        } else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            wasScreenOn = true;
        }
    }
}
