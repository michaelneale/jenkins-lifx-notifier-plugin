package com.cloudbees.lifxnotify.lifxnotifier;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.Run;
import hudson.model.listeners.RunListener;

@Extension
@SuppressWarnings("rawtypes")
public class JobListener extends RunListener<Run> {

    public JobListener() {
        super(Run.class);
    }

    @Override
    public void onStarted(Run r, TaskListener listener) {
        System.out.println("yeah again");
    }

    @Override
    public void onCompleted(Run r, TaskListener listener) {
    }

    @Override
    public void onFinalized(Run r) {
    }

}