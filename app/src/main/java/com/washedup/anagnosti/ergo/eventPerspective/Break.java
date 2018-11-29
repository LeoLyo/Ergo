package com.washedup.anagnosti.ergo.eventPerspective;

import android.os.Parcel;
import android.os.Parcelable;

public class Break implements Parcelable {

    private String break_id;
    private long endOfBreakTime;
    private String duration;
    private String break_superior;
    private String break_subordinate;
    private int is_breaking; // 0 - pending, 1 - onBreak, 2 - finishedBreak, 3 - declinedBreak;

    public Break() {
    }

    public Break(String break_id, long endOfBreakTime, String duration, String break_superior, String break_subordinate) {
        this.break_id = break_id;
        this.endOfBreakTime = endOfBreakTime;
        this.duration = duration;
        this.break_superior = break_superior;
        this.break_subordinate = break_subordinate;
        this.is_breaking = 0;
    }

    protected Break(Parcel in) {
        break_id = in.readString();
        endOfBreakTime = in.readLong();
        duration = in.readString();
        break_superior = in.readString();
        break_subordinate = in.readString();
        is_breaking = in.readInt();
    }

    public static final Creator<Break> CREATOR = new Creator<Break>() {
        @Override
        public Break createFromParcel(Parcel in) {
            return new Break(in);
        }

        @Override
        public Break[] newArray(int size) {
            return new Break[size];
        }
    };

    public String getBreak_id() {
        return break_id;
    }

    public void setBreak_id(String break_id) {
        this.break_id = break_id;
    }

    public long getEndOfBreakTime() {
        return endOfBreakTime;
    }

    public void setEndOfBreakTime(long endOfBreakTime) {
        this.endOfBreakTime = endOfBreakTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBreak_superior() {
        return break_superior;
    }

    public void setBreak_superior(String break_superior) {
        this.break_superior = break_superior;
    }

    public String getBreak_subordinate() {
        return break_subordinate;
    }

    public void setBreak_subordinate(String break_subordinate) {
        this.break_subordinate = break_subordinate;
    }

    public int getIs_breaking() {
        return is_breaking;
    }

    public void setIs_breaking(int is_breaking) {
        this.is_breaking = is_breaking;
    }

    @Override
    public String toString() {
        return "Break{" +
                "break_id='" + break_id + '\'' +
                ", endOfBreakTime='" + endOfBreakTime + '\'' +
                ", duration='" + duration + '\'' +
                ", break_superior='" + break_superior + '\'' +
                ", break_subordinate='" + break_subordinate + '\'' +
                ", is_breaking=" + is_breaking +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(break_id);
        parcel.writeLong(endOfBreakTime);
        parcel.writeString(duration);
        parcel.writeString(break_superior);
        parcel.writeString(break_subordinate);
        parcel.writeInt(is_breaking);
    }
}
