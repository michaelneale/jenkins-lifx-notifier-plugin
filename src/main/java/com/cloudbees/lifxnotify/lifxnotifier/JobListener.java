package com.cloudbees.lifxnotify.lifxnotifier;

import com.github.besherman.lifx.LFXClient;
import com.github.besherman.lifx.LFXHSBKColor;
import com.github.besherman.lifx.LFXLight;
import hudson.Extension;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

import java.io.IOException;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {

    private static final int RED = 0;
    private static final int GREEN = 120;
    private static final int BLUE = 181;

    private static final String LOG_PREFIX = "\\u00BF LIFX: "; // \u00BF = "Inverted question mark"

    public JobListener() {
        super(Run.class);
        printToConsole("build indicator loading. " +
                "If colours don't change, check you can adjust colour from phone/app. " +
                "Lights are automatically discovered.");
    }

    @Override
    public void onStarted(Run r, TaskListener listener) {
        changeColour(BLUE, 0.1f, 0f);
    }

    @Override
    public void onCompleted(Run r, TaskListener listener) {
        printToConsole("Attempting to Change the Colour of LIFX lights.");
        if (r.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
            changeColour(GREEN);
        } else {
            changeColour(RED);
        }
    }

    @Override
    public void onFinalized(Run r) {
    }

    private void changeColour(int hue) {
        changeColour(hue, 1.0f, 1.0f);
    }

    private void changeColour(int hue, float brightness, float saturation) {
        LFXClient client = new LFXClient();
        try {
            client.open(true);
            for (LFXLight light : client.getLights()) {
                printToConsole(String.format("Setting hue(\'%s\') on bulb(\'%s\').", hue, light.getLabel()));
                LFXHSBKColor color = new LFXHSBKColor(hue, saturation, brightness, 3500);
                light.setPower(true);
                light.setColor(color);
            }
        } catch (IOException e) {
            printToConsole(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            printToConsole(e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    private void printToConsole(String message) {
        System.out.println(LOG_PREFIX + message);
    }
}