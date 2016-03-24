package com.museupessoa.maf.assistenteentrevistas.units;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 24/03/2016.
 */
public class InterviewUnit {
    public String name;

    public InterviewUnit(String name) {
        this.name = name;
    }

    static public List<InterviewUnit> getInterviews(String PATH){
        String[] list;
        List<InterviewUnit> interviews = new ArrayList<>();
        File f = new File(PATH+"/Entervistas");
        list = f.list();
        for(int i=0;i<list.length;i++)interviews.add(new InterviewUnit(list[i]));
        return  interviews;
    }

}
