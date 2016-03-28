package com.museupessoa.maf.assistenteentrevistas.units;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProjectUnit {
    public String name;

    public ProjectUnit(String name) {
        this.name = name;
    }

    static public List<ProjectUnit> getProjects(String PATH){
        String[] list;
        List<ProjectUnit> projects = new ArrayList<>();;
        File f = new File(PATH+"/Projetos");
        list = f.list();
        for(int i=0;i<list.length;i++)projects.add(new ProjectUnit(list[i].substring(0,list[i].length()-4)));
        return  projects;
    }

    static  public boolean deleteProject(String nameProject, String PATH){
        File f = new File(PATH+"/Projetos/"+nameProject+".xml");
        return f.delete();
    }

    static public  boolean ifExists(String name,List<ProjectUnit> projectUnits){
        for (int x=0;x<projectUnits.size();x++)
            if(projectUnits.get(x).name.equals(name))return true;
        return  false;
    }

}
