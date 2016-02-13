package com.pharmahome.pharmahome.UI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.middleware.Confezione;
import com.pharmahome.pharmahome.core.middleware.ListaConfezioni;

/**
 * Created by ciao on 24/01/16.
 */
public class ItemListaHomeAdapter extends ArrayAdapter<Confezione> {

    private final Context context;
    private final ListaConfezioni values;
    private int i = 0;
    private int[] fascie = {R.drawable.led_scaduto, R.drawable.led_in_scadenza, R.drawable.led_buono};

    public ItemListaHomeAdapter(Context context, ListaConfezioni values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.farmaco_home_item, parent, false);
        TextView nomeFarmaco = (TextView) rowView.findViewById(R.id.nome_farmaco);
        ImageView semaforo = (ImageView) rowView.findViewById(R.id.semaforo);
        Confezione confezione = values.get(position);
        nomeFarmaco.setText(confezione.getNome());
        semaforo.setImageResource(confezione.getFascia().getIdentificatore());
        return rowView;
    }

}
