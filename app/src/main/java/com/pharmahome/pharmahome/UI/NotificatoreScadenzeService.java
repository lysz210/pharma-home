package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;

/**
 * Created by ciao on 06/02/16.
 */
public class NotificatoreScadenzeService extends IntentService {
    public static final String NOME = "Servizioni notifiche scadenze";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public NotificatoreScadenzeService() {
        super("Ciao");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.w("Servizio notivica", "service " + NOME + ", intent: " + intent.getAction());
        Context context = getApplicationContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.icon_pharmahome_notifiche)
                .setContentTitle("Pharma home notifica nuova app")
                .setContentText("NOTIFICA intent: " + intent.getIntExtra("prova", -1));

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 444;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}