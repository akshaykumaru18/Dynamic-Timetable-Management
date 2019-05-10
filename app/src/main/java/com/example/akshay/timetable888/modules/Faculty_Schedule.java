package com.example.akshay.timetable888.modules;

public class Faculty_Schedule {

    private String subject_Name;
    private String slot;
    private String department;
    private String subject;
    private String semester;

    public Faculty_Schedule(String subject_Name, String slot, String department, String subject,String semester) {
        this.subject_Name = subject_Name;
        this.slot = slot;
        this.department = department;
        this.subject = subject;
        this.semester = semester;
    }

    public Faculty_Schedule() {

    }

    public String getSubject_Name() {
        return subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        this.subject_Name = subject_Name;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
