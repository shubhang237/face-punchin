package com.example.student_attendance_app.Student.Model;


public class Section {
    private Subject subject;
    private Teacher teacher;
    private String slot;
    private Integer total;
    private Integer present;

    public Section(Subject subject,String slot, Integer total, Integer present) {
        this.subject = subject;
        this.slot = slot;
        this.total = total;
        this.present = present;
    }

    public Section(Subject subject, Teacher teacher, String slot) {
        this.subject = subject;
        this.teacher = teacher;
        this.slot = slot;
        this.total = 0;
        this.present = 0;
    }

    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getSlot() {
        return slot;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPresent() {
        return present;
    }

}




