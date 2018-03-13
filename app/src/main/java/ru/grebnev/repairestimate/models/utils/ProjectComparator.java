package ru.grebnev.repairestimate.models.utils;

import java.util.Comparator;

import ru.grebnev.repairestimate.models.Project;

public class ProjectComparator implements Comparator<Project> {
    @Override
    public int compare(Project project1, Project project2) {
        return Long.compare(project1.getDateProject(), project2.getDateProject());
    }
}
