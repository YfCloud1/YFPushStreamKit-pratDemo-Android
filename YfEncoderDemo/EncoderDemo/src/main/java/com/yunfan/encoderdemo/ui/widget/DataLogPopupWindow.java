package com.yunfan.encoderdemo.ui.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunfan.encoderdemo.R;

/**
 * Created by yunfan on 2017/4/7.
 */

public class DataLogPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private final Context mContext;
    private TextView mTvCurrentBufferSize;
    private TextView mTvCurrentBitrate;
    private TextView mTvCurrentFps;
    private TextView mTvCurrentSpeed;

    public DataLogPopupWindow(Context context) {
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
        setWidth(displayMetrics.widthPixels / 3);
        setHeight(displayMetrics.heightPixels / 4);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tanchuang_bg));
//        this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transparent)));
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(context).inflate(R.layout.data_log_pop_up, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        mTvCurrentBufferSize = (TextView) view.findViewById(R.id.tv_current_buffer_size_ms);
        mTvCurrentBitrate = (TextView) view.findViewById(R.id.tv_current_bitrate);
        mTvCurrentFps = (TextView) view.findViewById(R.id.tv_current_fps);
        mTvCurrentSpeed = (TextView) view.findViewById(R.id.tv_current_speed);
        setOnDismissListener(this);
    }

    @Override
    public void onDismiss() {

    }

    public void updateData(int bufferSize, int bitrate, int fps, int speed) {
        mTvCurrentBufferSize.setText("buffer-ms:" + bufferSize);
        mTvCurrentBitrate.setText("bitrate:" + bitrate);
        mTvCurrentSpeed.setText("speed:" + speed);
        mTvCurrentFps.setText("fps:" + fps);
    }
}
