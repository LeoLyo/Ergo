package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Role implements Parcelable {

    private String description;
    private ArrayList<String> subordinates;
    private String title;

    public Role() {
    }

    public Role(String description, ArrayList<String> subordinates, String title) {
        this.description = description;
        this.subordinates = subordinates;
        this.title = title;
    }

    protected Role(Parcel in) {
        description = in.readString();
        subordinates = in.createStringArrayList();
        title = in.readString();
    }

    public static final Creator<Role> CREATOR = new Creator<Role>() {
        @Override
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        @Override
        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(ArrayList<String> subordinates) {
        this.subordinates = subordinates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Role{" +
                "description='" + description + '\'' +
                ", subordinates=" + subordinates +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeStringList(subordinates);
        parcel.writeString(title);
    }
}
