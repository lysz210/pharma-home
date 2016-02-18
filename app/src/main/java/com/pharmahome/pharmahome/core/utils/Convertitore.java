/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pharmahome.pharmahome.core.utils;
/**
 *
 * @author skari
 */
import static java.lang.Math.pow;

public class Convertitore {
    private int[] vettore= new int[6];
    private String codice;
    private String risultato;
    private boolean errore=false;
   
    
    public Convertitore() {
    }
    
    public Convertitore(String codice){
        this.codice=codice;
        stringa_array(codice);
        risultato=creaNumero();
    }
    
    private void stringa_array (String barcode){
        int i_vector=0;
	int partenza;
        
        if(barcode.length()!=6)
            errore=true;
        else{
            for(int i=0; i<barcode.length();i++){
                char s=barcode.charAt(i);
                    if(s>='0'&&s<='9'){
                            vettore[i_vector]=s-'0';
                    }
                    else{
                            if(s>='B'&&s<='D'){
                                    partenza=10;
                                    vettore[i_vector]=s-'B'+partenza;
                            }
                            else{
                                    partenza=13;
                                    if(s>='F'&&s<='H'){
                                    vettore[i_vector]=s-'F'+partenza;
                                    }
                                    else{
                                            partenza=16;
                                            if(s>='J'&&s<='N'){
                                                    vettore[i_vector]=s-'J'+partenza;
                                            }
                                            else{
                                                    partenza=21;
                                                    if(s>='P'&&s<='Z'){
                                                            vettore[i_vector]=s-'P'+partenza;
                                                    }
                                                    else
                                                        errore=true;
                                            }
                                    }
                            }
                    }
                    i_vector++;
            }
        }
    }
    
    private String creaNumero(){
	long sum=0;
	int exp=0;
	int base=32;
        String aic;
	        
        if(errore)
            aic="";
        else{
            for(int i=vettore.length-1;i>=0;i--){
		sum=(long)(sum+vettore[i]*pow(base,exp));	
		exp++;
            }
            aic=Long.toString(sum);
            do{
                aic="0"+aic;
            }while(aic.length()<9);
            
            if(aic.length()>9)
                aic="";
        }
	
        return aic;
    }
    
      public void stampaBarcode(){
         
          System.out.println(creaNumero());
      }  
      
      
      public String ottieniOutput(){
          return creaNumero();
      }
}
    
    
    

