package ru.grebnev.repairestimate.models;

import java.util.List;

public class MaterialEmployment {

    private boolean selected;
    private String name;
    private String employment;
    private String unit;

    private List<Float> volumesOfUnit;

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

    public String getUnit() {
        return unit;
    }

    public List<Float> getVolumesOfUnit() {
        return volumesOfUnit;
    }

    public void setVolumesOfUnit(List<Float> volumesOfUnit) {
        this.volumesOfUnit = volumesOfUnit;
    }

    public float getPrice() {
        return price;
    }
}
