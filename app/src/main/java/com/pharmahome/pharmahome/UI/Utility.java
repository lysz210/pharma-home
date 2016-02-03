package com.pharmahome.pharmahome.UI;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ciao on 29/01/16.
 */

public class Utility {
    /**
     * Ridimensione l'altezza della listView in base al suo contenuto
     * utilizzato principalmente in situazioni dove il sistema
     * di renderizzazione automatico fallisce
     * @param   listView    elemento che deve essere riadattato in base al suo contenuto
     */
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

    /**
     * disattiva i touchEvents dall listView e disabilita la scrollbar
     * @param listView listView su cui disattivare i touchEvent
     */
    public static void disableListViewTouch(ListView listView){
        listView.setOnTouchListener(new ListView.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        listView.setVerticalScrollBarEnabled(false);
    }

    /**
     * predicato che verifica lo stato della connessione a internet.
     * @param context   context su cui viene effettuato la richiesca per il recupero del ConnectivityManager
     * @return  true    se la il dispositivo e' connesso a internet
     *          false   altrimenti
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * esegue la copia di un file in assets dentro a un file dei dati sui data in locate
     * @param context   context da utilizzare per recuperare gli assets e il package name
     * @param asset     path relativo della risorsa da copiare
     * @param tmpFile   eventuale nome temporaneo per il file
     * @return          il nome del file temporaneo se la copia ha avuto successo
     *                  null altrimenti
     */
    public static String cpAsset2Data(Context context, String asset, String tmpFile){
        String result = null;
        if(asset == null || asset.length() < 1){
            return result;
        }
        if(tmpFile == null || tmpFile.length() < 1){
            tmpFile = "tmp_file.tmp";
        }
        try {
            InputStream tmpfile =  context.getAssets().open(asset);
            String tmppath = "/data/data/" + context.getPackageName() + "/" + tmpFile;
            OutputStream tmpdest = new FileOutputStream(tmppath);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len=tmpfile.read(buffer)) > 0){
                tmpdest.write(buffer);
            }
            tmpfile.close();
            tmpdest.flush();
            tmpdest.close();
            result = tmppath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean rmFile(String path){
        File file = new File(path);
        return file.delete();
    }
}
