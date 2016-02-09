package com.pharmahome.pharmahome.UI.paginatoreInterface;

import android.widget.TextView;

import com.pharmahome.pharmahome.UI.paginatoreInterface.listener.OnHomeClickedListener;

/**
 * Created by ciao on 28/01/16.
 */
public interface Pagina extends MenuSecondarioController, OnHomeClickedListener, ParentProvider {
    String updateTitolo(TextView v);
}
