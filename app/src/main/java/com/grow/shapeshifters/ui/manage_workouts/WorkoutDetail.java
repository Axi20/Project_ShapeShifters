package com.grow.shapeshifters.ui.manage_workouts;

public class WorkoutDetail {
    private String clientName;
    private String workoutDate;

    public WorkoutDetail(String clientName, String workoutDate) {
        this.clientName = clientName;
        this.workoutDate = workoutDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(String workoutDate) {
        this.workoutDate = workoutDate;
    }

    @Override
    public String toString() {
        return workoutDate + " - " + clientName;
    }
}
