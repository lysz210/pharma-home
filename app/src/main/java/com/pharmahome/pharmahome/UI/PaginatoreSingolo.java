package com.pharmahome.pharmahome.UI;


import android.support.v7.app.AlertDialog;

/**
 * Created by ciao on 28/01/16.
 */
public interface PaginatoreSingolo {
    void setActualPage(Pagina  pagina);
    void visualizzaMessaggio(String msg, int dur);
    void visualizzaMessaggio(String msg);
    AlertDialog.Builder getAlertBuilder();
}
