package com.peteleco.appet;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.peteleco.appet.Autenticacao_Login.LoginActivity;

import java.util.Calendar;
import java.util.Date;

public class NotificationService extends FirebaseMessagingService {

    private final static String TAG = "NotificationService";
    private final static String CHANNEL_ID = "APPET-O";
    private final static String GROUP_KEY_WORK_EMAIL = "group_message_APPET-O";
    private final static int SUMMARY_ID = 0;
    public final static String CONTENT = "NotificationContent";
    public final static String TITLE = "NotificationTitle";

    public NotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "APPET-O";
            String description = "Canal do PET";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationCompat.Builder setNotification (Context context, String title, String content, Class<?> launcherActivity) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, launcherActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setGroup(GROUP_KEY_WORK_EMAIL);

        return builder;
    }

    public void showNotification(Context context, NotificationCompat.Builder builder) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

        Notification summaryNotification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("APPET")
                        //set content text to support devices running API level < 24
                        .setContentText("Messages")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setBigContentTitle("new messages")
                                .setSummaryText("messages"))
                        //specify which group this notification belongs to
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        notificationManager.notify(SUMMARY_ID, summaryNotification);
    }

    public void scheduleNotification(Context context, long delay_sec, String nomeProjeto, String nomeTarefa) {
        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        long delay_milis = delay_sec*1000;

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("PET - "+nomeProjeto)
                .setContentText("A "+nomeTarefa+" esta quase no prazo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setGroup(GROUP_KEY_WORK_EMAIL);

        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        //otificationManager.notify(notificationId, builder.build());

        Notification summaryNotification =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("APPET")
                        //set content text to support devices running API level < 24
                        .setContentText("Messages")
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        //build summary info into InboxStyle template
                        .setStyle(new NotificationCompat.InboxStyle()
                                .setBigContentTitle("Novas mensagens")
                                .setSummaryText("mensagens"))
                        //specify which group this notification belongs to
                        .setGroup(GROUP_KEY_WORK_EMAIL)
                        //set this notification as the summary for the group
                        .setGroupSummary(true)
                        .build();

        // TODO: Fazer esquema com que a ID da notificação seja variável para que uma notificação não sobre-escreva a outra
        Intent notificationIntent = new Intent(context, BcReceiver.class);
        notificationIntent.putExtra(BcReceiver.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(BcReceiver.NOTIFICATION, builder.build());
        notificationIntent.putExtra(BcReceiver.NOTIFICATION_GROUP, summaryNotification);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Date date = new Date();
        long futureInMillis = date.getTime() + delay_milis;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.i(TAG, ".setExact");
                alarmManager.setExact(AlarmManager.RTC, futureInMillis, pendingIntent2);
            } else {
                Log.i(TAG, ".set");
                alarmManager.set(AlarmManager.RTC, futureInMillis, pendingIntent2);
            }
        } else {
            Log.w(TAG, "Alarm manager is null");
        }
    }
    /*
    public void sendTaskAlertMessage (Context context, Date date, String project_name, String task_name) {

        Date currentTime = new Date();
        Date notifyDate = new Date();

        int month = Calendar.getInstance().getTime().getMonth() + 1;
        int year = Calendar.getInstance().getTime().getYear() + 1900;
        int day = Calendar.getInstance().getTime().getDay() + 6;

        String aux = day+"/"+month+"/"+year;

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            currentTime = sdf.parse(aux);


            notifyDate.setTime(date.getTime()-1000*24*60*60);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        if (notifyDate.equals(currentTime)) {
            Log.i(TAG, "Tarefa é pra amanha");

            String message = "LEMBRETE: A tarefa "+task_name+" esta a um dia do prazo!";
            createSampleDataNotification(context, project_name, message, true, channel_name);

        }else if (date.before(currentTime)) {
            Log.i(TAG, "Tarefa é pra ontem\n"+notifyDate+"\n"+date);
            String message = "LEMBRETE: A tarefa "+task_name+" esta atrasada!!";

            createSampleDataNotification(context, project_name, message, true, channel_name);
        }
    }
    */
}
