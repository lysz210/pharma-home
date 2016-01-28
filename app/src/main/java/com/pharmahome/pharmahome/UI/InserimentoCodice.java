package com.pharmahome.pharmahome.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pharmahome.pharmahome.R;

/**
 * Created by ciao on 26/01/16.
 */
public class InserimentoCodice extends Fragment implements Pagina {

    private Button avanti;
    private ImageButton scan;
    private EditText cb;
    private EditText nome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inserimento_codice, container, false);
        Activity activity = getActivity();
        avanti = (Button) view.findViewById(R.id.btn_avanti);
        scan = (ImageButton) view.findViewById(R.id.btn_scan_cb);
        cb = (EditText) view.findViewById(R.id.input_cb);
        nome = (EditText) view.findViewById(R.id.input_nome);

        avanti.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                avanti(v);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        cb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nome.setText("");
                }
            }
        });

        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cb.setText("");
                }
            }
        });

        ((PaginatoreSingolo)activity).setActualPage(this);

        return view;
    }

    // handlers

    private void avanti(View v){
        String tmp_cb = cb.getText().toString();
        String tmp_nome =  nome.getText().toString();
        final InsertActivity activity = (InsertActivity) getActivity();
        if(tmp_cb.length() + tmp_nome.length() == 0) {
            Toast.makeText(getActivity(), getString(R.string.msg_ins_nome_cb), Toast.LENGTH_LONG).show();
            return;
        }
        if(tmp_cb.length() > 0){
            String val = tmp_cb;
            activity.setFarmaco(val);
            avviaSceltaData();
        } else {
            String val = tmp_nome;
            final String[] lista = {
                    "Aspirina",
                    "Tachipirina",
                    "Medicina A",
                    "Medicina B",
                    "Medicina C",
                    "Medicina D",
                    "Medicina E",
                    "Medicina F",
                    "Medicina G",
                    "Medicina H",
                    "Medicina I",
                    "Medicina L",
                    "Medicina M",
                    "Medicina N"
            };
            activity.setListaFarmaci(lista);
            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            b.setTitle("ciao");
            b.setItems(lista, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.setFarmaco(lista[which]);
                    avviaSceltaData();
                }
            });
            AlertDialog a = b.create();
            a.show();
        }


    }

    private void avviaSceltaData(){
        Fragment frag = (Fragment) new SelezioneData();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        frag.setArguments(getActivity().getIntent().getExtras());
        transaction.replace(R.id.main_container, frag);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onLeftButtonClick(View v, Bundle info) {

    }

    @Override
    public void onScrollUp(View v) {

    }

    @Override
    public void onScrollDown(View v) {

    }
}
