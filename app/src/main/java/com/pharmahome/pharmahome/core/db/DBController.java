package com.pharmahome.pharmahome.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.pharmahome.pharmahome.core.utils.Utility;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;
import com.pharmahome.pharmahome.core.middleware.ListaFarmaci;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 04/12/15.
 */
public class DBController extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "PharmaHome.db";
    public static final int NOTIFICA_UPDATE_ID = 1;
    private static final String SQL_CREATE_FARMACI = FarmacoContract.Farmaco.SQL_CREATE_TABLE;
    private static final String SQL_CREATE_CONFEZIONI = FarmacoContract.Confezione.SQL_CREATE_TABLE;
    private static final String SQL_CREATE_CONF_VIEW = FarmacoContract.Confezione.SQL_CREATE_VIEW;
    private static final String SQL_DELETE_CONF_VIEW = FarmacoContract.Confezione.SQL_DROP_VIEW;
    private static final String SQL_DELETE_FARMACI = FarmacoContract.Farmaco.SQL_DROP_TABLE;
    private static final String SQL_DELETE_CONFEZIONI = FarmacoContract.Confezione.SQL_DROP_TABLE;
    private static boolean isUpdating = false;
    private Context context = null;

    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static boolean isUpdating() {
        return isUpdating;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate");
        db.execSQL(SQL_CREATE_FARMACI);
        db.execSQL(SQL_CREATE_CONFEZIONI);
        db.execSQL(SQL_CREATE_CONF_VIEW);
        copyFromPath(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        System.out.println("updating db");
        db.execSQL(SQL_DELETE_CONFEZIONI);
        db.execSQL(SQL_DELETE_FARMACI);
        db.execSQL(SQL_DELETE_CONF_VIEW);
        onCreate(db);
        //update();
    }

    public void copyFromPath(SQLiteDatabase db) {
        boolean result = false;
        final SQLiteDatabase tDB = db;
        (new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                System.out.print(params[0]);
                String tmppath = Utility.cpAsset2Data(context, "databases/PharmaHome.db", null);
                SQLiteDatabase tmpdb = SQLiteDatabase.openDatabase(tmppath, null, SQLiteDatabase.OPEN_READONLY);
                Cursor tcur = tmpdb.rawQuery(FarmacoContract.Farmaco.BASE_SEL, null);
                ListaFarmaci tlist = null;
                try {
                    tlist = FarmacoContract.Farmaco.toListaFarmaci(tcur);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (tlist != null || tlist.size() > 0) {
                    for (Farmaco f : tlist) {
                        modificaFarmaco(f);
                    }
                }
                Utility.rmFile(tmppath);
                return "Done!!";
            }
        }).execute("Start coping");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // funzioni personalizzate
    public void update(Farmaco farmaco) {
        modificaFarmaco(farmaco);
    }

    public ListaConfezioni getAllConfezioni() throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.BASE_SEL;
        Log.d("DB INTEROGAZIONE", select);
        String[] args = null;
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }


    public ListaConfezioni getConfezioniHome() throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.CONFEZIONI_X_HOME;
        Log.d("DB INTEROGAZIONE", select);
        String[] args = null;
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }

    public Confezione getConfezioneById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.SELECT_CONFEZIONE_BY_ID;
        String[] args = {id};
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni lc = null;
        try {
            lc = FarmacoContract.Confezione.toListaConfezioni(cur);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Confezione confezione = lc != null && lc.size() > 0 ? lc.get(0) : null;
        db.close();
        return confezione;
    }

    public ListaConfezioni ricercaPerAIC(String q) throws JSONException, ParseException {
        if (q == null || q.length() <= 0) {
            return new ListaConfezioni();
        }
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.SELECT_CONFEZIONE_BY_AIC;
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }

    public ListaConfezioni ricercaPerNome(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.SELECT_CONFEZIONE_BY_NOME;
        String[] args = {"%" + q + "%"};
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }

    public ListaConfezioni ricercaPerPA(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.SELECT_CONFEZIONE_BY_PA;
        String[] args = {"%" + q + "%"};
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }

    public ListaConfezioni ricercaPerProduttore(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.SELECT_CONFEZIONE_BY_DITTA;
        String[] args = {"%" + q + "%"};
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }

    public ListaConfezioni ricercaPerSintomi(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.SELECT_CONFEZIONE_BY_SINTOMI;
        String[] args = {"%" + q + "%"};
        Cursor cur = db.rawQuery(select, args);
        ListaConfezioni res = FarmacoContract.Confezione.toListaConfezioni(cur);
        db.close();
        return res;
    }

    public void eliminaConfezione(Confezione record) {
        SQLiteDatabase db = getWritableDatabase();
        String[] selectionArgs = {String.valueOf(record.getConfezioneId())};
        db.delete(
                FarmacoContract.Confezione.TABLE_NAME,
                FarmacoContract.Confezione._ID + " = ?",
                selectionArgs
        );
        db.close();
    }

    public long aggiungiConfezione(Confezione record) {
        SQLiteDatabase db = getWritableDatabase();
        long res = db.insert(
                FarmacoContract.Confezione.TABLE_NAME,
                null,
                FarmacoContract.Confezione.createRecordValues(record)
        );
        db.close();
        return res;
    }

    public long modificaConfezione(Confezione record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = FarmacoContract.Confezione.createRecordValues(record);
        long res = db.update(
                FarmacoContract.Confezione.TABLE_NAME,
                values,
                FarmacoContract.Confezione._ID + " = " + record.getConfezioneId(),
                null
        );
        db.close();
        return res;
    }

    public long aggiungiFarmaco(Farmaco record) {
        SQLiteDatabase db = getWritableDatabase();
        long res = db.insert(
                FarmacoContract.Farmaco.TABLE_NAME,
                null,
                FarmacoContract.Farmaco.createRecordValues(record)
        );
        db.close();
        return res;
    }

    public ListaFarmaci ricercaFarmacoPerAIC(String q) throws JSONException {
        if (q == null || q.length() <= 0) {
            return new ListaFarmaci();
        }
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Farmaco.SELECT_FARMACO_BY_AIC;
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        ListaFarmaci res = FarmacoContract.Farmaco.toListaFarmaci(cur);
        db.close();
        return res;
    }

    public ListaFarmaci ricercaFarmacoPerNome(String q) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Farmaco.SELECT_FARMACI_BY_NOME;
        String[] args = {"%" + q + "%"};
        Cursor cur = db.rawQuery(select, args);
        ListaFarmaci res = FarmacoContract.Farmaco.toListaFarmaci(cur);
        db.close();
        return res;
    }

    private int modificaFarmaco(Farmaco record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = FarmacoContract.Farmaco.createRecordValues(record);
        String[] args = {record.getAic()};
        int response = db.update(
                FarmacoContract.Farmaco.TABLE_NAME,
                values,
                FarmacoContract.Farmaco.COLUMN_NAME_AIC + " = ?",
                args
        );
        if (response == 0) {
            aggiungiFarmaco(record);
            response = 1;
        }
        db.close();
        return response;
    }
}