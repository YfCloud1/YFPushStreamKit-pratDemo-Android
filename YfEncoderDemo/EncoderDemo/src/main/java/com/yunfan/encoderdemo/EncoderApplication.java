package com.yunfan.encoderdemo;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.yunfan.encoderdemo.consts.Const;


/**
 * Created by yunfan on 2017/3/15.
 */

public class EncoderApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG)
            CrashReport.initCrashReport(getApplicationContext(), Const.BUGLY_KEY, false);
    }

}
