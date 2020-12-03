package com.sgpublic.jxproject.core;

import com.sgpublic.jxproject.JxLauncher;
import com.sgpublic.jxproject.exception.JxConfigException;
import com.sgpublic.jxproject.exception.JxIOException;
import com.sgpublic.jxproject.exception.JxMessage;
import com.sgpublic.xml.SXMLArray;
import com.sgpublic.xml.SXMLObject;
import com.sgpublic.xml.exception.SXMLException;

import java.util.HashMap;
import java.util.Map;

public class JxSharedPreference {
    private final String confName;
    private String confDir = "./config/";
    private String confSuffix = "jxsp";

    private final Map<String, String> strings = new HashMap<>();
    private final Map<String, Integer> ints = new HashMap<>();
    private final Map<String, Boolean> booleans = new HashMap<>();
    private final Map<String, Long> longs = new HashMap<>();
    private final Map<String, Double> doubles = new HashMap<>();

    public JxSharedPreference(String spName) throws JxConfigException {
        this.confName = spName + "." + confSuffix;
        if (JxLauncher.isFileExist(confDir, confName)){
            try {
                String jxsp = JxLauncher.doInputStream(confDir, confName);
                parseConfig(jxsp);
            } catch (SXMLException e) {
                e.printStackTrace();
            } catch (JxIOException e) {
                throw new JxConfigException(JxMessage.CONF_READ_FAILED);
            }
        }
    }

    public void setConfDir(String confDir){
        this.confDir = confDir;
    }

    public void setConfSuffix(String confSuffix) {
        this.confSuffix = confSuffix;
    }

    private void parseConfig(String configSting) throws SXMLException {
        SXMLObject config = new SXMLObject(configSting);
        SXMLArray array;
        if (!config.isTagNull("string")){
            array = config.getXMLArray("string");
            array.forEach((object, index) -> strings.put(object.getStringAttr("name"), object.getInnerData()));
        }
        if (!config.isTagNull("int")){
            array = config.getXMLArray("int");
            array.forEach((object, index) -> ints.put(object.getStringAttr("name"), object.getIntAttr("value")));
        }
        if (!config.isTagNull("long")){
            array = config.getXMLArray("long");
            array.forEach((object, index) -> longs.put(object.getStringAttr("name"), object.getLongAttr("value")));
        }
        if (!config.isTagNull("double")){
            array = config.getXMLArray("double");
            array.forEach((object, index) -> doubles.put(object.getStringAttr("name"), object.getDoubleAttr("value")));
        }
        if (!config.isTagNull("boolean")){
            array = config.getXMLArray("boolean");
            array.forEach((object, index) -> booleans.put(object.getStringAttr("name"), object.getBooleanAttr("value")));
        }
    }

    public String getString(String key, String defaultValue){
        String value = strings.get(key);
        if (value != null){
            return value;
        } else {
            return defaultValue;
        }
    }

    public Integer getInteger(String key, Integer defaultValue){
        Integer value = ints.get(key);
        if (value != null){
            return value;
        } else {
            return defaultValue;
        }
    }

    public Long getLong(String key, Long defaultValue){
        Long value = longs.get(key);
        if (value != null){
            return value;
        } else {
            return defaultValue;
        }
    }

    public Double getDouble(String key, Double defaultValue){
        Double value = doubles.get(key);
        if (value != null){
            return value;
        } else {
            return defaultValue;
        }
    }

    public Boolean getBoolean(String key, Boolean defaultValue){
        Boolean value = booleans.get(key);
        if (value != null){
            return value;
        } else {
            return defaultValue;
        }
    }

    public Editor edit(){
        return new Editor();
    }

    public class Editor {
        Editor(){}

        public Editor put(String key, String value){
            strings.put(key, value);
            return this;
        }

        public Editor put(String key, Integer value){
            ints.put(key, value);
            return this;
        }

        public Editor put(String key, Long value){
            longs.put(key, value);
            return this;
        }

        public Editor put(String key, Double value){
            doubles.put(key, value);
            return this;
        }

        public Editor put(String key, Boolean value){
            booleans.put(key, value);
            return this;
        }

        public void apply(){
            SXMLObject object = new SXMLObject();
            object.setRootTagName("map");

            strings.forEach((s, s2) -> {
                SXMLObject string = new SXMLObject();
                string.setRootTagName("string");
                string.putAttr("name", s);
                string.setInnerData(s2);
                object.putInnerObject(string);
            });
            ints.forEach((s, integer) -> {
                SXMLObject string = new SXMLObject();
                string.setRootTagName("int");
                string.putAttr("name", s);
                string.putAttr("value", integer);
                object.putInnerObject(string);
            });
            longs.forEach((s, aLong) -> {
                SXMLObject string = new SXMLObject();
                string.setRootTagName("long");
                string.putAttr("name", s);
                string.putAttr("value", aLong);
                object.putInnerObject(string);
            });
            doubles.forEach((s, aDouble) -> {
                SXMLObject string = new SXMLObject();
                string.setRootTagName("double");
                string.putAttr("name", s);
                string.putAttr("value", aDouble);
                object.putInnerObject(string);
            });
            booleans.forEach((s, aBoolean) -> {
                SXMLObject string = new SXMLObject();
                string.setRootTagName("boolean");
                string.putAttr("name", s);
                string.putAttr("value", aBoolean);
                object.putInnerObject(string);
            });

            try {
                JxLauncher.doOutputStream(confDir, confName, object.toString());
            } catch (JxIOException e){
                e.printStackTrace();
                throw new JxConfigException(JxMessage.CONF_WRITE_FAILED);
            }
        }
    }
}
