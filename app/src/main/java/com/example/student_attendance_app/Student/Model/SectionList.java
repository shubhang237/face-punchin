package com.example.student_attendance_app.Student.Model;

import java.util.List;

public class SectionList {
    private String status;
    private Section Section;
    private List<Student> Students;

    public SectionList(Section section, List<Student> students,String status) {
        this.Section = section;
        this.Students = students;
        this.status = status;
    }
    public Section getSection() {
        return Section;
    }
    public List<Student> getStudents() {
        return Students;
    }
    public String getStatus() { return status; }
}
