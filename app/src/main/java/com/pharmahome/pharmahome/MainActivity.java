package com.pharmahome.pharmahome;

// TODO attenzione inserire unique su campo aic db


import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pharmahome.core.db.DBController;
import com.pharmahome.core.db.FarmacoContract;
import com.pharmahome.core.middleware.Confezione;
import com.pharmahome.core.middleware.Farmaco;
import com.pharmahome.core.middleware.Fasce;
import com.pharmahome.core.middleware.ListaConfezioni;
import com.pharmahome.core.middleware.ListaFarmaci;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBController db = new DBController(this.getApplicationContext());
        /*
        db.update();
        if(true)
        return;
        */
        ListaFarmaci list = null;
        try {
            list = db.ricercaFarmacoPerAIC("022711117");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(Farmaco tm: list){

            for(String s: Farmaco.KEYS){
                System.out.println(s + ": " + tm.get(s));
            }
            System.out.println("#############################################################");
        }
        if(true){
            return;
        }
        Confezione c = new Confezione(list.get(0));
        Calendar tmp = Calendar.getInstance();
        Date data = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        /*
        try {
            c.setScadenza("2015-12-10");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.modificaConfezione(c);
        */
        try {
            ListaConfezioni listc = db.ricercaPerAIC("022711117");
            /*
            c = listc.get(0);
            c.setScadenza("2015-12-10");
            db.modificaConfezione(c);
            listc = db.ricercaPerAIC("022711117");
            */
            Fasce tf = listc.get(0).getFascia();
            System.out.print(tf.getColore());
            System.out.println(listc.get(0).getFascia().getNome() + ": " + df.format(listc.get(0).getScadenza().getTime()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
