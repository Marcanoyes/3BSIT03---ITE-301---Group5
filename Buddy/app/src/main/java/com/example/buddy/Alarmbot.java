package com.example.buddy;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;


public class Alarmbot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        bootup boothelper = new bootup(context);
        NotificationCompat.Builder nb = boothelper.getChannelNotification();
        boothelper.getManager().notify(1, nb.build());


    }
}
