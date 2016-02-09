package com.pharmahome.pharmahome.UI.paginatoreInterface;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.HashMap;

/**
 * Created by ciao on 08/02/16.
 */
public interface VisualizzatoreDialog {
    /**
     * costanti da utilizzari come key per la map dei listener
     */
    static final String KEY_BTN_NEUTRAL = "btn_neutral";    // costante per il bottone neutro
    static final String KEY_BTN_NEGATIVE = "btn_negative";  // bottone per la risposta negativa
    static final String KEY_BTN_POSITIVE = "btn_positive";  // bottone per la risposta affermativa
    static final String[] KEYS_BTN_AVAILBLE = {KEY_BTN_NEUTRAL, KEY_BTN_NEGATIVE, KEY_BTN_POSITIVE};
    /**
     * crea un dialogBuilder con un activity che potra' essere utilizzato per la
     * creazione di un dialog da visualizzare a chermo
     * @return
     */
    AlertDialog.Builder getAlertBuilder();

    /**
     * Crea un dialog semplice per la visualizzazione
     * @param titoloID
     * @param contenutoID
     */
    void visualizzaDialog(int titoloID, int contenutoID);

    /**
     * Creazione e visualizzazione di un alertDialog per la visualizzazione di una finestra modale con in abinata i listener passati
     * @param titoloID    titolo per la finestra modale
     * @param contenutoID contenuto per la finestra modlae
     * @param listeners bottoni per la finestra modale
     */
    void visualizzaDialog(int titoloID, int contenutoID, HashMap<String, OnClickListenerValue> listeners);

    /**
     * classe per costruire un bottone per gli alertDialog
     */
    class OnClickListenerValue {
        private int nomeID = 0;
        private DialogInterface.OnClickListener listener = null;

        public OnClickListenerValue(int nome){
            this(nome, null);
        }
        public OnClickListenerValue(int nome, DialogInterface.OnClickListener listener){
            this.nomeID = nome;
            this.listener = listener;
        }

        public int getNomeID(){
            return this.nomeID;
        }

        public DialogInterface.OnClickListener getListener(){
            if(this.listener == null){
                return new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                };
            }
            return this.listener;
        }
    }
}
