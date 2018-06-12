package com.lanjian.farm.util;

import android.widget.Toast;

import com.lanjian.farm.common.BaseApplication;

/**
 * Created by Administrator on 2017/8/25 0025.
 */

public class ToastUtils {
    private static Toast mToast = null;
    public static void showToastShort(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    public static void showToastLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.context, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

}
