package com.pharmahome.pharmahome.UI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.util.PharmaIterator;

/**
 * Created by ciao on 11/02/16.
 */
public class UpdateFarmaciService extends Service {
    public final static int NOTIFICA_UPDATE_ID = 101;
    public final static String ACTION_START = "UpdateFarmaciService.START";
    public final static String ACTION_STOP = "UpdateFarmaciService.STOP";
    private static PharmaIterator farmaciIterator = null;
    private static NetworkWorker worker = null;
    public static final String SHARED_NAME = "UpdateFarmaciService.SHARED";
    public static final String KEY_IS_UPDATING = "UpdateFarmaciService.KEY_IS_UPDATING";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(intent == null){
            if(worker == null){
                Log.w("UpdateService: ", "Auto call no worker... flags: " + flags + "startID: " + startId);
                worker = new NetworkWorker(this);
                worker.start();
            }
        } else if(intent.getAction().equals(ACTION_STOP) || startId > 1){
            Log.w("UpdateService: ", "Stopping... flags: " + flags + "startID: " + startId);
            worker.termina();
            stopForeground(true);
            stopSelf();
        }else if(intent.getAction().equals(ACTION_START)) {
            Log.w("UpdateService: ", "STARTING... flags: " + flags + "startID: " + startId);
            worker = new NetworkWorker(this);
            worker.start();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isUpdating(){
        return farmaciIterator != null;
    }

    private class NetworkWorker extends Thread {
        Context context = null;
        NotificationCompat.Builder mBuilder = null;
        NotificationManager mNotifyManager = null;
        int iteratorLength = 0;
        boolean mustStop = false;

        public NetworkWorker(Context context){
            Intent intent = new Intent(context, UpdateFarmaciService.class);
            intent.setAction(ACTION_STOP);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
            this.context = context;
            mBuilder = new NotificationCompat.Builder(context);
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentTitle(getString(R.string.notifica_update_titolo_principale))
                    .setContentText(getString(R.string.notifica_update_aggiornamento))
                    .setSmallIcon(R.drawable.icon_pharmahome_notifiche)
                    .setProgress(10, 0, true)
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, context.getString(R.string.notifica_action_annulla), pendingIntent);
        }


        @Override
        public void run(){
            Log.w("Updating: ", "running... ");
            mNotifyManager.notify(NOTIFICA_UPDATE_ID, mBuilder.build());
            DBController db = new DBController(context);
            SharedPreferences shared = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
            shared.edit().putBoolean(KEY_IS_UPDATING, true).commit();
            if(farmaciIterator == null){
                farmaciIterator = new PharmaIterator();
            }
            iteratorLength = farmaciIterator.length();
            if (!Utility.isNetworkAvailable(context)) {
                return;
            }
            for (Farmaco f : farmaciIterator) {
                db.update(f);
                int i = farmaciIterator.getActualState() + 1;
                // System.out.println(f.getLinkFogliettoIllustrativo());
                if (i % 100 == 0){
                    mBuilder.setProgress(iteratorLength, i, false);
                    mNotifyManager.notify(NOTIFICA_UPDATE_ID, mBuilder.build());
                    Log.w("Updating: ", "updated " + i);
                }
                if(mustStop){
                    break;
                }
            }
            mBuilder.setContentText(getString(R.string.notifica_update_aggiornamento_fine))
                    .setProgress(0, 0, false).mActions.remove(0);
            mNotifyManager.notify(NOTIFICA_UPDATE_ID, mBuilder.build());
            stopForeground(true);
            stopSelf();
            shared.edit().putBoolean(KEY_IS_UPDATING, false).commit();
            farmaciIterator = null;
        }

        public void termina(){
            mustStop = true;
        }
    }
}
