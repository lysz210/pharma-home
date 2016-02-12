package com.pharmahome.pharmahome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pharmahome.pharmahome.UI.PaginaInserimentoCodice;
import com.pharmahome.pharmahome.UI.OnFarmacoSelectedListener;
import com.pharmahome.pharmahome.UI.paginatoreInterface.Pagina;
import com.pharmahome.pharmahome.UI.paginatoreInterface.PaginatoreSingolo;
import com.pharmahome.pharmahome.UI.PaginaSelezioneData;
import com.pharmahome.pharmahome.UI.paginatoreInterface.VisualizzatoreDialog;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;
import com.pharmahome.pharmahome.core.middleware.ListaFarmaci;

import org.json.JSONException;

import java.util.HashMap;

public class InsertActivity extends AppCompatActivity implements OnFarmacoSelectedListener, PaginatoreSingolo {

    private HashMap<String, Object> data = new HashMap<>();

    public static String KEY_FARMACO = "farmaco";
    public static String KEY_LISTA_FARMACI = "lista_farmaci";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.main_container) != null){
            PaginaInserimentoCodice insert = new PaginaInserimentoCodice();

            insert.setArguments(getIntent().getExtras());

            PaginaSelezioneData select = new PaginaSelezioneData();
            select.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.main_container, insert).commit();
        }

        initMenuSecondarioHandlers();
        findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onHomeClickedListener();
            }
        });

    }

    private void initMenuSecondarioHandlers(){
        ((ImageButton)findViewById(R.id.btn_left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onLeftButtonClick(v, null);
            }
        });
        ((ImageButton)findViewById(R.id.btn_center)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onScrollDown(v);
            }
        });
        ((ImageButton)findViewById(R.id.btn_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onScrollUp(v);
            }
        });
    }

    public void setFarmaco(Farmaco farmaco){
        this.data.put(KEY_FARMACO, farmaco);
    }

    public Farmaco getFarmaco(){
        return (Farmaco) this.data.get(KEY_FARMACO);
    }

    public void setListaFarmaci(String[] lista){
        this.data.put(KEY_LISTA_FARMACI, lista);
    }
    public String[] getListaFarmaci(){
        return (String[]) this.data.get(KEY_LISTA_FARMACI);
    }

    public void onFarmacoSelected(int position) {
        PaginaSelezioneData selezioneData = new PaginaSelezioneData();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_container, selezioneData);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onFarmacoSelected(Confezione confezione) {

    }

    private Pagina actualPage = null;
    @Override
    public void setActualPage(Pagina pagina) {
        pagina.updateTitolo((TextView)findViewById(R.id.titolo_pagina));
        this.actualPage = pagina;
    }

    @Override
    public void visualizzaMessaggio(String msg, int dur) {
        Toast toast = Toast.makeText(this, msg, dur);
        toast.show();
    }

    @Override
    public void visualizzaMessaggio(String msg) {
        visualizzaMessaggio(msg, Toast.LENGTH_SHORT);
    }

    @Override
    public AlertDialog.Builder getAlertBuilder() {
        return new AlertDialog.Builder(this);
    }

    @Override
    public void visualizzaDialog(int titoloID, int contenuto) {
        AlertDialog.Builder builder = getAlertBuilder();
        builder.setMessage(contenuto);
        builder.setTitle(titoloID);
        builder.setNeutralButton(R.string.btn_chiudi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();
    }

    @Override
    public void visualizzaDialog(int titoloID, int contenuto, HashMap<String, OnClickListenerValue> listeners) {
        String[] keys = VisualizzatoreDialog.KEYS_BTN_AVAILBLE;
        if(listeners.size() <= 0){
            visualizzaDialog(titoloID, contenuto);
            return;
        }
        AlertDialog.Builder builder = getAlertBuilder();
        builder.setMessage(contenuto);
        builder.setTitle(titoloID);
        for(String k: keys){
            VisualizzatoreDialog.OnClickListenerValue tmp = listeners.get(k);
            if(tmp == null){
                continue;
            }
            switch (k){
                case VisualizzatoreDialog.KEY_BTN_POSITIVE:
                    builder.setPositiveButton(tmp.getNomeID(), tmp.getListener());
                    break;
                case VisualizzatoreDialog.KEY_BTN_NEGATIVE:
                    builder.setNegativeButton(tmp.getNomeID(), tmp.getListener());
                    break;
                case VisualizzatoreDialog.KEY_BTN_NEUTRAL:
                    builder.setNeutralButton(tmp.getNomeID(), tmp.getListener());
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ricerca, menu);
        return true;
    }

    /**
     * gestione del menu principale
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        visualizzaMessaggio("premuto " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_home:
                visualizzaMessaggio("premuto home");
                return true;
            case R.id.action_break:
                visualizzaMessaggio(("premuto interrompi"));

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * all'inserimento del codice a barre viene lanciato l'inserimento
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            DBController db = new DBController(this);
            try {
                String aic = scanningResult.getContents();
                ListaFarmaci lf = db.ricercaFarmacoPerAIC(aic);
                Farmaco farmaco = lf.size() > 0 ? lf.get(0) : null;
                if(farmaco != null)
                    ((PaginaInserimentoCodice) actualPage).avanti(farmaco);
                else
                    visualizzaDialog(R.string.msg_nessun_farmaco_titolo, R.string.msg_nessun_farmaco_corpo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
