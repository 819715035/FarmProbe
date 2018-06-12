package com.lanjian.farm.widget.scanView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;

import com.lanjian.farm.common.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leltek.viewer.model.Probe;

public class MLineView {
    final static Logger logger = LoggerFactory.getLogger(MLineView.class);
    private final PointF convexOrigin = new PointF();
    private Probe probe = Config.probe;
    private Paint paint = new Paint();
    private int r;
    private float maxTheta;
    private float lineX;
    private float lineTheta;
    private PointF point1;
    private PointF point2;
    private float minR;
    private float maxR;

    private static float sinF(float a) {
        return (float) Math.sin(a);
    }

    private static float cosF(float a) {
        return (float) Math.cos(a);
    }

    private static float atanF(float a) {
        return (float) Math.atan(a);
    }

    private static float calDist(float x, float y, float x2, float y2) {
        return (float) Math.sqrt(((x2 - x) * (x2 - x)) + (y2 - y)
                * (y2 - y));
    }

    public void init(Context context, Probe probe) {
        this.probe = probe;
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        point1 = new PointF();
        point2 = new PointF();
    }

    public void start(int canvasWidth, int canvasHeight) {
        float width = probe.getImageWidthPx();
        float height = probe.getImageHeightPx();
        if (r == 0) {
            lineX = width / 2;
            point1.y = 0;
            point2.y = height;
        } else {
            minR = r;
            maxR = probe.getImageHeightPx() - convexOrigin.y;
            lineTheta = 0;
            logger.debug("maxTheta=" + maxTheta);
        }
    }

    public void onDraw(Canvas canvas, float[] values) {
        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        float tranY = values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];

        if (r == 0) {
            point1.x = point2.x = lineX;
        } else {
            point1.x = convexOrigin.x + minR * sinF(lineTheta);
            point1.y = convexOrigin.y + minR * cosF(lineTheta);
            point2.x = convexOrigin.x + maxR * sinF(lineTheta);
            point2.y = convexOrigin.y + maxR * cosF(lineTheta);
        }
        float p1x = point1.x * scaleX + tranX;
        float p1y = point1.y * scaleY + tranY;
        float p2x = point2.x * scaleX + tranX;
        float p2y = point2.y * scaleY + tranY;

        canvas.drawLine(p1x, p1y, p2x, p2y, paint);


    }

    public boolean onTouchEvent(MotionEvent event, float[] values) {
        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        float tranY = values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = (x - tranX) / scaleX;
                y = (y - tranY) / scaleY;
                if (isInside(x, y, values)) {
                    if (r == 0) {
                        lineX = x;
                        probe.setScanlineMmode((int) (lineX / probe.getImageWidthPx() * 128));
                        logger.debug("@@lineX=" + lineX);
                    } else {
                        lineTheta = -calTheta(x, y);
                        probe.setScanlineMmode((int) ((lineTheta / maxTheta + 1) * 128 / 2)); // TODO 128 changed to the number of elements (elelment)
                        logger.debug("@@lineTheta=" + lineTheta);
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    public void setParams(float originXPx, float originYPx, float rPx) {
        r = Math.round(rPx);
        convexOrigin.x = originXPx;
        convexOrigin.y = originYPx;

        float theta = probe.getTheta();
        maxTheta = theta;
    }

    private float calTheta(float x, float y) {
        float diffX = convexOrigin.x - x;
        float diffY = Math.abs(y - convexOrigin.y);
        return atanF(diffX / diffY);
    }

    private boolean isInside(float x, float y, float[] values) {

        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        float tranY = values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];

        if (r == 0) {
            float width = probe.getImageWidthPx();
            float height = probe.getImageHeightPx();
            if ((x >= 0 && x <= width) && (y >= 0 && y <= height))
                return true;
        } else {
            float dist = calDist(x, y, convexOrigin.x, convexOrigin.y);
            if (dist < minR || dist > maxR)
                return false;
            float theta = calTheta(x, y);
            if (Math.abs(theta) > maxTheta)
                return false;
            return true;
        }
        return false;
    }

    enum TouchMode {
        NONE, MOVE, DRAG
    }
}    