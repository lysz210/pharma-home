package com.pharmahome.pharmahome.core.util;

import com.pharmahome.pharmahome.core.middleware.Farmaco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;


/**
 * Created by ciao on 05/12/15.
 */
public class PharmaIterator implements Iterator<Farmaco>, Iterable<Farmaco> {

    public static final String SRC_DOMAIN = "http://89.46.196.54";
    public static final String SRC_BASE = "pharmahome";
    public static final String SRC_DATA_LIST = "lista_farmaci.json";

    private JSONArray farmaci;
    private int nextIndex = 0;

    private String base_domain = PharmaIterator.SRC_DOMAIN + "/" + PharmaIterator.SRC_BASE;

    public PharmaIterator(){
        try {
            farmaci = new JSONArray(getJsonToken(base_domain + "/" + SRC_DATA_LIST));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getJsonToken(String url){
        StringBuilder tstr = null;
        try {
            URL tmp = new URL(url);
            InputStream is = tmp.openStream();

            //System.out.println("########################################3a");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            tstr = new StringBuilder();
            String tdata;
            while((tdata = reader.readLine()) != null){
                //System.out.println(tdata);
                tstr.append(tdata);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(tstr.toString());
        return tstr.toString();
    }

    @Override
    public boolean hasNext() {
        // TODO Auto-generated method stub
        return nextIndex < farmaci.length();
    }

    @Override
    public Farmaco next() {
        if(!hasNext()){
            return null;
        }
        Farmaco f = null;
        try {
            f = new Farmaco(new JSONObject(getJsonToken(base_domain + "/" + farmaci.get(nextIndex))));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        nextIndex += 1;
        return f;
    }
    @Override
    public void remove(){
        return;
    }

    public Iterator<Farmaco> iterator(){
        return this;
    }

    public static void main(String args[]) throws JSONException{
        PharmaIterator a = new PharmaIterator();
        for(Farmaco f: a)
            System.out.println(f);
    }
}