package com.example.student_attendance_app.Student.Model;

import java.util.List;

public class AttendanceList {
    private List<String> rollnos;
    private String status;

    public AttendanceList(List<String> rollnos, String status) {
        this.rollnos = rollnos;
        this.status = status;
    }

    public List<String> getRollnos() {
        return rollnos;
    }

    public String getStatus() {
        return status;
    }

    public void setRollnos(List<String> rollnos) {
        this.rollnos = rollnos;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
