package ru.grebnev.repairestimate.models;


import java.util.List;

public class User {
    private String userId;
    private List<Project> projectList;

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public List<Project> getProjectList() {
        return projectList;
    }
}
