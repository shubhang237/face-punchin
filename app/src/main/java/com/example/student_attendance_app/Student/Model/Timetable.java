package com.example.student_attendance_app.Student.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Timetable {
    @SerializedName("Monday")
    private List<Schedule> Monday;

    @SerializedName("Tuesday")
    private List<Schedule> Tuesday;

    @SerializedName("Wednesday")
    private List<Schedule> Wednesday;

    @SerializedName("Thursday")
    private List<Schedule> Thursday;

    @SerializedName("Friday")
    private List<Schedule> Friday;

    @SerializedName("status")
    private String status;

    public Timetable(List<Schedule> monday, List<Schedule> tuesday, List<Schedule> wednesday, List<Schedule> thursday, List<Schedule> friday, String status) {
        Monday = monday;
        Tuesday = tuesday;
        Wednesday = wednesday;
        Thursday = thursday;
        Friday = friday;
        this.status = status;
    }
    public List<Schedule> getMonday() {
        return Monday;
    }
    public List<Schedule> getTuesday() {
        return Tuesday;
    }
    public List<Schedule> getWednesday() {
        return Wednesday;
    }
    public List<Schedule> getThursday() {
        return Thursday;
    }
    public List<Schedule> getFriday() {
        return Friday;
    }
    public String getStatus() {
        return status;
    }
}
