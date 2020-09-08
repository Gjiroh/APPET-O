package com.peteleco.appet.Notifications;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.autofill.DateTransformation;
import android.text.style.TtsSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.peteleco.appet.Autenticacao_Login.LoginActivity;
import com.peteleco.appet.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationService extends FirebaseMessagingService {
    String TAG = "NotificationService";
    String CHANNEL_NAME = "channelTeste";

    public NotificationService(){
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


    //TODO: Configurar para mandar mais de uma notificação ao mesmo tempo
    public void createNotificationChannel( Context context, int importance, boolean showBadge, String name, String description, String channel_name){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (channel_name.isEmpty()){
                channel_name = CHANNEL_NAME;
            }

            String channelId = channel_name;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(showBadge);

            // 3
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createSampleDataNotification(Context context, String title, String message, boolean autoCancel, String channel_name){

        if (channel_name.isEmpty()){
            channel_name = CHANNEL_NAME;
        }

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_name);
        notificationBuilder.
                setSmallIcon(R.drawable.pet_logo).
                setContentTitle(title).
                setContentText(message).
                setPriority(NotificationCompat.PRIORITY_DEFAULT).
                setAutoCancel(autoCancel).
                setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1001, notificationBuilder.build());

    }

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

        String channel_name = project_name+task_name;
        createNotificationChannel(context, 4, true, channel_name,
                "Our task alerts notifications", channel_name);

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

}
