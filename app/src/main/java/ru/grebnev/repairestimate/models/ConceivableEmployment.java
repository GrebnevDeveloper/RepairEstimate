package ru.grebnev.repairestimate.models;

public class ConceivableEmployment {

    private boolean selected;
    private String name;
    private String type;

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
}
