package com.pharmahome.pharmahome.UI;

// TODO VERIFICA DELLA DATA PRIMA DI SALVARE.
// TODO MESSAGGIO DI NOTIFICA PER LA CONFERMA

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pharmahome.pharmahome.InsertActivity;
import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.paginatoreInterface.Pagina;
import com.pharmahome.pharmahome.UI.paginatoreInterface.PaginatoreSingolo;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ciao on 26/01/16.
 */
public class PaginaSelezioneData extends Fragment implements Pagina {

    public final static int TITOLO_ID = R.string.titolo_pagina_selezione_data;

    private Button salva;
    private ImageButton modifica;
    private TextView scadenzaView = null;
    private Calendar scadenza = null;
    private PaginatoreSingolo parent = null;
    private ScrollView scroller = null;

    private void setScadenza(int anno, int mese, int giorno){
        scadenza = new GregorianCalendar(anno, mese, giorno);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pagina_selezione_data, container, false);
        scroller = (ScrollView)view;
        InsertActivity activity = (InsertActivity) getActivity();
        ((TextView)view.findViewById(R.id.nome_farmaco)).setText(activity.getFarmaco().getNome());
        salva = (Button) view.findViewById(R.id.btn_salva);
        salva.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                salva(v);
            }
        });

        modifica = (ImageButton) view.findViewById(R.id.modifica);
        modifica.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                selezionaScadenza(scadenzaView);
            }
        });

        scadenzaView = (TextView) view.findViewById(R.id.scadenza);
        setParent(activity);
        ((PaginatoreSingolo)activity).setActualPage(this);

        ViewGroup infos = (ViewGroup) view.findViewById(R.id.lista_info_farmaco);
        infos.addView(new FarmacoInfoView(getContext(), (((InsertActivity)activity).getFarmaco().getAsInfoList())));

        view.findViewById(R.id.link_bugiardino).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                apriBugiardino(v);
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        selezionaScadenza(scadenzaView);
    }

    private void salva(View v){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        Confezione confezione = new Confezione(((InsertActivity) getActivity()).getFarmaco());
        confezione.setScadenza(scadenza);
        (new DBController(getContext())).aggiungiConfezione(confezione);
    }

    @Override
    public void onLeftButtonClick(View v, Bundle info) {

    }

    @Override
    public void onScrollUp(View v) {
        int y = (4 * scroller.getHeight()) / 5;
        scroller.scrollBy(0, -y);
    }

    @Override
    public void onScrollDown(View v) {
        int y = (4 * scroller.getHeight()) / 5;
        scroller.scrollBy(0, y);
    }

    /**
     * apre un selettore per scegliere una data di scadenza opportuna
     * @param view  textview dove va inserito la data selezionata
     */
    private void selezionaScadenza(TextView view){
        // TODO DA IMPLEMENTARE LA SELEZIONE IN MODO TALE CHE SIA IMPOSTOTA ALLA DATA SCELTA COME PRIMO GIORNO
        final TextView v = view;
        new ScadenzaDialog(
            getContext(),
            new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    v.setText(data);
                    setScadenza(year, monthOfYear, dayOfMonth);
                }
            }
        );
    }

    @Override
    public String updateTitolo(TextView v) {
        String titolo = getString(TITOLO_ID);
        v.setText(titolo);
        return titolo;
    }

    /**
     * richiede ad Android per l'apertura del link al bugiardino online
     * direttamente dal sito ufficiale AIFA
     * @param v     la view che effettua la richiesta di apertura del bugiardino
     */
    public void apriBugiardino(View v) {
        InsertActivity activity = (InsertActivity)getActivity();
        if(Utility.isNetworkAvailable(getContext())){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = activity.getFarmaco().getLinkFogliettoIllustrativo();
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } else {
            activity.visualizzaDialog(R.string.msg_fallimento_apertura_bugiardino_titolo, R.string.msg_nessuna_connessione_internet);
        }
    }

    @Override
    public void onHomeClickedListener() {
        parent.visualizzaMessaggio("Home click da selezione data");
    }

    @Override
    public void setParent(PaginatoreSingolo parent) {
        this.parent = parent;
    }

    @Override
    public PaginatoreSingolo getParent() {
        return this.parent;
    }
}