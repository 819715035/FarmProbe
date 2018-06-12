package com.lanjian.farm.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanjian.farm.R;
import com.lanjian.farm.common.MyBaseAdapter;

import java.util.List;

/**
 * @author lanjian
 * @email 819715035@qq.com
 * creat at $date$
 * description
 */
public class WiFiSpinnerAdapter extends MyBaseAdapter<String> {

    public WiFiSpinnerAdapter(List<String> list) {
        super(list);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView==null){
            myHolder = new MyHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_wifi_passwordtype_layout,null);
            myHolder.passwordTypeTv = convertView.findViewById(R.id.wifipasswordType_tv);
            convertView.setTag(myHolder);
        }else{
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.passwordTypeTv.setText(list.get(position));
        return convertView;
    }

    class MyHolder{
        TextView passwordTypeTv;
    }
}
