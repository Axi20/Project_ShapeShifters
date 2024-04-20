package com.grow.shapeshifters.ui.manage_workouts;

public class WorkoutDetail {
    private String clientName;
    private String workoutDate;
    private String workoutNotes;
    private String exerciseName;
    private String exerciseRepetition;
    private String exerciseSet;
    private String exerciseWeight;
    private long workoutId;

    public WorkoutDetail(String clientName, String workoutDate, long workoutId, String workoutNotes,
                         String exerciseName, String exerciseRepetition, String exerciseSet,
                         String exerciseWeight) {
        this.clientName = clientName;
        this.workoutDate = workoutDate;
        this.workoutId = workoutId;
        this.workoutNotes = workoutNotes;
        this.exerciseName = exerciseName;
        this.exerciseRepetition = exerciseRepetition;
        this.exerciseSet = exerciseSet;
        this.exerciseWeight = exerciseWeight;

    }

    public long getWorkoutId() {
        return workoutId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getWorkoutNotes() { return workoutNotes; }

    public String setWorkoutNotes(String workoutNotes) { this.workoutNotes = workoutNotes;
        return workoutNotes;
    }

    public String getExerciseName() { return exerciseName; }

    public String setExerciseName(String exerciseName) { this.exerciseName = exerciseName;
        return exerciseName;
    }

    public String getExerciseRepetition() { return exerciseRepetition; }

    public String setExerciseRepetition(String exerciseRepetition) { this.exerciseRepetition = exerciseRepetition;
        return exerciseRepetition;
    }

    public String getExerciseSet() { return exerciseSet; }

    public String setExerciseSet(String exerciseSet) { this.exerciseSet = exerciseSet;
        return exerciseSet;
    }

    public String getExerciseWeight() { return exerciseWeight; }

    public String setExerciseWeight(String exerciseWeight) { this.exerciseWeight = exerciseWeight;
        return exerciseWeight;
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
}
