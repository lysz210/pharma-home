package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import org.json.JSONException;

import java.text.ParseException;
import java.util.GregorianCalendar;

public class DettaglioFarmaco extends Fragment implements Pagina {

    public final static String TITOLO = "Scheda dettagliata";
    final static String ARG_POSITION = "position";
    int mCurrentPosition = 0;

    ScrollView scroller = null;
    private ListView listView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        Activity activity = getActivity();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dettaglio_farmaco, container, false);

        listView = (ListView) v.findViewById(R.id.listaConfezioni);
        Utility.disableListViewTouch(listView);
        updateListaConfezioni();
        ((PaginatoreSingolo)activity).setActualPage(this);

        ListView infos = (ListView) v.findViewById(R.id.lista_info_farmaco);
        infos.setAdapter(new ItemListaInfoFarmacoAdapter(getContext(), (((MainActivity)activity).getConfezione().getAsInfoList())));
        Utility.disableListViewTouch(infos);
        Utility.updateListConfezioniHeight(infos);

        ((Button)v.findViewById(R.id.link_bugiardino)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                apriBugiardino(v);
            }
        });
        return v;
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
            ListaHome farmaci = new ListaHome();
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
        GregorianCalendar oggi = new GregorianCalendar();
        final Confezione c = new Confezione(((MainActivity)getActivity()).getConfezione());
        DatePickerDialog d = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        try {
                            c.setScadenza(data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        MainActivity.getDBManager().aggiungiConfezione(c);
                        updateListaConfezioni();
                    }
                },
                oggi.get(GregorianCalendar.YEAR),
                oggi.get(GregorianCalendar.MONTH),
                oggi.get(GregorianCalendar.DATE)
        );
        d.setTitle("Modifica scadenza");
        d.show();

    }

    @Override
    public void onScrollUp(View v) {

    }

    @Override
    public void onScrollDown(View v) {

    }

    public void apriBugiardino(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MainActivity activity = (MainActivity)getActivity();
        String url = activity.getConfezione().getLinkFogliettoIllustrativo();
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    @Override
    public String updateTitolo(TextView v) {
        v.setText(TITOLO);
        return TITOLO;
    }
}