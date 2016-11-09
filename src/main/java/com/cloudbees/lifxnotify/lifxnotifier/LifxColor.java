package com.cloudbees.lifxnotify.lifxnotifier;


public class LifxColor {
    private String color;
    private boolean isCustom;

    public static final LifxColor NO_COLOR = new LifxColor("", false);
    /**
     * @param isCustom true if this color should be interpreted in RRGGBB format
     */
    public LifxColor(String color, boolean isCustom) {
        this.color = color;
        this.isCustom = isCustom;
    }

    public String getColor() {
        return color;
    }

    public boolean isCustom() {
        return isCustom;
    }
}
