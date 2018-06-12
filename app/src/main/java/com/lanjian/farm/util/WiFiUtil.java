package com.lanjian.farm.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @author lanjian
 * @email 819715035@qq.com
 * creat at $date$
 * description
 */
public class WiFiUtil {
    private WifiManager localWifiManager;//提供Wifi管理的各种主要API，主要包含wifi的扫描、建立连接、配置信息等
    //private List<ScanResult> wifiScanList;//ScanResult用来描述已经检测出的接入点，包括接入的地址、名称、身份认证、频率、信号强度等
    private List<WifiConfiguration> wifiConfigList;//WIFIConfiguration描述WIFI的链接信息，包括SSID、SSID隐藏、password等的设置
    private WifiInfo wifiConnectedInfo;//已经建立好网络链接的信息
    private WifiManager.WifiLock wifiLock;//手机锁屏后，阻止WIFI也进入睡眠状态及WIFI的关闭

    public WiFiUtil( Context context){
        localWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }

    //检查WIFI状态
    public int WifiCheckState(){
        return localWifiManager.getWifiState();
    }

    //开启WIFI
    public void WifiOpen(){
        if(!localWifiManager.isWifiEnabled()){
            localWifiManager.setWifiEnabled(true);
        }
    }

    //关闭WIFI
    public void WifiClose(){
        if(localWifiManager.isWifiEnabled()){
            localWifiManager.setWifiEnabled(false);
        }
    }

    //扫描wifi
    public void WifiStartScan(){
        localWifiManager.startScan();
    }

    //得到Scan结果
    public List<ScanResult> getScanResults(){
        WifiStartScan();
        return localWifiManager.getScanResults();//得到扫描结果
    }



    public int AddWifiConfig(String ssid,String pwd){
        int wifiId = -1;

        Log.i("AddWifiConfig","equals");
        WifiConfiguration wifiCong = new WifiConfiguration();
        wifiCong.SSID = "\""+ssid+"\"";//\"转义字符，代表"
        wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
        wifiCong.hiddenSSID = false;
        wifiCong.status = WifiConfiguration.Status.ENABLED;
        wifiId = localWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
        if(wifiId != -1){
            return wifiId;
        }


        return wifiId;
    }


    //创建一个WIFILock
    public void createWifiLock(String lockName){
        wifiLock = localWifiManager.createWifiLock(lockName);
    }

    //锁定wifilock
    public void acquireWifiLock(){
        wifiLock.acquire();
    }

    //解锁WIFI
    public void releaseWifiLock(){
        if(wifiLock.isHeld()){//判定是否锁定
            wifiLock.release();
        }
    }

    //得到建立连接的信息
    public void getConnectedInfo(){
        wifiConnectedInfo = localWifiManager.getConnectionInfo();
    }
    //得到连接的MAC地址
    public String getConnectedMacAddr(){
        return (wifiConnectedInfo == null)? "NULL":wifiConnectedInfo.getMacAddress();
    }

    //得到连接的名称SSID
    public String getConnectedSSID(){
        return (wifiConnectedInfo == null)? "NULL":wifiConnectedInfo.getSSID();
    }

    //得到连接的IP地址
    public int getConnectedIPAddr(){
        return (wifiConnectedInfo == null)? 0:wifiConnectedInfo.getIpAddress();
    }

    //得到连接的ID
    public int getConnectedID(){
        return (wifiConnectedInfo == null)? 0:wifiConnectedInfo.getNetworkId();
    }

    //得到密码类型
    public int getPassWordIntType(ScanResult scanResult){
        int pswType = 0;
        if (!TextUtils.isEmpty(scanResult.capabilities)) {

            if (scanResult.capabilities.contains("WPA") || scanResult.capabilities.contains("wpa")) {
                pswType = 2;

            } else if (scanResult.capabilities.contains("WEP") || scanResult.capabilities.contains("wep")) {
                pswType = 1;
            } else {
                pswType = 0;
            }
        }
        return pswType;
    }

    public String getPassWordTypeString(int passwordType){
        String pswType;
        if (passwordType==2) {
            pswType = "WPA";

        } else if (passwordType == 1) {
            pswType = "WEP";
        } else {
            pswType = "NONE";
        }
        return pswType;
    }

    //更新或创建WifiConfiguration
    public  boolean configWifiInfo(Context context, String SSID, String password, int type) {
        WifiConfiguration config = null;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig == null) continue;
                if (existingConfig.SSID.equals("\"" + SSID + "\"")  /*&&  existingConfig.preSharedKey.equals("\""  +  password  +  "\"")*/) {
                    config = existingConfig;
                    break;
                }
            }
        }
        if (config == null) {
            config = new WifiConfiguration();
        }
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        // 分为三种情况：0没有密码1用wep加密2用wpa加密
        if (type == 0) {// WIFICIPHER_NOPASSwifiCong.hiddenSSID = false;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (type == 1) {  //  WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == 2) {   // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int netId = config.networkId;
        if (netId == -1) {
            netId = wifiManager.addNetwork(config);
        }
        return wifiManager.enableNetwork(netId, true);
    }
}
