package com.cloudbees.lifxnotify.lifxnotifier;

import android.content.Context;
import hudson.Extension;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;
import lifx.java.android.client.LFXClient;
import lifx.java.android.entities.LFXHSBKColor;
import lifx.java.android.light.LFXLight;
import lifx.java.android.network_context.LFXNetworkContext;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {


    static final int RED = 0;
    static final int GREEN = 120;
    static final int BLUE = 181;
    static LFXNetworkContext localNetworkContext;

    /** kick off the auto discovery - this can take a minute - so start it early */
    static {
        localNetworkContext = LFXClient.getSharedInstance(new Context()).getLocalNetworkContext();
        localNetworkContext.connect();
    }

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
        for( LFXLight aLight : localNetworkContext.getAllLightsCollection().getLights()) {
            System.out.println("------> LIFX LIGHT changing to hue: " + hue);
            LFXHSBKColor color = LFXHSBKColor.getColor(hue, saturation, brightness, 3500);
            aLight.setColor( color);
        }
    }


    @Override
    public void onFinalized(Run r) {
    }

}