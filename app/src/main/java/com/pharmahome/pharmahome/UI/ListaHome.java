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
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 24/01/16.
 */
public class ListaHome extends ListFragment implements Pagina {

    public static final String TITOLO = "Le tue confezioni";

    OnFarmacoSelectedListener mCallback;

    private PaginatoreSingolo parent = null;

    private ListView scroller = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ListaConfezioni lista = null;
        try {
            lista = MainActivity.getDBManager().getAllConfezioni();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Activity activity = getActivity();
        setParent((PaginatoreSingolo)activity);
        ItemListaHomeAdapter adapter = null;
        adapter = new ItemListaHomeAdapter(activity, lista);
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
        v.setText(TITOLO);
        return TITOLO;
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
