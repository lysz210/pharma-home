package com.pharmahome.pharmahome.UI;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.util.PharmaIterator;

/**
 * Created by ciao on 11/02/16.
 */
public class UpdateFarmaciService extends IntentService {
    public final static int NOTIFICA_UPDATE_ID = 1;
    public final static String NOME = "UpdateFarmaciService.UPDATE";

    public UpdateFarmaciService(){
        super(NOME);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        new NetworkWorker(this).start();
    }

    private class NetworkWorker extends Thread {
        Context context = null;
        NotificationCompat.Builder mBuilder = null;
        NotificationManager mNotifyManager = null;
        PharmaIterator farmaciIterator = null;
        int iteratorLength = 0;

        public NetworkWorker(Context context){
            this.context = context;
            mBuilder = new NotificationCompat.Builder(context);
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentTitle(getString(R.string.notifica_update_titolo_principale))
                    .setContentText(getString(R.string.notifica_update_aggiornamento))
                    .setSmallIcon(R.drawable.icon_pharmahome_notifiche)
                    .setProgress(10, 0, true);
        }

        @Override
        public void run(){
            DBController db = new DBController(context);
            farmaciIterator = new PharmaIterator();
            iteratorLength = farmaciIterator.length();
            if (!Utility.isNetworkAvailable(context)) {
                return;
            }

            int i = 0;
            for (Farmaco f : farmaciIterator) {
                db.update(f);
                // System.out.println(f.getLinkFogliettoIllustrativo());
                if (++i % 100 == 0){
                    mBuilder.setProgress(iteratorLength, i, false);
                    mNotifyManager.notify(NOTIFICA_UPDATE_ID, mBuilder.build());
                }
            }

            mBuilder.setContentText(getString(R.string.notifica_update_aggiornamento_fine))
                    .setProgress(0, 0, false);
            mNotifyManager.notify(NOTIFICA_UPDATE_ID, mBuilder.build());
        }
    }
}
