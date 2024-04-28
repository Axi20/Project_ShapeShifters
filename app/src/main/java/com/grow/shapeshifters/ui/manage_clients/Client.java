package com.grow.shapeshifters.ui.manage_clients;

import androidx.annotation.NonNull;

import java.util.List;

public class Client {
    private long id;
    private String name;
    private String birthday;
    private String phoneNumber;
    private String level;
    private String goals;
    private String startDate;
    private String weight;
    private String notes;

    private List<TrainingSlot> trainingSlots;

    public Client() {
    }

    public List<TrainingSlot> getTrainingSlots() {
        return trainingSlots;
    }

    public void setTrainingSlots(List<TrainingSlot> trainingSlots) {
        this.trainingSlots = trainingSlots;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFitnessLevel() {
        return level;
    }

    public String getFitnessGoal() {
        return goals;
    }

    public String getMembershipStartDate() {
        return startDate;
    }

    public String getWeight() {
        return weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setDateOfBirth(String birthday) {
        this.birthday = birthday;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFitnessLevel(String level) {
        this.level = level;
    }

    public void setFitnessGoal(String goals) {
        this.goals = goals;
    }

    public void setMembershipStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
