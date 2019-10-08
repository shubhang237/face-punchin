package com.example.student_attendance_app.Student.Model;

public class Student {
    private String rollno;
    private TokenData.Userdata.Infodata user;

    public Student(String rollno, TokenData.Userdata.Infodata studentData) {
        this.rollno = rollno;
        this.user = studentData;
    }

    public String getRollno() {
        return rollno;
    }
    public TokenData.Userdata.Infodata getStudentData() {
        return user;
    }
}
