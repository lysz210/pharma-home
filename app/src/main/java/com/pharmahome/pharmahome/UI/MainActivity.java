package com.pharmahome.pharmahome.UI;

// TODO attenzione inserire unique su campo aic db


import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnFarmacoSelectedListener, PaginatoreSingolo {

    private Pagina actualPage = null;
    public void setActualPage(Pagina pagina){
        this.actualPage = pagina;
    }

    @Override
    public void visualizzaMessaggio(String msg, int dur) {
        Toast toast = Toast.makeText(this, msg, dur);
        toast.show();
    }
    @Override
    public void visualizzaMessaggio(String msg){
        visualizzaMessaggio(msg, Toast.LENGTH_SHORT);
    }

    private HashMap<String, Object> data = new HashMap<>();

    private static DBController _db = null;
    public static DBController getDBManager(){
        return _db;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _db = new DBController(this.getApplicationContext());

        if(findViewById(R.id.main_container) != null){
            ListaHome farmaci = new ListaHome();

            farmaci.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.main_container, farmaci).commit();
        }

        initMenuSecondarioHandlers();

        // codice di test iniziale
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        DBController db = new DBController(this.getApplicationContext());
//        /*
//        db.update();
//        if(true)
//        return;
//        */
//        ListaFarmaci list = null;
//        try {
//            list = db.ricercaFarmacoPerAIC("022711117");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        for(Farmaco tm: list){
//
//            for(String s: Farmaco.KEYS){
//                System.out.println(s + ": " + tm.get(s));
//            }
//            System.out.println("#############################################################");
//        }
//        if(true){
//            return;
//        }
//        Confezione c = new Confezione(list.get(0));
//        Calendar tmp = Calendar.getInstance();
//        Date data = null;
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        /*
//        try {
//            c.setScadenza("2015-12-10");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        db.modificaConfezione(c);
//        */
//        try {
//            ListaConfezioni listc = db.ricercaPerAIC("022711117");
//            /*
//            c = listc.get(0);
//            c.setScadenza("2015-12-10");
//            db.modificaConfezione(c);
//            listc = db.ricercaPerAIC("022711117");
//            */
//            Fasce tf = listc.get(0).getFascia();
//            System.out.print(tf.getColore());
//            System.out.println(listc.get(0).getFascia().getNome() + ": " + df.format(listc.get(0).getScadenza().getTime()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    private void initMenuSecondarioHandlers(){
        ImageButton leftBtn = ((ImageButton) findViewById(R.id.btn_left));
        ImageButton centerBtn = ((ImageButton)findViewById(R.id.btn_center));
        ImageButton rightBtn = ((ImageButton)findViewById(R.id.btn_right));
        leftBtn.setImageResource(R.drawable.icon_plus);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onLeftButtonClick(v, null);
            }
        });
        centerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onScrollDown(v);
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onScrollUp(v);
            }
        });
    }

    @Override
    public void onFarmacoSelected(Confezione confezione) {
        data.put(Confezione.KEY_CONFEZIONE, confezione);
        DettaglioFarmaco farmaco = new DettaglioFarmaco();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_container, farmaco);
        transaction.addToBackStack(null);

        transaction.commit();
        return;
    }

    public Confezione getConfezione(){
        return (Confezione) data.get(Confezione.KEY_CONFEZIONE);
    }
    public void removeConfezione(){
        data.remove(Confezione.KEY_CONFEZIONE);
    }

}
