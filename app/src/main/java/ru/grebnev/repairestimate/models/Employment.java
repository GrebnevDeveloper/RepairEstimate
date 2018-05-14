package ru.grebnev.repairestimate.models;

import java.util.List;

public class Employment {

    private String name;
    private float cost;

    private List<Float> volumes;
    private List<String> materials;
    private List<String> services;

    public Employment() {
    }

    public Employment(String name, List<Float> volumes) {
        this.name = name;
        this.volumes = volumes;
        this.cost = 0.0f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public List<Float> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Float> volumes) {
        this.volumes = volumes;
    }
}
