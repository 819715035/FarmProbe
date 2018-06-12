package com.lanjian.farm.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.lanjian.farm.R;
import com.lanjian.farm.common.BaseActivity;
import com.lanjian.farm.common.Config;

import leltek.viewer.model.Probe;

import static com.lanjian.farm.common.Config.probe;

public class ProbeScanActivity extends BaseActivity implements Probe.ScanListener, Probe.CineBufferListener, Probe.InfoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_probe_scan);
        initData();
    }

    private void initData() {
        probe = Config.probe;
        probe.setScanListener(this);
        probe.setCineBufferListener(this);
        probe.setInfoListener(this);
        probe.switchToDefaultMode();
    }

    @Override
    public void onModeSwitched(Probe.EnumMode mode) {

    }

    @Override
    public void onModeSwitchingError() {

    }

    @Override
    public void onConnectionError() {

    }

    @Override
    public void onScanStarted() {

    }

    @Override
    public void onScanStopped() {

    }

    @Override
    public void onNewFrameReady(Probe.Frame frame, Bitmap bitmap) {

    }

    @Override
    public void onNewMmodeReady(byte[] line) {

    }

    @Override
    public void onDepthSet(Float newDepth) {

    }

    @Override
    public void onDepthSetError(Float oldDepth) {

    }

    @Override
    public void onFreqSet(Float newFreq) {

    }

    @Override
    public void onFreqSetError(Float oldFreq) {

    }

    @Override
    public void onTgcTableBmodeSet(Integer newTgcTableBmode) {

    }

    @Override
    public void onTgcTableBmodeSetError(Integer oldTgcTableBmode) {

    }

    @Override
    public void onTgcTableCmodeSet(Integer newTgcTableCmode) {

    }

    @Override
    public void onTgcTableCmodeSetError(Integer oldTgcTableCmode) {

    }

    @Override
    public void onScanlineMmodeSet(Integer newScanlineMmode) {

    }

    @Override
    public void onScanlineMmodeSetError(Integer oldScanlineMmode) {

    }

    @Override
    public void onColorPrfSet(Float newColorPrf) {

    }

    @Override
    public void onColorPrfSetError(Float oldColorPrf) {

    }

    @Override
    public void onColorSensitivitySet(Long newColorSensitivity) {

    }

    @Override
    public void onColorSensitivitySetError(Long oldColorSensitivity) {

    }

    @Override
    public void onColorAngleSet(Float newColorAngle) {

    }

    @Override
    public void onColorAngleSetError(Float oldColorAngle) {

    }

    @Override
    public void onImageBufferOverflow() {

    }

    @Override
    public void onCineBufferCountIncreased(int newCineBufferCount) {

    }

    @Override
    public void onCineBufferCleared() {

    }

    @Override
    public void onBatteryLevelChanged(int newBatteryLevel) {

    }

    @Override
    public void onBatteryLevelTooLow(int BatteryLevel) {

    }

    @Override
    public void onTemperatureChanged(int newTemperature) {

    }

    @Override
    public void onTemperatureOverHeated(int temperature) {

    }

    @Override
    public void onButtonPressed(int button) {

    }

    @Override
    public void onButtonReleased(int button) {

    }

    @Override
    public void onGSensorReceive(int gSensor) {

    }
}
