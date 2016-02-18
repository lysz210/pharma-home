package com.pharmahome.pharmahome.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.paginatoreInterface.MessageCarrier;
import com.pharmahome.pharmahome.core.middleware.Farmaco;

import java.util.ArrayList;

/**
 * Created by ciao on 24/01/16.
 */
public class ItemListaInfoFarmacoAdapter extends ArrayAdapter<Farmaco.Info> {

    private final Context context;
    private final ArrayList<Farmaco.Info> values;
    private int i = 0;
    private MessageCarrier messenger = null;

    public ItemListaInfoFarmacoAdapter(Context context, ArrayList<Farmaco.Info> values, MessageCarrier messenger) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.messenger = messenger;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.farmaco_info_item, parent, false);
        TextView descrizione = (TextView) rowView.findViewById(R.id.descrizione);
        TextView voce = (TextView) rowView.findViewById(R.id.voce);
        Farmaco.Info info = values.get(position);
        descrizione.setText(info.descrizione());
        voce.setText(info.voce());
        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
