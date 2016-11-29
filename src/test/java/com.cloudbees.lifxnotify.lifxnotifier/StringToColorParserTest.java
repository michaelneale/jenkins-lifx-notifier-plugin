package com.cloudbees.lifxnotify.lifxnotifier;

import java.awt.Color;
import org.junit.Test;
import org.jvnet.hudson.test.WithoutJenkins;

import static org.junit.Assert.assertEquals;

public class StringToColorParserTest {

    @Test(expected = NoSuchColorException.class)
    @WithoutJenkins
    public void parse_shouldThrowExceptionIfColorNotFound() throws Exception {
        StringToColorParser.parse(new LifxColor("noSuchColor", false));
    }

    @Test
    @WithoutJenkins
    public void parse_shouldParseAwtColor() throws Exception {
        LifxColor blue = new LifxColor(Colors.BLUE, false);
        LifxColor cyan = new LifxColor(Colors.CYAN, false);
        LifxColor green = new LifxColor(Colors.GREEN, false);
        LifxColor orange = new LifxColor(Colors.ORANGE, false);
        LifxColor pink = new LifxColor(Colors.PINK, false);
        LifxColor red = new LifxColor(Colors.RED, false);
        LifxColor yellow = new LifxColor(Colors.YELLOW, false);
        LifxColor white = new LifxColor(Colors.WHITE, false);

        assertEquals(Color.BLUE, StringToColorParser.parse(blue));
        assertEquals(Color.CYAN, StringToColorParser.parse(cyan));
        assertEquals(Color.GREEN, StringToColorParser.parse(green));
        assertEquals(Color.ORANGE, StringToColorParser.parse(orange));
        assertEquals(Color.PINK, StringToColorParser.parse(pink));
        assertEquals(Color.RED, StringToColorParser.parse(red));
        assertEquals(Color.YELLOW, StringToColorParser.parse(yellow));
        assertEquals(Color.WHITE, StringToColorParser.parse(white));
    }

    @Test
    @WithoutJenkins
    public void parse_shouldParseCustomColor() throws Exception {
        LifxColor custom1 = new LifxColor("ff00ff", true); // 255, 0, 255
        LifxColor custom2 = new LifxColor("dd4355", true); // 221, 67, 85

        Color color1 = StringToColorParser.parse(custom1);
        Color color2 = StringToColorParser.parse(custom2);

        assertEquals(255, color1.getRed());
        assertEquals(0, color1.getGreen());
        assertEquals(255, color1.getBlue());
        assertEquals(221, color2.getRed());
        assertEquals(67, color2.getGreen());
        assertEquals(85, color2.getBlue());
    }
}
