package com.cloudbees.lifxnotify.lifxnotifier;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import static com.cloudbees.lifxnotify.lifxnotifier.Colors.*;

class StringToColorParser {

    private static final Map<String, Color> COLORS = new HashMap<>();

    static {
        COLORS.put(BLUE, Color.BLUE);
        COLORS.put(CYAN, Color.CYAN);
        COLORS.put(GREEN, Color.GREEN);
        COLORS.put(ORANGE, Color.ORANGE);
        COLORS.put(PINK, Color.PINK);
        COLORS.put(RED, Color.RED);
        COLORS.put(YELLOW, Color.YELLOW);
        COLORS.put(WHITE, Color.WHITE);
    }

    /**
     * Parses {@link LifxColor} to {@link java.awt.Color}.
     * @throws NoSuchColorException if color isn't recognized
     */
    public static Color parse(LifxColor lifxColor) throws NoSuchColorException {
        if (lifxColor.isCustom()) {
            return Color.decode("#" + lifxColor.getColor());
        }
        Color color = COLORS.get(lifxColor.getColor());
        if (color == null) {
            throw new NoSuchColorException();
        }
        return color;
    }
}
