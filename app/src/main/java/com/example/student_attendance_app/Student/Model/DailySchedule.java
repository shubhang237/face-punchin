package com.example.student_attendance_app.Student.Model;

import java.util.List;

public class DailySchedule {
    private List<Schedule> classes;
    private String date;
    private String status;

    public DailySchedule(List<Schedule> classes, String date, String status) {
        this.classes = classes;
        this.date = date;
        this.status = status;
    }

    public List<Schedule> getClasses() {
        return classes;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
