package com.yunfan.encoderdemo.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.yunfan.encoderdemo.R;
import com.yunfan.encoderdemo.ui.adapter.LogoAdapter;

/**
 * Created by yunfan on 2017/3/23.
 */

public class LogoPopupWindow extends PopupWindow {

    private final Context mContext;
    private final int[] mLogoItems;
    private LogoAdapter.OnSelectListener mSelectListener = new LogoAdapter.OnSelectListener() {
        @Override
        public void onSelect(int position) {
            mOnLogoChangeListener.onLogoChanged(position == 0 ? -1 : mLogoItems[position - 1]);
        }
    };

    public LogoPopupWindow(Context context, int[] logoItems, boolean landscape) {
        super(context);
        mContext = context;
        mLogoItems = logoItems;
        // 设置可以获得焦点
        this.setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        // 设置弹窗的宽度和高度
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        setWidth(displayMetrics.widthPixels);
        setHeight(landscape ? displayMetrics.heightPixels * 2 / 5 : displayMetrics.heightPixels / 4);

        //this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.half_transparent)));
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(context).inflate(R.layout.popup_logo_item, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        RecyclerView recyclerViewLogo = (RecyclerView) view.findViewById(R.id.recycler_view_logo);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewLogo.setLayoutManager(gridLayoutManager);
        LogoAdapter logoAdapter = new LogoAdapter(mContext, mLogoItems);
        logoAdapter.setSelectListener(mSelectListener);
        recyclerViewLogo.setAdapter(logoAdapter);

    }


    public void setOnLogoChangeListener(OnLogoChangeListener onLogoChangeListener) {
        mOnLogoChangeListener = onLogoChangeListener;
    }

    OnLogoChangeListener mOnLogoChangeListener;

    public interface OnLogoChangeListener {
        void onLogoChanged(int resourceId);
    }
}
