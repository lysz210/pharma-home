package com.pharmahome.pharmahome.UI;

import android.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by ciao on 29/01/16.
 */

public class Utility {
    public static void updateListConfezioniHeight(ListView listView){
        // aggiornamento altezza della view
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter != null){
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            int totalHeight = 0;
            View tView = null;
            for(int i=0; i < listAdapter.getCount(); i++){
                tView = listAdapter.getView(i, tView, listView);
                if(i==0){
                    tView.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));
                }
                tView.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += tView.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
            listView.setLayoutParams(params);
        }
    }

    public static void disableListViewTouch(ListView listView){
        listView.setOnTouchListener(new ListView.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }
}
