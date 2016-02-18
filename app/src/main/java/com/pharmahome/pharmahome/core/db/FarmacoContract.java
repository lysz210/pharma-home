package com.pharmahome.pharmahome.core.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;
import com.pharmahome.pharmahome.core.middleware.ListaFarmaci;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ciao on 04/12/15.
 */
public class FarmacoContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMERIC_TYPE = " NUM";
    private static final String INTEGER_TYPE = " INT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String UNIQUE = " UNIQUE";
    public FarmacoContract() {
    }

    public static abstract class Farmaco implements BaseColumns {
        public static final String TABLE_NAME = "Farmaci";

        public static final String _ID = "id_farmaco";
        public static final String COLUMN_NAME_AIC = "aic";
        public static final String COLUMN_NAME_NOME = "nome";
        public static final String COLUMN_NAME_PRINCIPIO_ATTIVO = "principioAttivo";
        public static final String COLUMN_NAME_DITTA = "ditta";
        public static final String COLUMN_NAME_CODICE_GRUPPO_EQUIVALENZA = "codiceGruppoEquivalenza";
        public static final String COLUMN_NAME_DESCRIZIONE_GRUPPO_EQUIVALENZA = "descrizioneGruppoEquivalenza";
        public static final String COLUMN_NAME_LINK_FOGLIETTO_ILLUSTRATIVO = "linkFogliettoIllustrativo";
        public static final String COLUMN_NAME_ECIPIENTI = "ecipienti";
        public static final String COLUMN_NAME_CATEGORIA = "categoria";
        public static final String COLUMN_NAME_PRECAUZIONI = "precauzioni";
        public static final String COLUMN_NAME_EFFETTI_INDESIDERATI = "effettiIndesiderati";
        public static final String COLUMN_NAME_AVVERTENZE_SPECIALI = "avvertenzeSpeciali";
        public static final String COLUMN_NAME_INTERAZIONI = "interazioni";
        public static final String COLUMN_NAME_POSOLOGIA = "posologia";
        public static final String COLUMN_NAME_SOMMINISTRAZIONE = "somministrazione";
        public static final String COLUMN_NAME_SOVRADOSAGGIO = "sovradosaggio";
        public static final String COLUMN_NAME_INDICAZIONI_TERAPEUTICHE = "indicazioniTerapeutiche";
        public static final String COLUMN_NAME_COMPORTAMENTOEMERGENZA = "comportamentoEmergenza";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + FarmacoContract.Farmaco.TABLE_NAME +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                        COLUMN_NAME_AIC + TEXT_TYPE + UNIQUE + COMMA_SEP +
                        COLUMN_NAME_NOME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PRINCIPIO_ATTIVO + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DITTA + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CODICE_GRUPPO_EQUIVALENZA + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DESCRIZIONE_GRUPPO_EQUIVALENZA + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LINK_FOGLIETTO_ILLUSTRATIVO + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ECIPIENTI + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CATEGORIA + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PRECAUZIONI + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EFFETTI_INDESIDERATI + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AVVERTENZE_SPECIALI + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_INTERAZIONI + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_POSOLOGIA + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SOMMINISTRAZIONE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SOVRADOSAGGIO + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_INDICAZIONI_TERAPEUTICHE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_COMPORTAMENTOEMERGENZA + TEXT_TYPE +
                        " )";


        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static String BASE_SEL =
                "SELECT * FROM " + TABLE_NAME;

        public static String SELECT_FARMACO_BY_AIC = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_AIC + "=?";
        public static String SELECT_FARMACI_BY_NOME = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_NOME + " LIKE ?";

        public static final ContentValues createRecordValues(com.pharmahome.pharmahome.core.middleware.Farmaco record) {
            ContentValues values = new ContentValues();
            for (String key : com.pharmahome.pharmahome.core.middleware.Farmaco.KEYS) {
                values.put(key, record.get(key));
            }
            return values;
        }

        public static final ListaFarmaci toListaFarmaci(Cursor cur) throws JSONException {
            ListaFarmaci lista = new ListaFarmaci();
            if (!cur.moveToFirst()) {
                return lista;
            }
            String[] colNames = cur.getColumnNames();
            JSONObject tmpo = null;
            com.pharmahome.pharmahome.core.middleware.Farmaco tmpf = null;
            do {
                tmpo = new JSONObject();
                for (String k : colNames) {
                    tmpo.put(k, cur.getString(cur.getColumnIndex(k)));
                }
                tmpf = new com.pharmahome.pharmahome.core.middleware.Farmaco(tmpo);
                lista.add(tmpf);
            } while (cur.moveToNext());
            return lista;
        }
    }

    public static abstract class Confezione implements BaseColumns {
        public static final String TABLE_NAME = "Confezioni";
        public static final String _ID = "id_confezione";
        public static final String COLUMN_NAME_FARMACO = "farmaco";
        public static final String COLUMN_NAME_SCADENZA = "scadenza";
        public static final String COLUMN_NAME_NUOVA_CONFEZIONE = "nuovaConfezione";

        public static final String VIEW_NAME = TABLE_NAME + "View";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        " (" +
                        _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                        COLUMN_NAME_FARMACO + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SCADENZA + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NUOVA_CONFEZIONE + INTEGER_TYPE + COMMA_SEP +
                        "FOREIGN KEY (" + COLUMN_NAME_FARMACO + ") " +
                        "REFERENCES " + FarmacoContract.Farmaco.TABLE_NAME + " (" + FarmacoContract.Farmaco._ID + ")" +
                        " )";
        public static final String SQL_DROP_VIEW =
                "DROP VIEW IF EXISTS " + VIEW_NAME;
        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static String SQL_ORDERBY_SCADENZA = " ORDER BY " + COLUMN_NAME_SCADENZA + ", " + Farmaco.COLUMN_NAME_NOME + " ASC";
        public static final String SQL_CREATE_VIEW =
                "CREATE VIEW IF NOT EXISTS " + VIEW_NAME + " AS " +
                        "SELECT * FROM " + FarmacoContract.Farmaco.TABLE_NAME + " f JOIN " + TABLE_NAME + " c" +
                        " ON c." + COLUMN_NAME_FARMACO + " = f." + FarmacoContract.Farmaco._ID + SQL_ORDERBY_SCADENZA;
        public static String BASE_SEL =
                "SELECT * FROM " + VIEW_NAME;

        public static String CONFEZIONI_X_HOME = "SELECT * FROM " +
                Farmaco.TABLE_NAME +
                " JOIN ( SELECT min(c." + _ID + ") AS " + _ID + ", c." + COLUMN_NAME_FARMACO + ", c." + COLUMN_NAME_SCADENZA + ", c." + COLUMN_NAME_NUOVA_CONFEZIONE +
                " FROM " + TABLE_NAME + " c WHERE c." + COLUMN_NAME_SCADENZA + " = ( SELECT min(i." + COLUMN_NAME_SCADENZA + ") FROM " +
                TABLE_NAME + " i WHERE i." + COLUMN_NAME_FARMACO + " = c." + COLUMN_NAME_FARMACO + ") GROUP BY " + COLUMN_NAME_FARMACO + ") ON " +
                Farmaco._ID + " = " + COLUMN_NAME_FARMACO + SQL_ORDERBY_SCADENZA;

        // query basilari
        public static String SELECT_CONFEZIONE_BY_ID = BASE_SEL + " WHERE " + _ID + "=?";
        public static String SELECT_CONFEZIONE_BY_AIC = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_AIC + "=?";
        public static String SELECT_CONFEZIONE_BY_NOME = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_NOME + " LIKE ?";
        public static String SELECT_CONFEZIONE_BY_PA = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_PRINCIPIO_ATTIVO + " LIKE ?";
        public static String SELECT_CONFEZIONE_BY_DITTA = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_DITTA + " LIKE ?";
        public static String SELECT_CONFEZIONE_BY_SINTOMI = BASE_SEL + " WHERE " + Farmaco.COLUMN_NAME_INDICAZIONI_TERAPEUTICHE + " LIKE ?";

        public static final ContentValues createRecordValues(com.pharmahome.pharmahome.core.middleware.Confezione record) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_FARMACO, record.getFarmacoId());
            values.put(COLUMN_NAME_SCADENZA, new SimpleDateFormat("yyyy-MM-dd").format(record.getScadenza().getTime()));
            values.put(COLUMN_NAME_NUOVA_CONFEZIONE, 1);
            return values;
        }

        public static final ListaConfezioni toListaConfezioni(Cursor cur) throws JSONException, ParseException {
            ListaConfezioni lista = new ListaConfezioni();
            if (!cur.moveToFirst()) {
                return lista;
            }
            String[] colNames = cur.getColumnNames();
            JSONObject tmpo = null;
            com.pharmahome.pharmahome.core.middleware.Confezione tmpc = null;
            com.pharmahome.pharmahome.core.middleware.Farmaco tmpf = null;
            do {
                tmpo = new JSONObject();
                for (String k : colNames) {
                    tmpo.put(k, cur.getString(cur.getColumnIndex(k)));
                }
                tmpf = new com.pharmahome.pharmahome.core.middleware.Farmaco(tmpo);
                tmpc = new com.pharmahome.pharmahome.core.middleware.Confezione(tmpf, tmpo.getString(COLUMN_NAME_SCADENZA), tmpo.getInt(COLUMN_NAME_NUOVA_CONFEZIONE) == 0 ? false : true, tmpo.getLong(_ID));
                lista.add(tmpc);
            } while (cur.moveToNext());
            return lista;
        }
    }
}