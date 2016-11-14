package com.cloudbees.lifxnotify.lifxnotifier;

import hudson.model.Run;
import hudson.model.TaskListener;
import org.junit.Test;
import org.jvnet.hudson.test.WithoutJenkins;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LifxColorTest {

    @Test
    @WithoutJenkins
    public void isValid_shouldReturnTrueForValidColor() throws Exception {
        String valid1 = "4e00fe";
        String valid2 = "000000";
        String valid3 = "ffffff";

        assertTrue(LifxColor.isValid(valid1));
        assertTrue(LifxColor.isValid(valid2));
        assertTrue(LifxColor.isValid(valid3));
    }

    @Test
    @WithoutJenkins
    public void isValid_shouldReturnFalseForNotValidColor() throws Exception {
        String tooShort = "fff";
        String tooLong = "aa000000";
        String notAllowedCharacters = "fffftt";

        assertFalse(LifxColor.isValid(tooShort));
        assertFalse(LifxColor.isValid(tooLong));
        assertFalse(LifxColor.isValid(notAllowedCharacters));
    }


}