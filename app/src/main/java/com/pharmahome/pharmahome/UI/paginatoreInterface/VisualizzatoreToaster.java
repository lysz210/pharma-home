package com.pharmahome.pharmahome.UI.paginatoreInterface;

/**
 * Created by ciao on 08/02/16.
 */
public interface VisualizzatoreToaster {
    void visualizzaMessaggio(String msg, int dur);
    void visualizzaMessaggio(String msg);
    void visualizzaMessaggio(int msg, int dur);
    void visualizzaMessaggio(int msg);
}
