package com.lanjian.farm.common;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by lj
 */
public class BaseApplication extends Application
{

    public static Context context;
    public List<AppCompatActivity> activitys = new LinkedList<>();

    @Override
    public void onCreate()
    {
        super.onCreate();
        //全局的上下文
        context = getApplicationContext();
    }

    //添加启动的activity
    public void addActivity(AppCompatActivity activity) {
        activitys.add(activity);
    }

    //移出activity
    public void removeActivity(AppCompatActivity activity) {
        activitys.remove(activity);
    }

    //关闭application
    public void closeApplication() {
        closeActivity();
    }


    //关闭activity
    public void closeActivity() {
        ListIterator<AppCompatActivity> listIterator = activitys.listIterator();
        while (listIterator.hasNext()) {
            AppCompatActivity activity = listIterator.next();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public static Context getContext() {
        return context;
    }

}
