package com.example.carl.animation_loading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class LoadingView extends LinearLayout {
    private ShapeView mShapeView;
    private View mShadowView;
    private int mTranslationDistance = 0;
    private final int ANIMATOR_DURATION=350;
    public LoadingView(Context context) {
        //super(context);
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context,attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTranslationDistance = dip2px(80);
        initLayout();
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    private void initLayout(){
        //this代表把布局加载到
        Log.i("chen","getContext:"+getContext());
//        View loadView = inflate(getContext(),R.layout.loading_view,null);
//        addView(loadView);
        inflate(getContext(),R.layout.loading_view,this);
        mShadowView = findViewById(R.id.shadow_view);
        mShapeView = findViewById(R.id.shape_view);
        post(new Runnable() {
            @Override
            public void run() {
                //onResume之后 看View的绘制流程源码分析哪一章
                startFallAnimator();
            }
        });

    }
    private void startFallAnimator(){
        //动画在谁身上
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView,"translationY",0,mTranslationDistance);
        //translationAnimator.setDuration(ANIMATOR_DURATION);
        //translationAnimator.start();

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView,"scaleX",1f,0.3f);
        //scaleAnimator.setDuration(ANIMATOR_DURATION);
        //scaleAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator,scaleAnimator);
        animatorSet.setDuration(ANIMATOR_DURATION);
        //下落速度越来越快 插值器
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
        //下落之后上抛，监听动画完毕。
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShapeView.exchanged();
                startUpAnimator();

            }
        });
    }
    private void startUpAnimator(){
        Log.i("chen","startUpAnimator->");
        //动画在谁身上
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mShapeView,"translationY",mTranslationDistance,0);
        //translationAnimator.setDuration(ANIMATOR_DURATION);
        //translationAnimator.start();

        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(mShadowView,"scaleX",0.3f,1f);
        //scaleAnimator.setDuration(ANIMATOR_DURATION);
        //scaleAnimator.start();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationAnimator,scaleAnimator);
        animatorSet.setDuration(ANIMATOR_DURATION);
        //上抛越来越慢 插值器
        animatorSet.setInterpolator(new DecelerateInterpolator());

        //下落之后上抛，监听动画完毕。
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startFallAnimator();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                startRotationAnimator();
            }
        });
        animatorSet.start();
    }
    private void  startRotationAnimator(){
        ObjectAnimator rotationAnimator = null;
        switch (mShapeView.getCurrentShape()){
            case Circle:
            case Square:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView,"rotation",0,180);
                break;
            case Triangle:
                rotationAnimator = ObjectAnimator.ofFloat(mShapeView,"rotation",0,-120);
                break;
        }
        rotationAnimator.setDuration(ANIMATOR_DURATION);
        rotationAnimator.setInterpolator(new DecelerateInterpolator());
        rotationAnimator.start();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(View.INVISIBLE);//不要再去摆放和计算少走一些系统源码
        //清除动画
        mShapeView.clearAnimation();
        mShadowView.clearAnimation();
        //把 Loading view从父布局移除
        ViewGroup parent = (ViewGroup)getParent();
        if (parent!=null){
            parent.removeView(this);
            removeAllViews();
        }


    }
}












