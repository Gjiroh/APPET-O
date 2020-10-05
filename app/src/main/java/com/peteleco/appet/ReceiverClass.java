package com.peteleco.appet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class ReceiverClass {

    Context context;
    public static String SCHEDULE_ID = "schedule_id";
    private static String TAG = "BroadcastTest";

    public ReceiverClass (Context context) {
        this.context = context;
    }

    public BroadcastReceiver createBroadcastForScheduling (IntentFilter filter) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Context.ALARM_SERVICE.equals(intent.getAction())){
                    long def = 0;
                    long schduleId;
                    try {
                        schduleId = intent.getIntExtra(SCHEDULE_ID, 0);
                    } catch (Exception e){
                        schduleId = def;
                    }

                    if (schduleId != def){
                        Log.i(TAG, "Agendamento");
                        NotificationService notificationService = new NotificationService();
                        notificationService.scheduleNotification(context, 2, "nomeProjeto", "Tarefa tal o prazo esta chegando");
                    }
                }
            }
        };
        context.registerReceiver(receiver, filter);
        return receiver;
    }

    public void unregisterBroadcast(BroadcastReceiver receiver) {
        context.unregisterReceiver(receiver);
    }
}
