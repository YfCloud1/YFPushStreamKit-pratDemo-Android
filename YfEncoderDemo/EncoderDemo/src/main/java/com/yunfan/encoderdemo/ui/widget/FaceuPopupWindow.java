package com.yunfan.encoderdemo.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.yunfan.encoderdemo.R;
import com.yunfan.encoderdemo.ui.adapter.FaceuSelectAdapter;

/**
 * Created by yunfan on 2017/3/23.
 */

public class FaceuPopupWindow extends PopupWindow {

    private final Context mContext;
    private final String[] mFaceuItems;
    private FaceuSelectAdapter.OnSelectListener mSelectListener = new FaceuSelectAdapter.OnSelectListener() {
        @Override
        public void onSelect(int position) {
            mOnFaceuChangeListener.onFaceuChanged(position == 0 ? null : mFaceuItems[position - 1]);
        }
    };

    public FaceuPopupWindow(Context context, String[] faceuItems, boolean landscape) {
        super(context);
        mContext = context;
        mFaceuItems = faceuItems;
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
        View view = LayoutInflater.from(context).inflate(R.layout.popup_faceu_item, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_faceu);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        FaceuSelectAdapter faceuSelectAdapter = new FaceuSelectAdapter(mContext, mFaceuItems);
        faceuSelectAdapter.setSelectListener(mSelectListener);
        recyclerView.setAdapter(faceuSelectAdapter);

    }


    public void setOnFaceuChangeListener(OnFaceuChangeListener onFaceuChangeListener) {
        mOnFaceuChangeListener = onFaceuChangeListener;
    }

    OnFaceuChangeListener mOnFaceuChangeListener;

    public interface OnFaceuChangeListener {
        void onFaceuChanged(String itemName);
    }
}
