package com.lanjian.farm.util;

import android.content.SharedPreferences;

import com.lanjian.farm.common.BaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/25 0025.
 */


public class SPUtils {

    //存储的sharedpreferences文件名
    private static final String FILE_NAME = "appData";
    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object)
    {

        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else
        {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public static int getValue(String key, int defValue) {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    public static boolean getValue( String key, boolean defValue) {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static String getValue(String key, String defValue) {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
    public static float getValue(String key, float defValue) {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        float value = sp.getFloat(key, defValue);
        return value;
    }

    public static long getValue(String key, long defValue) {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        long value = sp.getLong(key, defValue);
        return value;
    }



    /**
     * 移除某个key值已经对应的值
     * @param key
     */
    public static void remove( String key)
    {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear()
    {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     * @param key
     * @return
     */
    public static boolean contains(String key)
    {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll()
    {
        SharedPreferences sp = BaseApplication.context.getSharedPreferences(FILE_NAME,
                BaseApplication.context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     *
     */
    private static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod()
        {
            try
            {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e)
            {
            } catch (IllegalAccessException e)
            {
            } catch (InvocationTargetException e)
            {
            }
            editor.commit();
        }
    }
}
