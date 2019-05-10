package com.example.akshay.timetable888.modules;

public class Faculty_Names {

    private String Faculty_name;
    private String user_id;

    public Faculty_Names(String faculty_name, String user_id) {
        Faculty_name = faculty_name;
        this.user_id = user_id;
    }
    public Faculty_Names() {

    }

    public String getFaculty_name() {
        return Faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        Faculty_name = faculty_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
