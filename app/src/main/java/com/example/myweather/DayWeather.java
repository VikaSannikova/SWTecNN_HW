package com.example.myweather;

public class DayWeather {
    private String dateID;
    private int picID;
    private int tempID;

    public DayWeather(String date, int pic, int temp) {
        this.dateID = date;
        this.picID = pic;
        this.tempID = temp;
    }

    public String getDateID() {
        return dateID;
    }

    public void setDateID(String dateID) {
        this.dateID = dateID;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public int getPicID() {
        return picID;
    }

    public void setPicID(int picID) {
        this.picID = picID;
    }
}
