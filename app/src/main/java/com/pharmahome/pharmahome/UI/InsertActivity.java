package com.pharmahome.pharmahome.UI;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.Farmaco;

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
            InserimentoCodice insert = new InserimentoCodice();

            insert.setArguments(getIntent().getExtras());

            SelezioneData select = new SelezioneData();
            select.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.main_container, insert).commit();
        }

        initMenuSecondarioHandlers();


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
        SelezioneData selezioneData = new SelezioneData();

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
}
