package com.pharmahome.pharmahome;

// TODO attenzione inserire unique su campo aic db

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.pharmahome.pharmahome.UI.OnFarmacoSelectedListener;
import com.pharmahome.pharmahome.UI.PaginaDettaglioFarmaco;
import com.pharmahome.pharmahome.UI.PaginaListaHome;
import com.pharmahome.pharmahome.UI.PaginaRisultatiRicerca;
import com.pharmahome.pharmahome.UI.PaginaSelezioneData;
import com.pharmahome.pharmahome.UI.UpdateFarmaciService;
import com.pharmahome.pharmahome.UI.Utility;
import com.pharmahome.pharmahome.UI.paginatoreInterface.Pagina;
import com.pharmahome.pharmahome.UI.paginatoreInterface.PaginatoreSingolo;
import com.pharmahome.pharmahome.UI.paginatoreInterface.VisualizzatoreDialog;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnFarmacoSelectedListener, PaginatoreSingolo {

    private Pagina actualPage = null;
    private ViewSwitcher switcher = null;
    private EditText inputSearch = null;
    private int[] inputIcons = {android.R.drawable.ic_menu_search, android.R.drawable.ic_menu_close_clear_cancel};
    private boolean searchState = false;
    private MenuItem searchItem = null;
    private HashMap<String, Object> data = new HashMap<>();

    public void setActualPage(Pagina pagina) {
        pagina.updateTitolo((TextView) findViewById(R.id.titolo_pagina));
        this.actualPage = pagina;
    }

    private boolean getSearchState() {
        return searchState;
    }

    private void setSearchState(boolean stato) {
        searchState = stato;
    }

    @Override
    public void visualizzaMessaggio(String msg, int dur) {
        Snackbar.make(findViewById(R.id.cooodinator), msg, dur).show();
    }

    @Override
    public void visualizzaMessaggio(String msg) {
        visualizzaMessaggio(msg, Snackbar.LENGTH_SHORT);
    }

    @Override
    public void visualizzaMessaggio(int msg, int dur) {
        Snackbar.make(findViewById(R.id.cooodinator), msg, dur).show();
    }

    @Override
    public void visualizzaMessaggio(int msg) {
        visualizzaMessaggio(msg, Snackbar.LENGTH_SHORT);
    }

    @Override
    public AlertDialog.Builder getAlertBuilder() {
        return new AlertDialog.Builder(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        if (findViewById(R.id.main_container) != null) {
            PaginaListaHome farmaci = new PaginaListaHome();
            farmaci.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, farmaci).commit();
        }
        initMenuSecondarioHandlers();

        switcher = (ViewSwitcher) findViewById(R.id.title_search_swicher);
        inputSearch = (EditText) findViewById(R.id.input_search);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                String q = v.getText().toString();
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Log.d("ACTION_START FARMACO", q);
                        handled = true;
                        onSwitch();
                        PaginaRisultatiRicerca res = new PaginaRisultatiRicerca();
                        Bundle args = getIntent().getExtras();
                        if (args == null) {
                            args = new Bundle();
                        }
                        args.putString(PaginaRisultatiRicerca.KEY_Q, q);
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

    @Override
    public void onResume() {
        super.onResume();
        String idFarmaco = getIntent().getStringExtra(PaginaSelezioneData.KEY_FARMACO_AIC);
        if (idFarmaco != null && idFarmaco.length() > 0) {
            DBController db = new DBController(this);
            ListaConfezioni lConfezione = null;
            try {
                lConfezione = db.ricercaPerAIC(idFarmaco);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            onFarmacoSelected(lConfezione.get(0));
        }
        ListView v = (ListView) findViewById(android.R.id.list);
        if (v != null) {
            Utility.updateListConfezioniHeight(v, R.id.nome_farmaco);
        }
    }

    private void initMenuSecondarioHandlers() {
        ImageButton leftBtn = ((ImageButton) findViewById(R.id.btn_left));
        ImageButton centerBtn = ((ImageButton) findViewById(R.id.btn_center));
        ImageButton rightBtn = ((ImageButton) findViewById(R.id.btn_right));
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
        PaginaDettaglioFarmaco farmaco = new PaginaDettaglioFarmaco();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_container, farmaco);
        transaction.addToBackStack(null);

        transaction.commit();
        return;
    }

    public Confezione getConfezione() {
        return (Confezione) data.get(Confezione.KEY_CONFEZIONE);
    }

    public void removeConfezione() {
        data.remove(Confezione.KEY_CONFEZIONE);
    }

    public void setConfezione(Confezione confezione) {
        this.data.put(Confezione.KEY_CONFEZIONE, confezione);
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
                if (searchItem == null) {
                    searchItem = item;
                }
                onSwitch();
                return true;
            case R.id.action_home:
                visualizzaMessaggio("premuto home");
                return true;
            case R.id.action_update:
                // TODO COVERTIRE IN SERVICE
                if (!UpdateFarmaciService.isUpdating()) {
                    Intent t_intent = new Intent(this, UpdateFarmaciService.class);
                    t_intent.setAction(UpdateFarmaciService.ACTION_START);
                    startService(t_intent);
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void onSwitch() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        searchItem.setIcon(inputIcons[searchState ? 0 : 1]);
        inputSearch.setText("");
        if (searchState) {
//            getCurrentFocus().clearFocus();
            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else {
            inputSearch.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        searchState = !searchState;
        switcher.showNext();
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
        if (listeners.size() <= 0) {
            visualizzaDialog(titoloID, contenuto);
            return;
        }
        AlertDialog.Builder builder = getAlertBuilder();
        builder.setMessage(contenuto);
        builder.setTitle(titoloID);
        for (String k : keys) {
            VisualizzatoreDialog.OnClickListenerValue tmp = listeners.get(k);
            if (tmp == null) {
                continue;
            }
            switch (k) {
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
        builder.create().show();
    }
}
