package com.pharmahome.pharmahome.UI.paginatoreInterface.listener;

import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ciao on 13/02/16.
 */
public interface MyOnDateSetListener {
    void onDateSet(DatePicker view, Calendar date);

    void onDateError(DatePicker view, Calendar date);
}
