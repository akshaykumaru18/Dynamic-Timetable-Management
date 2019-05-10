package com.example.akshay.timetable888.modules;

public class SubjectSyllabus {

    private String chapterName;
    private String chapterContents;
    private String Reference_Book;

    public SubjectSyllabus(String chapterName, String chapterContents, String reference_Book) {
        this.chapterName = chapterName;
        this.chapterContents = chapterContents;
        Reference_Book = reference_Book;
    }

    public SubjectSyllabus() {

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
}
