package com.android.library;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * 签到控件
 */

public class SignView extends View {
    private int mHeight, mWidth;
    private String textTop = "√";
    //4只画笔 线 圆  √，文字
    private Paint textTopPaint, textBottomPaint, circularPaint, linePaint;
    private String textBottom = "Day1";

    private int redColor = Color.parseColor("#FD262D");
    private int grayColor = Color.parseColor("#999999");
    private int bottomTextColor = Color.parseColor("#999999");
    private int lineStyle, colorStyle;

    public SignView(Context context) {
        super(context);
        initPaint();
    }

    public SignView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        textTopPaint = new Paint();
        textTopPaint.setTextSize(convertTextSize(12));
        textTopPaint.setAntiAlias(true);
        textBottomPaint = new Paint();
        textBottomPaint.setTextSize(convertTextSize(13));
        textBottomPaint.setColor(bottomTextColor);
        textBottomPaint.setAntiAlias(true);

        circularPaint = new Paint();
        circularPaint.setAntiAlias(true);
        circularPaint.setStrokeWidth(2);
        circularPaint.setStyle(Paint.Style.STROKE);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(2);
    }

    /**
     * @param textTop    上边显示的文字
     * @param textBottom 下边显示的文字
     * @param lineStyle  显示的横线款式,0：都有线条，1左边无线条，2右边无线条；
     * @param colorStyle 显示的颜色款式,0：都是红色，1左红右灰，2都是灰色,3线左红右灰，圈是红色的
     */
    public void setTextTopAndBottom(String textTop, String textBottom, int lineStyle, int colorStyle) {
        this.textTop = textTop;
        this.textBottom = textBottom;
        this.lineStyle = lineStyle;
        this.colorStyle = colorStyle;
        invalidate();
    }


    private int getViewHeightMeasureSpec() {
        //测量字体
        Paint.FontMetrics fm = textTopPaint.getFontMetrics();
        double topTextHeight = Math.ceil(fm.descent - fm.ascent);
        fm = textBottomPaint.getFontMetrics();
        double bottomTextHeight = Math.ceil(fm.descent - fm.ascent);
        return (int) (topTextHeight + bottomTextHeight) + 50 ;//√与Day1之间距离50
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getViewHeightMeasureSpec();
        Log.v("SignView", "mWidth=" + mWidth + "\tmHeight=" + mHeight);
        setMeasuredDimension(widthMeasureSpec, mHeight);//宽度widthMeasureSpec取自父类,宽度为 手机宽度/7 1008+24*3（padding）
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeight != 0 && mWidth != 0) {
            switch (colorStyle) {
                case 0:
                    circularPaint.setColor(redColor);
                    textTopPaint.setColor(redColor);
                    linePaint.setColor(redColor);
                    break;
                case 1:
                    circularPaint.setColor(grayColor);
                    textTopPaint.setColor(grayColor);
                    linePaint.setColor(redColor);
                    break;
                case 2:
                    linePaint.setColor(grayColor);
                    circularPaint.setColor(grayColor);
                    textTopPaint.setColor(grayColor);
                    break;
                case 3:
                    linePaint.setColor(redColor);
                    circularPaint.setColor(redColor);
                    textTopPaint.setColor(redColor);
                    break;
            }

            //测量数字文字
            Paint.FontMetrics fm = textTopPaint.getFontMetrics();
            float textHight = (float) Math.ceil(fm.descent - fm.ascent);
            float textWidth = textTopPaint.measureText(textTop);

            //圆框
            float radius = (Math.max(textHight, textWidth) + 16) * 0.5f;
            float cx = mWidth * 0.5f;
            float cy = radius + 4 ;
            canvas.drawCircle(cx, cy, radius, circularPaint);

            //数字文字
            float x = cx - textWidth * 0.5f;
            float y = cy + (fm.descent - fm.ascent) / 2 - fm.descent;
            if (colorStyle == 3 || colorStyle == 0) {
                canvas.drawText(" √ ", x, y, textTopPaint);
            } else {
                // 显示数字
                // canvas.drawText(textTop, x, y, textTopPaint);
                canvas.drawText(" √ ", x, y, textTopPaint);
            }

            switch (lineStyle) {
                case 0://0：都有线条，1左边无线条，2右边无线条；
                    //左边横线
                    float stopX = cx - radius;
                    canvas.drawLine(0f, cy, stopX, cy, linePaint);
                    if (colorStyle == 1 || colorStyle == 3) {
                        linePaint.setColor(grayColor);
                    }
                    //右边横向
                    float startX = cx + radius;
                    canvas.drawLine(startX, cy, mWidth, cy, linePaint);
                    break;
                case 1:
                    if (colorStyle == 1 || colorStyle == 3) {
                        linePaint.setColor(grayColor);
                    }
                    //右边横线
                    startX = cx + radius;
                    canvas.drawLine(startX, cy, mWidth, cy, linePaint);
                    break;
                case 2:
                    //左边横线
                    stopX = cx - radius;
                    canvas.drawLine(0f, cy, stopX, cy, linePaint);
                    break;
            }
            linePaint.setColor(redColor);


            //下边文字
            fm = textBottomPaint.getFontMetrics();
            textHight = (float) Math.ceil(fm.descent - fm.ascent);
            textWidth = textBottomPaint.measureText(textBottom);
            x = (mWidth - textWidth) * 0.5f;
            y = (cy + radius) + textHight + 8;
            canvas.drawText(textBottom, x, y, textBottomPaint);
        }
    }


    //转换成sp
    private float convertTextSize(float size) {
        Context context = getContext();
        Resources resources;
        if (context == null) {
            resources = Resources.getSystem();
        } else {
            resources = context.getResources();
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, resources.getDisplayMetrics());
    }
}
