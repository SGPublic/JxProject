package com.sgpublic.jxproject.core;

import com.sgpublic.jxproject.JxLauncher;
import com.sgpublic.jxproject.exception.JxMessage;
import com.sgpublic.jxproject.exception.JxRecourseException;
import com.sgpublic.xml.SXMLArray;
import com.sgpublic.xml.SXMLObject;
import com.sgpublic.xml.exception.SXMLException;
import javafx.fxml.FXMLLoader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JxRecourse {
    private final static Map<String, String> defStrings = new HashMap<>();
    private final static Map<String, String> defColors = new HashMap<>();

    private final static Map<String, String> userStrings = new HashMap<>();
    private final static Map<String, String> userColors = new HashMap<>();

    public static void setup(){
        JxSharedPreference sharedPreference = new JxSharedPreference("app");
        JxSharedPreference.Editor editor = sharedPreference.edit();
        if (sharedPreference.getString("language", "null").equals("null")){
            editor.put("language", "default");
        }
        if (sharedPreference.getString("color", "null").equals("null")){
            editor.put("color", "default");
        }
        editor.apply();

        getStringRes();
        getColorRes();
    }

    public static String getString(String resName){
        if (userStrings.get(resName) == null){
            if (defStrings.get(resName) == null){
                throw new JxRecourseException(JxMessage.RES_ID_NOT_FOUND, "string", resName);
            } else {
                return defStrings.get(resName);
            }
        } else {
            return userStrings.get(resName);
        }
    }

    public static String getColor(String resName){
        if (userColors.get(resName) == null){
            if (defColors.get(resName) == null){
                throw new JxRecourseException(JxMessage.RES_ID_NOT_FOUND, "color", resName);
            } else {
                return defColors.get(resName);
            }
        } else {
            return userColors.get(resName);
        }
    }

    public static InputStream getMedia(String resSort, String resName){
        String res = "/" + resSort + "/" + resName;
        String[] resFormat = {
                ".png", ".jpg", ".svg"
        };
        InputStream stream = null;
        for (String formatIndex : resFormat){
            if (JxLauncher.isResourceExist(res + formatIndex)){
                stream = JxLauncher.getResourceAsStream(res + formatIndex);
                break;
            }
        }
        if (stream != null){
            return stream;
        } else {
            throw new JxRecourseException(JxMessage.RES_NOT_FOUND, " " + resName + " in " + resSort);
        }
    }

    public static FXMLLoader getLayout(String resName){
        String res = "/layout/" + resName + ".fxml";
        if (JxLauncher.isResourceExist(res)){
            return new FXMLLoader(
                    JxLauncher.getResource(res)
            );
        } else {
            throw new JxRecourseException(JxMessage.RES_NOT_FOUND, " " + resName + " in layout");
        }
    }

    private static void getStringRes(){
        String resName = "/values/strings";
        if (JxLauncher.isResourceExist(resName + ".xml")){
            getRes(resName, "string", defStrings);
        }
        String defResName = resName + "-" + JxLauncher.getSystemLanguage();
        String userResName = resName + "-" + JxLauncher.getLanguageProfile();
        if (JxLauncher.getLanguageProfile().equals("default")){
            if (JxLauncher.isResourceExist(defResName + ".xml")){
                getRes(defResName, "string", userStrings);
            }
        } else if (JxLauncher.isResourceExist(userResName + ".xml")){
            getRes(userResName, "string", userStrings);
        }
    }

    private static void getColorRes(){
        String resName = "/values/colors";
        if (JxLauncher.isResourceExist(resName + ".xml")){
            getRes(resName, "color", defColors);
        }
        String userResName = resName + "-" + JxLauncher.getColorProfile();
        if (!JxLauncher.getColorProfile().equals("default")
                && JxLauncher.isResourceExist(userResName + ".xml")){
            resName = userResName;
            getRes(resName, "color", userColors);
        }
    }

    private static void getRes(String resDir, String resName, Map<String, String> map){
        try {
            String res = JxLauncher.getResourceAsString(resDir + ".xml");
            SXMLArray array = new SXMLObject(res).getXMLArray(resName);
            array.forEach((object, index) -> map.put(
                    object.getStringAttr("name"),
                    object.getInnerData()
            ));
        } catch (JxRecourseException ignore) {
            throw new JxRecourseException(JxMessage.RES_NOT_FOUND, " of " + resName);
        } catch (SXMLException e) {
            e.printStackTrace();
            throw new JxRecourseException(JxMessage.RES_PARSE_FAILED);
        }
    }
}
