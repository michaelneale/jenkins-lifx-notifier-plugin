package com.cloudbees.lifxnotify.lifxnotifier;

import com.github.besherman.lifx.LFXClient;
import com.github.besherman.lifx.LFXHSBKColor;
import com.github.besherman.lifx.LFXLight;
import hudson.Extension;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {

    private static final int RED = 0;
    private static final int GREEN = 120;
    private static final int BLUE = 181;

    // TODO: 10/24/16 use 1 format for log messages everywhere. "LIFX: message"

    public JobListener() {
        super(Run.class);
        System.out.println("LIFX build indicator loading. " +
                "If colours don't change, check you can adjust colour from phone/app. " +
                "Lights are automatically discovered");
    }

    @Override
    public void onStarted(Run r, TaskListener listener) {
        changeColour(BLUE, 0.1f, 0f);
    }

    @Override
    public void onCompleted(Run r, TaskListener listener) {
        System.out.println("Attempting to Change the Colour of LIFX lights.");
        if (r.getResult().isBetterOrEqualTo(Result.SUCCESS)) {
            changeColour(GREEN);
        } else {
            changeColour(RED);
        }
    }


    private void changeColour(int hue) {
        changeColour(hue, 1.0f, 1.0f);
    }

    private void changeColour(int hue, float brightness, float saturation) {
        LFXClient client = new LFXClient();
        try {
            client.open(true);
            for (LFXLight light : client.getLights()) {
                System.out.format("------> LIFX: Setting hue(\'%s\') on bulb(\'%s\').", hue, light.getLabel());
                LFXHSBKColor color = new LFXHSBKColor(hue, saturation, brightness, 3500);
                light.setPower(true);
                light.setColor(color);
            }
        } catch (IOException e) {
            // TODO: 10/24/16 print error with speacial unicode character at the start (LIFX character)
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO: 10/24/16 print error with speacial unicode character at the start (LIFX character)
            e.printStackTrace();
        } finally {
            client.close();
        }
    }


    @Override
    public void onFinalized(Run r) {
    }

}