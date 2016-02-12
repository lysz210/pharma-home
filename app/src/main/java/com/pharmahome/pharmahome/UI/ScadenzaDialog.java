package com.pharmahome.pharmahome.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

import com.pharmahome.pharmahome.R;

import java.util.Calendar;

/**
 * Created by ciao on 03/02/16.
 */
public class ScadenzaDialog extends DatePickerDialog {

    private View customTitolo = null;
    public ScadenzaDialog(Context context, OnDateSetListener onDateSetListener){
        super(context, onDateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        Log.d("Dialog", "creazione dialog");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            // getDatePicker().setMinDate(oggi.getTimeInMillis());
        }
        customTitolo = getLayoutInflater().inflate(R.layout.titolo_nuova_confezione_calendario, null);
        setCustomTitle(customTitolo);
        show();
    }

    @Override
    public View onCreatePanelView(int featureId){
        Log.d("Dialog", "" + featureId);
        View v = super.onCreatePanelView(featureId);
        switch(featureId){
            case Window.FEATURE_CUSTOM_TITLE:
                v = null;
        }
        return v;
    }
}
