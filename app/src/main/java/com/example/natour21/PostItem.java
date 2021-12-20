package com.example.natour21;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

public class PostItem {

    private String Titolo;
    private String Descrizione;
    private String Minuti;
    private Double Lat1;
    private Double Lat2;
    private Double Lon1;
    private Double Lon2;



    public PostItem(String descr, String min, String tit, double lat1, double lat2, double lon1, double lon2){

        Titolo=tit;
        Descrizione=descr;
        Minuti=min;
        Lat1=lat1;
        Lat2=lat2;
        Lon1=lon1;
        Lon2=lon2;

    }









    public String getTitolo(){
        return Titolo;
    }

    public String getDescrizione(){
        return Descrizione;
    }

    public String getMinuti(){
        return Minuti;
    }

    public double getLat1(){
        return Lat1;}

    public double getLat2(){return Lat2;}

    public double getLon1(){return Lon1;}

    public Double getLon2() {
        return Lon2;
    }
}
