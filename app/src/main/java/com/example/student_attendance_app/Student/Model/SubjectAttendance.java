package com.example.student_attendance_app.Student.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubjectAttendance {
    @SerializedName("Sections")
    public List<Section> sections;
    @SerializedName("Student")
    private Student student;
    @SerializedName("status")
    private String status;
}
