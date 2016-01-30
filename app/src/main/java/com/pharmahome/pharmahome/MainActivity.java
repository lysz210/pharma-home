package com.pharmahome.pharmahome;

// TODO attenzione inserire unique su campo aic db

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.DettaglioFarmaco;
import com.pharmahome.pharmahome.UI.ListaHome;
import com.pharmahome.pharmahome.UI.OnFarmacoSelectedListener;
import com.pharmahome.pharmahome.UI.Pagina;
import com.pharmahome.pharmahome.UI.PaginatoreSingolo;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
