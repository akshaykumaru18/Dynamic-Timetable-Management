package com.example.akshay.timetable888.modules;

public class Faculty {

    private String studentName;
    private String lecturerName;
    private String hodName;
    private String phoneNumber;
    private String profile_image;
    private String security_level;
    private String user_id;
    private String emailID;
    private String department;


    public Faculty(String studentName, String lecturerName, String hodName, String phoneNumber,
                   String profile_image, String security_level, String user_id, String emailID, String department) {
        this.studentName = studentName;
        this.lecturerName = lecturerName;
        this.hodName = hodName;
        this.phoneNumber = phoneNumber;
        this.profile_image = profile_image;
        this.security_level = security_level;
        this.user_id = user_id;
        this.emailID = emailID;
        this.department = department;

    }

    public Faculty() {

    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getHodName() {
        return hodName;
    }

    public void setHodName(String hodName) {
        this.hodName = hodName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getSecurity_level() {
        return security_level;
    }

    public void setSecurity_level(String security_level) {
        this.security_level = security_level;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
