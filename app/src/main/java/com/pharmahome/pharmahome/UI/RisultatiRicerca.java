package com.pharmahome.pharmahome.UI;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pharmahome.pharmahome.InsertActivity;
import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 24/01/16.
 */
public class RisultatiRicerca extends Fragment implements Pagina {

    public static final String TITOLO = "Risultati ricerca";

    public static final String KEY_Q = "search_query";

    private LayoutInflater inflater = null;
    private ViewGroup risultatiContainer = null;

    private ListaConfezioni tutteLeConfezioni = new ListaConfezioni();

    private DBController db = null;

    OnFarmacoSelectedListener mCallback;
    private PaginatoreSingolo parent = null;


    private ScrollView scroller = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result, container, false);
        scroller = (ScrollView) view;

        this.inflater = inflater;
        this.risultatiContainer = (ViewGroup) view.findViewById(R.id.contenitore_risultati_ricerca);
        MainActivity activity = (MainActivity) getActivity();
        setParent(activity);

        try{
            mCallback = (OnFarmacoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

        db = activity.getDBManager();

        mCallback = activity;

        activity.setActualPage(this);

        String q = getArguments().getString(KEY_Q);
        if(q == null || q.length() < 1){
            activity.getSupportFragmentManager().popBackStack();
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.main_container);
            if(currentFragment != null){
                transaction.remove(currentFragment);
            }
            transaction.commit();
        }
        iniziaSequenzaRicerca(q);
        if(tutteLeConfezioni.size() == 0){
            inflater.inflate(R.layout.nessun_risultato, (ViewGroup) view.findViewById(R.id.contenitore_risultati_ricerca), true);
        }
        return view;
    }

    private void iniziaSequenzaRicerca(String q){
        cercaPerAic(q);
    }

    private void cercaPerAic(String q){
        ListaConfezioni lista = null;
        try {
            lista = db.ricercaPerAIC(q);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(lista != null && lista.size() > 0) {
            tutteLeConfezioni.addAll(lista);
            ItemListaHomeAdapter adapter = new ItemListaHomeAdapter(getContext(), lista);
            addListaRisultati(R.layout.search_result_aic, adapter);
        }
        cercaPerNome(q);
    }

    private void cercaPerNome(String q){
        ListaConfezioni lista = null;
        try {
            lista = db.ricercaPerNome(q);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(Confezione c: tutteLeConfezioni){
            lista.remove(c);
        }
        if(lista != null && lista.size() > 0) {
            tutteLeConfezioni.addAll(lista);
            ItemListaHomeAdapter adapter = new ItemListaHomeAdapter(getContext(), lista);
            addListaRisultati(R.layout.search_result_nome, adapter);
        }
        cercaPerPrincipioAttivo(q);
    }

    private void cercaPerPrincipioAttivo(String q){
        ListaConfezioni lista = null;
        try {
            lista = db.ricercaPerPA(q);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(Confezione c: tutteLeConfezioni){
            lista.remove(c);
        }
        if(lista != null && lista.size() > 0) {
            tutteLeConfezioni.addAll(lista);
            ItemListaHomeAdapter adapter = new ItemListaHomeAdapter(getContext(), lista);
            addListaRisultati(R.layout.search_result_principio_attivo, adapter);
        }
         cercaPerSintomo(q);
    }

    private void cercaPerSintomo(String q){
        ListaConfezioni lista = null;
        try {
            lista = db.ricercaPerPA(q);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(Confezione c: tutteLeConfezioni){
            lista.remove(c);
        }
        if(lista != null && lista.size() > 0) {
            tutteLeConfezioni.addAll(lista);
            ItemListaHomeAdapter adapter = new ItemListaHomeAdapter(getContext(), lista);
            addListaRisultati(R.layout.search_result_principio_attivo, adapter);
        }
    }

    private void addListaRisultati(int src, ItemListaHomeAdapter adapter){
        LinearLayout view = (LinearLayout) inflater.inflate(src, risultatiContainer, true);
        ListView lv = new ListView(getContext());//(ListView)view.findViewById(R.id.lista_risultati);
        lv.setAdapter(adapter);
        view.addView(lv);
        Utility.disableListViewTouch(lv);
        Utility.updateListConfezioniHeight(lv, R.id.nome_farmaco);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Confezione item = (Confezione) parent.getAdapter().getItem(position);
                mCallback.onFarmacoSelected(item);
            }
        });
    }

    @Override
    public void onLeftButtonClick(View v, Bundle info) {
        Intent i = new Intent(getActivity(), InsertActivity.class);
        getActivity().startActivity(i);
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

    @Override
    public String updateTitolo(TextView v) {
        v.setText(TITOLO);
        return TITOLO;
    }

    @Override
    public void onHomeClickedListener() {
        parent.visualizzaMessaggio("home click da risultati ricerca");
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
