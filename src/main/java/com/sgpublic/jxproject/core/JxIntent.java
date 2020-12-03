package com.sgpublic.jxproject.core;

import com.sgpublic.jxproject.JxLauncher;
import com.sgpublic.jxproject.exception.JxMessage;

import java.util.HashMap;
import java.util.Map;

public class JxIntent {
    public static final String ACTION_MAIN = "JxProject.JxIntent.action.ACTION_MAIN";

    private JxContext forceActivity;
    private Class<?> targetActivity;

    private static final Map<Class<?>, JxIntent> appExtra = new HashMap<>();
    private static final Map<String, Object> extra = new HashMap<>();

    public JxIntent(){}

    public JxIntent(JxContext forceActivity, Class<?> targetActivity){
        setClass(forceActivity, targetActivity);
    }

    public void setClass(JxContext forceActivity, Class<?> targetActivity){
        this.forceActivity = forceActivity;
        this.targetActivity = targetActivity;
    }

    public JxIntent putExtra(String key, String value){
        extra.put(key, value);
        return this;
    }

    public JxIntent putExtra(String key, Boolean value){
        extra.put(key, value);
        return this;
    }

    public JxIntent putExtra(String key, Integer value){
        extra.put(key, value);
        return this;
    }

    public JxIntent putExtra(String key, Long value){
        extra.put(key, value);
        return this;
    }

    public JxIntent putExtra(String key, Double value){
        extra.put(key, value);
        return this;
    }

    public JxIntent putExtra(String key, Byte value){
        extra.put(key, value);
        return this;
    }

    public String getStringExtra(String key, String defaultValue){
        Object extraValue = extra.get(key);
        if (extraValue instanceof String){
            return String.valueOf(String.valueOf(extraValue));
        } else {
            return defaultValue;
        }
    }

    public Integer getIntegerExtra(String key, Integer defaultValue){
        Object extraValue = extra.get(key);
        if (extraValue instanceof Integer){
            return Integer.valueOf(String.valueOf(extraValue));
        } else {
            return defaultValue;
        }
    }

    public Long getLongExtra(String key, Long defaultValue){
        Object extraValue = extra.get(key);
        if (extraValue instanceof Long){
            return Long.valueOf(String.valueOf(extraValue));
        } else {
            return defaultValue;
        }
    }

    public Double getDoubleExtra(String key, Double defaultValue){
        Object extraValue = extra.get(key);
        if (extraValue instanceof Double){
            return Double.valueOf(String.valueOf(extraValue));
        } else {
            return defaultValue;
        }
    }

    public Byte getByteExtra(String key, Byte defaultValue){
        Object extraValue = extra.get(key);
        if (extraValue instanceof Byte){
            return Byte.valueOf(String.valueOf(extraValue));
        } else {
            return defaultValue;
        }
    }

    public Boolean getBooleanExtra(String key, Boolean defaultValue){
        Object extraValue = extra.get(key);
        if (extraValue instanceof Boolean){
            return (Boolean) extraValue;
        } else {
            return defaultValue;
        }
    }

    public Class<?> getTargetActivity() {
        if (targetActivity == null) {
            throw new NullPointerException(JxMessage.INTENT_TARGET_NULL);
        } else {
            return targetActivity;
        }
    }

    public JxContext getForceActivity() {
        if (forceActivity == null) {
            throw new NullPointerException(JxMessage.INTENT_FORCE_NULL);
        } else {
            return forceActivity;
        }
    }

    static void putIntent(Class<?> clazz, JxIntent intent){
        if (JxContext.class.isAssignableFrom(clazz)){
            appExtra.put(clazz, intent);
        }
    }

    static void removeIntent(JxContext context){
        if (appExtra.get(context.getClass()) != null){
            appExtra.remove(context.getClass());
        }

    }

    static JxIntent getIntent(JxContext context){
        return appExtra.get(context.getClass());
    }
}
