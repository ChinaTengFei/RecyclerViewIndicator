package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017-06-11.
 */

public class CustomView extends ViewGroup {
    private static final String TAG = "CustomView";
    private CallBack callBack;

    public CustomView(Context context) {
        this(context,null);
    }
    ViewDragHelper mViewDragHelper;

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setCallBack(CallBack callBack){
        this.callBack = callBack;
    }
    public abstract static class CallBack{
        abstract public void ViewOffset(int top);
    }
    private void initView(Context context) {
        mViewDragHelper = ViewDragHelper.create(this,new ViewdragCallBack());
        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,3));
        view.setBackgroundColor(Color.RED);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            childAt.layout(0,50,getMeasuredWidth(),100);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    class ViewdragCallBack extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.d(TAG, "tryCaptureView() called with: child = [" + child + "], pointerId = [" + pointerId + "]");
            return true;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.d(TAG, "clampViewPositionVertical() called with: child = [" + child + "], top = [" + top + "], dy = [" + dy + "]");
            if (callBack != null) {
                callBack.ViewOffset(top);
            }
            if(top<0){
                return super.clampViewPositionVertical(child, top, dy);
            }
            return top;
        }
    }
}
