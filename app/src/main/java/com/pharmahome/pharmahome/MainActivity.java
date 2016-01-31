package com.pharmahome.pharmahome;

// TODO attenzione inserire unique su campo aic db

import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
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
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaFarmaci;

import org.json.JSONException;

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

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        _db = new DBController(this.getApplicationContext());
        _db.update();

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
                        break;
                }
                return handled;
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
        switch (item.getItemId()) {
            case R.id.action_search:
                if(searchItem == null){
                    searchItem = item;
                }
                onSwitch();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void onSwitch(){
        searchItem.setIcon(inputIcons[searchState ? 0 : 1]);
        inputSearch.setText("");
        inputSearch.clearFocus();
        inputSearch.clearComposingText();
        if(inputSearch.hasFocus()){
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
        }
        searchState = !searchState;
        switcher.showNext();
    }

}
