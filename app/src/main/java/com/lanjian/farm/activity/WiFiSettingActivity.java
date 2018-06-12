package com.lanjian.farm.activity;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.lanjian.farm.R;
import com.lanjian.farm.adapter.WiFiSpinnerAdapter;
import com.lanjian.farm.adapter.WifiListAdapter;
import com.lanjian.farm.common.BaseActivity;
import com.lanjian.farm.common.BaseDialog;
import com.lanjian.farm.common.Config;
import com.lanjian.farm.common.CustomTitleBar;
import com.lanjian.farm.util.LogUtils;
import com.lanjian.farm.util.SPUtils;
import com.lanjian.farm.util.WiFiUtil;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WiFiSettingActivity extends BaseActivity {

    @BindView(R.id.title_layout)
    CustomTitleBar titleLayout;
    @BindView(R.id.wifi_lv)
    ListView wifiLv;
    @BindView(R.id.default_connect_wifi_tv)
    TextView defaultConnectWifiTv;
    private WiFiUtil wiFiUtil;
    private WifiListAdapter adapter;
    private BaseDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setting);
        ButterKnife.bind(this);
        initData();
        setListener();
    }

    private void setListener() {
        titleLayout.setTitleClickListener(new CustomTitleBar.TitleClickListener() {
            @Override
            public void onLeftClick1() {
                finish();
            }

            @Override
            public void onRightClick1() {
                //扫描wifi
                adapter.setData(wiFiUtil.getScanResults());
            }

            @Override
            public void onLeftClick2() {

            }

            @Override
            public void onRightClick2() {
                //添加wifi
                showConnectedWiFiDialog("",0);
            }
        });
        wifiLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ScanResult scanResult = (ScanResult) wifiLv.getItemAtPosition(position);
                showConnectedWiFiDialog(scanResult.SSID,wiFiUtil.getPassWordIntType(scanResult));
            }
        });
    }

    private void showConnectedWiFiDialog(final String ssid, final int passwordType) {
        dialog = new BaseDialog.Builder(WiFiSettingActivity.this,
                R.layout.dialog_connect_wifi_layout, true,
                new BaseDialog.Builder.OnShowDialogListener() {
                    @Override
                    public void onShowDialog(View layout) {
                        final EditText ssidEt = layout.findViewById(R.id.ssid_et);
                        ssidEt.setText(ssid);
                        final EditText passwordEt = layout.findViewById(R.id.password_et);
                        final Spinner spinner = layout.findViewById(R.id.passwordType_sp);
                        List<String> spinnerList = new ArrayList<>();
                        spinnerList.add("None");
                        spinnerList.add("WEP");
                        spinnerList.add("WPA");
                        WiFiSpinnerAdapter wifiSpinnerAdapter = new WiFiSpinnerAdapter(spinnerList);
                        spinner.setAdapter(wifiSpinnerAdapter);
                        spinner.setSelection(passwordType);
                        Button cancelBtn = layout.findViewById(R.id.cancel_btn);
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        Button saveBtn = layout.findViewById(R.id.save_btn);
                        saveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                                //保存
                                //连接wifi
                                wiFiUtil.configWifiInfo(WiFiSettingActivity.this,
                                        ssidEt.getText().toString().trim(), passwordEt.getText().toString().trim(),
                                        spinner.getSelectedItemPosition());
                                SPUtils.put(Config.WIFI_SSID,ssidEt.getText().toString().trim());
                                SPUtils.put(Config.WIFI_PWD, passwordEt.getText().toString().trim());
                                SPUtils.put(Config.WIFI_PWD_type, spinner.getSelectedItemPosition());
                            }
                        });
                    }
                }).create();
        dialog.show();
    }

    private void initData() {
        wiFiUtil = new WiFiUtil(this);
        adapter = new WifiListAdapter(wiFiUtil.getScanResults());
        LogUtils.e("wiFiUtil.getScanResults()=" + wiFiUtil.getScanResults().size());
        wifiLv.setAdapter(adapter);
        defaultConnectWifiTv.setText("默认连接的wifi:"+SPUtils.getValue(Config.WIFI_SSID,""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        wiFiUtil.WifiOpen();
    }
}
