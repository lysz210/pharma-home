package com.pharmahome.pharmahome.core.middleware;

import com.pharmahome.pharmahome.core.db.FarmacoContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ciao on 04/12/15.
 */
public class Farmaco {

    public static final int MIN_LEN_AIC = 9;
    public static final String[] KEYS = {
            FarmacoContract.Farmaco.COLUMN_NAME_AIC,
            FarmacoContract.Farmaco.COLUMN_NAME_NOME,
            FarmacoContract.Farmaco.COLUMN_NAME_PRINCIPIO_ATTIVO,
            FarmacoContract.Farmaco.COLUMN_NAME_DITTA,
            FarmacoContract.Farmaco.COLUMN_NAME_CODICE_GRUPPO_EQUIVALENZA,
            FarmacoContract.Farmaco.COLUMN_NAME_DESCRIZIONE_GRUPPO_EQUIVALENZA,
            FarmacoContract.Farmaco.COLUMN_NAME_LINK_FOGLIETTO_ILLUSTRATIVO,
            FarmacoContract.Farmaco.COLUMN_NAME_ECIPIENTI,
            FarmacoContract.Farmaco.COLUMN_NAME_CATEGORIA,
            FarmacoContract.Farmaco.COLUMN_NAME_PRECAUZIONI,
            FarmacoContract.Farmaco.COLUMN_NAME_EFFETTI_INDESIDERATI,
            FarmacoContract.Farmaco.COLUMN_NAME_AVVERTENZE_SPECIALI,
            FarmacoContract.Farmaco.COLUMN_NAME_INTERAZIONI,
            FarmacoContract.Farmaco.COLUMN_NAME_POSOLOGIA,
            FarmacoContract.Farmaco.COLUMN_NAME_SOMMINISTRAZIONE,
            FarmacoContract.Farmaco.COLUMN_NAME_SOVRADOSAGGIO,
            FarmacoContract.Farmaco.COLUMN_NAME_INDICAZIONI_TERAPEUTICHE,
            FarmacoContract.Farmaco.COLUMN_NAME_COMPORTAMENTOEMERGENZA
    };
    private long _farmacoId = -1;
    private HashMap<String, String> farmaco = new HashMap<>();

    public Farmaco(Farmaco f) {
        farmaco = f.farmaco;
        _farmacoId = f.getFarmacoId();
    }

    public Farmaco(JSONObject f) throws JSONException {
        String tmp;
        for (int i = 0; i < KEYS.length; i += 1) {
            tmp = KEYS[i];
            if (f.has(tmp)) {
                farmaco.put(tmp, f.getString(tmp));
            }
        }
        if (f.has(FarmacoContract.Farmaco._ID)) {
            _farmacoId = f.getInt(FarmacoContract.Farmaco._ID);
        }
        return;
    }

    public ArrayList<Info> getAsInfoList() {
        ArrayList<Info> tmp = new ArrayList<>();
        Info tInfo = null;
        for (String k : KEYS) {
            if (k == "linkFogliettoIllustrativo")
                continue;
            tInfo = new Info(k, get(k));
            if (!tInfo.isEmpty())
                tmp.add(tInfo);
        }
        return tmp;
    }

    public long getFarmacoId() {
        return _farmacoId;
    }

    public String get(String key) {
        if (key == "sintomi") {
            key = "indicazioniTerapeutiche";
        }
        return farmaco.containsKey(key) ? farmaco.get(key) : "";
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return farmaco.containsKey("nome") ? farmaco.get("nome") : "";
    }

    /**
     * @return the principioAttivo
     */
    public String getPrincipioAttivo() {
        return farmaco.containsKey("principioAttivo") ? farmaco.get("principioAttivo") : "";
    }

    /**
     * @return the aic
     */
    public String getAic() {
        return farmaco.containsKey("aic") ? farmaco.get("aic") : "";
    }

    /**
     * @return the linkFogliettoIllustrativo
     */
    public String getLinkFogliettoIllustrativo() {
        return farmaco.containsKey("linkFogliettoIllustrativo") ? farmaco.get("linkFogliettoIllustrativo") : "";
    }

    /**
     * @return the ecipienti
     */
    public String getEcipienti() {
        return farmaco.containsKey("ecipienti") ? farmaco.get("ecipienti") : "";
    }

    /**
     * @return the categoria
     */
    public String getCategoria() {
        return farmaco.containsKey("categoria") ? farmaco.get("categoria") : "";
    }

    /**
     * @return the precauzioni
     */
    public String getPrecauzioni() {
        return farmaco.containsKey("precauzioni") ? farmaco.get("precauzioni") : "";
    }

    /**
     * @return the effettiIndesiderati
     */
    public String getEffettiIndesiderati() {
        return farmaco.containsKey("effettiIndesiderati") ? farmaco.get("effettiIndesiderati") : "";
    }

    /**
     * @return the avvertenzeSpeciali
     */
    public String getAvvertenzeSpeciali() {
        return farmaco.containsKey("avvertenzeSpeciali") ? farmaco.get("avvertenzeSpeciali") : "";
    }

    /**
     * @return the interaioni
     */
    public String getInterazioni() {
        return farmaco.containsKey("interazioni") ? farmaco.get("interazioni") : "";
    }

    /**
     * @return the posologia
     */
    public String getPosologia() {
        return farmaco.containsKey("posologia") ? farmaco.get("posologia") : "";
    }

    /**
     * @return the somministrazione
     */
    public String getSomministrazione() {
        return farmaco.containsKey("somministrazione") ? farmaco.get("somministrazione") : "";
    }

    /**
     * @return the sovradosaggio
     */
    public String getSovradosaggio() {
        return farmaco.containsKey("sovradosaggio") ? farmaco.get("sovradosaggio") : "";
    }

    /**
     * @return the sintomi
     */
    public String getIndicazioniTerapeutiche() {
        return farmaco.containsKey("indicazioniTerapeutiche") ? farmaco.get("indicazioniTerapeutiche") : "";
    }

    public String getSintomi() {
        return getIndicazioniTerapeutiche();
    }

    /**
     * @return the comportamentoEmergenza
     */
    public String getComportamentoEmergenza() {
        return farmaco.containsKey("comportamentoEmergenza") ? farmaco.get("comportamentoEmergenza") : "";
    }

    public class Info {
        private String voce;
        private String descrizione;

        public Info(String voce, String descrizione) {
            this.voce = voce;
            this.descrizione = descrizione;
        }

        public String voce() {
            return voce;
        }

        public String descrizione() {
            return descrizione;
        }

        public boolean isEmpty() {
            return descrizione == null || descrizione.length() == 0;
        }
    }
}
