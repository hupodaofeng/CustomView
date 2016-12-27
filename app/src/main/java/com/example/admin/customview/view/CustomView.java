package com.example.admin.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.admin.customview.R;


/**
 * author 郑
 * Created by admin on 2016/12/24.
 */
public class CustomView extends View {
    private int maxRingSide;//最外层圆的半径
    private int minRingBackgroundColor;//最里面圆的背景色
    private int maxRingBackgroundColor;//最外边圆的背景色
    private Paint mPaint;//画笔
    private int contentTextColor;//字体色，即当前进度文字颜色
    private float contentTextSize;//内容字体大小，即显示进度文字字体大小
    private float descTextSize;//字体大小
    private String descText;//描述文字
    private int descTextColor;//描述字体色
    private float currentProgress;//当前进度值
    private float maxProgress;//最大进度值
    private float scale;//当前进度与总进度的比例

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomAttr);
        //设置最大圆的半径
        maxRingSide = typedArray.getInt(R.styleable.CustomAttr_maxRingSide, 100);
        //设置当前进度的文字大小
        contentTextSize = typedArray.getDimension(R.styleable.CustomAttr_contentTextSize, 50);
        //设置当前进度的字体色
        contentTextColor = typedArray.getColor(R.styleable.CustomAttr_contentTextColor, Color.BLACK);
        //设置描述的内容
        descText = typedArray.getString(R.styleable.CustomAttr_descText);
        //设置描述字体的字体大小
        descTextSize = typedArray.getDimension(R.styleable.CustomAttr_descTextSize, 40);
        //设置描述字体的字体色
        descTextColor = typedArray.getColor(R.styleable.CustomAttr_descTextColor, Color.BLACK);
        //设置最小圆的背景色
        minRingBackgroundColor = typedArray.getColor(R.styleable.CustomAttr_minRingBackgroundColor, Color.WHITE);
        //设置最大圆的背景色
        maxRingBackgroundColor = typedArray.getColor(R.styleable.CustomAttr_maxRingBackgroundColor, Color.CYAN);
        //设置最大进度
        maxProgress = typedArray.getInteger(R.styleable.CustomAttr_maxProgress, 100);
        //设置当前进度的值
        currentProgress = typedArray.getInteger(R.styleable.CustomAttr_progress, 0);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mPaint = new Paint();//初始化画笔
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置自定义控件的大小
        setMeasuredDimension(maxRingSide*2, maxRingSide*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int minRingSide = maxRingSide / 4 * 3;
        scale = currentProgress / maxProgress;
        //最外层圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(maxRingBackgroundColor);
        mPaint.setStrokeWidth((maxRingSide-minRingSide)/2);
        canvas.drawCircle(maxRingSide, maxRingSide, (maxRingSide-minRingSide)/2+minRingSide, mPaint);
        //最里层实心圆
        mPaint.setColor(minRingBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(maxRingSide,maxRingSide,minRingSide,mPaint);
        //中间圆环
        drawCenterCircle(canvas, mPaint, minRingSide);
        //显示内容
        drawText(canvas,mPaint);
        //绘制中间彩色圆环
        drawCenterColorCircle(canvas, mPaint, minRingSide);
    }

    /**
     * 绘制中间彩色圆环
     * @param canvas
     * @param mPaint
     * @param minRingSide
     */
    private void drawCenterColorCircle(Canvas canvas, Paint mPaint,int minRingSide) {
        int ringWidth = (maxRingSide - minRingSide) / 2;
        int ringSide = (ringWidth/2) + minRingSide;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(ringWidth);
        int rectFTop = maxRingSide - ringSide;
        RectF rectF = new RectF(rectFTop, rectFTop, maxRingSide + ringSide, maxRingSide + ringSide);
        canvas.drawArc(rectF,270,(int)(360*scale),false,mPaint);
    }

    private void drawText(Canvas canvas, Paint mPaint) {
        Point point;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(descTextColor);
        mPaint.setTextSize(descTextSize);
        point = getTextRect(mPaint, descText, canvas);
        System.out.println(">>>>> width:" + point.x + " >>>>> height:" + point.y);
        canvas.drawText(descText,maxRingSide - (point.x/2),maxRingSide-(point.y/2)-point.y,mPaint);

        mPaint.setTextSize(contentTextSize);
        mPaint.setColor(contentTextColor);
        String str = (int)(scale*100) + "%";
        point = getTextRect(mPaint, str, canvas);
        System.out.println(">>>> width:" + point.x + ">>>> height:" + point.y);
        canvas.drawText(str, maxRingSide - (point.x / 2), maxRingSide + (point.y / 2) + point.y, mPaint);
    }

    /**
     * 画中间的圆环
     * @param canvas
     * @param mPaint
     * @param minRingSide
     */
    private void drawCenterCircle(Canvas canvas,Paint mPaint,int minRingSide) {
        int ringWidth = (maxRingSide - minRingSide) / 2;
        int ringSide = (ringWidth/2) + minRingSide;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(ringWidth);
        int rectFTop = maxRingSide - ringSide;
        RectF rectF = new RectF(rectFTop, rectFTop, maxRingSide + ringSide, maxRingSide + ringSide);
        canvas.drawArc(rectF,0,360,false,mPaint);
    }

    private Point getTextRect(Paint mPaint, String str, Canvas canvas) {
        Rect rect = new Rect();
        mPaint.getTextBounds(str, 0, str.length(), rect);
        return new Point(rect.width(), rect.height());
    }

    /**
     * 设置当前进度
     * @param currentProgress
     *         当前进度
     * @param maxProgress
     *         总进度
     */
    public void setProgress(float currentProgress,float maxProgress) {
        this.currentProgress = currentProgress;
        this.maxProgress = maxProgress;
        this.invalidate();
    }

    /**
     * 设置当前进度
     * @param currentProgress
     *      当前进度
     */
    public void setProgress(float currentProgress) {
        setProgress(currentProgress,maxProgress);
    }
}

