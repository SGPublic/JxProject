package com.sgpublic.jxproject.extra;

import java.util.regex.Pattern;

public class JxLog {
    private static int grade = 1;
    private static String filter = "";

    public static final int GRADE_VERBOSE = 0;
    public static final int GRADE_DEBUG = 1;
    public static final int GRADE_INFO = 2;
    public static final int GRADE_WARN = 3;
    public static final int GRADE_ERROR = 4;
    public static final int GRADE_ASSERT = 5;

    public static void setGrade(int grade){
        if (grade >= 0 && grade <= 5){
            JxLog.grade = grade;
        }
    }

    public static void setFilter(String filter){
        JxLog.filter = filter;
    }

    public static void v(String tag, String message){
        if (grade <= GRADE_VERBOSE && (Pattern.matches(filter, tag) || filter.equals(""))){
            if (message == null){
                System.out.println("V/" + tag + ": (null)");
            } else {
                System.out.println("V/" + tag + ": " + message);
            }
        }
    }

    public static void d(String tag, String message){
        if (grade <= GRADE_DEBUG && (Pattern.matches(filter, tag) || filter.equals(""))){
            if (message == null){
                System.out.println("D/" + tag + ": (null)");
            } else {
                System.out.println("D/" + tag + ": " + message);
            }
        }
    }

    public static void i(String tag, String message){
        if (grade <= GRADE_INFO && (Pattern.matches(filter, tag) || filter.equals(""))){
            if (message == null){
                System.out.println("I/" + tag + ": (null)");
            } else {
                System.out.println("I/" + tag + ": " + message);
            }
        }
    }

    public static void w(String tag, String message){
        if (grade <= GRADE_WARN && (Pattern.matches(filter, tag) || filter.equals(""))){
            if (message == null){
                System.out.println("W/" + tag + ": (null)");
            } else {
                System.out.println("W/" + tag + ": " + message);
            }
        }
    }

    public static void e(String tag, String message){
        if (grade <= GRADE_ERROR && (Pattern.matches(filter, tag) || filter.equals(""))){
            if (message == null){
                System.out.println("\033[31mE/" + tag + ": (null)");
            } else {
                System.out.println("\033[31mE/" + tag + ": " + message);
            }
        }
    }

    public static void a(String tag, String message){
        if (grade <= GRADE_ASSERT && (Pattern.matches(filter, tag) || filter.equals(""))){
            if (message == null){
                System.out.println("\033[31mA/" + tag + ": (null)");
            } else {
                System.out.println("\033[31mA/" + tag + ": " + message);
            }
        }
    }
}
