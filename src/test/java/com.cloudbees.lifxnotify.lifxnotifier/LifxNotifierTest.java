package com.cloudbees.lifxnotify.lifxnotifier;

import com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.LifxNotifierInProgress;
import hudson.model.TaskListener;
import org.junit.Test;
import org.jvnet.hudson.test.WithoutJenkins;

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

        verify(notifier, never()).changeColor(anyInt(), any(TaskListener.class));
        verify(notifier, never()).changeColor(anyInt(), anyFloat(), anyFloat(),
                any(TaskListener.class));
    }

    @Test
    @WithoutJenkins
    public void processJenkinsEvent_shouldChangeColorIfColorRecognized() throws Exception {
        LifxNotifierState state = LifxNotifierState.SUCCESSFUL;
        LifxNotifier notifier = mock(LifxNotifier.class);
        when(notifier.getColorForState(state)).thenReturn(LifxColor.NO_COLOR);

        notifier.processJenkinsEvent(mock(TaskListener.class), state);

        verify(notifier, never()).changeColor(anyInt(), any(TaskListener.class));
        verify(notifier, never()).changeColor(anyInt(), anyFloat(), anyFloat(),
                any(TaskListener.class));
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
}
