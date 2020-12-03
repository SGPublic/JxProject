package com.sgpublic.jxproject.core;

import com.sgpublic.jxproject.JxLauncher;
import com.sgpublic.jxproject.exception.JxManifestException;
import com.sgpublic.jxproject.exception.JxMessage;
import com.sgpublic.jxproject.exception.JxRecourseException;
import com.sgpublic.jxproject.exception.JxRuntimeException;
import com.sgpublic.xml.SXMLArray;
import com.sgpublic.xml.SXMLObject;
import com.sgpublic.xml.exception.SXMLException;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JxManifest {
    private static SXMLObject manifest;
    private static String[] stylesheets = {};
    private static String mainActivity;
    private static final Map<Class<?>, SXMLObject> activities = new HashMap<>();

    public static void setup(){
        try {
            String manifestString = JxLauncher.getResourceAsString("/JxManifest.xml");
            if (!manifestString.equals("")){
                callParsing(() -> {
                    manifest = new SXMLObject(manifestString);
                    if (!manifest.isAttrNull("stylesheet")){
                        stylesheets = manifest.getStringAttr("stylesheet").split("\\|");
                    }
                });
            } else {
                throw new JxManifestException(JxMessage.MANIFEST_READ_FAILED);
            }
        } catch (JxRecourseException e){
            e.printStackTrace();
            throw new JxManifestException(JxMessage.MANIFEST_NOTFOUND);
        }
    }

    public static String getPackage(){
        return getManifestAttr("package", null);
    }

    public static String getMainActivity(){
        if (mainActivity == null){
            callParsing(() -> {
                SXMLArray activities = manifest.getXMLObject("application")
                        .getXMLArray("activity");
                activities.forEach((object, index) -> {
                    String activity = object.getStringAttr("name");
                    if (activity.charAt(0) == '.'){
                        activity = getPackage() + activity;
                    }
                    if (!object.isTagNull("intent-filter")){
                        String intentFilter = object.getXMLObject("intent-filter")
                                .getXMLObject("action")
                                .getStringAttr("name");
                        if (intentFilter.equals(JxIntent.ACTION_MAIN)){
                            JxManifest.mainActivity = activity;
                        }
                    }
                    try {
                        Class<?> clazz = Class.forName(activity);
                        if (JxCompatActivity.class.isAssignableFrom(clazz)){
                            JxManifest.activities.put(clazz, object);
                        } else {
                            throw new JxRuntimeException(JxMessage.MANIFEST_NO_A_ACTIVITY, activity);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new JxRuntimeException(JxMessage.MANIFEST_ACTIVITY_NO_FOUND, activity);
                    }
                });
            });
        }
        return mainActivity;
    }

    public static boolean findActivity(Class<?> clazz){
        return activities.get(clazz) != null;
    }

    public static boolean findActivity(JxCompatActivity context){
        return activities.get(context.getClass()) != null;
    }

    public static String getLabel() {
        return getApplicationAttr("label", "JavaFX Extra Activity");
    }

    public static String getLabelOfActivity(JxCompatActivity context) throws JxManifestException {
        return parseStringAttr(
                getActivityAttr(context.getClass(), "label", getLabel())
        );
    }

    public static String getStageStyle() {
        return getApplicationAttr("stageStyle", "DECORATED");
    }

    public static String getStageStyleOfActivity(JxCompatActivity context) throws JxManifestException {
        return getActivityAttr(context.getClass(), "stageStyle", getStageStyle());
    }

    public static InputStream getIcon(){
        String icon = getApplicationAttr("icon", null);
        if (icon != null){
            return parseMediaAttr(icon);
        } else {
            return null;
        }
    }

    public static InputStream getActivityIcon(JxCompatActivity context){
        String icon = getActivityAttr(context.getClass(), "icon", null);
        if (icon == null){
            return getIcon();
        } else {
            return parseMediaAttr(icon);
        }
    }

    public static boolean isResizable(){
        return getApplicationAttr("resizable", true);
    }

    public static boolean isActivityResizable(JxCompatActivity context){
        return getActivityAttr(context.getClass(), "resizable", isResizable());
    }

    static void getStyleSheet(StyleSheetReader reader){
        ArrayList<String> list = new ArrayList<>();
        for (String stylesheet : stylesheets){
            if (!list.contains(stylesheet)){
                list.add(stylesheet);
                reader.onRead(stylesheet);
            }
        }
    }

    static void getStyleSheetOfActivity(JxCompatActivity context, StyleSheetReader reader){
        SXMLObject classObject = activities.get(context.getClass());
        if (classObject != null){
            try {
                ArrayList<String> list = new ArrayList<>();
                getStyleSheet(stylesheet -> {
                    list.add(stylesheet);
                    reader.onRead(stylesheet);
                });
                if (!classObject.isAttrNull("stylesheet")){
                    String[] stylesheets = classObject.getStringAttr("stylesheet").split("\\|");
                    for (String stylesheet : stylesheets){
                        if (!list.contains(stylesheet)){
                            System.out.println(stylesheet);
                            list.add(stylesheet);
                            reader.onRead(stylesheet);
                        }
                    }
                }
            } catch (SXMLException e) {
                e.printStackTrace();
                throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
            }
        } else {
            throw new JxManifestException(JxMessage.MANIFEST_ACTIVITY_NO_FOUND);
        }
    }

    public static String getManifestAttr(String attrName, String defValue){
        if (manifest.isAttrNull(attrName)){
            return defValue;
        } else {
            try {
                return manifest.getStringAttr(attrName);
            } catch (SXMLException e) {
                e.printStackTrace();
                throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
            }
        }
    }

    public static String getApplicationAttr(String attrName, String defValue){
        try {
            SXMLObject application = manifest.getXMLObject("application");
            if (application.isAttrNull(attrName)){
                return defValue;
            } else {
                return application.getStringAttr(attrName);
            }
        } catch (SXMLException e) {
            e.printStackTrace();
            throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
        }
    }

    public static String getActivityAttr(Class<?> clazz, String attrName, String defValue) throws JxManifestException {
        SXMLObject classObject = activities.get(clazz);
        if (classObject != null){
            if (classObject.isAttrNull(attrName)){
                return defValue;
            } else {
                try {
                    return classObject.getStringAttr(attrName);
                } catch (SXMLException e) {
                    e.printStackTrace();
                    throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
                }
            }
        } else {
            throw new JxManifestException(JxMessage.MANIFEST_ACTIVITY_NO_FOUND);
        }
    }

    public static boolean getManifestAttr(String attrName, boolean defValue){
        if (manifest.isAttrNull(attrName)){
            return defValue;
        } else {
            try {
                return manifest.getBooleanAttr(attrName);
            } catch (SXMLException e) {
                e.printStackTrace();
                throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
            }
        }
    }

    public static boolean getApplicationAttr(String attrName, boolean defValue){
        try {
            SXMLObject application = manifest.getXMLObject("application");
            if (application.isAttrNull(attrName)){
                return defValue;
            } else {
                return application.getBooleanAttr(attrName);
            }
        } catch (SXMLException e) {
            e.printStackTrace();
            throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
        }
    }

    public static boolean getActivityAttr(Class<?> clazz, String attrName, boolean defValue) throws JxManifestException {
        SXMLObject classObject = activities.get(clazz);
        if (classObject != null){
            if (classObject.isAttrNull(attrName)){
                return defValue;
            } else {
                try {
                    return classObject.getBooleanAttr(attrName);
                } catch (SXMLException e) {
                    e.printStackTrace();
                    throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
                }
            }
        } else {
            throw new JxManifestException(JxMessage.MANIFEST_ACTIVITY_NO_FOUND);
        }
    }

    public static long getManifestAttr(String attrName, long defValue){
        if (manifest.isAttrNull(attrName)){
            return defValue;
        } else {
            try {
                return manifest.getLongAttr(attrName);
            } catch (SXMLException e) {
                e.printStackTrace();
                throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
            }
        }
    }

    public static long getApplicationAttr(String attrName, long defValue){
        try {
            SXMLObject application = manifest.getXMLObject("application");
            if (application.isAttrNull(attrName)){
                return defValue;
            } else {
                return application.getLongAttr(attrName);
            }
        } catch (SXMLException e) {
            e.printStackTrace();
            throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
        }
    }

    public static long getActivityAttr(Class<?> clazz, String attrName, long defValue) throws JxManifestException {
        SXMLObject classObject = activities.get(clazz);
        if (classObject != null){
            if (classObject.isAttrNull(attrName)){
                return defValue;
            } else {
                try {
                    return classObject.getLongAttr(attrName);
                } catch (SXMLException e) {
                    e.printStackTrace();
                    throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
                }
            }
        } else {
            throw new JxManifestException(JxMessage.MANIFEST_ACTIVITY_NO_FOUND);
        }
    }

    public static double getManifestAttr(String attrName, double defValue){
        if (manifest.isAttrNull(attrName)){
            return defValue;
        } else {
            try {
                return manifest.getDoubleAttr(attrName);
            } catch (SXMLException e) {
                e.printStackTrace();
                throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
            }
        }
    }

    public static double getApplicationAttr(String attrName, double defValue){
        try {
            SXMLObject application = manifest.getXMLObject("application");
            if (application.isAttrNull(attrName)){
                return defValue;
            } else {
                return application.getDoubleAttr(attrName);
            }
        } catch (SXMLException e) {
            e.printStackTrace();
            throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
        }
    }

    public static double getActivityAttr(Class<?> clazz, String attrName, double defValue) throws JxManifestException {
        SXMLObject classObject = activities.get(clazz);
        if (classObject != null){
            if (classObject.isAttrNull(attrName)){
                return defValue;
            } else {
                try {
                    return classObject.getDoubleAttr(attrName);
                } catch (SXMLException e) {
                    e.printStackTrace();
                    throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
                }
            }
        } else {
            throw new JxManifestException(JxMessage.MANIFEST_ACTIVITY_NO_FOUND);
        }
    }

    private static void callParsing(ParsingEvent event){
        try {
            event.onParsing();
        } catch (SXMLException e) {
            e.printStackTrace();
            throw new JxManifestException(JxMessage.MANIFEST_PARSE_FAILED);
        }
    }

    private static String parseStringAttr(String value){
        if (value.charAt(0) == '@'){
            String[] valueParsed = value.substring(1).split("/");
            if (valueParsed.length == 2 && valueParsed[0].equals("string")){
                return JxRecourse.getString(valueParsed[1]);
            }
        } else {
            return value;
        }
        throw new JxManifestException(JxMessage.MANIFEST_ATTR_PARSE_FAILED);
    }

    private static String parseColorAttr(String value){
        if (value.charAt(0) == '@'){
            String[] valueParsed = value.substring(1).split("/");
            if (valueParsed.length == 2 && valueParsed[0].equals("color")) {
                return JxRecourse.getColor(valueParsed[1]);
            }
        } else {
            return value;
        }
        throw new JxManifestException(JxMessage.MANIFEST_ATTR_PARSE_FAILED);
    }

    private static InputStream parseMediaAttr(String value){
        if (value != null && value.charAt(0) == '@'){
            String[] valueParsed = value.substring(1).split("/");
            if (valueParsed.length == 2) {
                return JxRecourse.getMedia(valueParsed[0], valueParsed[1]);
            }
        }
        throw new JxManifestException(JxMessage.MANIFEST_ATTR_PARSE_FAILED);
    }

    private interface ParsingEvent {
        void onParsing() throws SXMLException;
    }

    public interface StyleSheetReader {
        void onRead(String stylesheet);
    }
}
