package com.pharmahome.pharmahome.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.core.middleware.Farmaco;

import java.util.ArrayList;

/**
 * Created by ciao on 03/02/16.
 * layout personalizzato per per la creazione della lista informazioni relativo al farmaco
 * Semplifica la gestione rispetto ad avere una ListView d amodificare ogni volta
 * che presenta molti problemi di ridemensionamento all'interno della ScrollView
 */
public class FarmacoInfoView extends LinearLayout {
    public FarmacoInfoView(Context context, ArrayList<Farmaco.Info> infos) {
        super(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setOrientation(VERTICAL);
        View tmpview = null;
        for (Farmaco.Info i : infos) {
            tmpview = LayoutInflater.from(context).inflate(R.layout.farmaco_info_item, null);
            ((TextView) tmpview.findViewById(R.id.voce)).setText(i.voce());
            ((TextView) tmpview.findViewById(R.id.descrizione)).setText(i.descrizione());
            addView(tmpview);
        }
    }
}
