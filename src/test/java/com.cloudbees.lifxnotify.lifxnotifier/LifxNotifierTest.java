package com.cloudbees.lifxnotify.lifxnotifier;

import com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.LifxNotifierInProgress;
import hudson.model.TaskListener;
import org.junit.Test;
import org.jvnet.hudson.test.WithoutJenkins;

import static com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.GROUP_COLOR_FAILURE;
import static com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.GROUP_COLOR_FAILURE_CUSTOM;
import static com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.GROUP_COLOR_SUCCESS;
import static com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.GROUP_COLOR_SUCCESS_CUSTOM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LifxNotifierTest {

    @Test
    @WithoutJenkins
    public void processJenkinsEvent_shouldNotChangeColorIfColorWasNotRecognized() throws Exception {
        LifxNotifierState state = LifxNotifierState.SUCCESSFUL;
        LifxNotifier notifier = mock(LifxNotifier.class);
        when(notifier.getColorForState(state)).thenReturn(LifxColor.NO_COLOR);

        notifier.processJenkinsEvent(mock(TaskListener.class), state);

        verify(notifier, never()).changeColor(any(float[].class), any(TaskListener.class));
    }

    @Test
    @WithoutJenkins
    public void processJenkinsEvent_shouldChangeColorIfColorRecognized() throws Exception {
        LifxNotifierState state = LifxNotifierState.SUCCESSFUL;
        LifxNotifier notifier = mock(LifxNotifier.class);
        when(notifier.getColorForState(state)).thenReturn(LifxColor.NO_COLOR);

        notifier.processJenkinsEvent(mock(TaskListener.class), state);

        verify(notifier, never()).changeColor(any(float[].class),any(TaskListener.class));
    }

    @Test
    @WithoutJenkins
    public void convertToHsb_shouldReturnExpectedHsb() throws NoSuchColorException {
        LifxNotifier notifier = new LifxNotifier(
                new LifxNotifierInProgress(""), "", "", "", "", "", "");

        float[] hsb = notifier.convertToHsb(new LifxColor("ff00ff", true));

        assertEquals(0.8333333f, hsb[0], 0.0f);
        assertEquals(1.0f, hsb[1], 0.0f);
        assertEquals(1.0f, hsb[2], 0.0f);
    }

    @Test
    @WithoutJenkins
    public void getColorForState_shouldReturnNoColorIfInProgressStateIsIgnored() throws Exception {
        LifxNotifier notifier = new LifxNotifier(null, "", "", "", "", "", "");

        LifxColor color = notifier.getColorForState(LifxNotifierState.IN_PROGRESS);

        assertEquals(LifxColor.NO_COLOR, color);
    }

    @Test
    @WithoutJenkins
    public void getColorForState_shouldReturnInProgressColorIfInProgressIsEnabled()
            throws Exception {
        String colorInProgress = "#ffffff";
        LifxNotifier notifier = new LifxNotifier(
                new LifxNotifierInProgress(colorInProgress), "", "", "", "", "", "");

        LifxColor color = notifier.getColorForState(LifxNotifierState.IN_PROGRESS);

        assertEquals(colorInProgress, color.getColor());
    }

    @Test
    @WithoutJenkins
    public void getColorForState_shouldReturnSuccessColorsForSuccessfulState()
            throws Exception {
        String colorSuccess = "#000000";
        LifxNotifier notifier = new LifxNotifier(new LifxNotifierInProgress(""),
                GROUP_COLOR_SUCCESS, "", colorSuccess, "", "", "");

        LifxColor color = notifier.getColorForState(LifxNotifierState.SUCCESSFUL);

        assertEquals(colorSuccess, color.getColor());
    }

    @Test
    @WithoutJenkins
    public void getColorForState_shouldReturnCustomSuccessColorsForSuccessfulState()
            throws Exception {
        String colorSuccessCustom = "#000000";
        LifxNotifier notifier = new LifxNotifier(new LifxNotifierInProgress(""),
                GROUP_COLOR_SUCCESS_CUSTOM, "", "", "", colorSuccessCustom, "");

        LifxColor color = notifier.getColorForState(LifxNotifierState.SUCCESSFUL);

        assertEquals(colorSuccessCustom, color.getColor());
    }

    @Test
    @WithoutJenkins
    public void getColorForState_shouldReturnFailureColorsForFailedState()
            throws Exception {
        String colorFailure = "#000000";
        LifxNotifier notifier = new LifxNotifier(new LifxNotifierInProgress(""),
                "", GROUP_COLOR_FAILURE, "", colorFailure, "", "");

        LifxColor color = notifier.getColorForState(LifxNotifierState.FAILED);

        assertEquals(colorFailure, color.getColor());
    }

    @Test
    @WithoutJenkins
    public void getColorForState_shouldReturnCustomFailureColorsForFailedState()
            throws Exception {
        String colorFailureCustom = "#000000";
        LifxNotifier notifier = new LifxNotifier(new LifxNotifierInProgress(""),
                "", GROUP_COLOR_FAILURE_CUSTOM, "", "", "", colorFailureCustom);

        LifxColor color = notifier.getColorForState(LifxNotifierState.FAILED);

        assertEquals(colorFailureCustom, color.getColor());
    }
}
