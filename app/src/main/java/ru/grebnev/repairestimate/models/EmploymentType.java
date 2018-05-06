package ru.grebnev.repairestimate.models;

public class EmploymentType {

    private boolean selected;
    private String name;

    public EmploymentType() {
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
}
