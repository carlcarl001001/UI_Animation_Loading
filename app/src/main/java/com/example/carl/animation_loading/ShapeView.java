package com.example.carl.animation_loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ShapeView extends View {
    private Shape mCurrentShape = Shape.Circle;
    private Paint mPaint;
    private Path mPath;
    public ShapeView(Context context) {
        //super(context);
        this(context,null);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs) {
        //super(context, attrs);
        this(context,attrs,0);
    }

    public ShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width,height),Math.min(width,height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentShape){
            case Circle:
                int center = getWidth()/2;
                mPaint.setColor(Color.MAGENTA);
                canvas.drawCircle(center,center,center,mPaint);
                break;
            case Square:
                mPaint.setColor(Color.BLUE);
                canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
                break;
            case Triangle:
                mPaint.setColor(Color.RED);
                if (mPath == null){
                    mPath = new Path();
                    mPath.moveTo(getWidth()/2,0);
                    mPath.lineTo(0,(float)((getWidth()/2)*Math.sqrt(3)));
                    mPath.lineTo(getWidth(),(float)((getWidth()/2)*Math.sqrt(3)));
                    mPath.close();
                }
                canvas.drawPath(mPath,mPaint);
                break;
        }
        //正方形

        //圆形

        //三角形


    }

    public void exchanged(){
        switch (mCurrentShape){
            case Circle:
                mCurrentShape = Shape.Square;
                break;
            case Square:
                mCurrentShape = Shape.Triangle;
                break;
            case Triangle:
                mCurrentShape = Shape.Circle;
                break;
        }
        invalidate();
    }

    public Shape getCurrentShape(){
        return mCurrentShape;
    }
    public enum Shape{
        Circle,Square,Triangle
    }
}
