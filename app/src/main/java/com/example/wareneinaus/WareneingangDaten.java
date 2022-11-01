package com.example.wareneinaus;

import java.util.Date;

public class WareneingangDaten {

    public Date datum;
    public String name;
    public String art;
    public String absender;
    public String inhalt;
    public String email;
    public String fotos;

    public WareneingangDaten(){
        datum=new Date();
        name="";
        art="";
        absender="";
        inhalt="";
        fotos="";
        email="";
    }
}
