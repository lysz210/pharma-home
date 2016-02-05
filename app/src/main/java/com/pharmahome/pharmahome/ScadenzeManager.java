package com.pharmahome.pharmahome;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by ciao on 27/01/16.
 */
public class ScadenzeManager extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            setNotificheAlRiavvio(context, intent);
        } else {
            // da verificare probabilmente non necessario
            // preferibile effettuare una schedulazione periodica
            // con nuovo settaggio al riavvio
            setNotificaNuovaConfezione(context, intent);
        }

    }

    private void setNotificheAlRiavvio(Context context, Intent intent){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.icon_pharmahome_notifiche)
                .setContentTitle("Pharma home notifica")
                .setContentText("Hello World!!");

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        return;
    }

    private void setNotificaNuovaConfezione(Context context, Intent intent){
        Log.w("myLog", "Avvio nuova activity");
        Intent i = new Intent(context, InsertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return;
    }
}
