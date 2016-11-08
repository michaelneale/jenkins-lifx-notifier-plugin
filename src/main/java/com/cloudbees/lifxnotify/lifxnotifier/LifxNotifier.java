package com.cloudbees.lifxnotify.lifxnotifier;

import com.github.besherman.lifx.LFXClient;
import com.github.besherman.lifx.LFXHSBKColor;
import com.github.besherman.lifx.LFXLight;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;

import static com.cloudbees.lifxnotify.lifxnotifier.LifxNotifierState.IN_PROGRESS;

/**
 * Lifx configuration view.
 * <p>
 * Created by vgaidarji on 10/26/16.
 */
public class LifxNotifier extends Notifier implements SimpleBuildStep {

    private static final String GROUP_COLOR_SUCCESS = "GROUP_COLOR_SUCCESS";
    private static final String GROUP_COLOR_SUCCESS_CUSTOM = "GROUP_COLOR_SUCCESS_CUSTOM";
    private static final String GROUP_COLOR_FAILURE = "GROUP_COLOR_FAILURE";
    private static final String GROUP_COLOR_FAILURE_CUSTOM = "GROUP_COLOR_FAILURE_CUSTOM";
    private static final List<String> COLORS = new ArrayList<String>();
    public static final String NO_COLOR = "";

    static {
        COLORS.add("blue");
        COLORS.add("cyan");
        COLORS.add("green");
        COLORS.add("orange");
        COLORS.add("pink");
        // COLORS.add("purple"); // no such color in jawa.awt.Color class
        COLORS.add("red");
        COLORS.add("yellow");
        COLORS.add("white");
    }

    private final LifxNotifierInProgress notifyInProgress;
    private final String groupColorSuccess;
    private final String groupColorFailure;
    private final String colorSuccess;
    private final String colorFailure;
    private final String colorSuccessCustom;
    private final String colorFailureCustom;

    @DataBoundConstructor
    public LifxNotifier(LifxNotifierInProgress notifyInProgress,
            String groupColorSuccess, String groupColorFailure,
            String colorSuccess, String colorFailure,
            String colorSuccessCustom, String colorFailureCustom) {
        this.notifyInProgress = notifyInProgress;
        this.groupColorSuccess =
                groupColorSuccess == null ? GROUP_COLOR_SUCCESS_CUSTOM : groupColorSuccess;
        this.groupColorFailure =
                groupColorFailure == null ? GROUP_COLOR_FAILURE_CUSTOM : groupColorFailure;
        this.colorSuccess = colorSuccess;
        this.colorFailure = colorFailure;
        this.colorSuccessCustom = colorSuccessCustom;
        this.colorFailureCustom = colorFailureCustom;
    }

    public String getColorSuccess() {
        return colorSuccess;
    }

    public String getColorFailure() {
        return colorFailure;
    }

    public String getGroupColorSuccess() {
        return groupColorSuccess;
    }

    public String getGroupColorFailure() {
        return groupColorFailure;
    }

    public String getColorSuccessCustom() {
        return colorSuccessCustom;
    }

    public String getColorFailureCustom() {
        return colorFailureCustom;
    }

    @SuppressWarnings("unused")
    public LifxNotifierInProgress getNotifyInProgress() {
        return notifyInProgress;
    }

    @SuppressWarnings("unused")
    public boolean isNotifyInProgress() {
        return notifyInProgress != null;
    }

    /**
     * Identifies which option from "color success group" is selected.
     */
    @SuppressWarnings("unused")
    public String isGroupColorSuccess(String groupColorSuccess) {
        return this.groupColorSuccess.equalsIgnoreCase(groupColorSuccess) ? "true" : "";
    }

    /**
     * Identifies which option from "color failure group" is selected.
     */
    @SuppressWarnings("unused")
    public String isGroupColorFailure(String groupColorFailure) {
        return this.groupColorFailure.equalsIgnoreCase(groupColorFailure) ? "true" : "";
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Override
    public boolean prebuild(AbstractBuild<?, ?> build, BuildListener listener) {
        LifxNotifierLogger logger = new LifxNotifierLogger(listener);
        logger.info("Build indicator is loading. " +
                "If colors don't change, please check if you can adjust color from phone/app. " +
                "Lights are automatically discovered.");
        return !isNotifyInProgress()
                || processJenkinsEvent(build, listener, IN_PROGRESS);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build,
            Launcher launcher,
            BuildListener listener) {
        return perform(build, listener, !isNotifyInProgress());
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run,
            @Nonnull FilePath workspace,
            @Nonnull Launcher launcher,
            @Nonnull TaskListener listener) throws InterruptedException, IOException {
        if (!perform(run, listener, false)) {
            run.setResult(Result.FAILURE);
        }
    }

    private boolean perform(Run<?, ?> run, TaskListener listener, boolean disableInProgress) {
        LifxNotifierState state;
        Result result = run.getResult();
        if (result == null && disableInProgress) {
            return true;
        } else if (result == null) {
            state = IN_PROGRESS;
        } else if (result.equals(Result.SUCCESS)) {
            state = LifxNotifierState.SUCCESSFUL;
        } else if (result.equals(Result.NOT_BUILT)) {
            return true;
        } else {
            state = LifxNotifierState.FAILED;
        }

        return processJenkinsEvent(run, listener, state);
    }

    private boolean processJenkinsEvent(
            final Run<?, ?> run,
            final TaskListener listener,
            final LifxNotifierState state) {
        LifxColor color = getColorForState(state);
        if (color != LifxColor.NO_COLOR) {
            // TODO convert RRGGBB String to hue
            int hue = 0;
            changeColor(hue, listener);
        }

        return true;
    }

    private LifxColor getColorForState(final LifxNotifierState state) {
        switch (state) {
            case IN_PROGRESS:
                if (isNotifyInProgress()) {
                    return new LifxColor(notifyInProgress.colorInProgress, false);
                }
                return LifxColor.NO_COLOR;
            case SUCCESSFUL:
                if (GROUP_COLOR_SUCCESS.equals(groupColorSuccess)) {
                    return new LifxColor(colorSuccess, false);
                } else {
                    return new LifxColor(colorSuccessCustom, true);
                }
            case FAILED:
                if (GROUP_COLOR_FAILURE.equals(groupColorFailure)) {
                    return new LifxColor(colorFailure, false);
                } else {
                    return new LifxColor(colorFailureCustom, true);
                }
            default:
                return LifxColor.NO_COLOR;
        }
    }

    private void changeColor(int hue, final TaskListener listener) {
        changeColor(hue, 1.0f, 1.0f, listener);
    }

    private void changeColor(int hue, float brightness, float saturation,
            final TaskListener listener) {
        LifxNotifierLogger logger = new LifxNotifierLogger(listener);
        LFXClient client = new LFXClient();
        try {
            client.open(true);
            for (LFXLight light : client.getLights()) {
                logger.info(
                        "Attempting to change the color to " + hue + ", on " + light.getLabel());
                LFXHSBKColor color = new LFXHSBKColor(hue, saturation, brightness, 3500);
                light.setPower(true);
                light.setColor(color);
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "Lifx notifier";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // to persist global configuration information,
            // set that to properties and call save().
            // propertyName = formData.getString("propertyName");
            save();
            return super.configure(req, formData);
        }

        @SuppressWarnings("unused")
        public FormValidation doCheckColorSuccessCustom(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0) {
                return FormValidation.error("Please set the custom success color.");
            }
            return FormValidation.ok();
        }

        @SuppressWarnings("unused")
        public FormValidation doCheckColorFailureCustom(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0) {
                return FormValidation.error("Please set the custom failure color.");
            }
            return FormValidation.ok();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillColorInProgressItems() {
            return getColors();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillColorSuccessItems() {
            return getColors();
        }

        @SuppressWarnings("unused")
        public ListBoxModel doFillColorFailureItems() {
            return getColors();
        }

        private ListBoxModel getColors() {
            ListBoxModel models = new ListBoxModel();
            for (String color : COLORS) {
                models.add(color);
            }
            return models;
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
    }

    public static class LifxNotifierInProgress {
        private final String colorInProgress;

        @DataBoundConstructor
        public LifxNotifierInProgress(String colorInProgress) {
            this.colorInProgress = colorInProgress;
        }

        public String getColorInProgress() {
            return colorInProgress;
        }
    }
}
