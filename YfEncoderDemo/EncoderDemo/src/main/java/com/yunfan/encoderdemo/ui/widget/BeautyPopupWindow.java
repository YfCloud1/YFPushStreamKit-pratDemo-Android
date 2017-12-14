package com.yunfan.encoderdemo.ui.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.yunfan.encoderdemo.R;

/**
 * Created by yunfan on 2017/3/23.
 */

public class BeautyPopupWindow extends PopupWindow {

    private final Context mContext;
    private OnBeautyChangeListener mOnBeautyChangeListener;

    public BeautyPopupWindow(Context context, boolean landscape) {
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
        setWidth(displayMetrics.widthPixels);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.half_transparent)));
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(context).inflate(R.layout.popup_beauty_item, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        RadioGroup rgBeauty = (RadioGroup) view.findViewById(R.id.rg_beauty_level);
        rgBeauty.setOnCheckedChangeListener(mOnCheckedChangeListener);

    }

    private String TAG = "BeautyPopupWindow";
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener =
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    Log.d(TAG, "checkedId:" + checkedId);
                    switch (checkedId) {
                        case R.id.rb_beauty_level_0:
                            mOnBeautyChangeListener.onBeautyChanged(0);
                            break;
                        case R.id.rb_beauty_level_1:
                            mOnBeautyChangeListener.onBeautyChanged(1);
                            break;
                        case R.id.rb_beauty_level_2:
                            mOnBeautyChangeListener.onBeautyChanged(2);
                            break;
                        case R.id.rb_beauty_level_3:
                            mOnBeautyChangeListener.onBeautyChanged(3);
                            break;
                        case R.id.rb_beauty_level_4:
                            mOnBeautyChangeListener.onBeautyChanged(4);
                            break;
                        default:
                            break;
                    }
                }
            };


    public void setOnBeautyChangeListener(OnBeautyChangeListener onBeautyChangeListener) {
        mOnBeautyChangeListener = onBeautyChangeListener;
    }

    public interface OnBeautyChangeListener {
        /**
         * @param level 美颜级别 0-5,0为不开启
         */
        void onBeautyChanged(int level);
    }
}
