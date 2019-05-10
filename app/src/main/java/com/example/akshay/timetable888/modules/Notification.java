package com.example.akshay.timetable888.modules;

public class Notification {

    private String FacultyName;
    private String user_id;
    private String From;
    private String To;
    private String Message;
    private String Date;
    private String Time;
    private String Token_id;
    private String Current_id;

    public Notification(String facultyName, String user_id, String from, String to, String message, String date, String time, String token_id, String current_id) {
        FacultyName = facultyName;
        this.user_id = user_id;
        From = from;
        To = to;
        Message = message;
        Date = date;
        Time = time;
        Token_id = token_id;
        Current_id = current_id;
    }



    public Notification() {

    }

    public String getFacultyName() {
        return FacultyName;
    }

    public void setFacultyName(String facultyName) {
        FacultyName = facultyName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getToken_id() {
        return Token_id;
    }

    public void setToken_id(String token_id) {
        Token_id = token_id;
    }

    public String getCurrent_id() {
        return Current_id;
    }

    public void setCurrent_id(String current_id) {
        Current_id = current_id;
    }
}
