package com.sgpublic.jxproject.exception;

public class JxMessage {
    public static final String MANIFEST_PARSE_FAILED = "Unable to parse JxManifest.xml.";
    public static final String MANIFEST_NOTFOUND = "Cannot found project JxManifest.xml.";
    public static final String MANIFEST_READ_FAILED = "Unable to read JxManifest.xml.";
    public static final String MANIFEST_NO_MAIN_ACTIVITY = "No main activity found.";
    public static final String MANIFEST_NO_A_ACTIVITY = "The activity of \"%s\" is not a JxCompatActivity.";
    public static final String MANIFEST_ACTIVITY_NO_FOUND = "The activity \"%s\" not found.";
    public static final String MANIFEST_ACTIVITY_ATTR_NO_FOUND = "The attribute \"%s\" in activity \"%s\" not found.";
    public static final String MANIFEST_ATTR_PARSE_FAILED = "Cannot parse the attribute \"%s\".";

    public static final String LAUNCH_FAILURE = "Failed to launch main activity, did you forget to add a \"intent-filter\" it in JxManifest.xml?";
    public static final String ACTIVITY_EXPORT = "Unable to find the class, did you forget to register it in JxManifest.xml?";
    public static final String ACTIVITY_NOT_AVAILABLE = "This class is not a JxCompatActivity that can be launched, please use correct inheritance.";
    public static final String ACTIVITY_LAUNCH_FAILURE = "Failed to launch the activity.";
    public static final String ACTIVITY_CLOSE_FAILURE = "Failed to close the activity.";
    public static final String ACTIVITY_MAXIMIZED_FAILURE = "Failed to maximize the activity.";
    public static final String ACTIVITY_ICONIFIED_FAILURE = "Failed to iconify the activity.";
    public static final String ACTIVITY_NOT_FIND = "Unable to find the activity.";
    public static final String INTENT_TARGET_NULL = "Target activity undefine.";
    public static final String INTENT_FORCE_NULL = "Force activity undefine.";

    public static final String LAYOUT_NOT_FIND = "The layout file named %s cannot be found";

    public static final String RES_NOT_FOUND = "Resource%s not found.";
    public static final String RES_PARSE_FAILED = "Failed to parse the resource%s.";
    public static final String RES_ID_NOT_FOUND = "The %s resource with ID \"%s\" was not found";

    public static final String FILE_NOT_FOUNT = "The target file not found.";
    public static final String FILE_READ_FAILED = "Cannot read the target file.";
    public static final String FILE_CREATE_FAILED = "Cannot create the target file.";
    public static final String FILE_WRITE_FAILED = "Failed to write the target file.";

    public static final String CONF_READ_FAILED = "Cannot read the config file.";
    public static final String CONF_PARSE_FAILED = "Failed to parse the config file.";
    public static final String CONF_WRITE_FAILED = "Failed to write the config file.";
}
