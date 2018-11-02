package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Day implements Parcelable {

    private Timestamp date;
    private String time_end;
    private String time_start;

    public Day(){

    }

    public Day(Timestamp date, String time_end, String time_start) {
        this.date = date;
        this.time_end = time_end;
        this.time_start = time_start;
    }

    protected Day(Parcel in) {
        date = in.readParcelable(Timestamp.class.getClassLoader());
        time_end = in.readString();
        time_start = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    @Override
    public String toString() {
        return "Day{" +
                "date=" + date +
                ", time_end='" + time_end + '\'' +
                ", time_start='" + time_start + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(date, i);
        parcel.writeString(time_end);
        parcel.writeString(time_start);
    }
}
