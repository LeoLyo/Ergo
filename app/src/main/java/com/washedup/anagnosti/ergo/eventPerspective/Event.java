package com.washedup.anagnosti.ergo.eventPerspective;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class Event {

    private String event_id;
    private String event_name;
    private String organiser_name;
    private String description_of_event;
    private String location_coordinates;
    private String location_address;
    private String location_name;
    private ArrayList<String> emails_of_people;
    private String current_event_image;
    private ArrayList<Day> days;
    private ArrayList<Role> roles;
    private ArrayList<Person> people;

    public Event(String event_id, String event_name, String current_event_image){
        this.event_id=event_id;
        this.event_name=event_name;
        this.current_event_image=current_event_image;
    }

    public Event(String event_id, String event_name,String organiser_name, String description_of_event, String location_coordinates, String location_address, String location_name, ArrayList<String> emails_of_people, String current_event_image){
        this.event_id=event_id;
        this.event_name=event_name;
        this.organiser_name=organiser_name;
        this.description_of_event=description_of_event;
        this.location_coordinates=location_coordinates;
        this.location_address=location_address;
        this.location_name=location_name;
        this.emails_of_people=emails_of_people;
        this.current_event_image=current_event_image;
    }

    @Exclude
    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getOrganiser_name() {
        return organiser_name;
    }

    public void setOrganiser_name(String organiser_name) {
        this.organiser_name = organiser_name;
    }

    public String getDescription_of_event() {
        return description_of_event;
    }

    public void setDescription_of_event(String description_of_event) {
        this.description_of_event = description_of_event;
    }

    public String getLocation_coordinates() {
        return location_coordinates;
    }

    public void setLocation_coordinates(String location_coordinates) {
        this.location_coordinates = location_coordinates;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public ArrayList<String> getEmails_of_people() {
        return emails_of_people;
    }

    public void setEmails_of_people(ArrayList<String> emails_of_people) {
        this.emails_of_people = emails_of_people;
    }

    public String getCurrent_event_image() {
        return current_event_image;
    }

    public void setCurrent_event_image(String current_event_image) {
        this.current_event_image = current_event_image;
    }
}
