package com.example.student_attendance_app.Student.Model;

import java.util.List;

public class PostAttendance {

    private String date;
    private List<String> rollnos;
    private boolean lab;
    private String slot;
    private String subslot;

    public PostAttendance(String date, List<String> rollnos,String slot,boolean lab,String subslot) {
        this.date = date;
        this.rollnos = rollnos;
        this.slot = slot;
        this.lab = lab;
        this.subslot = subslot;
    }

    public PostAttendance(String date, List<String> rollnos,String slot,boolean lab) {
        this.date = date;
        this.rollnos = rollnos;
        this.slot = slot;
        this.lab = lab;
    }

    public String getDate() {
        return date;
    }

    public List<String> getRollnos() {
        return rollnos;
    }

    public boolean isLab() {
        return lab;
    }

    public String getSubslot() {
        return subslot;
    }

    public String getSlot() {
        return slot;
    }
}
