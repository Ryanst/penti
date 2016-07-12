package com.ryanst.penti.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ryanst.penti.R;


public class NavigationBar extends FrameLayout {

    private Toolbar toolbar;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private CharSequence title;
    private CharSequence subTitle;
    private int bgColor;
    private boolean titleBold;
    private boolean showSubTitle;
    private boolean showBack;


    public NavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        View navbarView = inflate(getContext(), R.layout.layout_navigation_bar, null);

        toolbar = (Toolbar) navbarView.findViewById(R.id.toolbar);
        tvTitle = (TextView) navbarView.findViewById(R.id.tv_title);
        tvSubTitle = (TextView) navbarView.findViewById(R.id.tv_sub_title);

        TypedArray typeArray = context.obtainStyledAttributes(attrs,
                R.styleable.tool_bar);
        title = typeArray.getString(R.styleable.tool_bar_title_text);
        tvTitle.setText(title);

        titleBold = typeArray.getBoolean(R.styleable.tool_bar_title_bold, false);
        if (titleBold) {
            tvTitle.setTypeface(null, Typeface.BOLD);
        }

        showSubTitle = typeArray.getBoolean(R.styleable.tool_bar_show_subtitle, false);
        if (showSubTitle) {
            tvSubTitle.setVisibility(VISIBLE);
        }

        subTitle = typeArray.getString(R.styleable.tool_bar_subtitle_text);
        tvSubTitle.setText(subTitle);

        showBack = typeArray.getBoolean(R.styleable.tool_bar_back, true);
        if (showBack) {
            toolbar.setNavigationIcon(R.drawable.back);
        }

        bgColor = typeArray.getColor(R.styleable.tool_bar_bgColor, 0);
        if (bgColor != 0) {
            toolbar.setBackgroundColor(bgColor);
        }

        typeArray.recycle();

        addView(navbarView);
    }

    public NavigationBar(Context context) {
        super(context);
    }

    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void setSubtitle(CharSequence title) {
        tvSubTitle.setText(title);
    }

    public void showSubTitle(boolean show) {
        if (show) {
            tvSubTitle.setVisibility(VISIBLE);
        } else {
            tvSubTitle.setVisibility(GONE);
        }
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        toolbar.setNavigationOnClickListener(listener);
    }

    @Override
    public void setBackgroundColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    public void setSubTitleColor(int color) {
        tvSubTitle.setTextColor(color);
    }

    public Toolbar getbar() {
        return toolbar;
    }

    public MenuItem findMenu(int menu) {
        return toolbar.getMenu().findItem(menu);
    }

    public void setBackVisibility(Boolean isVisiable) {
        if (isVisiable) {
            toolbar.setNavigationIcon(R.drawable.back);
        } else {
            toolbar.setNavigationIcon(null);
        }
    }
}
