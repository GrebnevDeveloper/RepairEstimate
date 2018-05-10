package ru.grebnev.repairestimate.models;

import java.util.List;

public class ConceivableEmployment {

    private boolean selected;
    private String name;
    private String type;

    private List<String> volumes;

    public ConceivableEmployment() {
    }

    public ConceivableEmployment(String name) {
        this.selected = false;
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public List<String> getVolumes() {
        return volumes;
    }

    public void setVolumes(List volumes) {
        this.volumes = volumes;
    }
}
