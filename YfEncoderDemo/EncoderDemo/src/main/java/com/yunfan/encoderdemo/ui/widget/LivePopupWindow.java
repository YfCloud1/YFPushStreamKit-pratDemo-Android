package com.yunfan.encoderdemo.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import com.yunfan.encoderdemo.consts.Const;
import com.yunfan.encoderdemo.R;


/**
 * Created by yunfan on 2017/3/15.
 */

public class LivePopupWindow extends PopupWindow implements View.OnClickListener {
    private final String TAG = "LivePopupWindow";
    private final Context mContext;
    private String mLiveUrl;
    private EditText mEtLiveUrl;
    private EditText mEtBitrate;
    private EditText mEtFramerate;
    private View mLayoutSetting;
    private View lineSetting;
    private View mLineSetting;
    private ToggleButton mTbScreenSetting;
    private boolean mIsLandscape = false;

    public LivePopupWindow(Context context) {
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
        setWidth(displayMetrics.widthPixels * 7 / 8);
        setHeight(displayMetrics.heightPixels * 3 / 4);

        //this.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.half_transparent)));
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(context).inflate(R.layout.popup_prepare_live, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        Button btnStartLive = (Button) view.findViewById(R.id.btn_start_live);
        Button btnClose = (Button) view.findViewById(R.id.btn_close);
        mTbScreenSetting = (ToggleButton) view.findViewById(R.id.tb_screen_setting);
        ImageButton ibSetting = (ImageButton) view.findViewById(R.id.ib_setting);
        mLayoutSetting = view.findViewById(R.id.layout_setting);
        mLineSetting = view.findViewById(R.id.line_setting);
        mEtLiveUrl = (EditText) view.findViewById(R.id.et_live_url);
        mEtLiveUrl.setText(Const.DEFAULT_LIVE_URL);
        mEtBitrate = (EditText) view.findViewById(R.id.et_bitrate);
        mEtFramerate = (EditText) view.findViewById(R.id.et_frame_rate);
        mEtLiveUrl.setSelection(mEtLiveUrl.getText().length());

        btnStartLive.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        ibSetting.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_live:
                if (mOnClickListener != null) {
                    int bitrate = 0;
                    int frameRate = 0;
                    String bitrateStr = mEtBitrate.getText().toString();
                    String frameRateStr = mEtFramerate.getText().toString();
                    if (!TextUtils.isEmpty(bitrateStr))
                        bitrate = Integer.parseInt(bitrateStr);
                    if (!TextUtils.isEmpty(frameRateStr))
                        frameRate = Integer.parseInt(frameRateStr);
                    boolean isLandscape = mTbScreenSetting.isChecked();


                    mOnClickListener.onStartLive(
                            bitrate,
                            frameRate,
                            isLandscape,
                            mEtLiveUrl.getText().toString());
                }
                dismiss();
                break;
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.ib_setting:
                showSetting();
                break;
            default:
                break;
        }
    }

    private void showSetting() {
        boolean isVisible = mLayoutSetting.getVisibility() == View.VISIBLE;
        mLayoutSetting.setVisibility(isVisible ? View.GONE : View.VISIBLE);
        mLineSetting.setVisibility(isVisible ? View.GONE : View.VISIBLE);

    }

    public interface OnClickListener {
        void onStartLive(int bitrate, int frameRate, boolean isLandscape, String liveUrl);
    }

    OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }


}
