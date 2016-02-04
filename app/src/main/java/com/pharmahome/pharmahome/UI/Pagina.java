package com.pharmahome.pharmahome.UI;

import android.widget.TextView;

/**
 * Created by ciao on 28/01/16.
 */
public interface Pagina extends MenuSecondarioController, OnHomeClickedListener, ParentProvider {
    public String updateTitolo(TextView v);
}
