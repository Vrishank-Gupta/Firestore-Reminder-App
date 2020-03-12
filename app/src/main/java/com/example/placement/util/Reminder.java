package com.example.placement.util;

public class Reminder {
    String name, email, date, subject, description, contact;
    int day;
    boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Reminder(String name, String email, String date, String subject, String description, String contact, int day, boolean status) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.subject = subject;
        this.description = description;
        this.contact = contact;
        this.day = day;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    @Override
    public String toString() {
        return "Reminder{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", contact='" + contact + '\'' +
                ", day=" + day +
                ", status=" + status +
                '}';
    }
}
