package com.pharmahome.pharmahome.core.middleware;

import com.pharmahome.pharmahome.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ciao on 05/12/15.
 */
public enum Fasce {
    BUONO(2),
    IN_SCADENZA(1),
    SCADUTO(0);

    public static int RANGE = 15;
    private int colore;
    private String nome;
    private int identificatore;

    Fasce(int v) {
        switch (v) {
            case 0:
                colore = 0xff0000;
                nome = "scaduto";
                identificatore = R.drawable.led_scaduto;
                break;
            case 1:
                colore = 0x00ffff;
                nome = "in_scadenza";
                identificatore = R.drawable.led_in_scadenza;
                break;
            case 2:
                colore = 0x00ff00;
                nome = "buono";
                identificatore = R.drawable.led_buono;
        }
    }

    public static Fasce getFasca(Calendar data) {
        GregorianCalendar oggi = new GregorianCalendar();
        if (data.before(oggi)) {
            return SCADUTO;
        }
        oggi.add(oggi.DATE, RANGE);
        if (data.before(oggi)) {
            return IN_SCADENZA;
        }
        return BUONO;
    }

    public int getColore() {
        return colore;
    }

    public String getNome() {
        return nome;
    }

    public int getIdentificatore() {
        return identificatore;
    }
}
