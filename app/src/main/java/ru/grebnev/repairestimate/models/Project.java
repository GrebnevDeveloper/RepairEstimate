package ru.grebnev.repairestimate.models;


public class Project {

    private long dateProject;
    private float sumProject;
    private String nameProject;

    public Project() {
    }

    public Project(long dateProject, float sumProject, String nameProject) {
        this.dateProject = dateProject;
        this.sumProject = sumProject;
        this.nameProject = nameProject;
    }

    public long getDateProject() {
        return dateProject;
    }

    public float getSumProject() {
        return sumProject;
    }

    public String getNameProject() {
        return nameProject;
    }
}
