package com.peclab.nurgissa.thunderlist;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANEL_ID");
        Intent dIntent = new Intent(context, MainActivity.class);
        String extra = (String) intent.getExtras().get("EXTRA_TITLE");
        System.out.println(extra);
//        dIntent.putExtra("EXTRA_REMINDER_TITLE", intent.getExtras().getString("EXTRA_TITLE"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, dIntent, 0);

        builder.setContentTitle("Thunderlist");
        builder.setContentText("You should complete scheduled task.");
        builder.setSmallIcon(R.drawable.ic_access_time_white_24dp);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CHANEL_ID",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());
    }
}
