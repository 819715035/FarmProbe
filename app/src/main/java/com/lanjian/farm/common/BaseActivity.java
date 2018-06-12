package com.lanjian.farm.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.lanjian.farm.util.ScreenUtils;

/**
 * Created by zzz
 */
public class BaseActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BaseApplication application = (BaseApplication) getApplication();
        //该Activity总是调整屏幕的大小以便留出软键盘的空间  //当该Activity主窗口获取焦点时，软键盘也总是被隐藏的
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        application.addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        BaseApplication application = (BaseApplication) getApplication();
        application.removeActivity(this);
    }

    /**
     * Activity跳转
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * Activity跳转
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        openActivity(pClass, pBundle, null);
    }

    /**
     * Activity跳转
     *
     * @param pClass
     * @param pBundle
     * @param uri
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle, Uri uri) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    /**
     * Activity跳转
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * Activity跳转
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        openActivity(pAction, pBundle, null);
    }

    /**
     * Activity跳转
     *
     * @param pAction
     * @param pBundle
     * @param uri
     */
    protected void openActivity(String pAction, Bundle pBundle, Uri uri) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }
}
