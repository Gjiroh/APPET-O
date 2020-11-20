package com.peteleco.appet;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class BcReceiver extends BroadcastReceiver {

    public static String SCHEDULE_ID = "schedule_id";
    public static String NOTIFICATION_ID = "notification_id"; // Isso é uma referencia p/ lista
    public static String NOTIFICATION = "notification"; // Isso é uma referencia p/ lista
    public static String NOTIFICATION_GROUP = "notificationGroup";
    public static String TAG = "BROADCAST";
    public static String POSITION = "position";
    private final static int SUMMARY_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        //String action = intent.getType();
        //Log.i(TAG, "Action: "+action);
        Notification notification;
        ArrayList<String> notifications;
        ArrayList<String> notifications_ids;

        try {
            notifications = intent.getStringArrayListExtra(NOTIFICATION);
            notifications_ids = intent.getStringArrayListExtra(NOTIFICATION_ID);

        } catch (Exception e){
            notifications = null;
            notifications_ids = null;
        }
        if (notifications!=null && notifications_ids!=null){
            if(notifications.size() == notifications_ids.size()){
                int i = intent.getIntExtra(POSITION, -1);
                if(i > -1){
                    notification = intent.getParcelableExtra(notifications.get(i));
                    int notificationId = intent.getIntExtra(notifications_ids.get(i), 0);

                    if(notificationId!=0) {
                        Log.i(TAG, "Notificação");
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        Notification notificationGroup = intent.getParcelableExtra(NOTIFICATION_GROUP);
                        try {
                            notificationManager.notify(notificationId, notification);
                            notificationManager.notify(SUMMARY_ID, notificationGroup);
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao lançar notificação");
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }else{
                    Log.e(TAG, "Erro ao recurepar posição da notificação");
                }
            }
        }
    }
}
