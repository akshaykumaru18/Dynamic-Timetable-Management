package com.example.akshay.timetable888.modules;

public class DepartmentInfo {

    private String DepartmentName;
    private String HodName;

    public DepartmentInfo(String departmentName, String hodName) {
        DepartmentName = departmentName;
        HodName = hodName;
    }

    public DepartmentInfo() {

    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getHodName() {
        return HodName;
    }

    public void setHodName(String hodName) {
        HodName = hodName;
    }
}
