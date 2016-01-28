package com.pharmahome.pharmahome.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pharmahome.pharmahome.R;

/**
 * Created by ciao on 26/01/16.
 */
public class SelezioneData extends Fragment implements Pagina {

    private Button salva;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selezione_data, container, false);
        InsertActivity activity = (InsertActivity) getActivity();
        ((TextView)view.findViewById(R.id.nome_farmaco)).setText(activity.getFarmaco());
        salva = (Button) view.findViewById(R.id.btn_salva);
        salva.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                salva(v);
            }
        });

        ((PaginatoreSingolo)activity).setActualPage(this);
        return view;
    }

    private void salva(View v){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
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
