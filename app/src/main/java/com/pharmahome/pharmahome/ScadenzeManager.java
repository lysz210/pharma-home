package com.pharmahome.pharmahome;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pharmahome.pharmahome.UI.NotificatoreScadenzeService;

import java.util.Calendar;

/**
 * Created by ciao on 27/01/16.
 * Gestore delle nofiche, effettua una verifica giornaliera per lanciare una notifica all'utente
 * Nel caso il sistema venga riavviato, viene resettato immediatamente la nuova notifica giornaliera
 */
public class ScadenzeManager extends BroadcastReceiver {
    public static final String NOTIFICA_INTENT_ACTION = "ScadenzeManager.NOTIFICA_INTENT_ACTION";
    public static final String BOOT_INTENT_ACTION = "android.intent.action.BOOT_COMPLETED";
    public static final int NOTIFICA_CODE = 444;
    public static int test = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(NOTIFICA_INTENT_ACTION.equals(intent.getAction()) || BOOT_INTENT_ACTION.equals(intent.getAction())) {
            Log.w("ScadenzaManager", "Avvia della scadenza manager intent: " + intent.getAction());
            // richiama la notifica da lanciare oggi
            lanciaNotificaOggi(context, intent);
            // inserimento del task per il richiamo di questa funzione domani
            setNotificaGiornaliera(context, intent);
        }
    }
    private void setNotificaNuovaConfezione(Context context, Intent intent){
        Log.w("myLog", "Avvio nuova activity intent: " + intent.getAction());
        Intent i = new Intent(context, InsertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return;
    }

    /**
     * settaggio del task da lanciare per il giorno seguente in modo che il ciclo delle notifiche
     * si ripeta quotidianamente
     * @param context   contesto da utilizzare
     * @param intent    intent ricevuto dal chiamante
     */
    private void setNotificaGiornaliera(Context context, Intent intent){
        Log.w("ScadenzeManager", "setNofiticaGiornaliera, intent: " + intent.getAction() + " " + intent.getIntExtra("prova", -1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MINUTE, 5);

        Intent t_intent = new Intent(context, ScadenzeManager.class);
        t_intent.putExtra("prova", test);
        t_intent.setAction(NOTIFICA_INTENT_ACTION);
        test++;
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, NOTIFICA_CODE, t_intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis() + 12000, 5000, alarmIntent);
    }

    /**
     * lancia la notifica sulle confezioni che sono in via di scadenza o quelle gia scadute
     * @param context   contesto da utilizzare
     */
    private void lanciaNotificaOggi(Context context, Intent intent){
        Log.w("lanciaNotificaOggi", "avvio notifica di oggi");
        Intent t_intent = new Intent(context, NotificatoreScadenzeService.class);
        context.startService(t_intent);
        return;
    }
}
