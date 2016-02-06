package com.pharmahome.pharmahome;

// TODO attenzione inserire unique su campo aic db

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.pharmahome.pharmahome.UI.DettaglioFarmaco;
import com.pharmahome.pharmahome.UI.ListaHome;
import com.pharmahome.pharmahome.UI.OnFarmacoSelectedListener;
import com.pharmahome.pharmahome.UI.Pagina;
import com.pharmahome.pharmahome.UI.PaginatoreSingolo;
import com.pharmahome.pharmahome.UI.RisultatiRicerca;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnFarmacoSelectedListener, PaginatoreSingolo {

    private Pagina actualPage = null;
    public void setActualPage(Pagina pagina){
        pagina.updateTitolo((TextView)findViewById(R.id.titolo_pagina));
        this.actualPage = pagina;
    }

    private ViewSwitcher switcher = null;
    private EditText inputSearch = null;
    private int[] inputIcons = {android.R.drawable.ic_menu_search, android.R.drawable.ic_menu_close_clear_cancel};
    private boolean searchState = false;
    private MenuItem searchItem = null;
    private void setSearchState(boolean stato){
        searchState = stato;
    }
    private boolean getSearchState(){
        return searchState;
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

    @Override
    public AlertDialog.Builder getAlertBuilder() {
        return new AlertDialog.Builder(this);
    }

    private HashMap<String, Object> data = new HashMap<>();
//TODO elimminare questa parte crea problemi con versioni vecchi
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

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        _db = new DBController(this.getApplicationContext());
        // TODO  verifica connessione internet durante l'update
        //_db.update();
        //_db.onUpgrade(_db.getWritableDatabase(), 1, 2);

        if(findViewById(R.id.main_container) != null){
            ListaHome farmaci = new ListaHome();

            farmaci.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction().add(R.id.main_container, farmaci).commit();
        }
        initMenuSecondarioHandlers();

        switcher = (ViewSwitcher) findViewById(R.id.title_search_swicher);
        inputSearch = (EditText) findViewById(R.id.input_search);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                String q = v.getText().toString();
                switch (actionId){
                    case EditorInfo.IME_ACTION_SEARCH:
                        Log.d("NOME FARMACO", q);
                        handled = true;
                        onSwitch();
                        RisultatiRicerca res = new RisultatiRicerca();
                        Bundle args = getIntent().getExtras();
                        if(args == null){
                            args = new Bundle();
                        }
                        args.putString(RisultatiRicerca.KEY_Q, q);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        res.setArguments(args);

                        transaction.replace(R.id.main_container, res);
                        transaction.addToBackStack(null);

                        transaction.commit();
                        break;
                }
                return handled;
            }
        });


        findViewById(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualPage.onHomeClickedListener();
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        visualizzaMessaggio("premuto " + item.getItemId());
        switch (item.getItemId()) {
            case R.id.action_search:
                if(searchItem == null){
                    searchItem = item;
                }
                onSwitch();
                return true;
            case R.id.action_home:
                visualizzaMessaggio("premuto home");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void onSwitch(){
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        searchItem.setIcon(inputIcons[searchState ? 0 : 1]);
        inputSearch.setText("");
        if(searchState){
            getCurrentFocus().clearFocus();
            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else {
            inputSearch.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        searchState = !searchState;
        switcher.showNext();
    }

}
