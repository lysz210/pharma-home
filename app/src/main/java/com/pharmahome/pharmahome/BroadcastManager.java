package com.pharmahome.pharmahome;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.pharmahome.pharmahome.UI.NotificatoreScadenzeService;
import com.pharmahome.pharmahome.UI.UpdateFarmaciService;

import java.util.Calendar;

/**
 * Created by ciao on 27/01/16.
 * Gestore delle nofiche, effettua una verifica giornaliera per lanciare una notifica all'utente
 * Nel caso il sistema venga riavviato, viene resettato immediatamente la nuova notifica giornaliera
 */
public class BroadcastManager extends BroadcastReceiver {
    public static final String NOTIFICA_INTENT_ACTION = "BroadcastManager.NOTIFICA_INTENT_ACTION";
    public static final String NOTIFICA_CATEGORY = "ScadenzaManager.NOTIFICA_CATEGORY";
    public static final String BOOT_INTENT_ACTION = "android.intent.action.BOOT_COMPLETED";
    public static final String INSTALL_INTENT_ACTION = "android.intent.action.PACKAGE_INSTALL";
    public static final String FST_LAUNCH_INTENT_ACTION = "android.intent.action.PACKAGE_FIRST_LAUNCH";
    public static final String SHARED_KEY_DELAY = "ScadenzaManager.INTERVALLO";
    public static final String SHARED_KEY_HOUR = "ScadenzaManager.HOUR";
    public static final String SHARED_KEY_MINUTE = "ScadenzaManager.MINUTE";
    public static final long DEFAULT_DELAY = AlarmManager.INTERVAL_DAY;
    public static final int DEFAULT_HOUR = 8;
    public static final int DEFAULT_MINUTE = 00;
    public static final int DEFFAULT_SECONDS = 00;
    public static final String SHARED_NAME = "ScadenzaManager.SHARED";
    public static final int NOTIFICA_CODE = 444;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.w("ScadenzaManager", "Avvia della scadenza manager intent: " + intent.getAction());
        switch (intent.getAction()) {
            case "DEBUG_TEST":
            case INSTALL_INTENT_ACTION:
            case FST_LAUNCH_INTENT_ACTION:
            case BOOT_INTENT_ACTION:
                // inserimento del task per il richiamo di questa funzione domani
                setNotificaGiornaliera(context, intent);
            case NOTIFICA_INTENT_ACTION:
                // richiama la notifica da lanciare oggi
                lanciaNotificaOggi(context, intent);
                break;
            case UpdateFarmaciService.ACTION_START:
                lanciaUpdateFarmaci(context, intent);
                break;
        }
    }

    /**
     * settaggio del task da lanciare per il giorno seguente in modo che il ciclo delle notifiche
     * si ripeta quotidianamente
     *
     * @param context contesto da utilizzare
     * @param intent  intent ricevuto dal chiamante
     */
    private void setNotificaGiornaliera(Context context, Intent intent) {
//        Log.w("BroadcastManager", "setNofiticaGiornaliera, intent: " + intent.getAction() + " " + intent.getIntExtra("prova", -1));
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences shared = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        long delay = shared.getLong(SHARED_KEY_DELAY, DEFAULT_DELAY);
        int hour = shared.getInt(SHARED_KEY_HOUR, DEFAULT_HOUR);
        int minute = shared.getInt(SHARED_KEY_MINUTE, DEFAULT_MINUTE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, DEFFAULT_SECONDS);
        Intent t_intent = new Intent(context, BroadcastManager.class);
        t_intent.setAction(NOTIFICA_INTENT_ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, NOTIFICA_CODE, t_intent, 0);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), delay, alarmIntent);
    }

    /**
     * lancia la notifica sulle confezioni che sono in via di scadenza o quelle gia scadute
     *
     * @param context contesto da utilizzare
     */
    private void lanciaNotificaOggi(Context context, Intent intent) {
//        Log.w("lanciaNotificaOggi", "avvio notifica di oggi");
        Intent t_intent = new Intent(context, NotificatoreScadenzeService.class);
        context.startService(t_intent);
        return;
    }

    private void lanciaUpdateFarmaci(Context context, Intent intent) {
//        Log.w("lanciaUpdateFarmaci", "avvio fase di aggiornamento dati farmaci");
        Intent t_intent = new Intent(context, UpdateFarmaciService.class);
        context.startService(t_intent);
        return;
    }
}
