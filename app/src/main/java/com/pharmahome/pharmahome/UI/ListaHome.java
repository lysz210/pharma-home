package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by ciao on 24/01/16.
 */
public class ListaHome extends ListFragment {
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

        ItemListaHomeAdapter adapter = null;
        try {
            adapter = new ItemListaHomeAdapter(getActivity(), MainActivity.getDBManager().getAllConfezioni());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //String item = (String) getListAdapter().getItem(position);
        // Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_LONG).show();

        mCallback.onFarmacoSelected(position);

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

}
