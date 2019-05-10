package com.example.akshay.timetable888.modules;

public class AddNewSubject {

    private String Subject_Name;
    private String Semester;
    private String Department;
    private String Subject_Code;
    private String Number_of_Units;
    private String chapterName;
    private String chapterContents;
    private String Reference_Book;
    private String faculty;
    private String selectedSlot;

    public AddNewSubject(String subject_Name, String semester, String subject_Code,
                         String number_of_Units, String chapterName, String reference_Book, String Department,
                         String faculty,String selectedSlot) {
        Subject_Name = subject_Name;
        Semester = semester;
        this.Department = Department;
        Subject_Code = subject_Code;
        Number_of_Units = number_of_Units;
        this.chapterName = chapterName;
        Reference_Book = reference_Book;
        this.faculty = faculty;
        this.selectedSlot = selectedSlot;
    }

    public AddNewSubject() {

    }

    public String getSubject_Name() {
        return Subject_Name;
    }

    public void setSubject_Name(String subject_Name) {
        Subject_Name = subject_Name;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getSubject_Code() {
        return Subject_Code;
    }

    public void setSubject_Code(String subject_Code) {
        Subject_Code = subject_Code;
    }

    public String getNumber_of_Units() {
        return Number_of_Units;
    }

    public void setNumber_of_Units(String number_of_Units) {
        Number_of_Units = number_of_Units;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterContents() {
        return chapterContents;
    }

    public void setChapterContents(String chapterContents) {
        this.chapterContents = chapterContents;
    }

    public String getReference_Book() {
        return Reference_Book;
    }

    public void setReference_Book(String reference_Book) {
        Reference_Book = reference_Book;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getSelectedSlot() {
        return selectedSlot;
    }

    public void setSelectedSlot(String selectedSlot) {
        this.selectedSlot = selectedSlot;
    }
}
