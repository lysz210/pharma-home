package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 24/01/16.
 */
public class ListaHome extends ListFragment implements Pagina {
    OnFarmacoSelectedListener mCallback;

    public final static String[] values = new String[] {
            "Aspirina",
            "Tachipirina",
            "Medicina A",
            "Medicina B",
            "Medicina C",
            "Medicina D",
            "Medicina E",
            "Medicina F",
            "Medicina G",
            "Medicina H",
            "Medicina I",
            "Medicina L",
            "Medicina M",
            "Medicina N"
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListaConfezioni lista = null;
        try {
            lista = MainActivity.getDBManager().getAllConfezioni();
            if(lista.size() == 0){
                String[] mock = new String[] {
                        "12/08/2016",
                        "13/08/2016",
                        "14/08/2016"
                };
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Activity activity = getActivity();
        ItemListaHomeAdapter adapter = null;
        adapter = new ItemListaHomeAdapter(activity, lista);
        setListAdapter(adapter);
        ((PaginatoreSingolo)activity).setActualPage(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Confezione item = (Confezione) getListAdapter().getItem(position);
        // Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_LONG).show();

        mCallback.onFarmacoSelected(item);

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            mCallback = (OnFarmacoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onAggiungiConfezione(View v, Bundle info) {
        return;
    }

    @Override
    public void onScrollUp(View v) {

    }

    @Override
    public void onScrollDown(View v) {

    }
}
