package ru.grebnev.repairestimate.models;

public class Employment {

    private String name;

    private float volumeM3;

    private float volumeM2;

    private float cost;

    public Employment() {
    }

    public Employment(String name, float volumeM3, float volumeM2) {
        this.name = name;
        this.volumeM3 = volumeM3;
        this.volumeM2 = volumeM2;
        this.cost = 0.0f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVolumeM3() {
        return volumeM3;
    }

    public void setVolumeM3(float volumeM3) {
        this.volumeM3 = volumeM3;
    }

    public float getVolumeM2() {
        return volumeM2;
    }

    public void setVolumeM2(float volumeM2) {
        this.volumeM2 = volumeM2;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
