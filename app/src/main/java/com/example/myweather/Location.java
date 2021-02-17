package com.example.myweather;

public class Location {
    private boolean tomorrowID;
    private String nameID;
    private boolean todayID;

    public Location(boolean tomorrowID, String nameID, boolean todayID) {
        this.tomorrowID = tomorrowID;
        this.nameID = nameID;
        this.todayID = todayID;
    }

    public boolean getTomorrowID() {
        return tomorrowID;
    }

    public void setTomorrowID(boolean tomorrowID) {
        this.tomorrowID = tomorrowID;
    }

    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public boolean getTodayID() {
        return todayID;
    }

    public void setTodayID(boolean todayID) {
        this.todayID = todayID;
    }
}
