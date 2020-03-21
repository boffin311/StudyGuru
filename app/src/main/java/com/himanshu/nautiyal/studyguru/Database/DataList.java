package com.himanshu.nautiyal.studyguru.Database;

public class DataList {
    String day;
    String date;
    String totolTime;
    String ratings;

    public void setDay(String day) {
        this.day = day;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotolTime(String totolTime) {
        this.totolTime = totolTime;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTotolTime() {
        return totolTime;
    }

    public String getRatings() {
        return ratings;
    }

    public DataList(String day, String date, String totolTime, String ratings) {
        this.day = day;
        this.date = date;
        this.totolTime = totolTime;
        this.ratings = ratings;
    }

    public DataList() {
    }
}
