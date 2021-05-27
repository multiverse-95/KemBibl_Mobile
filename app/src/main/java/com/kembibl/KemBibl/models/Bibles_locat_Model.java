package com.kembibl.KemBibl.models;

public class Bibles_locat_Model {
    //property basics
    private String name_bible;
    private String address_bible;
    private String image;



    // конструктор
    public Bibles_locat_Model(String name_bible, String address_bible, String image){

        this.name_bible = name_bible;
        this.address_bible = address_bible;
        this.image = image;
    }

    //getters
    public String getName_bible() {return name_bible; }
    public String getAddress_bible() {return address_bible; }
    public String getImage() { return image; }
}
