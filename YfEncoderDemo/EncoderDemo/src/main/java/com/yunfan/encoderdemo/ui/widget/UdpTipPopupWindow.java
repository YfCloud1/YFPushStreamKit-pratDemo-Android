package com.yunfan.encoderdemo.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yunfan.encoderdemo.R;
import com.yunfan.encoderdemo.ui.adapter.FaceuSelectAdapter;

/**
 * Created by yunfan on 2017/3/23.
 */

public class UdpTipPopupWindow extends PopupWindow {

    private final Context mContext;

    public UdpTipPopupWindow(Context context) {
        super(context);
        mContext = context;
        // 设置可以获得焦点
        this.setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        // 设置弹窗的宽度和高度
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        setWidth(displayMetrics.widthPixels / 6 * 5);
        setHeight(displayMetrics.heightPixels / 4);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.xiala_bg));
//        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(context).inflate(R.layout.udp_tip_pop_up, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {

    }


}
