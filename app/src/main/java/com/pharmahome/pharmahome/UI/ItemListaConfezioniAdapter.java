package com.pharmahome.pharmahome.UI;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.paginatoreInterface.MessageCarrier;
import com.pharmahome.pharmahome.UI.paginatoreInterface.VisualizzatoreDialog;
import com.pharmahome.pharmahome.UI.paginatoreInterface.listener.MyOnDateSetListener;
import com.pharmahome.pharmahome.core.db.DBController;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;
import com.pharmahome.pharmahome.core.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ciao on 24/01/16.
 */
public class ItemListaConfezioniAdapter extends ArrayAdapter<Confezione> {

    SimpleDateFormat formatter = null;
    private Context context;
    private ListaConfezioni confezioni;
    private int i = 0;
    private MessageCarrier messenger = null;

    public ItemListaConfezioniAdapter(Context context, ListaConfezioni values, MessageCarrier messenger) {
        super(context, -1, values);
        formatter = Utility.getDateFormatter(context);
        this.context = context;
        this.confezioni = values;
        this.messenger = messenger;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup viewGroup = parent;
        View rowView = inflater.inflate(R.layout.confezione_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.scadenza);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.semaforo);
        Confezione confezione = getItem(position);
        textView.setText(formatter.format(confezione.getScadenza().getTime()));
        imageView.setImageResource(confezione.getFascia().getIdentificatore());

        // inserimento degli eventi per ciascun bottone
        // ogni bottone riceve un gestore personalizzato
        final ImageButton elimina = (ImageButton) rowView.findViewById(R.id.elimina);
        ImageButton modifica = (ImageButton) rowView.findViewById(R.id.modifica);

        elimina.setOnClickListener(new View.OnClickListener() {
            private int pos = position;

            public void onClick(View v) {
                elimina(v, pos, viewGroup);
            }
        });
        modifica.setOnClickListener(new View.OnClickListener() {
            private int pos = position;

            public void onClick(View v) {
                modifica(v, pos);
            }
        });

        if(confezione.isNuovaConfezione()){
            rowView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.evidenziato_nuova_confezione));
        }

        return rowView;
    }


    private void elimina(View view, int pos, ViewGroup viewGroup) {
        Toast.makeText(getContext(), "Eliminazione confezione " + pos, Toast.LENGTH_SHORT).show();
        Confezione confezione = getItem(pos);
        new DBController(context).eliminaConfezione(confezione);
        remove(confezione);
        notifyDataSetChanged();
        Utility.updateListConfezioniHeight((ListView) viewGroup);
    }

    private void modifica(View view, int pos) {
        final int p = pos;
        new ScadenzaDialog(
                getContext(),
                new MyOnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, Calendar data) {
                        DBController db = new DBController(context);
                        Confezione c = getItem(p);
                        c.setScadenza(data);
                        c.setNuovaConfezione();
                        db.modificaConfezione(c);
                        remove(c);
                        insert(c, p);
                    }

                    @Override
                    public void onDateError(DatePicker view, Calendar data) {
                        VisualizzatoreDialog.HandlerBuilder builder = new VisualizzatoreDialog.HandlerBuilder();
                        builder.setNeutralButton(new VisualizzatoreDialog.OnClickListenerValue(R.string.btn_chiudi));
                        messenger.visualizzaDialog(R.string.dialog_scadenza_data_non_valida_titolo, R.string.dialog_scadenza_data_non_valida_contenuto, builder.create());
                    }
                }, messenger
        );
    }

    public void setNewData(ListaConfezioni lc) {
        int len;
        while (getCount() > 0) {
            remove(getItem(0));
        }
        notifyDataSetChanged();
        confezioni = lc;
        len = lc.size();
        for (int i = 0; i < len; i++) {
            insert(lc.get(i), i);
        }
    }
}
