package com.example.wareneinaus;

import java.util.Date;

public class anlieferer {

    public Date datum;
    public String name;
    public String art;
    public String absender;
    public String inhalt;
    public String[] fotos;

    public anlieferer(){
        datum=new Date();
        name="";
        art="";
        absender="";
        inhalt="";
        fotos=new String[]{};
    }
}
