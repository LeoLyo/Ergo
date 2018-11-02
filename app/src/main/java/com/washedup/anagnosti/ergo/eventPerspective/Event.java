package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;

public class Event implements Parcelable {

    private String event_id;
    private String event_name;
    private String organiser_name;
    private String description_of_event;
    private String location_coordinates; //GeoPoint
    private String location_address;
    private String location_name;
    private String date_start; //Date
    private String date_end; //Date
    private ArrayList<String> invited_users;
    private ArrayList<String> accepted_users;
    private String event_image_url;
    private ArrayList<Day> days;
    private ArrayList<Role> roles;
    private ArrayList<Person> people;

    /**
     * Neophodan prazan konstruktor.
     */

    public Event() {

    }

    public Event(String event_id, String event_name, String event_image_url){
        this.event_id=event_id;
        this.event_name=event_name;
        this.event_image_url=event_image_url;
    }

    public Event(String event_id, String event_name, String organiser_name, String description_of_event, String location_coordinates, String location_address, String location_name, String date_start, String date_end, ArrayList<String> invited_users, ArrayList<String> accepted_users, String event_image_url, ArrayList<Day> days, ArrayList<Role> roles, ArrayList<Person> people){
        this.event_id=event_id;
        this.event_name=event_name;
        this.organiser_name=organiser_name;
        this.description_of_event=description_of_event;
        this.location_coordinates=location_coordinates;
        this.location_address=location_address;
        this.location_name=location_name;
        this.date_start = date_start;
        this.date_end = date_end;
        this.invited_users=invited_users;
        this.accepted_users = accepted_users;
        this.event_image_url=event_image_url;
        this.days = days;
        this.roles = roles;
        this.people = people;
    }


    protected Event(Parcel in) {
        event_id = in.readString();
        event_name = in.readString();
        organiser_name = in.readString();
        description_of_event = in.readString();
        location_coordinates = in.readString();
        location_address = in.readString();
        location_name = in.readString();
        date_start = in.readString();
        date_end = in.readString();
        invited_users = in.createStringArrayList();
        accepted_users = in.createStringArrayList();
        event_image_url = in.readString();
        days = in.createTypedArrayList(Day.CREATOR);
        roles = in.createTypedArrayList(Role.CREATOR);
        people = in.createTypedArrayList(Person.CREATOR);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
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

    public ArrayList<String> getInvited_users() {
        return invited_users;
    }

    public void setInvited_users(ArrayList<String> invited_users) {
        this.invited_users = invited_users;
    }

    public String getEvent_image_url() {
        return event_image_url;
    }

    public void setEvent_image_url(String event_image_url) {
        this.event_image_url = event_image_url;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        this.people = people;
    }
    public ArrayList<String> getAccepted_users() {
        return accepted_users;
    }

    public void setAccepted_users(ArrayList<String> accepted_users) {
        this.accepted_users = accepted_users;
    }

    public String getDate_start() {
        return date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event_id='" + event_id + '\'' +
                ", event_name='" + event_name + '\'' +
                ", organiser_name='" + organiser_name + '\'' +
                ", description_of_event='" + description_of_event + '\'' +
                ", location_coordinates='" + location_coordinates + '\'' +
                ", location_address='" + location_address + '\'' +
                ", location_name='" + location_name + '\'' +
                ", date_start='" + date_start + '\'' +
                ", date_end='" + date_end + '\'' +
                ", invited_users=" + invited_users +
                ", accepted_users=" + accepted_users +
                ", event_image_url='" + event_image_url + '\'' +
                ", days=" + days +
                ", roles=" + roles +
                ", people=" + people +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(event_id);
        parcel.writeString(event_name);
        parcel.writeString(organiser_name);
        parcel.writeString(description_of_event);
        parcel.writeString(location_coordinates);
        parcel.writeString(location_address);
        parcel.writeString(location_name);
        parcel.writeString(date_start);
        parcel.writeString(date_end);
        parcel.writeStringList(invited_users);
        parcel.writeStringList(accepted_users);
        parcel.writeString(event_image_url);
        parcel.writeTypedList(days);
        parcel.writeTypedList(roles);
        parcel.writeTypedList(people);
    }
}
