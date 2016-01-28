package com.pharmahome.pharmahome.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;
import com.pharmahome.pharmahome.core.middleware.ListaFarmaci;
import com.pharmahome.pharmahome.core.util.PharmaIterator;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 04/12/15.
 */
public class DBController extends SQLiteOpenHelper {

    private static final String SQL_CREATE_FARMACI = FarmacoContract.Farmaco.SQL_CREATE_TABLE;
    private static final String SQL_CREATE_CONFEZIONI = FarmacoContract.Confezione.SQL_CREATE_TABLE;

    private static final String SQL_CREATE_CONF_VIEW = FarmacoContract.Confezione.SQL_CREATE_VIEW;
    private static final String SQL_DELETE_CONF_VIEW = FarmacoContract.Confezione.SQL_DROP_VIEW;

    private static final String SQL_DELETE_FARMACI = FarmacoContract.Farmaco.SQL_DROP_TABLE;

    private static final String SQL_DELETE_CONFEZIONI = FarmacoContract.Confezione.SQL_DROP_TABLE;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PharmaHome.db";

    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate");
        db.execSQL(SQL_CREATE_FARMACI);
        db.execSQL(SQL_CREATE_CONFEZIONI);
        db.execSQL(SQL_CREATE_CONF_VIEW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CONFEZIONI);
        db.execSQL(SQL_DELETE_FARMACI);
        db.execSQL(SQL_DELETE_CONF_VIEW);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // funzioni personalizzate
    public void update() {
        ( new AsyncTask<String, Integer, String>() {

            private Exception exception;

            protected String doInBackground(String... command) {

                System.out.println("Asinctask doInBG");
                System.out.println(command[0]);
                PharmaIterator tf = new PharmaIterator();
                int i = 0;
                for(Farmaco f: tf){
                    modificaFarmaco(f);
                    // System.out.println(f.getLinkFogliettoIllustrativo());
                    if(++i%100 == 0)
                        publishProgress(i);

                    if(i==5) {
                        return "Done!";
                    }
                }
                return "Done!";

            }

            protected void onProgressUpdate(Integer... progress) {
                System.out.println(progress[0]);
            }

            protected void onPostExecute(String feed) {
                // TODO: check this.exception
                // TODO: do something with the feed
                System.out.println("#####################################################################");
                System.out.println(feed);
            }
        }).execute("start");
    }

    public Cursor selectAll(){
        //System.out.println("#####################################################################");
        //System.out.println("selectAll");
        return getReadableDatabase().rawQuery("SELECT * FROM Farmaci", null);
    }


    public ListaConfezioni getAllConfezioni() throws JSONException, ParseException{
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.CONFEZIONI_X_HOME;
        Log.d("DB INTEROGAZIONE", select);
        String[] args = null;
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Confezione.toListaConfezioni(cur);
    }
    public Confezione getConfezioneById(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Farmaco.BASE_SEL + "WHERE " + FarmacoContract.Farmaco._ID + "=?";
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
        return lc != null && lc.size() > 0 ? lc.get(0) : null;
    }
    public ListaConfezioni ricercaPerAIC(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.BASE_SEL + " WHERE aic = ?";
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Confezione.toListaConfezioni(cur);
    }

    public ListaConfezioni ricercaPerNome(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.BASE_SEL + " WHERE nome LIKE ?";
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Confezione.toListaConfezioni(cur);
    }

    public ListaConfezioni ricercaPerPA(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.BASE_SEL + " WHERE principioAttivo LIKE ?";
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Confezione.toListaConfezioni(cur);
    }
    public ListaConfezioni ricercaPerProduttore(String q) throws JSONException, ParseException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Confezione.BASE_SEL + " WHERE ditta LIKE ?";
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Confezione.toListaConfezioni(cur);
    }
    public void eliminaConfezione(Confezione record) {
        SQLiteDatabase db = getWritableDatabase();
        String[] selectionArgs = {String.valueOf(record.getConfezioneId())};
        db.delete(
                FarmacoContract.Confezione.TABLE_NAME,
                FarmacoContract.Confezione._ID + " = ?",
                selectionArgs
        );
    }
    public long aggiungiConfezione(Confezione record) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(
                FarmacoContract.Confezione.TABLE_NAME,
                null,
                FarmacoContract.Confezione.createRecordValues(record)
        );
    }
    public long modificaConfezione(Confezione record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = FarmacoContract.Confezione.createRecordValues(record);
        return db.update(
                FarmacoContract.Confezione.TABLE_NAME,
                values,
                FarmacoContract.Confezione._ID + " = " + record.getConfezioneId(),
                null
        );
    }

    public long aggiungiFarmaco(Farmaco record) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(
                FarmacoContract.Farmaco.TABLE_NAME,
                null,
                FarmacoContract.Farmaco.createRecordValues(record)
        );
    }

    public ListaFarmaci ricercaFarmacoPerAIC(String q) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Farmaco.BASE_SEL + " WHERE aic = ?";
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Farmaco.toListaFarmaci(cur);
    }
    public ListaFarmaci ricercaFarmacoPerNome(String q) throws JSONException {
        SQLiteDatabase db = getWritableDatabase();
        String select = FarmacoContract.Farmaco.BASE_SEL + " WHERE nome LIKE ?";
        String[] args = {q};
        Cursor cur = db.rawQuery(select, args);
        return FarmacoContract.Farmaco.toListaFarmaci(cur);
    }
    private void modificaFarmaco(Farmaco record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = FarmacoContract.Farmaco.createRecordValues(record);
        int response = db.update(
                FarmacoContract.Farmaco.TABLE_NAME,
                values,
                FarmacoContract.Farmaco._ID + " = " + record.getFarmacoId(),
                null
        );
        if(response == 0){
            aggiungiFarmaco(record);
        }
    }
}