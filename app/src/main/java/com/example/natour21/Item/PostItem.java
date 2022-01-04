package com.example.natour21.Item;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

public class PostItem {

    private String Titolo;
    private String Descrizione;
    private String Minuti;
    double Lat1;
    double Lat2;
    double Lon1;
    double Lon2;
    int Review;
    int Id;
    private String Difficoltà;
    private String Durata;
    private String Startpoint;


    public PostItem(String descr, String min, String tit, double lat1, double lat2, double lon1, double lon2,  int id,String difficoltà, String durata, String startpoint){

        Titolo=tit;
        Descrizione=descr;
        Minuti=min;
        Lat1=lat1;
        Lat2=lat2;
        Lon1=lon1;
        Lon2=lon2;
        Id=id;
        Difficoltà=difficoltà;
        Durata=durata;
        Startpoint=startpoint;


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

    public double getLat1(){return Lat1;}

    public double getLat2() {
        return Lat2;
    }

    public double getLon1() {
        return Lon1;
    }

    public double getLon2() {
        return Lon2;
    }



    public int getId(){return Id;}

    public String getDifficoltà(){return Difficoltà;}

    public String getDurata(){return Durata;}

    public String getStartpoint(){return Startpoint;}
}
