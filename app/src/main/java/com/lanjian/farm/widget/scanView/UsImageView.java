package com.lanjian.farm.widget.scanView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import leltek.viewer.model.Probe;

public class UsImageView extends AppCompatImageView {
    public final static String ACTION_TOUCH = "com.leltek.intent.action.TOUCH";
    final static Logger logger = LoggerFactory.getLogger(UsImageView.class);
    static final int CLICK = 3;
    static final int cb_level = 256;
    static final int cb_width = 40;
    static final int cb_height = 1;
    static final int cb_top_margin = 100;
    static final int cb_right_margin = 50;
    private static int colorRMapArr[] = {77, 77, 75, 73, 72, 72, 72, 71, 70, 68, 67, 66, 66, 65, 64, 64, 63, 62, 60, 59, 58, 58, 57, 56, 56, 55, 55, 53, 52, 51, 51, 50, 49, 48, 48, 46, 44, 43, 42, 42, 42, 41, 40, 39, 38, 36, 35, 35, 35, 35, 34, 32, 30, 29, 29, 29, 28, 27, 26, 25, 25, 24, 23, 21, 20, 19, 19, 19, 18, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 4, 3, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 18, 72, 109, 113, 110, 113, 115, 117, 119, 122, 126, 131, 133, 136, 138, 141, 144, 146, 149, 153, 156, 159, 162, 164, 167, 170, 174, 177, 181, 184, 188, 191, 195, 198, 203, 208, 213, 215, 216, 218, 220, 221, 221, 223, 224, 225, 226, 226, 226, 226, 226, 227, 227, 228, 228, 229, 229, 229, 229, 229, 230, 230, 231, 231, 231, 231, 231, 231, 231, 232, 232, 232, 233, 235, 236, 235, 234, 233, 233, 234, 235, 235, 235, 236, 237, 238, 238, 238, 238, 238, 239, 239, 240, 241, 242, 242, 242, 242, 242, 242, 242, 243, 243, 244, 244, 245, 245, 246, 246, 246, 246, 246, 247, 248, 248, 248, 249, 250, 250, 251, 251, 252, 252, 253, 253, 253, 253, 253};
    private static int colorGMapArr[] = {245, 245, 243, 241, 238, 235, 233, 231, 228, 224, 220, 219, 217, 214, 211, 208, 206, 203, 199, 195, 192, 189, 187, 186, 183, 181, 178, 174, 170, 168, 165, 163, 160, 158, 156, 153, 148, 145, 142, 140, 138, 135, 132, 127, 124, 122, 120, 118, 116, 113, 109, 106, 103, 100, 97, 94, 92, 91, 88, 86, 82, 79, 75, 73, 70, 68, 65, 62, 60, 57, 54, 51, 48, 45, 43, 40, 37, 33, 30, 26, 22, 17, 13, 10, 8, 6, 3, 2, 2, 2, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 3, 5, 7, 10, 15, 19, 22, 27, 32, 36, 39, 42, 44, 46, 49, 53, 56, 59, 61, 64, 67, 70, 72, 75, 78, 82, 84, 87, 90, 92, 95, 97, 101, 104, 108, 111, 114, 117, 119, 122, 124, 127, 131, 134, 137, 139, 141, 144, 147, 151, 154, 157, 159, 162, 165, 167, 170, 173, 177, 180, 183, 185, 188, 190, 193, 195, 199, 203, 207, 209, 211, 213, 216, 219, 221, 224, 228, 231, 234, 237, 239, 241, 243};
    private static int colorBMapArr[] = {254, 254, 254, 254, 253, 252, 252, 251, 251, 250, 249, 248, 248, 248, 247, 246, 246, 244, 243, 242, 241, 241, 242, 242, 241, 240, 239, 238, 237, 236, 236, 235, 234, 234, 234, 233, 231, 230, 230, 231, 231, 230, 230, 229, 228, 227, 227, 227, 226, 225, 224, 223, 222, 222, 221, 220, 220, 221, 221, 220, 220, 219, 218, 217, 217, 217, 217, 216, 215, 215, 215, 215, 214, 214, 214, 214, 214, 213, 212, 210, 209, 208, 207, 207, 207, 206, 205, 204, 202, 201, 200, 198, 197, 195, 192, 188, 183, 179, 174, 169, 164, 160, 156, 151, 146, 141, 138, 135, 132, 129, 124, 119, 115, 112, 109, 106, 104, 100, 96, 91, 86, 82, 78, 75, 72, 69, 67, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0};
    final boolean DEBUG = false;
    public float saveScale = 1f;
    public boolean scanOn = true;
    public boolean centerLineOn = false;
    public boolean keyboardOn = false;
    protected float origWidth, origHeight;
    Matrix matrix;
    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 4f;
    float[] m;
    int viewWidth, viewHeight;
    int oldMeasuredWidth, oldMeasuredHeight;
    ScaleGestureDetector mScaleDetector;
    GestureDetector mGestureDetector;
    private Context mContext = null;
    private int r;
    private Matrix fitMatrix;
    private Paint rulerPaint, centerPaint, textPaint, textLengthPaint;
    private Rect textLengthBounds;
    private int startX = 6;
    private int canvasHeight = 0;
    private String unit = " ";
    private int scaleWidth = 10;
    private RoiView roiView = new RoiView();
    private MLineView mLineView = new MLineView();
    private boolean measureOn = false;
    private boolean roiOn = false;
    private boolean mLineOn = false;
    private TouchMode mode = TouchMode.NONE;
    private Probe probe;
    private Float newDepth;
    private String stringDepth;
    private Float[] allDepth;
    private int indexDepth;
    private float tipOffset;
    private boolean hasWindowFocus = false;

    public UsImageView(Context context) {
        super(context);
    }

    public UsImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context, Probe probe) {
        super.setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mGestureDetector = new GestureDetector(context, new GestureListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        this.probe = probe;
        mContext = context;
        rulerPaint = new Paint();
        rulerPaint.setStyle(Paint.Style.STROKE);
        rulerPaint.setStrokeWidth(6);
        rulerPaint.setAntiAlias(false);
        rulerPaint.setColor(Color.WHITE);
        centerPaint = new Paint();
        centerPaint.setStyle(Paint.Style.STROKE);
        centerPaint.setStrokeWidth(8);
        centerPaint.setAntiAlias(false);
        centerPaint.setColor(Color.WHITE);
        textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(0);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(32);
        textPaint.setColor(Color.WHITE);
        textLengthPaint = new TextPaint();
        textLengthPaint.setStyle(Paint.Style.FILL);
        textLengthPaint.setStrokeWidth(textPaint.getTextSize() / 10);
        textLengthPaint.setAntiAlias(true);
        textLengthPaint.setTextSize(textPaint.getTextSize() * 2);
        textLengthPaint.setShadowLayer(5.0f, textPaint.getTextSize() / 5, textPaint.getTextSize() / 5, Color.BLACK);
        textLengthPaint.setColor(Color.WHITE);


        initRoi();
        initMLine();

        textLengthBounds = new Rect();
        allDepth = probe.getAllDepth();
        newDepth = probe.getDepth();

        setParams();

        setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (keyboardOn) {
                    Intent intent = new Intent(ACTION_TOUCH);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    return true;
                }
                mScaleDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);
                PointF curr = new PointF(event.getX(), event.getY());
                float[] values = new float[9];
                getImageMatrix().getValues(values);

                float tranX = values[Matrix.MTRANS_X];
                float scaleX = values[Matrix.MSCALE_X];
                float tranY = values[Matrix.MTRANS_Y];
                float scaleY = values[Matrix.MSCALE_Y];

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mLineOn) {
                            mLineView.onTouchEvent(event, values);
                        }
                        last.set(curr);
                        start.set(last);

                        if (start.x < textLengthBounds.width()) {
                            mode = TouchMode.DRAG_DEPTH;
                        } else {
                            mode = TouchMode.DRAG;
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == TouchMode.ROI) {
                            if (roiView.onTouchEvent(event, values)) {
                                invalidate();
                                return true;
                            }
                        }
                        if (mode == TouchMode.DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);
                        }

                        if (mode == TouchMode.DRAG_DEPTH) {
                            if (curr.x < textLengthBounds.width()) {
                                int newIndexDepth = Math.round(indexDepth + allDepth.length * (start.y - curr.y) / canvasHeight * 2);
                                if (newIndexDepth < 0) newIndexDepth = 0;
                                else if (newIndexDepth >= allDepth.length)
                                    newIndexDepth = allDepth.length - 1;
                                newDepth = allDepth[newIndexDepth];
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK) {
                            performClick();
                        } else {
                            if ((mode == TouchMode.DRAG_DEPTH) && (curr.x < textLengthBounds.width()) && (newDepth != probe.getDepth())) {
                                fitScreen();
                                probe.setDepth(newDepth);
                            }
                        }
                        mode = TouchMode.NONE;
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = TouchMode.NONE;
                        break;
                }

                setImageMatrix(matrix);
                invalidate();
                return true; // indicate event was handled
            }

        });
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.hasWindowFocus = hasWindowFocus;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (DEBUG) logger.debug("onMeasure()");

        int imgWidth = probe.getImageWidthPx();
        int imgHeight = probe.getImageHeightPx();

        if (DEBUG)
            logger.debug(String.format("viewWidth=%d, viewHeight=%d", viewWidth, viewHeight));
        if (DEBUG) logger.debug(String.format("imgWidth=%d, imgHeight=%d", imgWidth, imgHeight));

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;

            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);

            fitMatrix = new Matrix(getImageMatrix());
        }
        //fixTrans();
    }

    public float[] getUsImageMatrixValues() {
        float[] values = new float[9];
        getImageMatrix().getValues(values);
        return values;
    }

    public Matrix getUsImageMatrix() {
        return getImageMatrix();
    }

    public void setUsImageMatrix(Matrix matrix) {
        setImageMatrix(matrix);
    }

    public void initRoi() {
        roiView.init(mContext, probe);
    }

    public void startRoi() {
        if (roiOn)
            return;
        roiView.start(getWidth(), getHeight());
        roiOn = true;
        invalidate();
    }

    public void stopRoi() {
        roiOn = false;
        invalidate();
    }

    public void updateRoi(float newAngle) {
        if (r == 0) {
            roiView.linearColorAngleUpdate(newAngle);
            invalidate();
        }
    }

    private void drawRoi(Canvas canvas) {
        if (roiOn && scanOn) {
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            roiView.onDraw(canvas, values);
        }
    }

    public void initMLine() {
        mLineView.init(mContext, probe);
    }

    public void startMLine() {
        if (mLineOn)
            return;
        mLineView.start(getWidth(), getHeight());
        mLineOn = true;
        invalidate();
    }

    public void stopMLine() {
        mLineOn = false;
        invalidate();
    }

    private void drawMLine(Canvas canvas) {
        if (mLineOn && scanOn) {
            float[] values = new float[9];
            getImageMatrix().getValues(values);
            mLineView.onDraw(canvas, values);
        }
    }

    private void drawGrayBar(Canvas canvas) {
        int cb_start_x = canvas.getWidth() - cb_width - cb_right_margin;
        int cb_start_y = cb_top_margin;
        int startY = cb_start_y;
        for (int i = cb_level - 1; i >= 0; i--) {
            Paint paint = new Paint();
            paint.setColor(Color.rgb(i, i, i));
            canvas.drawRect(cb_start_x, startY, cb_start_x + cb_width, startY + cb_height, paint);
            startY += cb_height;
        }
    }

    private void drawColorBar(Canvas canvas) {
        int cb_start_x = canvas.getWidth() - cb_width - cb_right_margin;
        int cb_start_y = cb_top_margin;
        int startY = cb_start_y;
        for (int i = cb_level - 1; i >= 0; i--) {
            Paint paint = new Paint();
            paint.setColor(Color.rgb(colorRMapArr[i], colorGMapArr[i], colorBMapArr[i]));
            canvas.drawRect(cb_start_x, startY, cb_start_x + cb_width, startY + cb_height, paint);
            startY += cb_height;
        }

        String unit = "cm/s";
        Rect bounds = new Rect();
        // Measure the text rectangle to get the height
        textPaint.getTextBounds(unit, 1, 2, bounds);
        int y = cb_top_margin + (cb_level * cb_height - unit.length() * bounds.height()) / 2;
        int x = cb_start_x - bounds.width() - 5;
        int fontHeight = bounds.height() + 5;
        for (char c : unit.toCharArray()) {
            canvas.drawText("" + c, x, y, textPaint);
            y += fontHeight;
        }
        float speed = probe.getColorMaxFlowSpeed();
        String upBound = String.format(Locale.ENGLISH, "%.1f", speed);
        String lowBound = String.format(Locale.ENGLISH, "%.1f", -speed);

        textPaint.getTextBounds(upBound, 0, 1, bounds);
        x = cb_start_x;
        y = cb_top_margin - 5;
        canvas.drawText(upBound, x, y, textPaint);
        y = cb_top_margin + cb_level * cb_height + bounds.height() + 5;
        canvas.drawText(lowBound, x, y, textPaint);
    }


    public void toggleCenterLine() {
        centerLineOn = !centerLineOn;
        invalidate();
    }


    public void fitImage() {
        setImageMatrix(fitMatrix);
    }

    public void setParams(float originXPx, float originYPx, float rPx) {
        r = Math.round(rPx);
        roiView.setParams(originXPx, originYPx, rPx);
        mLineView.setParams(originXPx, originYPx, rPx);
        tipOffset = originYPx + rPx;
    }

    public void setParams() {
        indexDepth = java.util.Arrays.binarySearch(allDepth, newDepth);
        stringDepth = String.format("%.1f", newDepth);
        textLengthPaint.getTextBounds(stringDepth, 0, stringDepth.length(), textLengthBounds);
        setParams(probe.getOriginXPx(), probe.getOriginYPx(), probe.getRPx());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRuler(canvas);
        drawMeasure(canvas);
        drawRoi(canvas);
        drawMLine(canvas);
        if (probe.getMode() == Probe.EnumMode.MODE_B) {
            drawGrayBar(canvas);
        } else if (probe.getMode() == Probe.EnumMode.MODE_C) {
            drawColorBar(canvas);
        }
    }

    private void drawRuler(Canvas canvas) {

        if (getDrawable() == null || !hasWindowFocus)
            return;

        float[] values = new float[9];
        getImageMatrix().getValues(values);
        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        int tranY = (int) values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];
        canvasHeight = canvas.getHeight();
        float imgHeight = probe.getImageHeightPx();
//        float theta = probe.getTheta();
//        float tipOffset = (r - r * (float) Math.cos(theta));
        int pxPerCm = (int) getPxPerCm(canvas);
        int offsetY = (int) (tipOffset * scaleY);
        int startY;
        int startCm = 0;

        if (tranY > 0) {
            startY = tranY + offsetY;
        } else {
            tranY = -tranY;
            startY = (int) (pxPerCm - (tranY % pxPerCm) + offsetY);
            startY = startY % pxPerCm;
            if (tranY > offsetY) {
                tranY -= offsetY;
                startCm = (int) (tranY / pxPerCm);
                if ((tranY % pxPerCm) != 0)
                    startCm++;
            }
        }

        for (int i = 0; i <= probe.getDepth(); i++) {
            canvas.drawLine(startX, startY, startX + scaleWidth, startY, rulerPaint);
            if (centerLineOn) {
                int imgWidth = probe.getImageWidthPx();
                float centeX = imgWidth / 2 * scaleX + tranX;
                canvas.drawLine(centeX, startY, centeX + scaleWidth, startY, centerPaint);
            }
            canvas.drawText(startCm + unit, startX + scaleWidth + 10, startY, textPaint);
            startY += pxPerCm;
            startCm++;
            if (startY > (canvasHeight - textLengthBounds.height() * 1.5))
                break;
        }
        Float showedDepath = (mode == TouchMode.DRAG_DEPTH) ? newDepth : probe.getDepth();
        stringDepth = String.format("%.1f", showedDepath);
        canvas.drawText(stringDepth + unit, 0, canvasHeight - textLengthBounds.height() / 4, textLengthPaint);
    }

    private double getPxPerCm(Canvas canvas) {
        float[] values = new float[9];
        getImageMatrix().getValues(values);
        float scaleY = values[Matrix.MSCALE_Y];
        float imgHeight = probe.getImageHeightPx();
        float theta = probe.getTheta();
        float tipOffset = (r - r * (float) Math.cos(theta));
        double pxPerCm = (imgHeight - tipOffset) * scaleY / probe.getDepth();
        return pxPerCm;
    }

    private double getCmPerPx(Canvas canvas) {
        double pxPerCm = getPxPerCm(canvas);
        return (1f / pxPerCm);
    }

    private void drawMeasure(Canvas canvas) {
        if (!measureOn)
            return;
        double cmPerPx = getCmPerPx(canvas);
        if (cmPerPx == 0)
            return;
        float[] values = new float[9];
        getImageMatrix().getValues(values);
    }

    void fitScreen() {
        if (saveScale != 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;

            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
            saveScale = 1;
        }
    }

    enum TouchMode {
        NONE, DRAG, ZOOM, MEASURE, ANNOTATE, ELLIPSE, ROI, MMLINE, DRAG_DEPTH
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = TouchMode.ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            float X = event.getX();
            float Y = event.getY();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            logger.debug("onLongPress");
        }
    }
}
