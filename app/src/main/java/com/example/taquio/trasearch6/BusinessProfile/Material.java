package com.example.taquio.trasearch6.BusinessProfile;

/**
 * Created by Del Mar on 2/23/2018.
 */

public class Material {

    String name = null;
    boolean selected = false;

    public Material(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
