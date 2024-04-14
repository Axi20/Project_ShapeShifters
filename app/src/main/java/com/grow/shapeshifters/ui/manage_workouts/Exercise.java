package com.grow.shapeshifters.ui.manage_workouts;

import androidx.annotation.NonNull;

public class Exercise {
    private String name;
    private long id;
    private int repetitions;
    private int sets;
    private Float weight;

    public Exercise(String name, int repetitions, int sets, Float weight) {
        this.name = name;
        this.repetitions = repetitions;
        this.sets = sets;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPersonalTrainingId() {
        return id;
    }

    public void setPersonalTrainingId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
