package com.lanjian.farm.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lanjian.farm.R;
import com.lanjian.farm.common.BaseActivity;
import com.lanjian.farm.common.Config;
import com.lanjian.farm.common.CustomTitleBar;
import com.lanjian.farm.util.LogUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leltek.viewer.model.Probe;
import leltek.viewer.model.WifiProbe;

public class MainActivity extends BaseActivity implements Probe.SystemListener, Probe.InfoListener {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.title_layout)
    CustomTitleBar titleLayout;
    @BindView(R.id.connect_bluetooth_btn)
    Button connectBluetoothBtn;
    @BindView(R.id.connect_wifi_btn)
    Button connectWifiBtn;
    @BindView(R.id.connect_probe_btn)
    Button connectProbeBtn;
    @BindView(R.id.probe_setting_btn)
    Button probeSettingBtn;
    private Probe probe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initdata();
        setListener();
    }

    private void setListener() {
        titleLayout.setTitleClickListener(new CustomTitleBar.TitleClickListener() {
            @Override
            public void onLeftClick1() {

            }

            @Override
            public void onRightClick1() {

            }

            @Override
            public void onLeftClick2() {

            }

            @Override
            public void onRightClick2() {
                openActivity(WiFiSettingActivity.class);
            }
        });
    }

    private void initdata() {

        setBanner();
    }

    //设置轮播广告
    private void setBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        ArrayList<Integer> showImages = new ArrayList<>();
        showImages.add(R.mipmap.banner1);
        showImages.add(R.mipmap.banner2);
        showImages.add(R.mipmap.banner3);
        showImages.add(R.mipmap.banner4);
        showImages.add(R.mipmap.banner5);
        showImages.add(R.mipmap.banner6);
        //设置图片集合
        banner.setImages(showImages);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置标题集合（当banner样式有显示title时）
        ArrayList<String> titles = new ArrayList<>();
        titles.add("图片1");
        titles.add("图片2");
        titles.add("图片3");
        titles.add("图片4");
        titles.add("图片5");
        titles.add("图片6");
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({R.id.connect_bluetooth_btn, R.id.connect_wifi_btn, R.id.connect_probe_btn, R.id.probe_setting_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.connect_bluetooth_btn:
                //连接蓝牙
                openActivity(BluetoothSettingActivity.class);
                break;
            case R.id.connect_wifi_btn:
                //连接wifi
                break;
            case R.id.connect_probe_btn:
                //连接探头
                connectProbe();
                break;
            case R.id.probe_setting_btn:
                //探头设置
                break;
        }
    }

    private void connectProbe() {
        String cfgRoot = "cfg";
        probe = WifiProbe.init(cfgRoot, this);
        probe.setSystemListener(this);
        probe.setInfoListener(this);
        if (probe.isConnected()) {
            LogUtils.e("isConnected");
            return;
        }
        if (!probe.isRequesting()) {
            LogUtils.e("!probe.isRequesting()");
            //初始化
            probe.initialize();
        }
    }

    @Override
    public void onInitializing(Integer percentage) {
        LogUtils.e("onInitializing:"+percentage);
    }

    @Override
    public void onInitialized() {
        Config.probe = probe;
        openActivity(ProbeScanActivity.class);
    }

    @Override
    public void onInitializationError(String message) {

    }

    @Override
    public void onInitializingLowVoltageError(String message) {

    }

    @Override
    public void onInitializingHiTemperatureError(String message) {

    }

    @Override
    public void onSystemError(String message) {

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

    //自动轮播图
    public class GlideImageLoader extends ImageLoader {
        private static final long serialVersionUID = -4629552771428843015L;

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */

            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);
            imageView.setImageResource((Integer) path);
        }

    }

}
