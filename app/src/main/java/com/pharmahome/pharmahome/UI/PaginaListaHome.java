package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.pharmahome.pharmahome.InsertActivity;
import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.paginatoreInterface.Pagina;
import com.pharmahome.pharmahome.UI.paginatoreInterface.PaginatoreSingolo;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 24/01/16.
 */
public class PaginaListaHome extends ListFragment implements Pagina {

    public static final int TITOLO_ID = R.string.titolo_pagina_lista_home;

    OnFarmacoSelectedListener mCallback;

    private PaginatoreSingolo parent = null;

    private ListView scroller = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        DBController db = new DBController(activity);
        ListaConfezioni lista = null;
        try {
            lista = db.getConfezioniHome();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setParent((PaginatoreSingolo)activity);
        ItemListaHomeAdapter adapter = null;
        if(lista == null) lista = new ListaConfezioni();
        adapter = new ItemListaHomeAdapter(activity, lista, parent);
        setListAdapter(adapter);
        ((PaginatoreSingolo)activity).setActualPage(this);
        try{
            mCallback = (OnFarmacoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        scroller = getListView();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Confezione item = (Confezione) getListAdapter().getItem(position);
        mCallback.onFarmacoSelected(item);
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
        String titolo = getString(TITOLO_ID);
        v.setText(titolo);
        return titolo;
    }

    @Override
    public int getTitoloId() {
        return TITOLO_ID;
    }

    @Override
    public void onHomeClickedListener() {
        parent.visualizzaMessaggio("premuto home da lista home");
    }

    @Override
    public void setParent(PaginatoreSingolo parent) {
        this.parent = parent;
    }

    @Override
    public PaginatoreSingolo getParent() {
        return parent;
    }
}
