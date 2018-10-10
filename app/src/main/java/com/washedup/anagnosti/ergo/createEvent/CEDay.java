package com.washedup.anagnosti.ergo.createEvent;

import java.util.Date;

public class CEDay {

    private Date date;
    private String timeStart;
    private String timeEnd;
    private boolean isFilled;

    public CEDay() {
        this.date = new Date();
        this.timeStart = "00:00";
        this.timeEnd = "00:00";
        this.isFilled = false;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", timeStart: " + timeStart +", timeEnd: " + timeEnd;
    }
}
