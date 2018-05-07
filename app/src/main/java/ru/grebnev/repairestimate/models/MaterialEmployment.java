package ru.grebnev.repairestimate.models;

public class MaterialEmployment {

    private boolean selected;

    private String name;

    private String employment;

    private String unit;

    private float volumeOfUnit;

    private float price;

    public MaterialEmployment() {
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

    public String getEmployment() {
        return employment;
    }
}
