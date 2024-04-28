package com.grow.shapeshifters.ui.manage_clients;

public class TrainingSlot {
    String dayOfWeek;
    String timeSlot;

    public TrainingSlot(String dayOfWeek, String timeSlot) {
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public String getTimeSlot() {
        return timeSlot;
    }

}
