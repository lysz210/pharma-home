package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Created by ciao on 24/01/16.
 */
public class ItemListaConfezioniAdapter extends ArrayAdapter<Confezione> {

    private final Context context;
    private final ListaConfezioni values;
    private int i = 0;
    private int[] fascie = {R.drawable.led_buono, R.drawable.led_in_scadenza, R.drawable.led_scaduto};
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public ItemListaConfezioniAdapter(Context context, ListaConfezioni values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.confezione_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.scadenza);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.semaforo);
        Confezione confezione = getItem(position);
        textView.setText(formatter.format(confezione.getScadenza().getTime()));
        imageView.setImageResource(confezione.getFascia().getIdentificatore());

        // inserimento degli eventi per ciascun bottone
        // ogni bottone riceve un gestore personalizzato
        final Button elimina = (Button) rowView.findViewById(R.id.elimina);
        Button modifica = (Button) rowView.findViewById(R.id.modifica);

        elimina.setOnClickListener(new View.OnClickListener() {
            private int pos = position;
            public void onClick(View v) {
                elimina(v, pos);
            }
        });
        modifica.setOnClickListener(new View.OnClickListener() {
            private int pos = position;
            public void onClick(View v) {
                modifica(v, pos);
            }
        });

        return rowView;
    }


    private void elimina(View view, int pos){
        Toast.makeText(getContext(), "Eliminazione confezione " + pos, Toast.LENGTH_SHORT).show();
        Confezione confezione = (Confezione)getItem(pos);
        MainActivity.getDBManager().eliminaConfezione(confezione);
        remove(confezione);
        notifyDataSetChanged();
    }

    private void modifica(View view, int pos){
        final int p = pos;
        GregorianCalendar oggi = new GregorianCalendar();

        DatePickerDialog d = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String data = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        Confezione c = getItem(p);
                        try {
                            c.setScadenza(data);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        MainActivity.getDBManager().modificaConfezione(c);
                        remove(c);
                        insert(c, p);
                    }
                },
                oggi.get(GregorianCalendar.YEAR),
                oggi.get(GregorianCalendar.MONTH),
                oggi.get(GregorianCalendar.DATE)
        );
        d.show();
    }
}
