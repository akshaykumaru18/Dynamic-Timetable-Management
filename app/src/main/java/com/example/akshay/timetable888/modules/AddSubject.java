package com.example.akshay.timetable888.modules;

public class AddSubject {

    private String subject_name;
    private String Subject_code;
    private String Number_of_Units;
    private String faculty;
    private String Semester;
    private String selectedSlot;



    public AddSubject(String subject_name, String subject_code, String Number_of_Units, String faculty, String semester, String selectedSlot) {
        this.subject_name = subject_name;
        Subject_code = subject_code;
        this.Number_of_Units = Number_of_Units;
        this.faculty = faculty;
        this.Semester = semester;
        this.selectedSlot = selectedSlot;

    }

    public AddSubject() {

    }

    public String getsubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return Subject_code;
    }

    public void setSubject_code(String subject_code) {
        Subject_code = subject_code;
    }

    public String getNumber_of_Units() {
        return Number_of_Units;
    }

    public void setNumber_of_Units(String number_of_Units) {
        this.Number_of_Units = number_of_Units;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        this.Semester = semester;
    }

    public String getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(String selectedSlot) {
        this.selectedSlot = selectedSlot;
    }
}
