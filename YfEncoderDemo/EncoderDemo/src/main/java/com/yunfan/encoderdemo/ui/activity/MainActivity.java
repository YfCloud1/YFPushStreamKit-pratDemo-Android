package com.yunfan.encoderdemo.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.yunfan.auth.YfAuthentication;
import com.yunfan.encoderdemo.R;
import com.yunfan.encoderdemo.consts.Const;
import com.yunfan.encoderdemo.ui.widget.UdpTipPopupWindow;
import com.yunfan.encoderdemo.ui.widget.banner.Banner;
import com.yunfan.encoderdemo.ui.widget.banner.listener.OnBannerListener;
import com.yunfan.encoderdemo.ui.widget.banner.loader.ImageLoader;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;
import static com.yunfan.encoderdemo.consts.Const.ACCESS_KEY;
import static com.yunfan.encoderdemo.consts.Const.TOKEN;

public class MainActivity extends BaseActivity implements OnBannerListener {
    private final String TAG = "MainActivity";
    private Banner mBanner;
    private ToggleButton mTbScreenSetting;
    private View mLayoutSetting;
    private View mLineSetting;
    private EditText mEtLiveUrl;
    private EditText mEtBitrate;
    private EditText mEtFramerate;
    private RelativeLayout mLayoutPreLiveSetting;
    private UdpTipPopupWindow mUdpTipPopupWindow;
    private ImageButton mIbUdpTip;
    private TextView mTvUdpTip;
    private LinearLayout mLayoutSetBitrateFps;
    private ImageButton mIbSetting;
    private long mFirstBackTime;
    private RelativeLayout mLayoutPreRecordSetting;
    private ToggleButton mTbScreenSettingRecord;
    private EditText mEtRecordPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YfAuthentication.getInstance().authenticate(ACCESS_KEY,TOKEN, authCallBack);
        if (android.os.Build.VERSION.SDK_INT >= M) {
            checkEncoderPermission();
        } else {
            initView();
        }
    }

    private static final int CODE_FOR_READ_WRITE = 111;

    @TargetApi(M)
    private void checkEncoderPermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                | checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                | checkSelfPermission(Manifest.permission.CAMERA) | checkSelfPermission(Manifest.permission.RECORD_AUDIO) | checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE}, CODE_FOR_READ_WRITE);
        } else {
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CODE_FOR_READ_WRITE) {
            if (permissions[0].equals(Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && permissions[1].equals(Manifest.permission.RECORD_AUDIO)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && permissions[2].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && permissions[3].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && permissions[4].equals(Manifest.permission.READ_PHONE_STATE)
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                //用户同意使用camera
                initView();
            } else {
                finish();
            }
        }
    }

    private YfAuthentication.AuthCallBack authCallBack = new YfAuthentication.AuthCallBack() {
        @Override
        public void onAuthenticateSuccess() {
            Log.d(TAG, "鉴权成功~！");
            YfAuthentication.getInstance().refresh();
        }

        @Override
        public void onAuthenticateError(int errorCode) {
            Log.d(TAG, "鉴权失败啦：" + errorCode);
        }
    };

    private void initView() {
        mBanner = (Banner) findViewById(R.id.banner_view);
        mBanner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDisplayMetrics().heightPixels / 4 * 3));
        ArrayList<Drawable> drawables = getDrawables();
        mBanner.configIndicatorMargin(16)
                .setImages(drawables)
                .setDelayTime(3000)
                .setImageLoader(new CustomImageLoader())
                .setOnBannerListener(MainActivity.this)
                .start();
        Button btnStartLivePopup = (Button) findViewById(R.id.btn_start_live_popup);
        btnStartLivePopup.setOnClickListener(mClickListener);

        //===========直播前设置界面=============
        mLayoutPreLiveSetting = (RelativeLayout) findViewById(R.id.layout_pre_live_setting);
        Button btnStartLive = (Button) findViewById(R.id.btn_start_live);
        Button btnStartVodRecord = (Button) findViewById(R.id.btn_start_vod_record);
        Button btnClose = (Button) findViewById(R.id.btn_close);
        mTbScreenSetting = (ToggleButton) findViewById(R.id.tb_screen_setting);
        mIbSetting = (ImageButton) findViewById(R.id.ib_setting);
        mIbUdpTip = (ImageButton) findViewById(R.id.ib_udp_tip);
        mTvUdpTip = (TextView) findViewById(R.id.tv_weak_net);
        mLayoutSetting = findViewById(R.id.layout_setting);
        mLineSetting = findViewById(R.id.line_setting);
        mEtLiveUrl = (EditText) findViewById(R.id.et_live_url);
        mEtLiveUrl.setText(Const.DEFAULT_LIVE_URL);
        mLayoutSetBitrateFps = (LinearLayout) findViewById(R.id.layout_set_bitrate_fps);
        mEtBitrate = (EditText) findViewById(R.id.et_bitrate);
        mEtFramerate = (EditText) findViewById(R.id.et_frame_rate);
        mEtLiveUrl.setSelection(mEtLiveUrl.getText().length());

        //===========录制前设置界面=============
        mLayoutPreRecordSetting = (RelativeLayout) findViewById(R.id.layout_pre_record_setting);
        mTbScreenSettingRecord = (ToggleButton) findViewById(R.id.tb_screen_setting_record);
        mEtRecordPath = (EditText) findViewById(R.id.et_record_path);
        mEtRecordPath.setText(String.format("%s/yunfanencoder/Record/test.mp4",
                Environment.getExternalStorageDirectory().getPath()));
        mEtRecordPath.setSelection(mEtRecordPath.getText().length());
        Button btnStartRecord = (Button) findViewById(R.id.btn_start_record);
        Button btnCloseRecord = (Button) findViewById(R.id.btn_close_record);


        btnStartLive.setOnClickListener(mClickListener);
        btnStartRecord.setOnClickListener(mClickListener);
        btnStartVodRecord.setOnClickListener(mClickListener);
        btnClose.setOnClickListener(mClickListener);
        btnCloseRecord.setOnClickListener(mClickListener);
        mIbSetting.setOnClickListener(mClickListener);
        mIbUdpTip.setOnClickListener(mClickListener);
        animationIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        animationOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        animationIn.setAnimationListener(listener);
        animationOut.setAnimationListener(listener);
    }


    private Animation animationIn, animationOut;
    private Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation == animationIn) {
            } else {
                mLayoutPreLiveSetting.setVisibility(View.GONE);
                mLayoutPreRecordSetting.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start_live_popup:
                    if (mLayoutPreRecordSetting.getVisibility() == View.VISIBLE ||
                            mLayoutPreLiveSetting.getVisibility() == View.VISIBLE)
                        return;
                    mLayoutPreLiveSetting.startAnimation(animationIn);
                    mLayoutPreLiveSetting.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_start_live:
                    toLive(Const.TYPE_LIVE);
                    break;
                case R.id.btn_close:
                    mLayoutPreLiveSetting.startAnimation(animationOut);
                    break;
                case R.id.btn_start_vod_record:
                    if (mLayoutPreRecordSetting.getVisibility() == View.VISIBLE ||
                            mLayoutPreLiveSetting.getVisibility() == View.VISIBLE)
                        return;
                    mLayoutPreRecordSetting.startAnimation(animationIn);
                    mLayoutPreRecordSetting.setVisibility(View.VISIBLE);
                    break;
                case R.id.ib_setting:
                    boolean isGone = mLayoutSetBitrateFps.getVisibility() == View.GONE;
                    mLayoutSetBitrateFps.setVisibility(isGone ? View.VISIBLE : View.GONE);
                    mIbSetting.setBackground(getResources().getDrawable(isGone ?
                            R.drawable.ic_btn_set_h : R.drawable.ic_btn_set_n));
                    break;
                case R.id.ib_udp_tip:
                    showUdpTip();
                    break;
                case R.id.btn_start_record:
                    toLive(Const.TYPE_VOD);
                    break;
                case R.id.btn_close_record:
                    mLayoutPreRecordSetting.startAnimation(animationOut);
                    break;
            }
        }
    };

    private void showUdpTip() {
        if (mUdpTipPopupWindow == null) {
            mUdpTipPopupWindow = new UdpTipPopupWindow(MainActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mUdpTipPopupWindow.showAsDropDown(mTvUdpTip, 0, 0, Gravity.CENTER);
        } else {
            mUdpTipPopupWindow.showAsDropDown(mIbUdpTip);
        }
    }

    private void toLive(int recordType) {
        int bitrate = 0;
        int frameRate = 0;
        String bitrateStr = mEtBitrate.getText().toString();
        String frameRateStr = mEtFramerate.getText().toString();
        if (!TextUtils.isEmpty(bitrateStr))
            bitrate = Integer.parseInt(bitrateStr);
        if (!TextUtils.isEmpty(frameRateStr))
            frameRate = Integer.parseInt(frameRateStr);
        boolean isLandscape = recordType == Const.TYPE_LIVE ?
                mTbScreenSetting.isChecked() : mTbScreenSettingRecord.isChecked();
        String liveUrl = mEtLiveUrl.getText().toString();
        String recordPath = mEtRecordPath.getText().toString();
        boolean enableUDP =((ToggleButton) findViewById(R.id.tb_udp)).isChecked();
        boolean enableHEVC =((ToggleButton) findViewById(R.id.tb_hevc)).isChecked();

        if (!YfAuthentication.getInstance().isAuthenticateSucceed()) {
            Snackbar.make(mBanner, "鉴权未通过，不能推流，开始重新鉴权~", Snackbar.LENGTH_SHORT).show();
            YfAuthentication.getInstance().authenticate(ACCESS_KEY,TOKEN, authCallBack);
            return;
        }
        Intent intent = new Intent(MainActivity.this, LiveRecorderActivity.class);
        intent.putExtra(Const.KEY_BITRATE, bitrate == 0 ? Const.DEFAULT_BITRATE : bitrate);
        intent.putExtra(Const.KEY_FRAME_RATE, frameRate == 0 ? Const.DEFAULT_FRAME_RATE : frameRate);
        intent.putExtra(Const.KEY_SCREEN_ORIENTATION, isLandscape);
        intent.putExtra(Const.KEY_LIVE_URL, liveUrl);
        intent.putExtra(Const.KEY_RECORD_PATH, recordPath);
        intent.putExtra(Const.KEY_RECORD_TYPE, recordType);
        intent.putExtra(Const.KEY_UDP, enableUDP);
        intent.putExtra(Const.KEY_HEVC, enableHEVC);
        startActivity(intent);
    }

    @NonNull
    private ArrayList<Drawable> getDrawables() {
//        List<String> arrayList = new ArrayList<>();
//        String[] urls = getResources().getStringArray(R.array.url);
//        Collections.addAll(arrayList, urls);
        Drawable lunbo_001 = getResources().getDrawable(R.drawable.pic_lunbo_01);
        Drawable lunbo_002 = getResources().getDrawable(R.drawable.pic_lunbo_02);
        Drawable lunbo_003 = getResources().getDrawable(R.drawable.pic_lunbo_03);
        Drawable lunbo_004 = getResources().getDrawable(R.drawable.pic_lunbo_04);
        ArrayList<Drawable> drawables = new ArrayList<>();
        drawables.add(lunbo_001);
        drawables.add(lunbo_002);
        drawables.add(lunbo_003);
        drawables.add(lunbo_004);
        return drawables;
    }

    @Override
    public void OnBannerClick(int position) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //开始轮播
        if (mBanner != null) mBanner.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //结束轮播
        if (mBanner != null) mBanner.stopAutoPlay();
    }

    private class CustomImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageDrawable((Drawable) path);
//            Glide.with(context.getApplicationContext())
//                    .load(path)
//                    .crossFade()
//                    .into(imageView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLayoutPreLiveSetting.getVisibility() == View.VISIBLE ||
                    mLayoutPreRecordSetting.getVisibility() == View.VISIBLE) {
                mLayoutPreLiveSetting.setVisibility(View.GONE);
                mLayoutPreRecordSetting.setVisibility(View.GONE);
                return false;
            } else {
                if (System.currentTimeMillis() - mFirstBackTime > 2000) {
                    mFirstBackTime = System.currentTimeMillis();
                    Snackbar.make(mLayoutPreLiveSetting, R.string.exit_prompt, Snackbar.LENGTH_SHORT).show();
                    return false;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
