package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pharmahome.pharmahome.BroadcastManager;
import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 06/02/16.
 */
public class NotificatoreScadenzeService extends IntentService {
    public static final String NOME = "Servizio notifiche scadenze";
    private int nConfezioniInScadenza = 0;
    private int nConfezioniScaduti = 0;
    private int totaleConfezioniDaNotificare = 0;
    private ListaConfezioni confezioniInScadenza = new ListaConfezioni();
    private ListaConfezioni confezioniScaduti = new ListaConfezioni();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public NotificatoreScadenzeService() {
        super(NOME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Log.w("Servizio notivica", "service " + NOME + ", intent: " + intent.getAction());
        if (!retrieveConfezioniDaNotificare()) {
//            Log.w("Servizio notivica", "Non ci sono confezioni da notificare");
            return;
        }
        Context context = getApplicationContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.icon_pharmahome_notifiche)
                .setContentTitle(getString(R.string.notifica_titolo_principale))
                .setContentText(getString(R.string.notifica_contenuto_principale))
                .setAutoCancel(true)
                .setCategory(BroadcastManager.NOTIFICA_CATEGORY);

        // test per notifica piu' grande
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notifica_titolo_principale));
        String sommario = getString(R.string.notifica_sommario_header);
        if (nConfezioniInScadenza > 0) {
            sommario += String.format(getString(R.string.notifica_sommario_in_scadenza_ptn), nConfezioniInScadenza);
            inboxStyle.addLine(getString(R.string.notifica_sottotilo_in_scadenza));
            for (Confezione c : confezioniInScadenza) {
                inboxStyle.addLine("  - " + c.getNome());
            }
        }
        if (nConfezioniScaduti > 0) {
            sommario += String.format(getString(R.string.notifica_sommario_scaduto_ptn), nConfezioniScaduti);
            inboxStyle.addLine(getString(R.string.notifica_sottotilo_scaduti));
            for (Confezione c : confezioniScaduti) {
                inboxStyle.addLine("  - " + c.getNome());
            }
        }
        inboxStyle.setSummaryText(sommario);
        mBuilder.setStyle(inboxStyle);
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(BroadcastManager.NOTIFICA_CODE, mBuilder.build());
    }

    /**
     * recura tutte le confezioni da notificare, restituisce true nel caso ci sia almeno una confezione
     *
     * @return
     */
    private boolean retrieveConfezioniDaNotificare() {
        DBController db = new DBController(getApplicationContext());
        try {
            ListaConfezioni tutte = db.getAllConfezioni();
            for (Confezione c : tutte) {
                switch (c.getFascia()) {
                    case SCADUTO:
                        confezioniScaduti.add(c);
                        nConfezioniScaduti += 1;
                        break;
                    case IN_SCADENZA:
                        confezioniInScadenza.add(c);
                        nConfezioniInScadenza += 1;
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        totaleConfezioniDaNotificare = nConfezioniInScadenza + nConfezioniScaduti;
        return totaleConfezioniDaNotificare > 0;
    }
}