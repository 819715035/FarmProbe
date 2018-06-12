package com.lanjian.farm.adapter;

import android.net.wifi.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanjian.farm.R;
import com.lanjian.farm.common.MyBaseAdapter;
import com.lanjian.farm.util.LogUtils;

import java.util.List;

/**
 * @author lanjian
 * @email 819715035@qq.com
 * creat at $date$
 * description
 */
public class WifiListAdapter extends MyBaseAdapter<ScanResult> {

    public WifiListAdapter(List<ScanResult> list) {
        super(list);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView==null){
            myHolder = new MyHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_wifilist_layout,null);
            myHolder.ssidTv = convertView.findViewById(R.id.ssid_tv);
            myHolder.levelTv = convertView.findViewById(R.id.level_tv);
            myHolder.psdTypeTv = convertView.findViewById(R.id.psdType_tv);
            convertView.setTag(myHolder);
        }else{
            myHolder = (MyHolder) convertView.getTag();
        }
        if (position%2==0){
            convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.papayawhip));
        }else{
            convertView.setBackgroundColor(parent.getContext().getResources().getColor(R.color.lightgoldenrodyellow));
        }
        ScanResult scanResult = list.get(position);
        myHolder.ssidTv.setText(scanResult.SSID);
        myHolder.levelTv.setText(scanResult.level+"");
        String pswType = "NONE";
        if (!TextUtils.isEmpty(scanResult.capabilities)) {

            if (scanResult.capabilities.contains("WPA") || scanResult.capabilities.contains("wpa")) {
                pswType = "WPA";

            } else if (scanResult.capabilities.contains("WEP") || scanResult.capabilities.contains("wep")) {
                pswType = "WEP";
            } else {
                pswType = "NONE";
            }
        }
        myHolder.psdTypeTv.setText(pswType);
        return convertView;
    }

    class MyHolder{
        TextView ssidTv;
        TextView levelTv;
        TextView psdTypeTv;
    }
}
