package com.nfrc.verticaldraglistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ListViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by zhangl on 2018/10/25.
 */

public class VerticalDragListView extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mDragListView;
    private int mMenuHeight;
    private boolean mMenuIsOpen = false;

    public VerticalDragListView(@NonNull Context context) {
        this(context,null);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalDragListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mViewDragHelper = ViewDragHelper.create(this, mViewDragHelperCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();
        if (childCount != 2){
            throw new RuntimeException("只能放两个子布局");
        }
        mDragListView = getChildAt(1);
    }

    private ViewDragHelper.Callback mViewDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {

            return mDragListView == child;
        }


        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if(top < 0){
                top = 0;
            }
            if (top > mMenuHeight){
                top = mMenuHeight;
            }
            return top;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {   //松开

            if (releasedChild == mDragListView){


                if (mDragListView.getTop() > mMenuHeight/2){
                    //打开
                    mViewDragHelper.settleCapturedViewAt(0,mMenuHeight);
                    mMenuIsOpen = true;
                }else {

                    //关闭
                    mViewDragHelper.settleCapturedViewAt(0,0);
                    mMenuIsOpen = false;
                }

                invalidate();

            }


        }
    };



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mMenuHeight = getChildAt(0).getMeasuredHeight();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mViewDragHelper.processTouchEvent(event);
        return true;
    }



    private float mDownY;


    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断View是否滚动到了最顶部,还能不能向上滚
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mDragListView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mDragListView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mDragListView, -1) || mDragListView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mDragListView, -1);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mMenuIsOpen){
            return true;
        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mViewDragHelper.processTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                if ((moveY - mDownY) > 0 && !canChildScrollUp()){

                    //向下滑动  并滚动到顶部

                    return true;
                }


                break;


                default:
                    break;

        }


        return super.onInterceptTouchEvent(ev);


    }

    @Override
    public void computeScroll() {

        if(mViewDragHelper.continueSettling(true)){
            invalidate();
        }

    }
}
