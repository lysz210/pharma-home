package com.pharmahome.core.middleware;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ciao on 05/12/15.
 */
public enum Fasce {
    buono (2),
    inScadenza (1),
    scaduto (0);

    public static int RANGE = 15;
    private int colore;
    private String nome;

    Fasce(int v){
        switch (v) {
            case 0:
                colore = 0xff0000;
                nome = "scaduto";
                break;
            case 1:
                colore = 0x00ffff;
                nome = "in_scadenza";
                break;
            case 2:
                colore = 0x00ff00;
                nome = "buono";
        }
    }

    public static Fasce getFasca(Calendar data){
        GregorianCalendar oggi = new GregorianCalendar();
        if(data.before(oggi)){
            return scaduto;
        }
        oggi.add(oggi.DATE, RANGE);
        if(data.before(oggi)){
            return inScadenza;
        }
        return buono;
    }

    public int getColore(){
        return colore;
    }

    public String getNome(){
        return nome;
    }
}
