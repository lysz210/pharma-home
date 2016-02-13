package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.paginatoreInterface.Pagina;
import com.pharmahome.pharmahome.UI.paginatoreInterface.PaginatoreSingolo;
import com.pharmahome.pharmahome.UI.paginatoreInterface.listener.MyOnDateSetListener;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;
import java.util.Calendar;

public class PaginaDettaglioFarmaco extends Fragment implements Pagina {

    public static int TITOLO_ID = R.string.titolo_pagina_dettaglio_farmaco;
    final static String ARG_POSITION = "position";
    int mCurrentPosition = 0;

    ScrollView scroller = null;
    public final static int SCROLL_Y = 50;
    private ListView listView = null;
    private PaginatoreSingolo parent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        Activity activity = getActivity();
        setParent((PaginatoreSingolo)activity);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pagina_dettaglio_farmaco, container, false);
        scroller = (ScrollView) view;

        listView = (ListView) view.findViewById(R.id.listaConfezioni);
        Utility.disableListViewTouch(listView);
        updateListaConfezioni();
        ((PaginatoreSingolo)activity).setActualPage(this);

        ViewGroup infos = (ViewGroup) view.findViewById(R.id.lista_info_farmaco);
        infos.addView(new FarmacoInfoView(getContext(), (((MainActivity)activity).getConfezione().getAsInfoList())));
        //infos.setAdapter(new ItemListaInfoFarmacoAdapter(getContext(), );
        //Utility.disableListViewTouch(infos);
        //Utility.updateListConfezioniHeight(infos);

        view.findViewById(R.id.link_bugiardino).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                apriBugiardino(v);
            }
        });
        return view;
    }

    private void updateListaConfezioni(){
        ListaConfezioni lc = null;
        Activity activity = getActivity();
        try {
            lc = MainActivity.getDBManager().ricercaPerAIC(
                    ((MainActivity)activity).getConfezione().getAic()
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(lc == null || lc.size()==0){
            PaginaListaHome farmaci = new PaginaListaHome();
            ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction().add(R.id.main_container, farmaci).commit();
        }

        ItemListaConfezioniAdapter adapter = (ItemListaConfezioniAdapter) listView.getAdapter();
        if(adapter == null){
            adapter = new ItemListaConfezioniAdapter(getContext(), lc);
            listView.setAdapter(adapter);
        }else {
            adapter.setNewData(lc);
            listView.invalidateViews();
        }
        Utility.updateListConfezioniHeight(listView);
    }



    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((MainActivity)getActivity()).removeConfezione();
    }

    public void updateArticleView(int position) {
        TextView article = (TextView) getActivity().findViewById(R.id.intestazione_scheda_dettaglio);
        article.setText(((MainActivity) getActivity()).getConfezione().getNome());
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    @Override
    public void onLeftButtonClick(View v, Bundle info) {
        final Confezione c = new Confezione(((MainActivity)getActivity()).getConfezione());
        new ScadenzaDialog(
                getContext(),
                new MyOnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, Calendar data) {
                        c.setScadenza(data);
                        MainActivity.getDBManager().aggiungiConfezione(c);
                        updateListaConfezioni();
                    }

                    @Override
                    public void onDateError(DatePicker view, Calendar data) {
                        Log.w("date error:", "errore data");
                    }
                }
        );
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
     * richiede ad Android per l'apertura del link al bugiardino online
     * direttamente dal sito ufficiale AIFA
     * @param v     la view che effettua la richiesta di apertura del bugiardino
     */
    public void apriBugiardino(View v) {
        MainActivity activity = (MainActivity)getActivity();
        if(Utility.isNetworkAvailable(getContext())){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = activity.getConfezione().getLinkFogliettoIllustrativo();
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } else {
            activity.visualizzaDialog(R.string.msg_fallimento_apertura_bugiardino_titolo, R.string.msg_nessuna_connessione_internet);
        }
    }

    @Override
    public String updateTitolo(TextView v) {
        String titolo = getString(TITOLO_ID);
        v.setText(titolo);
        return titolo;
    }

    @Override
    public void onHomeClickedListener() {
        parent.visualizzaMessaggio("premuto home da scheda dettagliata");
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