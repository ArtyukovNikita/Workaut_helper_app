package com.example.workauthelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.core.view.GravityCompat;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

    private DrawerLayout drawerLayout;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Если боковое меню открыто, не перехватываем события
        if (drawerLayout != null && isDrawerOpen()) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }
}