package com.example.student_attendance_app.Student.Model;

public class Lab {

    String slot;
    Subject subject;
    Teacher teacher;

    public Lab(String slot, Subject subject, Teacher teacher) {
        this.slot = slot;
        this.subject = subject;
        this.teacher = teacher;
    }

    public Lab(String slot, Subject subject) {
        this.slot = slot;
        this.subject = subject;
    }

    public String getSlot() {
        return slot;
    }

    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
