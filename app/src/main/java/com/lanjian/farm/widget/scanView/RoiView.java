package com.lanjian.farm.widget.scanView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.lanjian.farm.R;
import com.lanjian.farm.util.LogUtils;

import java.util.ArrayList;

import leltek.viewer.model.Probe;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class RoiView {
    private float b0x;
    private float b0y;
    private float b1x;
    private float b1y;
    private float b2x;
    private float b2y;
    private float b3x;
    private float theta; //偏转的角度

    public enum TouchMode {
        NONE, MOVE, DRAG
    }
    private final static float rdRatio = (float) (Math.PI / 180);
    private final static float drRatio = (float) (180 / Math.PI);
    private final float minWidth = 100;
    private final PointF convexOrigin = new PointF();
    private final float roiDiffThetaMinLimit = 10 * rdRatio;
    private final float roiDepthMinLimit = 100;
    private Probe probe;
    private Paint paint = new Paint();
    private ArrayList<Ball> balls = new ArrayList<>();
    private Ball ball0;
    private Ball ball1;
    private Ball ball2;
    private Ball ball3;
    private int ballId;
    private int halfWidthOfBall;
    public TouchMode touchMode = TouchMode.NONE;
    private PointF roiStartMovingPoint = new PointF();
    private PointF convexMidRoi = new PointF();
    private float startMovingArc;
    private int canvasWidth;
    private int canvasHeight;
    private float roiWidth = 100;
    private float roiHeight = 100;
    private PointF roiStart = new PointF();
    private int r;
    private float maxTheta;
    private float roiStartTheta;
    private float roiDiffTheta;
    private float roiStartR;
    private float roiEndR;
    private float roiDepth;
    private float maxEndRho;
    private boolean isMoveInside;//是否在血流框内部，true为内部，false为外部
    public void init(Context context, Probe probe) {

        this.probe = probe;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.rec15x8);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        ball0 = new Ball(bitmap, new PointF());
        ball1 = new Ball(bitmap, new PointF());
        ball2 = new Ball(bitmap, new PointF());
        ball3 = new Ball(bitmap, new PointF());
        balls.add(ball0);
        balls.add(ball1);
        balls.add(ball2);
        balls.add(ball3);
    }

    public void linearColorAngleUpdate(float newAngle) {
        theta = newAngle;
        float minX, maxX;
        minX = min(ball0.getX(),ball3.getX());
        maxX = max(ball2.getX(),ball1.getX());

        float deltaX = (ball2.getY()-ball0.getY()) * probe.getColorAngleTan();
        if(deltaX>=0) {
            ball0.setX(minX);
            ball1.setX(maxX-deltaX);
            ball2.setX(maxX);
            ball3.setX(minX+deltaX);
        } else {
            ball0.setX(minX-deltaX);
            ball1.setX(maxX);
            ball2.setX(maxX+deltaX);
            ball3.setX(minX);
        }
        setLinearRoiData();
    }

    public void start(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        float width = probe.getImageWidthPx();
        float height = probe.getImageHeightPx();

        if (r == 0) {
            roiWidth = width / 3;
            roiHeight = height / 4;
            roiStart.x = (width - roiWidth) / 2;
            roiStart.y = height / 3;

            float deltaX = calDeltaXByAngle();
            float p0x = roiStart.x;
            float p0y = roiStart.y;
            float p1x = roiStart.x + roiWidth;
            float p1y = roiStart.y;
            float p2x = p1x + deltaX;
            float p2y = p0y + roiHeight;
            float p3x = p0x + deltaX;
            float p3y = p2y;

            ball0.setX(p0x);
            ball0.setY(p0y);
            ball1.setX(p1x);
            ball1.setY(p1y);
            ball2.setX(p2x);
            ball2.setY(p2y);
            ball3.setX(p3x);
            ball3.setY(p3y);
        } else {

            roiDiffTheta = maxTheta;
            //logger.debug("r:{}, roiDiffTheta:{}", r, roiDiffTheta);
            //血流框宽度为扫描区域的一半
            roiStartTheta = -maxTheta / 2;
            roiDepth = height / 4;
            //开始的深度
            roiStartR = r + height / 8;
            roiEndR = roiStartR + roiDepth;
            maxEndRho = probe.getImageHeightPx() - convexOrigin.y;

            float deltaX = roiStartR * sinF(roiStartTheta);
            float y = convexOrigin.y + (roiStartR * cosF(roiStartTheta));
            ball0.setX(convexOrigin.x + deltaX);
            ball0.setY(y);
            ball1.setX(convexOrigin.x - deltaX);
            ball1.setY(y);

            deltaX = roiEndR * sinF(roiStartTheta);
            y = convexOrigin.y + (roiEndR * cosF(roiStartTheta));
            ball2.setX(convexOrigin.x - deltaX);
            ball2.setY(y);
            ball3.setX(convexOrigin.x + deltaX);
            ball3.setY(y);

            setConvexMidRoi();
        }

        if (r == 0) {
            setLinearRoiData();
        } else {
            setConvexRoiData();
        }

        halfWidthOfBall = ball0.getWidthOfBall() / 2;
    }

    //默认是可移动状态
    private TouchMode isMove = TouchMode.MOVE;
    //实线时移动采集框，虚线时改变采集框大小
    //改变线的类型
    public void changeLineType(){
        if (isMove == TouchMode.MOVE){
            //移动采集框
            paint.setPathEffect(null);
            paint.setColor(Color.GREEN);
            paint.setPathEffect(new DashPathEffect(new float[]{5,5},5));
            isMove = TouchMode.DRAG;
        }else{
            paint.setPathEffect(null);
            isMove = touchMode.MOVE;
        }
    }
    //移动采集框或者改变大小
    public boolean moveOrChangeSize(MotionEvent eventEnd, float[] values){
        LogUtils.e("moveOrChangeSize");
        if (isMove == TouchMode.DRAG){
            touchMode = TouchMode.DRAG;
            //根据跟顶点的偏移量，重新设置滑动后的位置，让顶点相对滑动距离改变大小
            MotionEvent event = MotionEvent.obtain(eventEnd);
            event.offsetLocation(offsetX,offsetY);
            return onTouchEvent(event,values);
        }else if (isMove == TouchMode.MOVE){
            return onTouchEvent(eventEnd,values);
        }
        return true;
    }

    private float offsetX;
    private float offsetY;
    //cf模式时刚点击下去时的事件
    public void onDown(MotionEvent e, float[] values){
        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        float tranY = values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];

        float b0x = ball0.getX() * scaleX + tranX;
        float b0y = ball0.getY() * scaleY + tranY;
        float b1x = ball1.getX() * scaleX + tranX;
        float b1y = ball1.getY() * scaleY + tranY;
        float b2x = ball2.getX() * scaleX + tranX;
        float b2y = ball2.getY() * scaleY + tranY;
        float b3x = ball3.getX() * scaleX + tranX;
        float b3y = ball3.getY() * scaleY + tranY;
        float centerX = (b0x+b1x)/2;
        float centerY = (b0y+b3y)/2;
        //判断刚按下去的时候，跟顶点的距离
        if (e.getX()<centerX){
            if (e.getY()<centerY){
                this.ballId = 0;
                offsetX = b0x-e.getX();
                offsetY = b0y-e.getY();
            }else{
                this.ballId = 3;
                offsetX = b3x-e.getX();
                offsetY = b3y-e.getY();
            }
        }else{
            if (e.getY()<centerY){
                this.ballId = 1;
                offsetX = b1x-e.getX();
                offsetY = b1y-e.getY();
            }else{
                this.ballId = 2;
                offsetX = b2x-e.getX();
                offsetY = b2y-e.getY();
            }
        }
    }

    public void setIsMove(TouchMode mode){
        this.isMove = mode;
        touchMode = TouchMode.NONE;
        paint.setPathEffect(null);
    }
    public void onDraw(Canvas canvas, float[] values) {

        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        float tranY = values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];

        float b0x = ball0.getX() * scaleX + tranX;
        float b0y = ball0.getY() * scaleY + tranY;
        float b1x = ball1.getX() * scaleX + tranX;
        float b1y = ball1.getY() * scaleY + tranY;
        float b2x = ball2.getX() * scaleX + tranX;
        float b2y = ball2.getY() * scaleY + tranY;
        float b3x = ball3.getX() * scaleX + tranX;
        float b3y = ball3.getY() * scaleY + tranY;
        if (r == 0) {
            if (probe.getColorAngleTan() == 0) {
                canvas.drawRect(b0x, b0y, b2x, b2y, paint);
            } else {
                Path path = new Path();
                path.moveTo(b0x, b0y);
                path.lineTo(b1x, b1y);
                path.lineTo(b2x, b2y);
                path.lineTo(b3x, b3y);
                path.lineTo(b0x, b0y);
                canvas.drawPath(path, paint);
            }
        } else {
            RectF rectF = new RectF();
            rectF.left = convexOrigin.x - roiStartR;
            rectF.top = convexOrigin.y - roiStartR;
            rectF.right = convexOrigin.x + roiStartR;
            rectF.bottom = convexOrigin.y + roiStartR;

            rectF.left = rectF.left * scaleX + tranX;
            rectF.top = rectF.top * scaleY + tranY;
            rectF.right = rectF.right * scaleX + tranX;
            rectF.bottom = rectF.bottom * scaleY + tranY;

            canvas.drawArc(rectF, roiStartTheta * drRatio + 90, roiDiffTheta * drRatio, false, paint);

            rectF.left = convexOrigin.x - roiEndR;
            rectF.top = convexOrigin.y - roiEndR;
            rectF.right = convexOrigin.x + roiEndR;
            rectF.bottom = convexOrigin.y + roiEndR;

            rectF.left = rectF.left * scaleX + tranX;
            rectF.top = rectF.top * scaleY + tranY;
            rectF.right = rectF.right * scaleX + tranX;
            rectF.bottom = rectF.bottom * scaleY + tranY;

            canvas.drawArc(rectF, roiStartTheta * drRatio + 90, roiDiffTheta * drRatio, false, paint);
            Path leftpath = new Path();
            leftpath.moveTo(b0x,b0y);
            leftpath.lineTo(b3x, b3y);
            canvas.drawPath(leftpath,paint);
            //canvas.drawLine(b0x, b0y, b3x, b3y, paint);
            Path rightPath = new Path();
            rightPath.moveTo(b1x, b1y);
            rightPath.lineTo(b2x, b2y);
            canvas.drawPath(rightPath,paint);
            //canvas.drawLine(b1x, b1y, b2x, b2y, paint);
        }

        for (Ball ball : balls) {
            float x = ball.getX() * scaleX + tranX;
            float y = ball.getY() * scaleY + tranY;
            canvas.drawBitmap(ball.getBitmap(), x - halfWidthOfBall, y - halfWidthOfBall, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event, float[] values) {
        float tranX = values[Matrix.MTRANS_X];
        float scaleX = values[Matrix.MSCALE_X];
        float tranY = values[Matrix.MTRANS_Y];
        float scaleY = values[Matrix.MSCALE_Y];

        b0x = ball0.getX() * scaleX + tranX;
        b0y = ball0.getY() * scaleY + tranY;
        b1x = ball1.getX() * scaleX + tranX;
        b1y = ball1.getY() * scaleY + tranY;
        b2x = ball2.getX() * scaleX + tranX;
        b2y = ball2.getY() * scaleY + tranY;
        b3x = ball3.getX() * scaleX + tranX;

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                LogUtils.e("MotionEvent.ACTION_DOWN");
                touchMode = TouchMode.NONE;
                roiStartMovingPoint.x = x;
                roiStartMovingPoint.y = y;
                //实线时可以触摸四点可以改变大小
               /* for (Ball ball : balls) {
                    float centerX = ball.getX() * scaleX + tranX;
                    float centerY = ball.getY() * scaleY + tranY;

                    float dist = calDist(x, y, centerX, centerY);

                    if (dist < (halfWidthOfBall*4)) {
                        touchMode = TouchMode.DRAG;
                        ballId = ball.getID();
                        break;
                    }
                }*/
                setMoveInside(false);
                if (isMove ==  TouchMode.MOVE) {
                    //判断是否在采集框内部
                    if (isInside(x, y, values)) {
                        touchMode = TouchMode.MOVE;
                        if (r > 0) {
                            setConvexMidRoi();
                            startMovingArc = calArc(roiEndR, roiDiffTheta);
                        }
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (touchMode == TouchMode.MOVE) {
                    LogUtils.e("TouchMode.MOVE");
                    float diffX = x - roiStartMovingPoint.x;
                    float diffY = y - roiStartMovingPoint.y;

                    if (r == 0) {
                        moveLinearRoi(diffX/scaleX, diffY/scaleY);
                    } else {
                        float newMidX = convexMidRoi.x + diffX/scaleX;
                        float newMidY = convexMidRoi.y + diffY/scaleY;
                        moveConvexRoi(newMidX, newMidY,(x - tranX) / scaleX,(y - tranY) / scaleY);
                    }

                    roiStartMovingPoint.x = x;
                    roiStartMovingPoint.y = y;
                    setMoveInside(true);

                } else if (touchMode == TouchMode.DRAG) {
                    LogUtils.e("TouchMode.DRAG");
                    if (r == 0) {
                        //拖曳线阵
                        linearDragMode(values[Matrix.MSCALE_X], tranX, scaleX, tranY, scaleY, x, y);
                    } else { //convex mode
                        convexDragMode(tranX, scaleX, tranY, scaleY, x, y);
                    }
                }

                break;
        }

        //如果是虚线只触发改变大小事件，不会触发平移事件
        return isMove == TouchMode.DRAG;
    }

    private void linearDragMode(float value, float tranX, float scaleX, float tranY, float scaleY, float x, float y) {
        float imageWidth = probe.getImageWidthPx() * value;
        //限制拉伸的区域从四分之一的地方开始
        //float skipWidth = imageWidth / 4f;
        float skipWidth = 0;
        //限制左侧拉伸的最大范围
        float leftLimit = max(tranX,b1x-imageWidth/2);
        //限制右侧拉伸的宽度为扫描区域的一半
        float rightLimit = min(b3x+imageWidth/2,tranX+imageWidth);
        //如果偏转角度大于0
        if (theta>0){
            //限制左侧拉伸的最大范围
            leftLimit = max(tranX,b2x-imageWidth/2);
            //限制右侧拉伸的宽度为扫描区域的一半
            rightLimit = min(b0x+imageWidth/2,tranX+imageWidth);
        }
        //float leftLimit = max(0f, tranX + skipWidth);
        //float rightLimit = min(canvasWidth, tranX + imageWidth - skipWidth);

        if (ballId == 0) {
            if (y < 0f)
                y = 0f;

            float maxY = b2y - minWidth;
            if (y > maxY)
                y = maxY;

            roiHeight = b2y - y;
            float deltaX = calDeltaXByAngle();

            if (probe.getColorAngleTan() < 0) {
                leftLimit -= deltaX;
            }

            if (x < leftLimit)
                x = leftLimit;

            float maxX = b1x - minWidth;
            if (x > maxX)
                x = maxX;

            roiWidth = b1x - x;
            float temp = imageWidth - skipWidth - skipWidth - Math.abs(deltaX);
            if (roiWidth > temp) {
                roiWidth = temp;
            }

            if (roiWidth > canvasWidth) {
                roiWidth = canvasWidth;
                x = b1x - roiWidth;
            }
            float p0x = x;
            float p0y = y;
            float p1y = y;
            float p3x = x + deltaX;

            p0x = (p0x - tranX) / scaleX;
            p0y = (p0y - tranY) / scaleY;
            p3x = (p3x - tranX) / scaleX;
            float p1x = p0x+(ball2.getX()-p3x);
            p1y = (p1y - tranY) / scaleY;

            ball0.setX(p0x);
            ball0.setY(p0y);
            ball1.setX(p1x);
            ball1.setY(p1y);
            ball3.setX(p3x);

        } else if (ballId == 1) {
            if (y < 0)
                y = 0;

            float maxY = b2y - minWidth;
            if (y > maxY)
                y = maxY;

            roiHeight = b2y - y;

            float deltaX = calDeltaXByAngle();
            if (probe.getColorAngleTan() > 0) {
                rightLimit -= deltaX;
            }

            if (x > rightLimit)
                x = rightLimit;

            float minX = b0x + minWidth;
            if (x < minX)
                x = minX;

            roiWidth = x - b0x;
            float temp = imageWidth - skipWidth - skipWidth - Math.abs(deltaX);
            if (roiWidth > temp) {
                roiWidth = temp;
            }

            if (roiWidth > canvasWidth) {
                roiWidth = canvasWidth;
                x = roiWidth + b0x;
            }
            float p1x = x;
            float p1y = y;
            float p0y = y;
            float p2x = x + deltaX;

            p1x = (p1x - tranX) / scaleX;
            p1y = (p1y - tranY) / scaleY;
            p2x = (p2x - tranX) / scaleX;
            float p0x = p1x - (p2x-ball3.getX());
            p0y = (p0y - tranY) / scaleY;

            ball0.setX(p0x);
            ball0.setY(p0y);
            ball1.setX(p1x);
            ball1.setY(p1y);
            ball2.setX(p2x);

        } else if (ballId == 2) {
            if (y > canvasHeight)
                y = canvasHeight;

            float minY = b1y + minWidth;
            if (y < minY)
                y = minY;

            roiHeight = y - b1y;

            float deltaX = calDeltaXByAngle();
            if (probe.getColorAngleTan() < 0) {
                rightLimit += deltaX;
            }

            if (x > rightLimit)
                x = rightLimit;

            float minX = b3x + minWidth;
            if (x < minX)
                x = minX;

            roiWidth = x - b3x;
            float temp = imageWidth - skipWidth - skipWidth - Math.abs(deltaX);
            if (roiWidth > temp) {
                roiWidth = temp;
            }

            if (roiWidth > canvasWidth) {
                roiWidth = canvasWidth;
                x = roiWidth + b3x;
            }
            float p2x = x;
            float p2y = y;
            float p3y = y;
            float p1x = x - deltaX;

            p1x = (p1x - tranX) / scaleX;
            p2x = (p2x - tranX) / scaleX;
            p2y = (p2y - tranY) / scaleY;
            float p3x = p2x-(p1x-ball0.getX());
            p3y = (p3y - tranY) / scaleY;

            ball2.setX(p2x);
            ball2.setY(p2y);
            ball3.setX(p3x);
            ball3.setY(p3y);
            ball1.setX(p1x);


        } else if (ballId == 3) {
            if (y > canvasHeight)
                y = canvasHeight;

            float minY = b0y + minWidth;
            if (y < minY)
                y = minY;

            roiHeight = y - b0y;

            float deltaX = calDeltaXByAngle();
            if (probe.getColorAngleTan() > 0) {
                leftLimit += deltaX;
            }

            if (x < leftLimit)
                x = leftLimit;

            float maxX = b2x - minWidth;
            if (x > maxX)
                x = maxX;

            roiWidth = b2x - x;
            float temp = imageWidth - skipWidth - skipWidth - Math.abs(deltaX);
            if (roiWidth > temp) {
                roiWidth = temp;
            }

            if (roiWidth > canvasWidth) {
                roiWidth = canvasWidth;
                x = b2x - roiWidth;
            }
            float p3x = x;
            float p3y = y;
            float p2y = y;
            float p0x = x - deltaX;

            p0x = (p0x - tranX) / scaleX;
            p3x = (p3x - tranX) / scaleX;
            p3y = (p3y - tranY) / scaleY;
            float p2x = p3x+(ball1.getX()-p0x);
            p2y = (p2y - tranY) / scaleY;

            ball3.setX(p3x);
            ball3.setY(p3y);
            ball2.setX(p2x);
            ball2.setY(p2y);
            ball0.setX(p0x);
        }

        setLinearRoiData();
    }

    //拉伸凸阵的血流框
    private void convexDragMode(float tranX, float scaleX, float tranY, float scaleY, float x, float y) {
        x = (x - tranX) / scaleX;
        y = (y - tranY) / scaleY;
        if (ballId == 0) {
            float newTheta = calTheta(x, y);
            float newDiffTheta = newTheta - roiStartTheta;
            if (newDiffTheta < roiDiffThetaMinLimit)
                newTheta = roiDiffThetaMinLimit + roiStartTheta;
            else if (newDiffTheta > maxTheta && roiStartTheta<0){
                //限制左上的顶点可以拉伸的范围为半个扫描区域maxTheta为半个区域
                newTheta = maxTheta+roiStartTheta;
            }else if ( newTheta>maxTheta){
                //限制左上角的顶点不会移出扫描区域
                newTheta = maxTheta;
            }
            roiDiffTheta = newTheta - roiStartTheta;
            float newRho = calDist(x, y, convexOrigin.x, convexOrigin.y);
            float maxRho = roiEndR - roiDepthMinLimit;
            if (newRho < r) {
                newRho = r;
            } else if (newRho > maxRho) {
                newRho = maxRho;
            }

            roiDepth += roiStartR - newRho;
            roiStartR = newRho;
            setConvexBalls();
        } else if (ballId == 1) {
            float newTheta = calTheta(x, y);
            float roiEndTheta = roiStartTheta + roiDiffTheta;
            float newDiffTheta = roiEndTheta - newTheta;
            float minTheta = -maxTheta;
            if (newDiffTheta < roiDiffThetaMinLimit){
                newTheta = roiEndTheta - roiDiffThetaMinLimit;
            }else if (newDiffTheta>maxTheta && roiEndTheta>0){
                //限制拉伸的范围不能超过扫描区域的一半
                newTheta = roiEndTheta-maxTheta;
            }else if (newTheta < minTheta) {
                //限制扫描区域不会移出扫描区域
                newTheta = minTheta;
            }
            roiStartTheta = newTheta;
            roiDiffTheta = roiEndTheta - newTheta;

            float newRho = calDist(x, y, convexOrigin.x, convexOrigin.y);
            float maxRho = roiEndR - roiDepthMinLimit;
            if (newRho < r) {
                newRho = r;
            } else if (newRho > maxRho) {
                newRho = maxRho;
            }

            roiDepth += roiStartR - newRho;
            roiStartR = newRho;
            setConvexBalls();
        } else if (ballId == 2) {
            float newTheta = calTheta(x, y);
            float roiEndTheta = roiStartTheta + roiDiffTheta;
            float newDiffTheta = roiEndTheta - newTheta;
            float minTheta = -maxTheta;
            if (newDiffTheta < roiDiffThetaMinLimit){
                newTheta = roiEndTheta - roiDiffThetaMinLimit;
            }else if (newDiffTheta>maxTheta && roiEndTheta>0){
                //限制拉伸的范围不能超过扫描区域的一半
                newTheta = roiEndTheta-maxTheta;
            }else if (newTheta < minTheta) {
                //限制扫描区域不会移出扫描区域
                newTheta = minTheta;
            }
            roiStartTheta = newTheta;
            roiDiffTheta = roiEndTheta - newTheta;

            float newRho = calDist(x, y, convexOrigin.x, convexOrigin.y);
            float minRho = roiStartR + roiDepthMinLimit;
            float maxRho = maxEndRho;
            if (newRho < minRho) {
                newRho = minRho;
            } else if (newRho > maxRho) {
                newRho = maxRho;
            }

            roiDepth = newRho - roiStartR;
            roiEndR = roiStartR + roiDepth;
            setConvexBalls();
        } else if (ballId == 3) {
            float newTheta = calTheta(x, y);
            float newDiffTheta = newTheta - roiStartTheta;
            if (newDiffTheta < roiDiffThetaMinLimit)
                newTheta = roiDiffThetaMinLimit + roiStartTheta;
            else if (newDiffTheta > maxTheta && roiStartTheta<0){
                //限制左上的顶点可以拉伸的范围为半个扫描区域maxTheta为半个区域
                newTheta = maxTheta+roiStartTheta;
            }else if ( newTheta>maxTheta){
                //限制左上角的顶点不会移出扫描区域
                newTheta = maxTheta;
            }
            roiDiffTheta = newTheta - roiStartTheta;

            float newRho = calDist(x, y, convexOrigin.x, convexOrigin.y);
            float minRho = roiStartR + roiDepthMinLimit;
            float maxRho = maxEndRho;
            if (newRho < minRho) {
                newRho = minRho;
            } else if (newRho > maxRho) {
                newRho = maxRho;
            }

            roiDepth = newRho - roiStartR;
            roiEndR = roiStartR + roiDepth;
            setConvexBalls();
        }

        setConvexRoiData();
    }

    private void moveLinearRoi(float diffX, float diffY) {

        float imageWidth = probe.getImageWidthPx();
        float imageHeight = probe.getImageHeightPx();
//        float skipWidth = imageWidth / 4f;
        float skipWidth = 0;
        float leftLimit = skipWidth;
        float rightLimit = imageWidth - skipWidth;

        float p0x = ball0.getX();
        float p0y = ball0.getY();
        float p1x = ball1.getX();
        float p2x = ball2.getX();
        float p2y = ball2.getY();
        float p3x = ball3.getX();

        if (probe.getColorAngleTan() <= 0f) {
            if (p3x + diffX < leftLimit) {
                diffX = leftLimit - p3x;
            } else if (p1x + diffX > rightLimit) {
                diffX = rightLimit - p1x;
            }
        } else {
            if (p0x + diffX < leftLimit) {
                diffX = leftLimit - p0x;
            } else if (p2x + diffX > rightLimit) {
                diffX = rightLimit - p2x;
            }
        }

        if (p0y + diffY < 0)
            diffY = -p0y;
        else if (p2y + diffY > imageHeight)
            diffY = imageHeight - p2y;

        ball0.addX(diffX);
        ball0.addY(diffY);
        ball1.addX(diffX);
        ball1.addY(diffY);
        ball2.addX(diffX);
        ball2.addY(diffY);
        ball3.addX(diffX);
        ball3.addY(diffY);

        setLinearRoiData();
    }

    private void moveConvexRoi(float newMidX, float newMidY,float x,float y) {
        //血流框中心的角度
        float newMidTheta = calTheta(newMidX, newMidY);
        float minTheta1 = -maxTheta;
        float halfDiffTheta = roiDiffTheta / 2f;
        if (newMidTheta - halfDiffTheta < minTheta1) {
            //靠近右边
            newMidTheta = minTheta1 + halfDiffTheta;
        }else if (newMidTheta + halfDiffTheta > maxTheta) {
            //靠近左边
            newMidTheta = maxTheta - halfDiffTheta;

        }
        if(halfDiffTheta> maxTheta/2) {
            //如果血流框大于扫描区域时
            //newMidTheta = maxTheta - halfDiffTheta;
            float roiEndTheta = roiStartTheta + roiDiffTheta;
            //限制血流框不能大于扫描区域
            float newTheta = (roiEndTheta-maxTheta);
            roiDiffTheta = roiEndTheta - newTheta;
            startMovingArc = calArc(roiEndR, roiDiffTheta);
        }
        float newMidRho = calDist(newMidX, newMidY, convexOrigin.x, convexOrigin.y);
        float halfDepth = roiDepth / 2f;
        float newEndRho = newMidRho + halfDepth;
        float minEndRho = r + roiDepth;
        if (newEndRho < minEndRho) {
            newMidRho = minEndRho - halfDepth;
        }
        else if (newEndRho > maxEndRho) {
            newMidRho = maxEndRho - halfDepth;
        }
        setParamsByMidRoi(startMovingArc, newMidRho, newMidTheta);
        setConvexMidRoi();
        setConvexBalls();
        setConvexRoiData();
    }

    private void setLinearRoiData() {
        probe.setLinearRoiData(ball0.getX(), ball0.getY(), ball2.getX(), ball2.getY(), probe.getColorAngleTan());
    }

    private void setConvexRoiData() {
        probe.setConvexRoiData(roiStartR, roiEndR,
                roiStartTheta, roiStartTheta + roiDiffTheta);
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

        float b0x = ball0.getX() * scaleX + tranX;
        float b0y = ball0.getY() * scaleY + tranY;
        float b1x = ball1.getX() * scaleX + tranX;
        float b2y = ball2.getY() * scaleY + tranY;
        if (r == 0) {
            if (probe.getColorAngleTan() == 0 && x >= b0x && x <= b1x
                    && y >= b0y && y <= b2y)
                return true;
            if (y < b0y || y > b2y)
                return false;
            float diff = (y - b0y) * probe.getColorAngleTan();
            if (x >= (b0x + diff) && x <= (b1x + diff)) {
                return true;
            }
        } else {
            x = (x - tranX) / scaleX;
            y = (y - tranY) / scaleY;
            float dist = calDist(x, y, convexOrigin.x, convexOrigin.y);
            if (dist < roiStartR || dist > roiEndR)
                return false;

            float theta = calTheta(x, y);
            if (theta >= roiStartTheta && theta <= roiStartTheta + roiDiffTheta)
                return true;
        }

        return false;
    }


    private static float calDist(float x, float y, float x2, float y2) {
        return (float) Math.sqrt(((x2 - x) * (x2 - x)) + (y2 - y)
                * (y2 - y));
    }

    private static float calArc(float rho, float diffTheta) {
        return rho * diffTheta;
    }

    private static float calDiffTheta(float rho, float arc) {
        return arc / rho;
    }

    private static float cosF(float a) {
        return (float) Math.cos(a);
    }

    private static float sinF(float a) {
        return (float) Math.sin(a);
    }

    private static float atanF(float a) {
        return (float) Math.atan(a);
    }

    private float calDeltaXByAngle() {
        return roiHeight * probe.getColorAngleTan();
    }

    private void setConvexMidRoi() {
        setCoordinate(roiStartR + roiDepth / 2, roiStartTheta + roiDiffTheta / 2f, convexMidRoi);
    }

    private void setCoordinate(float rho, float theta, PointF p) {
        p.x = convexOrigin.x - (rho * sinF(theta));
        p.y = convexOrigin.y + (rho * cosF(theta));
    }

    private void setParamsByMidRoi(float arc, float rho, float theta) {
        float halfDepth = roiDepth / 2f;
        roiStartR = rho - halfDepth;
        roiEndR = rho + halfDepth;
        roiDiffTheta = calDiffTheta(roiEndR, arc);
        float halfDiffTheta = roiDiffTheta / 2f;
        roiStartTheta = theta - halfDiffTheta;
    }

    private void setConvexBalls() {
        float roiEndTheta = roiStartTheta + roiDiffTheta;
        setCoordinate(roiStartR, roiEndTheta, ball0.point);
        setCoordinate(roiStartR, roiStartTheta, ball1.point);
        setCoordinate(roiEndR, roiStartTheta, ball2.point);
        setCoordinate(roiEndR, roiEndTheta, ball3.point);
    }

    public void setParams(float originXPx, float originYPx, float rPx) {
        r = Math.round(rPx);
        convexOrigin.x = originXPx;
        convexOrigin.y = originYPx;

        float theta = probe.getTheta();
        // maxTheta = theta * 0.5f;
        maxTheta = theta;

        if (r!= 0) {
            startMovingArc = calArc(roiEndR, roiDiffTheta);
        }
    }

    private class Ball {
        private Bitmap bitmap;
        private PointF point;

        public Ball(Bitmap bitmap, PointF point) {
            this.bitmap = bitmap;
            this.point = point;
        }

        public int getWidthOfBall() {
            return bitmap.getWidth();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public float getX() {
            return point.x;
        }

        public void setX(float x) {
            point.x = x;
        }

        public float getY() {
            return point.y;
        }

        public void setY(float y) {
            point.y = y;
        }

        public void addY(float y) {
            point.y = point.y + y;
        }

        public void addX(float x) {
            point.x = point.x + x;
        }
    }


    public boolean isMoveInside() {
        return isMoveInside;
    }

    public void setMoveInside(boolean moveInside) {
        isMoveInside = moveInside;
    }

}
