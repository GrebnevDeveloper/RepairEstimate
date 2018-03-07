package ru.grebnev.repairestimate.models;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Project {

    long dateProject;

    int sumProject;

    @NonNull
    String nameProject;
}
