package com.museupessoa.maf.assistenteentrevistas.units;


import java.util.List;

public class AudioUnit {
    public  String question;
    public  List<String> audio;
    public  List<Integer> time;

    public AudioUnit(String question, List<String> audio,List<Integer> time){
        this.question=question;
        this.audio = audio;
        this.time = time;
    }

}
