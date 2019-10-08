package com.example.student_attendance_app.Student.Model;

public class Teacher {
    String designation;
    TokenData.Userdata.Infodata user;

    public Teacher(String designation, TokenData.Userdata.Infodata teacherData) {
        this.designation = designation;
        this.user = teacherData;
    }

    public String getDesignation() {
        return designation;
    }

    public TokenData.Userdata.Infodata getTeacherData() {
        return user;
    }

}