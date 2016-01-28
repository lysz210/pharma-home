package com.pharmahome.pharmahome.core.middleware;

import com.pharmahome.pharmahome.core.db.FarmacoContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ciao on 04/12/15.
 */
public class Farmaco {

    public static final String[] KEYS = {
            "aic",
            "nome",
            "principioAttivo",
            "ditta",
            "codiceGruppoEquivalenza",
            "descrizioneGruppoEquivalenza",
            "linkFogliettoIllustrativo",
            "ecipienti",
            "categoria",
            "precauziooni",
            "effettiIndesiderati",
            "avvertenzeSpeciali",
            "interazioni",
            "posologia",
            "somministrazione",
            "sovradosaggio",
            "sintomi",
            "comportamentoEmergenza"
    };

    private long _farmacoId = -1;

    private HashMap<String, String> farmaco = new HashMap<>();

    public Farmaco(Farmaco f){
        farmaco = f.farmaco;
        _farmacoId = f.getFarmacoId();
    }

    public Farmaco(JSONObject f) throws JSONException {
        String tmp;
        for(int i=0; i<KEYS.length; i+=1){
            tmp = KEYS[i];
            if(f.has(tmp)){
                farmaco.put(tmp, f.getString(tmp));
            }
        }
        if(f.has(FarmacoContract.Farmaco._ID)){
            _farmacoId = f.getInt(FarmacoContract.Farmaco._ID);
        }
        return;
    }

    public long getFarmacoId(){
        return _farmacoId;
    }

    public String get(String key){
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
    public String getSintomi() {
        return farmaco.containsKey("sintomi") ? farmaco.get("sintomi") : "";
    }
    /**
     * @return the comportamentoEmergenza
     */
    public String getComportamentoEmergenza() {
        return farmaco.containsKey("comportamentoEmergenza") ? farmaco.get("comportamentoEmergenza") : "";
    }
}
