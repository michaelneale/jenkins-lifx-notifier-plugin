package com.cloudbees.lifxnotify.lifxnotifier;

import hudson.model.TaskListener;

public class LifxNotifierLogger {

    private TaskListener listener;

    public LifxNotifierLogger(TaskListener listener) {
        this.listener = listener;
    }

    public TaskListener getListener() {
        return listener;
    }

    public void info(String message) {
        listener.getLogger().println("[LifxNotifier] - " + message);
    }

    public void error(String message) {
        listener.getLogger().println("[LifxNotifier] - [ERROR] - " + message);
    }
}