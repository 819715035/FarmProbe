package com.lanjian.farm.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class LogUtils {

    private LogUtils() {
    }

    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;

    //得到当前类名及方法名
    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        return tag;
    }



    public static void d(String content) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, content, tr);

    }

    public static void e(String content) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.e(tag, content, tr);
    }

    public static void i(String content) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.i(tag, content, tr);
    }

    public static void v(String content) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.v(tag, content, tr);
    }

    public static void w(String content) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.w(tag, tr);
    }

    //自定义标签
    public static void myD(String tag, String content){
        if (!allowW)
            return;
        Log.d(tag, content);
    }
    public static void myE(String tag, String content){
        if (!allowW)
            return;
        Log.e(tag, content);
    }
    public static void myI(String tag, String content){
        if (!allowW)
            return;
        Log.i(tag, content);
    }
    public static void myV(String tag, String content){
        if (!allowW)
            return;
        Log.v(tag, content);
    }
    public static void myW(String tag, String content){
        if (!allowW)
            return;
        Log.w(tag, content);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

}
