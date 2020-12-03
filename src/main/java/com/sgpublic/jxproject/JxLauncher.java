package com.sgpublic.jxproject;

import com.sgpublic.jxproject.core.*;
import com.sgpublic.jxproject.core.JxIntent;
import com.sgpublic.jxproject.exception.*;
import com.sgpublic.jxproject.core.JxSharedPreference;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class JxLauncher extends JxContext {
    private static Class<?> clazz;
    private static final Map<Class<?>, ArrayList<JxBroadcastReceiver>> broadcast = new HashMap<>();

    public JxLauncher(Class<?> clazz){
        JxLauncher.clazz = clazz;
        JxManifest.setup();
        JxRecourse.setup();
        launch();
    }

    private void launch() {
        try {
            String mainActivity = JxManifest.getMainActivity();
            if (mainActivity != null &&  !"".equals(mainActivity)){
                Class<?> clazz = Class.forName(mainActivity);
                if (JxCompatActivity.class.isAssignableFrom(clazz)){
                    clazz.getMethod("launch", Class.class, String[].class)
                            .invoke(clazz.getDeclaredConstructor().newInstance(), clazz, null);
                } else {
                    throw new JxRuntimeException(JxMessage.ACTIVITY_NOT_AVAILABLE);
                }
            } else {
                throw new JxManifestException(JxMessage.MANIFEST_NO_MAIN_ACTIVITY);
            }
        } catch (ClassNotFoundException e) {
            throw new JxRuntimeException(JxMessage.ACTIVITY_NOT_FIND);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new JxRuntimeException(JxMessage.ACTIVITY_LAUNCH_FAILURE);
        }
    }

    public static void setLanguageProfile(String language){
        JxSharedPreference.Editor editor = new JxSharedPreference("app").edit();
        editor.put("language", language);
        editor.apply();
    }

    public static String getLanguageProfile(){
        JxSharedPreference sp = new JxSharedPreference("app");
        return sp.getString("language", "default");
    }

    public static String getSystemLanguage(){
        return System.getProperty("user.language");
    }

    public static void setColorProfile(String color){
        JxSharedPreference.Editor editor = new JxSharedPreference("app").edit();
        editor.put("color", color);
        editor.apply();
    }

    public static String getColorProfile(){
        JxSharedPreference sp = new JxSharedPreference("app");
        return sp.getString("color", "default");
    }

    public static boolean isResourceExist(String name){
        InputStream inputStream = clazz.getResourceAsStream(name);
        return inputStream != null;
    }

    public static InputStream getResourceAsStream(String name) throws JxIOException {
        InputStream inputStream = clazz.getResourceAsStream(name);
        if (inputStream == null){
            throw new JxRecourseException(JxMessage.RES_NOT_FOUND, " " + name);
        } else {
            return inputStream;
        }
    }

    public static String getResourceAsString(String name) throws JxIOException {
        InputStream inputStream = clazz.getResourceAsStream(name);
        if (inputStream == null){
            throw new JxRecourseException(JxMessage.RES_NOT_FOUND, " " + name);
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines().parallel().collect(Collectors.joining("\n"));
        }
    }

    public static URL getResource(String name) throws JxIOException {
        URL url = clazz.getResource(name);
        if (url == null){
            throw new JxRecourseException(JxMessage.RES_NOT_FOUND, " " + name);
        } else {
            return url;
        }
    }

    public static boolean isFileExist(String dir, String fileName){
        File file = new File(dir, fileName);
        return file.exists();
    }

    public static String doInputStream(String dir, String fileName) throws JxIOException {
        try {
            File file = new File(dir, fileName);
            if (!file.exists()){
                throw new JxIOException(JxMessage.FILE_NOT_FOUNT);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file.getCanonicalFile())
                ));
                String configSting = reader.lines().parallel()
                        .collect(Collectors.joining("\n"));
                reader.close();
                return configSting;
            }
        } catch (IOException e){
            e.printStackTrace();
            throw new JxIOException(JxMessage.FILE_READ_FAILED);
        }
    }

    public static void doOutputStream(String dir, String fileName, String content) throws JxRuntimeException {
        try {
            File file = new File(dir, fileName);
            File parent = new File(dir);
            if (!parent.exists() & !parent.mkdirs() & !file.createNewFile()){
                throw new JxIOException(JxMessage.FILE_WRITE_FAILED);
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file.getCanonicalFile())
            ));
            writer.write(content);
            writer.close();
        } catch (IOException | JxIOException e){
            e.printStackTrace();
            throw new JxIOException(JxMessage.FILE_WRITE_FAILED);
        }
    }

    public static void removeBroadcast(Class<?> clazz){
        if (broadcast.get(clazz) != null){
            broadcast.remove(clazz);
        }
    }

    public static void removeTargetBroadcast(Class<?> clazz, JxBroadcastReceiver receiver){
        if (broadcast.get(clazz) != null){
            broadcast.get(clazz).remove(receiver);
        } else {
            Throwable e = new JxBroadcastException("Target broadcast is not exist.");
            e.printStackTrace();
        }
    }

    public static void addBroadcast(Class<?> clazz, JxBroadcastReceiver receiver) throws JxBroadcastException {
        broadcast.computeIfAbsent(clazz, k -> new ArrayList<>());
        broadcast.get(clazz).add(receiver);
    }
}
