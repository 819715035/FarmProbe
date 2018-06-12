package com.lanjian.farm.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanjian.farm.R;
import com.lanjian.farm.util.DensityUtil;

/**
 * @author lanjian
 * @email 819715035@qq.com
 * creat at $date$
 * description
 */
public class CustomTitleBar extends RelativeLayout implements View.OnClickListener {


    private String leftTitle1;
    private String rightTitle1;
    private String leftTitle2;
    private String middleTitle;
    private String rightTitle2;

    private int leftTextColor1;
    private int middleTextColor;
    private int rightTextColor1;

    private float leftTextSize1;
    private float rightTextSize1;
    private float middleTextSize;

    private int leftTextColor2;
    private int rightTextColor2;

    private float leftTextSize2;
    private float rightTextSize2;

    private TextView tvLeft1;
    private TextView tvMiddle;
    private TextView tvRight1;
    private TextView tvLeft2;
    private TextView tvRight2;

    private int leftImage1;
    private int rightImage1;
    private int rightImage2;
    private int leftImage2;

    private TitleClickListener listener;

    public CustomTitleBar(Context context) {
        this(context, null);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar, defStyleAttr, 0);
        initView(array);
        array.recycle();
    }

    private void initView(TypedArray array) {

        LayoutInflater.from(getContext()).inflate(R.layout.title_layout, this);

        tvLeft1 = (TextView) findViewById(R.id.tvLeft1Title);
        tvLeft1.setOnClickListener(this);
        tvLeft2 = (TextView) findViewById(R.id.tvLeft2Title);
        tvLeft2.setOnClickListener(this);
        tvMiddle = (TextView) findViewById(R.id.tvMiddleTitle);
        tvMiddle.setOnClickListener(this);
        tvRight1 = (TextView) findViewById(R.id.tvRight1Title);
        tvRight1.setOnClickListener(this);
        tvRight2 = (TextView) findViewById(R.id.tvRight2Title);
        tvRight2.setOnClickListener(this);


        leftTitle1 = array.getString(R.styleable.CustomTitleBar_leftTitle1);
        leftTitle2 = array.getString(R.styleable.CustomTitleBar_leftTitle2);
        middleTitle = array.getString(R.styleable.CustomTitleBar_middleTitle);
        rightTitle1 = array.getString(R.styleable.CustomTitleBar_rightTitle1);
        rightTitle2 = array.getString(R.styleable.CustomTitleBar_rightTitle2);

        leftTextColor1 = array.getColor(R.styleable.CustomTitleBar_leftTextColor1, Color.GRAY);
        leftTextColor2 = array.getColor(R.styleable.CustomTitleBar_leftTextColor2, Color.GRAY);
        middleTextColor = array.getColor(R.styleable.CustomTitleBar_middleTextColor, Color.TRANSPARENT);
        rightTextColor1 = array.getColor(R.styleable.CustomTitleBar_rightTextColor1, Color.GRAY);
        rightTextColor2 = array.getColor(R.styleable.CustomTitleBar_rightTextColor2, Color.GRAY);

        leftImage1 = array.getResourceId(R.styleable.CustomTitleBar_leftImage1, 0);
        leftImage2 = array.getResourceId(R.styleable.CustomTitleBar_leftImage2, 0);
        rightImage1 = array.getResourceId(R.styleable.CustomTitleBar_rightImage1, 0);
        rightImage2 = array.getResourceId(R.styleable.CustomTitleBar_rightImage2, 0);


        leftTextSize1 = array.getDimension(R.styleable.CustomTitleBar_leftTextSize1, DensityUtil.dip2px(17));
        leftTextSize2 = array.getDimension(R.styleable.CustomTitleBar_leftTextSize2, DensityUtil.dip2px(17));
        rightTextSize1 = array.getDimension(R.styleable.CustomTitleBar_rightTextSize1, DensityUtil.dip2px( 17));
        rightTextSize2 = array.getDimension(R.styleable.CustomTitleBar_rightTextSize2, DensityUtil.dip2px(17));
        middleTextSize = array.getDimension(R.styleable.CustomTitleBar_middleTextSize, DensityUtil.dip2px(17));

        if (leftImage1 > 0) {
            setLeftImage1(leftImage1);
        } else {
            setLeftTitle1(leftTitle1);
        }
        if (leftImage2 > 0) {
            setLeftImage2(leftImage2);
        } else {
            setLeftTitle2(leftTitle2);
        }

        if (rightImage1 > 0) {
            setRightImage1(rightImage1);
        } else {
            setRightTitle1(rightTitle1);
        }
        if (rightImage2 > 0) {
            setRightImage2(rightImage2);
        } else {
            setRightTitle2(rightTitle2);
        }

        tvLeft1.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize1);
        tvLeft2.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize2);
        tvRight1.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize1);
        tvRight2.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize2);
        tvMiddle.setTextSize(TypedValue.COMPLEX_UNIT_PX, middleTextSize);


        setMiddleTitle(middleTitle);
        setLeftTextColor1(leftTextColor1);
        setLeftTextColor2(leftTextColor2);
        setMiddleTextColor(middleTextColor);
        setRightTextColor1(rightTextColor1);
        setRightTextColor2(rightTextColor2);
    }

    /**
     * @param size 单位sp
     */
    public void setLeftTextSize1(float size) {
        tvLeft1.setTextSize(size);
    }
    /**
     * @param size 单位sp
     */
    public void setLeftTextSize2(float size) {
        tvLeft2.setTextSize(size);
    }

    /**
     * @param size 单位sp
     */
    public void setMiddleTextSize(float size) {
        tvMiddle.setTextSize(size);
    }

    /**
     * @param size 单位sp
     */
    public void setRightTextSize1(float size) {
        tvRight1.setTextSize(size);
    }
    /**
     * @param size 单位sp
     */
    public void setRightTextSize2(float size) {
        tvRight2.setTextSize(size);
    }

    public void setLeftTextColor1(int color) {
        tvLeft1.setTextColor(color);
    }
    public void setLeftTextColor2(int color) {
        tvLeft2.setTextColor(color);
    }

    public void setMiddleTextColor(int color) {
        tvMiddle.setTextColor(color);
    }

    public void setRightTextColor1(int color) {
        tvRight1.setTextColor(color);
    }
    public void setRightTextColor2(int color) {
        tvRight2.setTextColor(color);
    }


    public void setLeftTitle1(String title) {
        tvLeft1.setText(title);
    }
    public void setLeftTitle2(String title) {
        tvLeft2.setText(title);
    }

    public void setRightTitle1(String title) {
        tvRight1.setText(title);
    }
    public void setRightTitle2(String title) {
        tvRight2.setText(title);
    }


    public void setMiddleTitle(int titleId) {
        tvMiddle.setText(titleId);
    }

    public void setMiddleTitle(String title) {
        tvMiddle.setText(title);
    }

    public void setLeftImage1(int leftImage) {

        setLeftTitle1(leftTitle1);
        Drawable drawable = getResources().getDrawable(leftImage);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft1.setCompoundDrawablePadding(DensityUtil.dip2px(8));
        tvLeft1.setCompoundDrawables(drawable, null, null, null);
    }
    public void setLeftImage2(int leftImage) {

        setLeftTitle2(leftTitle2);
        Drawable drawable = getResources().getDrawable(leftImage);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft2.setCompoundDrawablePadding(DensityUtil.dip2px( 8));
        tvLeft2.setCompoundDrawables(drawable, null, null, null);
    }
    public void setRightImage2(int rightImage2) {

        setRightTitle2(rightTitle2);
        Drawable drawable = getResources().getDrawable(rightImage2);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvRight2.setCompoundDrawablePadding(DensityUtil.dip2px(8));
        tvRight2.setCompoundDrawables(drawable, null, null, null);
    }
    public void setRightImage1(int rightImage1) {

        setRightTitle1(rightTitle1);
        Drawable drawable = getResources().getDrawable(rightImage1);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvRight1.setCompoundDrawablePadding(DensityUtil.dip2px( 8));
        tvRight1.setCompoundDrawables(drawable, null, null, null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLeft1Title:
                if (listener != null) {
                    listener.onLeftClick1();
                }
                break;
            case R.id.tvRight1Title:
                if (listener != null) {
                    listener.onRightClick1();
                }
                break;
            case R.id.tvLeft2Title:
                if (listener != null) {
                    listener.onLeftClick2();
                }
                break;
            case R.id.tvRight2Title:
                if (listener != null) {
                    listener.onRightClick2();
                }
                break;
        }
    }

    public void setTitleClickListener(TitleClickListener listener) {
        this.listener = listener;
    }


    public interface TitleClickListener {

        void onLeftClick1();

        void onRightClick1();
        void onLeftClick2();

        void onRightClick2();

    }

}
