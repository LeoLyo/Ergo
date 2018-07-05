package com.washedup.anagnosti.ergo.createEvent;

import java.util.ArrayList;

public class CERole {
    private String name, description;
    private long uniqueId;
    private ArrayList<CERole> subordinates;
    private boolean isChecked;

    public CERole(String name, long uniqueId){
        this.name = name;
        this.uniqueId = uniqueId;
        this.description="";
        this.isChecked=false;
    }

    public String getAllSubordinatesInString(){
        String sSubs="";
        if(subordinates.size()==0){
            return "No subordinates.";
        }
        else if(subordinates.size()==1){
            return subordinates.get(subordinates.size()-1).getName()+".";
        }
        for(int i=0;i<subordinates.size()-1;i++){
            sSubs+=sSubs+subordinates.get(i).getName()+", ";
        }
        sSubs+=subordinates.get(subordinates.size()-1).getName()+".";
        return sSubs;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ArrayList<CERole> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(ArrayList<CERole> subordinates) {
        this.subordinates = subordinates;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void removeSubordinates(){
        subordinates.clear();
    }

    @Override
    public String toString() {
        return name;
    }
}
