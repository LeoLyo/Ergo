package com.washedup.anagnosti.ergo.createEvent;

import java.util.ArrayList;

public class CEPerson {

    private String email;
    private CERole roleOfIndividual;
    private ArrayList<CEPerson> subordinates;
    private CEPerson parentOfIndividual;
    private ArrayList<Integer> previousPositions;
    private int depth;

    public CEPerson( String email, CERole roleOfIndividual, int depth, CEPerson parentOfIndividual) {
        this.email = email;
        this.roleOfIndividual = roleOfIndividual;
        this.subordinates = new ArrayList<>();
        this.depth=depth;
        this.previousPositions=new ArrayList<>();
        this.parentOfIndividual=parentOfIndividual;
    }

    public CEPerson( String email, CERole roleOfIndividual, int depth) {
        this.email = email;
        this.roleOfIndividual = roleOfIndividual;
        this.subordinates = new ArrayList<>();
        this.depth=depth;
        this.previousPositions=new ArrayList<>();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CERole getRoleOfIndividual() {
        return roleOfIndividual;
    }

    public void setRoleOfIndividual(CERole roleOfIndividual) {
        this.roleOfIndividual = roleOfIndividual;
    }

    public ArrayList<CEPerson> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(ArrayList<CEPerson> subordinates) {
        this.subordinates = subordinates;
    }

    public CEPerson getParentOfIndividual() {
        return parentOfIndividual;
    }

    public void setParentOfIndividual(CEPerson parentOfIndividual) {
        this.parentOfIndividual = parentOfIndividual;
    }

    public ArrayList<Integer> getPreviousPositions() {
        return previousPositions;
    }

    public void setPreviousPositions(ArrayList<Integer> previousPositions) {
        this.previousPositions = previousPositions;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return email;
    }

}
