package com.pharmahome.pharmahome.UI;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;
import com.pharmahome.pharmahome.UI.paginatoreInterface.MessageCarrier;
import com.pharmahome.pharmahome.UI.paginatoreInterface.listener.MyOnDateSetListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ciao on 03/02/16.
 */
public class ScadenzaDialog extends DatePickerDialog {

    private static boolean dataValida = true;
    private View customTitolo = null;
    private MessageCarrier messenger = null;

    public ScadenzaDialog(final Context context, final MyOnDateSetListener onDateSetListener, MessageCarrier messenger) {
        super(context,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (dataValida)
                            onDateSetListener.onDateSet(view, new GregorianCalendar(year, monthOfYear, dayOfMonth));
                        else
                            onDateSetListener.onDateError(view, new GregorianCalendar(year, monthOfYear, dayOfMonth));
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1);
        Log.d("Dialog", "creazione dialog");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // getDatePicker().setMinDate(oggi.getTimeInMillis());
        }
        dataValida = true;
        customTitolo = getLayoutInflater().inflate(R.layout.titolo_nuova_confezione_calendario, null);
        setCustomTitle(customTitolo);
        this.messenger = messenger;
        show();
    }

    @Override
    public View onCreatePanelView(int featureId) {
        Log.d("Dialog", "" + featureId);
        View v = super.onCreatePanelView(featureId);
        switch (featureId) {
            case Window.FEATURE_CUSTOM_TITLE:
                v = null;
        }
        return v;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        Log.w("Clicke: ", "" + day);
        boolean dataValida = false;
        Calendar oggi = Calendar.getInstance();
        GregorianCalendar oggi2 = new GregorianCalendar(oggi.get(Calendar.YEAR), oggi.get(Calendar.MONTH), oggi.get(Calendar.DAY_OF_MONTH));
        GregorianCalendar scadenza = new GregorianCalendar(year, month, day);
        switch (scadenza.compareTo(oggi2)) {
            case 1:
                dataValida = true;
                break;
            case 0:
            case -1:
                dataValida = false;
                Toast.makeText(getContext(), R.string.msg_data_inserita_non_valida, Toast.LENGTH_SHORT).show();
                break;
        }
        this.dataValida = dataValida;
    }
}
