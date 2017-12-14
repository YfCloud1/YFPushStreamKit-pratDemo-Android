package com.yunfan.encoderdemo.ui.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.LinearLayout;

import com.yunfan.encoderdemo.R;

/**
 * Created by yunfan on 2017/4/6.
 */

public class AdaptiveBitrateDialog extends Dialog implements DialogInterface.OnDismissListener {

    private Context mContext;

    protected AdaptiveBitrateDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
    }


    protected AdaptiveBitrateDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_adaptive_bitrate);
        LinearLayout llAdaptiveBitrate = (LinearLayout) findViewById(R.id.ll_adaptive_bitrate);
        llAdaptiveBitrate.setBackground(mContext.getResources().getDrawable(R.drawable.tanchuang_bg));
        findViewById(R.id.ib_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
