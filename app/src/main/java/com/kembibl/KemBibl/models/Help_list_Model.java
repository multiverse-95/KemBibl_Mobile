package com.kembibl.KemBibl.models;

public class Help_list_Model {
    private String question;
    private String answer;
    private String image;



    //конструктор
    public Help_list_Model(String question, String answer, String image){

        this.question = question;
        this.answer = answer;
        this.image = image;
    }

    //getters
    public String getQuestion() {return question; }
    public String getAnswer() {return answer; }
    public String getImage() { return image; }
}
