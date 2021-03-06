package com.pharmahome.pharmahome.core.middleware;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ciao on 04/12/15.
 */
public class Confezione extends Farmaco {

    public static final String KEY_CONFEZIONE = "confezione";

    private Calendar scadenza;
    private boolean nuovaConfezione;
    private Fasce fascia;
    private long _confezioneId;

    public Confezione(Farmaco f) {
        super(f);
        scadenza = null;
        nuovaConfezione = false;
        fascia = null;
    }

    public Confezione(Farmaco f, String data, boolean nuovaConfezione, long id) throws ParseException {
        this(f, data, nuovaConfezione);
        _confezioneId = id;
    }

    public Confezione(Farmaco f, String data, boolean nuovaConfezione) throws ParseException {
        super(f);
        Calendar d = Calendar.getInstance();
        //System.out.println(data);
        d.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(data));
        fascia = calcolaFascia(d);
        scadenza = d;
        this.nuovaConfezione = nuovaConfezione;
    }

    public Confezione(Farmaco f, String data) throws ParseException {
        this(f, data, false);
    }

    public Confezione(Farmaco f, boolean nuovaConfezione) {
        super(f);
        this.nuovaConfezione = nuovaConfezione;
    }

    public static Fasce calcolaFascia(Calendar data) {
        return Fasce.getFasca(data);
    }

    public long getConfezioneId() {
        return _confezioneId;
    }

    public Calendar getScadenza() {
        return scadenza;
    }

    public void setScadenza(Calendar data) {
        fascia = calcolaFascia(data);
        scadenza = data;
    }

    public void setScadenza(String data) throws ParseException {
        setScadenza(new SimpleDateFormat("yyyy-MM-dd").parse(data));
    }

    public void setScadenza(Date data) {
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(data);
        setScadenza(tmp);
    }

    public boolean isNuovaConfezione() {
        return nuovaConfezione;
    }

    public void resetNuovaConfezione() {
        this.nuovaConfezione = false;
    }
    public void setNuovaConfezione() {
        this.nuovaConfezione = true;
    }

    public Fasce getFascia() {
        return fascia;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj instanceof Confezione) || equals((Confezione) obj);
    }

    public boolean equals(Confezione c) {
        return c == this || this._confezioneId == c.getConfezioneId();
    }
}
