package com.kembibl.KemBibl.models;

public class Book_Exempl_Model {
    private String address_book;
    private String fond_book;
    private String count_book;
    private String image;



    //конструктор
    public Book_Exempl_Model(String address_book, String fond_book, String count_book, String image){

        this.address_book = address_book;
        this.fond_book = fond_book;
        this.count_book = count_book;
        this.image = image;
    }

    //getters
    public String getAddress_book() {return address_book; }
    public String getFond_book() {return fond_book; }
    public String getCount_book() {return count_book; }
    public String getImage() { return image; }
}
