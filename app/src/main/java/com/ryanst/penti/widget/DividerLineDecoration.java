package com.ryanst.penti.widget;

/**
 * Created by mingge on 16/1/19.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 分隔线装饰
 */
public class DividerLineDecoration extends RecyclerView.ItemDecoration {
    /**
     * 水平方向
     */
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    /**
     * 垂直方向
     */
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    private boolean showFirst = false;
    private boolean showLast = false;

    // 画笔
    private Paint paint;

    // 布局方向
    private int orientation;
    // 分割线颜色
    private int color;
    // 分割线尺寸
    private int size;
    //padding右边
    private int right;
    //padding左边
    private int left;

    public void setShowFirst(boolean showFirst) {
        this.showFirst = showFirst;
    }

    public void setShowLast(boolean showLast) {
        this.showLast = showLast;
    }

    public DividerLineDecoration() {
        this(VERTICAL);
    }

    public DividerLineDecoration(int orientation) {
        this.orientation = orientation;

        paint = new Paint();
    }

    /**
     * @param orientation
     * @param right
     * @param left
     * @param size
     * @param color
     */
    public DividerLineDecoration(int orientation, int right, int left, int size, int color) {
        this.orientation = orientation;
        this.right = right;
        this.left = left;
        this.size = size;
        this.color = color;

        paint = new Paint();
        paint.setColor(color);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (orientation == HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    /**
     * 设置分割线颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    /**
     * 设置分割线尺寸
     *
     * @param size 尺寸
     */
    public void setSize(int size) {
        this.size = size;
    }

    // 绘制垂直分割线
    protected void drawVertical(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + size;

            c.drawRect(left, top, right, bottom, paint);
        }
    }

    // 绘制水平分割线
    protected void drawHorizontal(Canvas c, RecyclerView parent) {
//        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        if (showFirst) {
            final View child = parent.getChildAt(0);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = top + size;
            c.drawRect(left, top, right, bottom, paint);
        }
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + size;
            c.drawRect(left, top, right, bottom, paint);
        }
        if (showLast) {
            final View child = parent.getChildAt(childCount - 1);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + size;
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}
