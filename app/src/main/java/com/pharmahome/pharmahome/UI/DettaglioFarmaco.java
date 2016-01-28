package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;

public class DettaglioFarmaco extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = 0;
    public final static String[] values = new String[] {
            "12/08/2016",
            "13/08/2016",
            "14/08/2016"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dettaglio_farmaco, container, false);

        ListView lv = (ListView) v.findViewById(R.id.listaConfezioni);
        ListaConfezioni lc = null;
        try {
            lc = MainActivity.getDBManager().ricercaPerAIC(
                    ((MainActivity)getActivity()).getConfezione().getAic()
            );
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ItemListaConfezioniAdapter adapter = new ItemListaConfezioniAdapter(getActivity(), lc);
        lv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(int position) {
        TextView article = (TextView) getActivity().findViewById(R.id.intestazione_scheda_dettaglio);
        article.setText(((MainActivity)getActivity()).getConfezione().getNome());
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}