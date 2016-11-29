package com.cloudbees.lifxnotify.lifxnotifier;

import java.util.regex.Pattern;

public class LifxColor {
    public static final String PATTERN_RRGGBB = "^[0-9a-f]{6}$";
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

    public static boolean isValid(String color) {
        return Pattern.compile(PATTERN_RRGGBB).matcher(color).matches();
    }
}
