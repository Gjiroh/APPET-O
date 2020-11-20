package com.peteleco.appet;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BcReceiver extends BroadcastReceiver {

    public static String SCHEDULE_ID = "schedule_id";
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_GROUP = "notificationGroup";
    public static String TAG = "BROADCAST";
    private final static int SUMMARY_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        //String action = intent.getType();
        //Log.i(TAG, "Action: "+action);
        Notification notification;

        try {
            notification = intent.getParcelableExtra(NOTIFICATION);
        } catch (Exception e){
            notification = null;
        }
        if (notification != null){
            Log.i(TAG, "Notificação");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notificationGroup = intent.getParcelableExtra(NOTIFICATION_GROUP);
            int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
            notificationManager.notify(notificationId, notification);
            notificationManager.notify(SUMMARY_ID, notificationGroup);
        }
        /*long def = 0;
        long schduleId;

        try {
            schduleId = intent.getIntExtra(SCHEDULE_ID, 0);
        } catch (Exception e){
            schduleId = def;
        }

        if (schduleId != def){
            Log.i(TAG, "Agendamento");
            NotificationService notificationService = new NotificationService();
            notificationService.scheduleNotification(context, 20, "nomeProjeto", "Tarefa tal o prazo esta chegando");
        }*/
    }
}
