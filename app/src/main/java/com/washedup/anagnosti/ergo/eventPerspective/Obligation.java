package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Obligation implements Parcelable{
    private String date;
    private String what;
    private String where;
    private String details;
    private String obligation_superior;
    private String obligation_subordinate;
    private int status; // 0 - Yes, 1 - No, 2 - Maybe, 3 - pending (jebale_te_3_tacke)

    public Obligation() {
    }

    public Obligation(String date, String what, String where, String details, String obligation_superior, String obligation_subordinate, int status) {
        this.date = date;
        this.what = what;
        this.where = where;
        this.details = details;
        this.obligation_superior = obligation_superior;
        this.obligation_subordinate = obligation_subordinate;
        this.status = status;
    }

    protected Obligation(Parcel in) {
        date = in.readString();
        what = in.readString();
        where = in.readString();
        details = in.readString();
        obligation_superior = in.readString();
        obligation_subordinate = in.readString();
        status = in.readInt();
    }

    public static final Creator<Obligation> CREATOR = new Creator<Obligation>() {
        @Override
        public Obligation createFromParcel(Parcel in) {
            return new Obligation(in);
        }

        @Override
        public Obligation[] newArray(int size) {
            return new Obligation[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getObligation_superior() {
        return obligation_superior;
    }

    public void setObligation_superior(String obligation_superior) {
        this.obligation_superior = obligation_superior;
    }

    public String getObligation_subordinate() {
        return obligation_subordinate;
    }

    public void setObligation_subordinate(String obligation_subordinate) {
        this.obligation_subordinate = obligation_subordinate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(what);
        parcel.writeString(where);
        parcel.writeString(details);
        parcel.writeString(obligation_superior);
        parcel.writeString(obligation_subordinate);
        parcel.writeInt(status);
    }
}
