package com.lanjian.farm.adapter;

import android.bluetooth.BluetoothDevice;
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
public class BlueToothDeviceListAdapter extends MyBaseAdapter<BluetoothDevice> {

    public BlueToothDeviceListAdapter(List<BluetoothDevice> t) {
        super(t);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null){
            holder = new Holder();
            convertView = View.inflate(parent.getContext(), R.layout.item_bluetoothdevice,null);
            holder.deviceNameTv = convertView.findViewById(R.id.deviceName_tv);
            holder.deviceIdTv = convertView.findViewById(R.id.deviceId_tv);
            holder.deviceResult = convertView.findViewById(R.id.deviceConnect_tv);
            convertView.setTag(holder);
        }
        holder = (Holder) convertView.getTag();
        BluetoothDevice device = list.get(position);
        holder.deviceNameTv.setText(device.getName());
        holder.deviceIdTv.setText(device.getAddress());
        if(device.getBondState()==BluetoothDevice.BOND_BONDED)
        {    //显示已配对设备
            holder.deviceResult.setText("已配对");
        }else if(device.getBondState()!=BluetoothDevice.BOND_BONDED)
        {
            holder.deviceResult.setText("未配对");
        }
        return convertView;
    }

    public class Holder{
        TextView deviceNameTv;
        TextView deviceIdTv;
        TextView deviceResult;
    }
}
