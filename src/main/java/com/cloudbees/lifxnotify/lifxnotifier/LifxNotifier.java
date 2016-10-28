package com.cloudbees.lifxnotify.lifxnotifier;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Lifx configuration view.
 * <p>
 * Created by vgaidarji on 10/26/16.
 */
public class LifxNotifier extends BuildWrapper {

    private static final String GROUP_COLOR_SUCCESS = "GROUP_COLOR_SUCCESS";
    private static final String GROUP_COLOR_SUCCESS_CUSTOM = "GROUP_COLOR_SUCCESS_CUSTOM";
    private static final String GROUP_COLOR_FAILURE = "GROUP_COLOR_FAILURE";
    private static final String GROUP_COLOR_FAILURE_CUSTOM = "GROUP_COLOR_FAILURE_CUSTOM";

    private final String groupColorSuccess;
    private final String groupColorFailure;
    private final String colorSuccess;
    private final String colorFailure;
    private final String colorSuccessCustom;
    private final String colorFailureCustom;

    private static final List<String> COLORS = new ArrayList<String>();

    static {
        COLORS.add("blue");
        COLORS.add("cyan");
        COLORS.add("green");
        COLORS.add("orange");
        COLORS.add("pink");
        COLORS.add("purple");
        COLORS.add("red");
        COLORS.add("yellow");
        COLORS.add("white");
    }

    @DataBoundConstructor
    public LifxNotifier(String groupColorSuccess, String groupColorFailure,
            String colorSuccess, String colorFailure,
            String colorSuccessCustom, String colorFailureCustom) {
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

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildWrapperDescriptor {
        public DescriptorImpl() {
            load();
        }

        @Override
        public String getDisplayName() {
            return "Lifx notifier";
        }

        @SuppressWarnings("unused")
        public String getDefaultColorSuccessCustom() {
            return "00ff00";
        }

        @SuppressWarnings("unused")
        public String getDefaultColorFailureCustom() {
            return "ff0000";
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

        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            save();
            return super.configure(req, formData);
        }
    }
}
