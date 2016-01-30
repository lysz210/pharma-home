package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.db.FarmacoContract;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.middleware.ListaFarmaci;

import org.json.JSONException;

/**
 * Created by ciao on 26/01/16.
 */
public class InserimentoCodice extends Fragment implements Pagina {

    private Button avanti;
    private ImageButton scan;
    private EditText cb;
    private String lastCb = "";
    private void setLastCb(String s){ lastCb = s;}
    private EditText nome;
    private String lastNome = "";
    private void setLastNome(String s){ lastNome = s;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inserimento_codice, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    public int counter = 0;
    public int getcounter(){
        return counter;
    }
    public void incounter(){
        counter++;
    }

    private void initView(View view, Bundle savedInstance){
        final Activity activity = getActivity();
        final DBController db = new DBController(getContext());
        avanti = (Button) view.findViewById(R.id.btn_avanti);
        scan = (ImageButton) view.findViewById(R.id.btn_scan_cb);
        cb = (EditText) view.findViewById(R.id.input_cb);
        nome = (EditText) view.findViewById(R.id.input_nome);

        avanti.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tcb = cb.getText().toString();
                String tnome = nome.getText().toString();
                try {
                    if (tcb.length() >= Farmaco.MIN_LEN_AIC) {
                        avanti(db.ricercaFarmacoPerAIC(tcb));
                    } else if(tnome.length() >= 0) {
                        avanti(db.ricercaFarmacoPerNome(tnome));
                    } else {
                        ((PaginatoreSingolo)activity).visualizzaMessaggio("Deve essere inserito il codice AIC oppure il nome del farmaco.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        cb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLastNome(nome.getText().toString());
                    nome.setText("");
                    cb.setText(lastCb);
                }
            }
        });


        cb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                incounter();
                Log.d("COUNTER", "" + getcounter());
                String aic = v.getText().toString();
                if (aic.length() < 9 ) {
                    return handled;
                }
                int ek = event.getKeyCode();
                if (ek == KeyEvent.KEYCODE_ENTER || ek == KeyEvent.KEYCODE_TAB || ek == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    try {
                        ListaFarmaci lf = db.ricercaFarmacoPerAIC(aic);
                        avanti(lf);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return handled;
                    }
                    handled = false;
                }
                return handled;
            }
        });

        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setLastCb(cb.getText().toString());
                    cb.setText("");
                    nome.setText(lastNome);
                }
            }
        });
        nome.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                String nome = v.getText().toString();
                int ek = event.getKeyCode();
                if (ek == KeyEvent.KEYCODE_ENTER || ek == KeyEvent.KEYCODE_TAB || ek == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    try {
                        ListaFarmaci lf = db.ricercaFarmacoPerNome(nome);

                        Log.d("NOME FARMACO", nome + lf.size());
                        avanti(lf);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return handled;
                    }
                    handled = false;
                }
                return handled;
            }
        });

        ((PaginatoreSingolo)activity).setActualPage(this);
    }

    // handlers

    private void avanti(Farmaco farmaco){
        InsertActivity activity = (InsertActivity) getActivity();
        activity.setFarmaco(farmaco);
        avviaSceltaData();
    }

    private void avanti(ListaFarmaci listaFarmaci){
        final ListaFarmaci lf = listaFarmaci;
        int len = listaFarmaci.size();
        switch (len){
            case 0: {
                break;
            }
            case 1: {
                avanti(listaFarmaci.get(0));
            }
            default: {
                String[] lista = new String[listaFarmaci.size()];
                for(int i=0; i<len; i++) {
                    lista[i] = listaFarmaci.get(i).getNome();
                }
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("ciao");
                b.setItems(lista, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        avanti(lf.get(which));
                    }
                });
                AlertDialog a = b.create();
                a.show();
            }
        }
    }

    private void avviaSceltaData(){
        Fragment frag = (Fragment) new SelezioneData();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        frag.setArguments(getActivity().getIntent().getExtras());
        transaction.replace(R.id.main_container, frag);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onLeftButtonClick(View v, Bundle info) {
        getActivity().finish();
    }

    @Override
    public void onScrollUp(View v) {

    }

    @Override
    public void onScrollDown(View v) {

    }
}
