package com.murui.easecopy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.murui.easecopy.R;

/**
 * Created by MQ on 2017/6/6.
 */

public class SwipeMenuLayout extends LinearLayout {
    private static final String TAG = "SwipeMenuLayout";
    public static final int STATE_CLOSED = 0;//关闭状态
    public static final int STATE_OPEN = 1;//打开状态
    public static final int STATE_MOVING_LEFT = 2;//左滑将要打开状态
    public static final int STATE_MOVING_RIGHT = 3;//右滑将要关闭状态

    public int currentState = 0;
    private int menuWidth;//菜单总长度
    private OverScroller mScroller;
    private int mScaledTouchSlop;
    private int mRightId;//右边隐藏菜单id
    private View rightMenuView;//右边的菜单按钮
    private int mLastX, mLastY;
    private int mDownX, mDownY;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mScaledTouchSlop = configuration.getScaledTouchSlop();
        configuration.getScaledMaximumFlingVelocity();
        configuration.getScaledMinimumFlingVelocity();
        //获取右边菜单id
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout);
        mRightId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_right_id, 0);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mRightId != 0) {
            rightMenuView = findViewById(mRightId);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        menuWidth = rightMenuView.getMeasuredWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (mDownX - event.getX());
                int dy = (int) (mDownY - event.getY());
                //如果Y轴偏移量大于X轴偏移量 不再滑动
                if (Math.abs(dy) >= Math.abs(dx)) return false;

                int deltaX = (int) (mLastX - event.getX());
                if (deltaX > 0 && currentState != STATE_OPEN) {
                    //向左滑动, 滑动的距离够,标志为打开状态
                    if (deltaX >= (menuWidth / 2) || getScrollX() >= (menuWidth / 2)) {
                        currentState = STATE_MOVING_LEFT;
                        break;
                    } else if (getScrollX() < menuWidth) {//滑动距离不达标, 则还原到开始状态
                        scrollBy(deltaX, 0);
                        currentState = STATE_MOVING_RIGHT;
                    }
                } else if (deltaX < 0) {
                    //向右滑动
                    scrollBy(deltaX, 0);
                    if (getScrollX() <= (menuWidth * 5 / 6) || Math.abs(deltaX) >= (menuWidth / 6)) {
                        currentState = STATE_MOVING_RIGHT;
                    }
                }
                mLastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (currentState == STATE_MOVING_LEFT) {
                    //左滑打开
                    smoothToOpenMenu();
                    currentState = STATE_OPEN;
                } else if (currentState == STATE_MOVING_RIGHT) {
                    //右滑关闭
                    smoothToCloseMenu();
                    currentState = STATE_CLOSED;
                } else if (currentState == STATE_OPEN) {
                    smoothToCloseMenu();
                }
                //如果小于滑动距离并且菜单是关闭状态 此时Item可以有点击事件
                int deltx = (int) (mDownX - event.getX());
                return !(Math.abs(deltx) < mScaledTouchSlop && isMenuClosed()) || super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // Get current x and y positions
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            scrollTo(currX, currY);
            postInvalidate();
        }
        if (isMenuOpen()) {
            currentState = STATE_OPEN;
        } else if (isMenuClosed()) {
            currentState = STATE_CLOSED;
        }
    }

    /**
     * 判断menu此时的状态
     *
     * @return true 打开状态 false 处于关闭状态
     */
    public boolean isMenuOpen() {
        return getScrollX() >= menuWidth;
    }

    /**
     * 判断menu此时的状态
     *
     * @return true 关闭状态 false 未关闭状态
     */
    public boolean isMenuClosed() {
        return getScrollX() <= 0;
    }

    /**
     * 关闭菜单
     */
    public void smoothToCloseMenu() {
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
        invalidate();
    }

    /**
     * 关闭菜单
     */
    public void smoothToOpenMenu() {
        mScroller.startScroll(getScrollX(), 0, menuWidth - getScrollX(), 0, 100);
        invalidate();
    }
}
