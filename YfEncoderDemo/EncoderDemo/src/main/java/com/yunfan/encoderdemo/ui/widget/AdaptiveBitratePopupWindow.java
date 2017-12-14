package com.yunfan.encoderdemo.ui.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.yunfan.encoderdemo.R;

import org.w3c.dom.Text;

/**
 * Created by yunfan on 2017/3/23.
 */

public class AdaptiveBitratePopupWindow extends PopupWindow implements View.OnClickListener {

    private final Context mContext;
    private EditText mEtBitrate;
    private RadioGroup mRgBitrate;

    public AdaptiveBitratePopupWindow(Context context, boolean landscape) {
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

//        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.tanchuang_bg));
        // 设置弹窗的布局界面
        View view = LayoutInflater.from(context).inflate(R.layout.popup_adaptive_bitrate_item, null);
        setContentView(view);
        initView(view);
    }

    private void initView(View view) {
        mRgBitrate = (RadioGroup) view.findViewById(R.id.rg_bitrate);
        mRgBitrate.setOnCheckedChangeListener(mOnCheckedChangeListener);
        Button btnFinish = (Button) view.findViewById(R.id.btn_finish);
        ImageButton btnBitrateTip = (ImageButton) view.findViewById(R.id.ib_bitrate_tip);

        mEtBitrate = (EditText) view.findViewById(R.id.et_bitrate);
        btnFinish.setOnClickListener(this);
        btnBitrateTip.setOnClickListener(this);

    }


    public void setOnChooseBitrateListener(OnChooseBitrateListener onChooseBitrateListener) {
        mOnChooseBitrateListener = onChooseBitrateListener;
    }

    private OnChooseBitrateListener mOnChooseBitrateListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_bitrate_tip:
                AdaptiveBitrateDialog adaptiveBitrateDialog =
                        new AdaptiveBitrateDialog(mContext, R.style.MyDialogStyle);
                adaptiveBitrateDialog.show();
//                Dialog dialog = new Dialog(mContext, R.style.MyDialogStyle);
//                dialog.setContentView(R.layout.dialog_adaptive_bitrate);
//                dialog.show();
                break;
            case R.id.btn_finish:
                chooseBitrate();
                break;
        }
    }

    private void chooseBitrate() {
        String bitrateStr = mEtBitrate.getText().toString();
        if (!TextUtils.isEmpty(bitrateStr)) {
            Integer bitrate = Integer.valueOf(bitrateStr);
            if (bitrate > 0) {
                mOnChooseBitrateListener.onChooseBitrate(bitrate);
            }
        } else {
            int checkedRadioButtonId = mRgBitrate.getCheckedRadioButtonId();
            switch (checkedRadioButtonId) {
                case R.id.rb_suggest_bitrate_1:
                    mOnChooseBitrateListener.onChooseBitrate(100);
                    break;
                case R.id.rb_suggest_bitrate_2:
                    mOnChooseBitrateListener.onChooseBitrate(200);
                    break;
                case R.id.rb_suggest_bitrate_3:
                    mOnChooseBitrateListener.onChooseBitrate(300);
                    break;
                case R.id.rb_suggest_bitrate_4:
                    mOnChooseBitrateListener.onChooseBitrate(400);
                    break;
                default:
                    break;
            }
        }
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_suggest_bitrate_1:
                    mEtBitrate.setText("100");
                    break;
                case R.id.rb_suggest_bitrate_2:
                    mEtBitrate.setText("200");
                    break;
                case R.id.rb_suggest_bitrate_3:
                    mEtBitrate.setText("300");
                    break;
                case R.id.rb_suggest_bitrate_4:
                    mEtBitrate.setText("400");
                    break;
                default:
                    break;
            }
        }
    };

    public interface OnChooseBitrateListener {
        void onChooseBitrate(int bitrate);
    }
}
