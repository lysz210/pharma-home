package com.pharmahome.pharmahome.UI;

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
import android.widget.ListView;
import android.widget.TextView;

import com.pharmahome.pharmahome.InsertActivity;
import com.pharmahome.pharmahome.MainActivity;
import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ciao on 26/01/16.
 */
public class SelezioneData extends Fragment implements Pagina {

    public final static String TITOLO = "Seleziona la data di scadenza";

    private Button salva;
    private ImageButton modifica;
    private TextView scadenzaView = null;
    private Calendar scadenza = null;
    private void setScadenza(int anno, int mese, int giorno){
        scadenza = new GregorianCalendar(anno, mese, giorno);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selezione_data, container, false);
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

        ((PaginatoreSingolo)activity).setActualPage(this);

        ListView infos = (ListView) view.findViewById(R.id.lista_info_farmaco);
        infos.setAdapter(new ItemListaInfoFarmacoAdapter(getContext(), (activity.getFarmaco().getAsInfoList())));
        Utility.disableListViewTouch(infos);
        Utility.updateListConfezioniHeight(infos);

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

    }

    @Override
    public void onScrollDown(View v) {

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
            new GregorianCalendar(),
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
        v.setText(TITOLO);
        return TITOLO;
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
            activity.visualizzaMessaggio("Apertura bugiarnido fallita! Non e' disponibile nessuna connessione a internet.");
        }
    }
}
