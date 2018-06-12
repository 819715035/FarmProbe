package com.lanjian.farm.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter{

    public List<T> list;

    public MyBaseAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getMyView(position,convertView,parent);
        return view;
    }

    public abstract View getMyView(int position, View convertView, ViewGroup parent);

    public void setData(List<T> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
