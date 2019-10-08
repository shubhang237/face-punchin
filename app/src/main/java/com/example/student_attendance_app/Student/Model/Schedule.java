package com.example.student_attendance_app.Student.Model;

public class Schedule {
    private Lab lab;
    private Section section;
    private String day;
    private String startTime;
    private String endTime;
    private String location;
    private String category;

    public Schedule(Lab lab,Section section, String day, String startTime, String endTime, String location,String category) {
        this.lab = lab;
        this.section = section;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.category = category;
    }

    public Section getSection() {
        return section;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

    public Lab getLab() {
        return lab;
    }

    public String getCategory() {
        return category;
    }
}
