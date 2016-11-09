package com.cloudbees.lifxnotify.lifxnotifier;

import com.cloudbees.lifxnotify.lifxnotifier.LifxNotifier.LifxNotifierInProgress;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.WithoutJenkins;

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

        notifier.processJenkinsEvent(mock(Run.class), mock(TaskListener.class), state);

        verify(notifier, never()).changeColor(anyInt(), any(TaskListener.class));
        verify(notifier, never()).changeColor(anyInt(), anyFloat(), anyFloat(), any(TaskListener.class));
    }

    @Test
    @WithoutJenkins
    public void processJenkinsEvent_shouldChangeColorIfColorRecognized() throws Exception {
        LifxNotifierState state = LifxNotifierState.SUCCESSFUL;
        LifxNotifier notifier = mock(LifxNotifier.class);
        when(notifier.getColorForState(state)).thenReturn(LifxColor.NO_COLOR);

        notifier.processJenkinsEvent(mock(Run.class), mock(TaskListener.class), state);

        verify(notifier, never()).changeColor(anyInt(), any(TaskListener.class));
        verify(notifier, never()).changeColor(anyInt(), anyFloat(), anyFloat(), any(TaskListener.class));
    }

}
