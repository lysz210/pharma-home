package com.pharmahome.pharmahome.UI;

import android.content.DialogInterface;
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
import com.pharmahome.pharmahome.UI.paginatoreInterface.VisualizzatoreDialog;
import com.pharmahome.pharmahome.UI.paginatoreInterface.listener.MyOnDateSetListener;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.Farmaco;
import com.pharmahome.pharmahome.core.utils.Utility;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ciao on 26/01/16.
 */
public class PaginaSelezioneData extends Fragment implements Pagina {

    public final static int TITOLO_ID = R.string.titolo_pagina_selezione_data;
    public final static String KEY_FARMACO_AIC = "PaginaSelezioneData.KEY_FARMACO_AIC";

    private Button salva;
    private ImageButton modifica;
    private TextView scadenzaView = null;
    private Calendar scadenza = null;
    private PaginatoreSingolo parent = null;
    private ScrollView scroller = null;

    private void setScadenza(int anno, int mese, int giorno) {
        scadenza = new GregorianCalendar(anno, mese, giorno);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pagina_selezione_data, container, false);
        scroller = (ScrollView) view;
        InsertActivity activity = (InsertActivity) getActivity();
        ((TextView) view.findViewById(R.id.nome_farmaco)).setText(farmaco.getNome());
        salva = (Button) view.findViewById(R.id.btn_salva);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                VisualizzatoreDialog.HandlerBuilder builder = new VisualizzatoreDialog.HandlerBuilder();
                builder.setNegativeButton(new VisualizzatoreDialog.OnClickListenerValue(R.string.btn_chiudi));
                builder.setPositiveButton(new VisualizzatoreDialog.OnClickListenerValue(R.string.btn_salva,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                salva(v);
                            }
                        }
                ));
                parent.visualizzaDialog(R.string.dialog_scadenza_data_salva_titolo, R.string.dialog_scadenza_data_salva_contenuto, builder.create());
            }
        });

        modifica = (ImageButton) view.findViewById(R.id.modifica);
        modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selezionaScadenza(scadenzaView);
            }
        });

        scadenzaView = (TextView) view.findViewById(R.id.scadenza);
        setParent(activity);
        parent.setActualPage(this);

        ViewGroup infos = (ViewGroup) view.findViewById(R.id.lista_info_farmaco);
        infos.addView(new FarmacoInfoView(getContext(), (farmaco.getAsInfoList())));

        view.findViewById(R.id.link_bugiardino).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                apriBugiardino(v);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (scadenza == null)
            selezionaScadenza(scadenzaView);
    }

    private void salva(View v) {
        if (!verificaDataScadenza()) return;
        Confezione confezione = new Confezione(farmaco);
        confezione.setScadenza(scadenza);
        confezione.setNuovaConfezione();
        (new DBController(getContext())).aggiungiConfezione(confezione);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(KEY_FARMACO_AIC, confezione.getAic());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
     *
     * @param view textview dove va inserito la data selezionata
     */
    private void selezionaScadenza(TextView view) {
        // TODO DA IMPLEMENTARE LA SELEZIONE IN MODO TALE CHE SIA IMPOSTOTA ALLA DATA SCELTA COME PRIMO GIORNO
        final TextView v = view;
        new ScadenzaDialog(
                getContext(),
                new MyOnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, Calendar data) {
                        scadenza = data;
                        v.setText(Utility.getDateFormatter(getContext()).format(data.getTime()));
                    }

                    @Override
                    public void onDateError(DatePicker view, Calendar data) {
                        VisualizzatoreDialog.HandlerBuilder builder = new VisualizzatoreDialog.HandlerBuilder();
                        builder.setNeutralButton(new VisualizzatoreDialog.OnClickListenerValue(R.string.btn_chiudi));
                        parent.visualizzaDialog(R.string.dialog_scadenza_data_non_valida_titolo, R.string.dialog_scadenza_data_non_valida_contenuto, builder.create());
                    }
                }, parent
        );
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

    private boolean verificaDataScadenza() {
        boolean dataValida = false;
        if (scadenza == null) {
            parent.visualizzaDialog(R.string.notifica_scadenza_vuoto_titolo, R.string.notifica_scadenza_vuoto_contenuto);
            return false;
        }
        Calendar oggi = Calendar.getInstance();
        GregorianCalendar oggi2 = new GregorianCalendar(oggi.get(Calendar.YEAR), oggi.get(Calendar.MONTH), oggi.get(Calendar.DAY_OF_MONTH));
        switch (scadenza.compareTo(oggi2)) {
            case 1:
                dataValida = true;
                break;
            case 0:
            case -1:
                dataValida = false;
                parent.visualizzaDialog(R.string.notifica_scadenza_error_titolo, R.string.notifica_scadenza_error_contenuto);
                break;
        }
        return dataValida;
    }

    /**
     * richiede ad Android per l'apertura del link al bugiardino online
     * direttamente dal sito ufficiale AIFA
     *
     * @param v la view che effettua la richiesta di apertura del bugiardino
     */
    public void apriBugiardino(View v) {
        InsertActivity activity = (InsertActivity) getActivity();
        if (Utility.isNetworkAvailable(getContext())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = farmaco.getLinkFogliettoIllustrativo();
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } else {
            activity.visualizzaDialog(R.string.msg_fallimento_apertura_bugiardino_titolo, R.string.msg_nessuna_connessione_internet);
        }
    }

    @Override
    public void onHomeClickedListener() {
//        parent.visualizzaMessaggio("Home click da selezione data");
        VisualizzatoreDialog.HandlerBuilder builder = new VisualizzatoreDialog.HandlerBuilder();
        builder.setNegativeButton(new VisualizzatoreDialog.OnClickListenerValue(R.string.btn_no));
        builder.setPositiveButton(new VisualizzatoreDialog.OnClickListenerValue(R.string.btn_si, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        }));
        parent.visualizzaDialog(R.string.notifica_interrompi_sequenza_inserimento_titolo, R.string.notifica_interrompi_sequenza_inserimento_contenuto, builder.create());
    }

    @Override
    public PaginatoreSingolo getParent() {
        return this.parent;
    }

    @Override
    public void setParent(PaginatoreSingolo parent) {
        this.parent = parent;
    }

    private Farmaco farmaco = null;

    public void setFarmaco(Farmaco farmaco){
        this.farmaco = farmaco;
    }
}
