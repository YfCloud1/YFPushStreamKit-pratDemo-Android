package com.yunfan.encoderdemo.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.yunfan.encoder.effect.filter.AlphaBlendFilter;
import com.yunfan.encoder.filter.FaceUnityFilter;
import com.yunfan.encoder.filter.YfBlurBeautyFilter;
import com.yunfan.encoder.widget.RecordMonitor;
import com.yunfan.encoder.widget.YfEncoderKit;
import com.yunfan.encoderdemo.R;
//import com.yunfan.encoderdemo.bean.StreamInfo;
import com.yunfan.encoderdemo.consts.Const;
import com.yunfan.encoderdemo.ui.adapter.MenuAdapter;
import com.yunfan.encoderdemo.ui.fragment.BaseFragment;
import com.yunfan.encoderdemo.ui.fragment.LiveMenuOneFragment;
import com.yunfan.encoderdemo.ui.fragment.LiveMenuTwoFragment;
import com.yunfan.encoderdemo.ui.fragment.VodMenuFragment;
import com.yunfan.encoderdemo.ui.widget.DataLogPopupWindow;
import com.yunfan.encoderdemo.ui.widget.ScaleGLSurfaceView;
import com.yunfan.encoderdemo.ui.widget.indicator.CircleIndicator;
import com.yunfan.encoderdemo.utils.Util;
import com.yunfan.net.K2Pagent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import top.eg100.code.excel.jxlhelper.ExcelManager;

import static com.yunfan.encoderdemo.consts.Const.DEFAULT_BITRATE;
import static com.yunfan.encoderdemo.consts.Const.DEFAULT_FRAME_RATE;
import static com.yunfan.encoderdemo.consts.Const.DEFAULT_LANDSCAPE;

public class LiveRecorderActivity extends BaseActivity {
    private final String TAG = "LiveRecorderActivity";

    private ImageButton mIbMenu;
    private ViewPager mVpMenu;
    private View mLayoutMenu;
    private final int CODE_FOR_OPEN_CAMERA = 100;
    private ScaleGLSurfaceView mGlSurfaceView;
    private boolean mLandscape;
    private int surfaceWidth;
    private int surfaceHeight;
    private YfEncoderKit mYfEncoderKit;
    private YfBlurBeautyFilter mBeautyFilter;
    //const
    private final String CACHE_DIRS = Environment.getExternalStorageDirectory().getPath() + "/yunfanencoder";
    private final int VIDEO_WIDTH = 640;
    private final int VIDEO_HEIGHT = 360;
    private final int VIDEO_FRAME_RATE = 24;
    private int PREVIEW_WIDTH = 1280;
    private int PREVIEW_HEIGHT = 720;
    private int VIDEO_BITRATE = 600;

    private final int BEAUTY_INDEX = 1, LOGO_INDEX = 2, FACE_INDEX = 3;
    private boolean mSetBeauty;
    private boolean mHardEncoder = false;
    private boolean onServerConnected;
    private int mBitrate;
    private int mFrameRate;
    private boolean mIsLandscape;
    private String mLiveUrl;
    private ImageButton ibStartRecord;
    private FaceUnityFilter mFaceUnityFilter;
    private boolean mSetFaceu;
    private int mRecordType;
    private ImageButton mIbRecordFinish;
    private boolean mEnableUdp;
    private boolean mEnableHEVC;
    private RelativeLayout mRlVodSave;
    private TextView mTvTime;
    private CoordinatorLayout mContainer;
    private DataLogPopupWindow mDataLogPopupWindow;
    private String mRecordPath;
    private TextView mTvCurrentBufferSize;
    private TextView mTvCurrentBitrate;
    private TextView mTvCurrentFps;
    private TextView mTvCurrentSpeed;
    private TextView mTvUDPSpeed;
    private TextView mTvUDPMsg;
    private RelativeLayout mLayoutDataLog;
    private View mViewParticle;
    private boolean mForceStop;
    private NetworkConnectChangedReceiver receiver;
    private boolean netIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mCurrentBitrate = mBitrate = intent.getIntExtra(Const.KEY_BITRATE, DEFAULT_BITRATE);
        mFrameRate = intent.getIntExtra(Const.KEY_FRAME_RATE, DEFAULT_FRAME_RATE);
        mLandscape = intent.getBooleanExtra(Const.KEY_SCREEN_ORIENTATION, DEFAULT_LANDSCAPE);
        mRecordType = intent.getIntExtra(Const.KEY_RECORD_TYPE, Const.TYPE_LIVE);
        mLiveUrl = intent.getStringExtra(Const.KEY_LIVE_URL);
        mRecordPath = intent.getStringExtra(Const.KEY_RECORD_PATH);
        mEnableUdp = intent.getBooleanExtra(Const.KEY_UDP, false);
        mEnableHEVC = intent.getBooleanExtra(Const.KEY_HEVC, false);
        Log.d(TAG, String.format(" mBitrate: %d mFrameRate: %d mIsLandscape: %s mLiveUrl: %s",
                mBitrate, mFrameRate, mLandscape, mLiveUrl));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkEncoderPermission();
        } else {
            initView();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkEncoderPermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA)
                | checkSelfPermission(Manifest.permission.RECORD_AUDIO);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    CODE_FOR_OPEN_CAMERA);
        } else {
            initView();
        }
    }


    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(mLandscape ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_live);
        mGlSurfaceView = (ScaleGLSurfaceView) findViewById(R.id.gl_surface_view);
        //直播界面
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvTime.setText(mRecordType == Const.TYPE_LIVE ? R.string.live_ing : R.string.record_ing);
        mContainer = (CoordinatorLayout) findViewById(R.id.container);
        ToggleButton tbFlash = (ToggleButton) findViewById(R.id.tb_flash);
        ImageButton tbSwitchCamera = (ImageButton) findViewById(R.id.ib_switch_camera);
        mIbMenu = (ImageButton) findViewById(R.id.ib_menu);
        mIbRecordFinish = (ImageButton) findViewById(R.id.ib_record_finish);
        ibStartRecord = (ImageButton) findViewById(R.id.ib_close_record);

        //log
        mLayoutDataLog = (RelativeLayout) findViewById(R.id.layout_data_log);
        mTvCurrentBufferSize = (TextView) findViewById(R.id.tv_current_buffer_size_ms);
        mTvCurrentBitrate = (TextView) findViewById(R.id.tv_current_bitrate);
        mTvCurrentFps = (TextView) findViewById(R.id.tv_current_fps);
        mTvCurrentSpeed = (TextView) findViewById(R.id.tv_current_speed);
        mTvUDPMsg = (TextView) findViewById(R.id.tv_udp_msg);
        mTvUDPSpeed = (TextView) findViewById(R.id.tv_udp_speed);
        mIbRecordFinish.setVisibility(mRecordType == Const.TYPE_LIVE ? View.GONE : View.VISIBLE);
        //保存录制视频层
        mRlVodSave = (RelativeLayout) findViewById(R.id.rl_vod_save);
        mViewParticle = findViewById(R.id.view_particle);
        //菜单层
        mLayoutMenu = findViewById(R.id.layout_menu);
        View ibCloseMenu = findViewById(R.id.ib_close_menu);
        mVpMenu = (ViewPager) findViewById(R.id.vp_menu);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        indicator.configureIndicator(14, 14, 32);
        ArrayList<Fragment> fragments = new ArrayList<>();
        if (mRecordType == Const.TYPE_LIVE) {
            LiveMenuOneFragment liveMenuOneFragment = LiveMenuOneFragment.newInstance(mRecordType, mLandscape);
            LiveMenuTwoFragment liveMenuTwoFragment = LiveMenuTwoFragment.newInstance(mRecordType);
            liveMenuOneFragment.setOnFragmentInteractionListener(mFragmentInteractionListener);
            liveMenuTwoFragment.setOnFragmentInteractionListener(mFragmentInteractionListener);
            fragments.add(liveMenuOneFragment);
            fragments.add(liveMenuTwoFragment);
        } else {
            VodMenuFragment vodMenuFragment = VodMenuFragment.newInstance(mRecordType, mLandscape);
            vodMenuFragment.setOnFragmentInteractionListener(mFragmentInteractionListener);
            fragments.add(vodMenuFragment);
            indicator.setVisibility(View.INVISIBLE);
        }
        mVpMenu.setAdapter(new MenuAdapter(getSupportFragmentManager(), fragments));
        indicator.setViewPager(mVpMenu);

        mIbMenu.setOnClickListener(mOnClickListener);
        mIbRecordFinish.setOnClickListener(mOnClickListener);
        ibCloseMenu.setOnClickListener(mOnClickListener);
        tbSwitchCamera.setOnClickListener(mOnClickListener);
        tbFlash.setOnCheckedChangeListener(mOnCheckedChangeListener);
        ibStartRecord.setOnClickListener(mOnClickListener);
        mGlSurfaceView.initScaleGLSurfaceView(new ScaleGLSurfaceView.OnScareCallback() {
            @Override
            public int getCurrentZoom() {
                return mYfEncoderKit.getCurrentZoom();
            }

            @Override
            public int getMaxZoom() {
                return mYfEncoderKit.getMaxZoom();
            }

            @Override
            public boolean onScale(int zoom) {
                return mYfEncoderKit.manualZoom(zoom);
            }
        });
        mGlSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP) {
                    float xPercent = event.getY() / ((float) mGlSurfaceView.getHeight());
                    float yPercent = (((float) mGlSurfaceView.getWidth()) - event.getX()) /
                            ((float) mGlSurfaceView.getWidth());

                    Rect focusRect = ScaleGLSurfaceView.calculateTapArea(xPercent, yPercent, 1f);
                    if (mYfEncoderKit != null) {
                        mYfEncoderKit.manualFocus(focusRect);
                    }
                }
                return false;
            }
        });

        setSurfaceSize(mLandscape, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        initRecorder(mGlSurfaceView);
        initFaceu();
        maybeRegisterReceiver();//监听wifi连接状况
    }

    private void initFaceu() {

        if (mFaceUnityFilter == null) {
            mFaceUnityFilter = new FaceUnityFilter(LiveRecorderActivity.this);
            String[] faceuItems = getResources().getStringArray(R.array.faceu);
            mFaceUnityFilter.setEffect(faceuItems[0], false);//使用assets里的文件只需要写文件名即可，第二个参数填false;
//              mFaceUnityFilter.setEffect(CACHE_DIRS+"/"+m_item_names[mCurrentEffectId],true);//使用完整路径的话，第二个参数填true
            mFaceUnityFilter.setGestureEffect("heart.mp3", false);//同上
            mFaceUnityFilter.enableBeautyEffect(true);//开启faceUnity自带的美颜
            mFaceUnityFilter.setBeautyType(FaceUnityFilter.BEAUTY_NATURE);//设置美颜类型
            mFaceUnityFilter.setBeautyBlurLevel(5);//设置磨皮等级，取值0~5
            mFaceUnityFilter.setBeautyCheekThinningLevel(0);//设置瘦脸等级，0为关闭，1为默认，大于1为继续增强效果
            mFaceUnityFilter.setBeautyColorLevel(0.6);//设置色彩效果等级，0为关闭，1为默认，大于1为继续增强效果；美颜类型为nature时代表美白等级
            mFaceUnityFilter.setBeautyEyeEnlargingLevel(0);//设置大眼效果等级，0为关闭，1为默认，大于1为继续增强效果
            mFaceUnityFilter.setIndex(FACE_INDEX);
        }
    }

    private void initRecorder(GLSurfaceView s) {
        Log.d(TAG, "初始化编码器");
        //初始化编码工具：context、截图/录制视频等文件保存的根目录、摄像头输出宽度、摄像头输出高度、编码宽度、编码高度、是否硬编、视频帧率
        mYfEncoderKit = new YfEncoderKit(this, CACHE_DIRS, PREVIEW_WIDTH, PREVIEW_HEIGHT,
                VIDEO_WIDTH, VIDEO_HEIGHT, mHardEncoder, VIDEO_FRAME_RATE);
        mYfEncoderKit.setContinuousFocus()//设置连续自动对焦
                .setLandscape(mLandscape)//设置是否横屏模式（默认竖屏）
                .enableFlipFrontCamera(true)//设置前置摄像头是否镜像处理，默认为false
                .setRecordMonitor(mRecordMonitor)//设置回调
                .setDefaultCamera(true)//设置默认打开摄像头---true为前置，false为后置
                .setDropVideoFrameOnly(true)//设置默认打开只丢视频帧策略
                .openCamera(s);//设置预览窗口
        mBeautyFilter = new YfBlurBeautyFilter(this);
        mBeautyFilter.setIndex(BEAUTY_INDEX);
        mYfEncoderKit.addFilter(mBeautyFilter);//默认打开滤镜
        mSetBeauty = true;


    }

    /**
     * @param landscape 是否为横屏模式，预览宽度，预览高度
     */
    private void setSurfaceSize(boolean landscape, int width, int height) {
        mLandscape = landscape;
        ViewGroup.LayoutParams lp = mGlSurfaceView.getLayoutParams();
        int realScreenWidth = Util.getScreenWidth(this);
        Log.d(TAG, "realScreenWidth:" + realScreenWidth + "," + landscape);
        if (landscape) {
            surfaceWidth = realScreenWidth * 16 / 9;
            surfaceHeight = surfaceWidth * height / width;
        } else {
            surfaceHeight = realScreenWidth * 16 / 9;
            //考虑到高度可能被内置虚拟按键占用，因此为了保证预览界面为16:9，不能直接获取高度。
            surfaceWidth = surfaceHeight * height / width;
        }
        lp.width = surfaceWidth;
        lp.height = surfaceHeight;
        Log.d(TAG, "计算出来的宽高:" + surfaceWidth + "___" + surfaceHeight);
        mGlSurfaceView.setLayoutParams(lp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "on resume!");
        if (mYfEncoderKit != null)
            mYfEncoderKit.onResume();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startRecorder(true);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mYfEncoderKit != null)
            mYfEncoderKit.onStop(true);
    }

    protected void startRecorder(boolean changeUI) {
        if (mYfEncoderKit == null || mYfEncoderKit.isRecording()) {//不允许推流过程中进行参数设置
            return;
        }
        Log.d(TAG, "开始录制");
        onServerConnected = false;
        //设置编码参数：直播/录制、码率
        if (mRecordType == Const.TYPE_LIVE) {
            mYfEncoderKit.changeMode(YfEncoderKit.MODE_LIVE, mBitrate);
            mYfEncoderKit.setAdjustQualityAuto(true, 300);//打开码率自适应，最低码率300k
            mYfEncoderKit.setBufferSizeBySec(1);//最多缓存1秒的数据，超过1秒则丢帧
            mYfEncoderKit.enableUDP(mEnableUdp);
            mYfEncoderKit.enableHEVCEncoder(mEnableHEVC);
            mYfEncoderKit.enableHttpDNS(false);
            mYfEncoderKit.setLiveUrl(mLiveUrl);

            mCurrentBitrate = mBitrate;
        } else {
            String recordName;
            try {
                recordName = mRecordPath.substring(mRecordPath.lastIndexOf("/") + 1,
                        mRecordPath.lastIndexOf("."));
            } catch (Exception e) {
                e.printStackTrace();
                recordName = mRecordPath.substring(mRecordPath.lastIndexOf("/"));
            }
            mYfEncoderKit.changeMode(YfEncoderKit.MODE_VOD, VIDEO_BITRATE);
            mYfEncoderKit.setVodSaveName(recordName);
        }

        mYfEncoderKit.startRecord();
        mForceStop = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mYfEncoderKit != null) {
            mYfEncoderKit.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroyRecorder();
        unregisterReceiver(receiver);
    }

    private void destroyRecorder() {
        Log.d(TAG, "destroyRecorder");
        if (mYfEncoderKit != null) {
            if (mYfEncoderKit.isRecording())
                stopRecorder();
            mYfEncoderKit.release();
            mYfEncoderKit = null;
        }
        addCommonInfo("停止推流");
        exportInfo("final");

    }

    private void stopRecorder() {

        mHandler.removeCallbacks(mUpdateTimeRunnable);
        mYfEncoderKit.stopRecord();
        onServerConnected = false;

    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_switch_camera:
                    mYfEncoderKit.switchCamera();
                    break;
                case R.id.ib_menu:
                    showFunctionMenu(true);
                    break;
                case R.id.ib_close_menu:
                    showFunctionMenu(false);
                    break;
                case R.id.ib_close_record:
                    showStopRecordDialog();
                    break;
                case R.id.ib_record_finish:
                    stopVodRecord();
                    break;
            }
        }
    };

    private void showStopRecordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiveRecorderActivity.this);
        builder.setTitle(getResources().getString(R.string.prompt));
        if (mRecordType == Const.TYPE_LIVE) {
            builder.setNegativeButton(getResources().getString(R.string.end_live),
                    mAlertDialogClickListener);
            builder.setPositiveButton(getResources().getString(R.string.continue_live),
                    mAlertDialogClickListener);
        } else {
            builder.setNegativeButton(getResources().getString(R.string.end_record),
                    mAlertDialogClickListener);
            builder.setPositiveButton(getResources().getString(R.string.continue_record),
                    mAlertDialogClickListener);
        }
        builder.show();
    }

    private DialogInterface.OnClickListener mAlertDialogClickListener
            = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    if (mRecordType == Const.TYPE_LIVE) {
                        finish();
                    } else {
                        stopVodRecord();
                    }
                    break;
            }
        }
    };

    private void stopVodRecord() {
        stopRecorder();
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_praticle);
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        rotateAnimation.setInterpolator(interpolator);
        mViewParticle.setAnimation(rotateAnimation);
        mRlVodSave.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
    }


    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.tb_flash:
                    mYfEncoderKit.setFlash(!buttonView.isChecked());
                    break;
            }
        }
    };

    private void showFunctionMenu(boolean show) {
        mLayoutMenu.setVisibility(show ? View.VISIBLE : View.GONE);
        mIbMenu.setVisibility(!show ? View.VISIBLE : View.GONE);
        if (mRecordType == Const.TYPE_VOD) {
            mIbRecordFinish.setVisibility(!show ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_FOR_OPEN_CAMERA) {
            if (permissions[0].equals(Manifest.permission.CAMERA)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[1].equals(Manifest.permission.RECORD_AUDIO)
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

    private final Runnable mUpdateDataLogRunnable = new Runnable() {
        @Override
        public void run() {
            updateData(mCurrentBufferMs, mCurrentBitrate == -1 ? mBitrate : mCurrentBitrate,
                    mCurrentFPS, mCurrentSpeed, mUDPSendSpeed, mUDPRecSpeed, mUDPMsg);
            mHandler.postDelayed(this, 1000);
        }
    };

    public void updateData(double bufferSize, double bitrate, int fps, double speed, double udpSendSpeed, double udpRecSpeed, String udpMsg) {
        mTvCurrentBufferSize.setText(String.format("buffer-ms:%s", bufferSize));
        mTvCurrentBitrate.setText(String.format("bitrate:%s", bitrate));
        mTvCurrentSpeed.setText(String.format("speed:%s", speed));
        mTvCurrentFps.setText(String.format("fps:%s", fps));
        mTvUDPSpeed.setText(String.format("udp send:%s , rec:%s", udpSendSpeed, udpRecSpeed));
        mTvUDPMsg.setText(String.format("udp log:%s", udpMsg));
    }

    private boolean mSetLogo;
    BaseFragment.OnFragmentInteractionListener mFragmentInteractionListener =
            new BaseFragment.OnFragmentInteractionListener() {
                @Override
                public void onBeautyChanged(int level) {
                    changeBeauty(level);
                }

                @Override
                public void onFaceuChanged(String itemName) {
                    Log.d(TAG, "onFaceuChanged: " + itemName);
                    changeFaceu(itemName);
                }

                @Override
                public void onAudioPlay(boolean enable) {
                    mYfEncoderKit.enableAudioPlay(enable);
                }

                @Override
                public void onFlipCamera(boolean isChecked) {
                    mYfEncoderKit.enableFlipFrontCamera(!mYfEncoderKit.isFlipFrontCameraEnable());
                }

                @Override
                public void onMute(boolean isMute) {
                    Log.d(TAG, "onMute: " + isMute);
                    mYfEncoderKit.enablePushAudio(!isMute);
                }

                @Override
                public void onEnableLog(boolean isChecked) {
                    if (isChecked) {
//                        if (mDataLogPopupWindow == null)
//                            mDataLogPopupWindow = new DataLogPopupWindow(LiveRecorderActivity.this);
//                        mDataLogPopupWindow.showAtLocation(mGlSurfaceView, Gravity.CENTER, 0, 0);
                        mLayoutDataLog.setVisibility(View.VISIBLE);
                        mHandler.removeCallbacks(mUpdateDataLogRunnable);
                        mHandler.post(mUpdateDataLogRunnable);
                    } else {
                        mLayoutDataLog.setVisibility(View.GONE);
                        mHandler.removeCallbacks(mUpdateDataLogRunnable);
//                        if (mDataLogPopupWindow != null) mDataLogPopupWindow.dismiss();
                    }
                }

                @Override
                public void onChangeBitrate(int bitrate) {
                    Log.d(TAG, "onChangeBitrate: " + bitrate);
//                    mYfEncoderKit.setBitrate(bitrate);
                    if (mYfEncoderKit != null) {
                        boolean autoAdjustBitrate = bitrate > 0;
                        mYfEncoderKit.setAdjustQualityAuto(autoAdjustBitrate, 300);
                        Toast.makeText(LiveRecorderActivity.this, autoAdjustBitrate ?
                                        "自适应码率已开启，最小码率300" : "自适应码率已关闭",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onChangeLogo(int resourceId) {
                    changeLogo(resourceId);
                }

                @Override
                public void onHideMenu() {
                    mLayoutMenu.setVisibility(View.GONE);
                    mIbMenu.setVisibility(View.GONE);
                    if (mRecordType == Const.TYPE_VOD) {
                        mIbRecordFinish.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onShowMenu() {
                    mIbMenu.setVisibility(View.VISIBLE);
                    if (mRecordType == Const.TYPE_VOD) {
                        mIbRecordFinish.setVisibility(View.VISIBLE);
                    }
                }
            };

    private void changeLogo(int resourceId) {
        Log.d(TAG, "resourceId: " + resourceId);
        if (resourceId == -1) {
            mYfEncoderKit.removeFilter(LOGO_INDEX);
            mSetLogo = false;
        } else {
            mYfEncoderKit.removeFilter(LOGO_INDEX);
            AlphaBlendFilter logoFilter = new AlphaBlendFilter(1);
            logoFilter.setIndex(LOGO_INDEX);
            float landscapeMarginRight = 0.08f;//横屏模式下logo的marginright所占宽度的比例
            float portMarginRight = 0.12f;//竖屏模式下logo的marginright所占宽度的比例
            float landscapeMarginTop = 0.05f;//横屏模式下logo的marginTop所占宽度的比例
            float portMarginTop = 0.08f;//竖屏模式下logo的marginTop所占宽度的比例
            float landscapeLogoHeight = 0.2f;//横屏模式下logo的高度所占屏幕高度的比例
//            float logoWidth = 454, logoHeight = 160;//计算logo的比例
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
            float screenWidth = Util.getScreenWidth(this);
            float screenHeight = Util.getScreenHeight(this);
            float logoWidth = bitmap.getWidth();
            float logoHeight = bitmap.getHeight();
            if (mLandscape) {
                /**
                 * 配置logo的源及在画面中的位置，请注意屏幕的横屏竖屏模式及屏幕比例
                 * 需要注意的是在有内置虚拟键的情况下屏幕比例并不是16:9
                 * 这里不对该情况进行处理，仅考虑一般的16:9的情况。
                 * @param bitmap logo源
                 * @param widthPercent logo的宽度占屏幕宽度的比例（0~1）
                 * @param heightPercent logo的高度占屏幕高度的比例（0~1），
                 *                      譬如宽度设置为0.2f，那么在通常16:9竖屏的情况下，
                 *                      宽高比1：1的logo这里就应该是 0.2f * 9 / 16
                 * @param xPercent logo左边缘相对屏幕左边缘的距离比（0~1），
                 *                 通常情况下，该值与widthPercent之和不应大于1，
                 *                 否则logo则无法完全显示在屏幕内
                 * @param yPercent logo上边缘相对屏幕上边缘的距离比（0~1），
                 *                 通常情况下，该值与heightPercent之和不应大于1，
                 *                 否则logo则无法完全显示在屏幕内
                 *                 清楚上述四个参数后，可以根据个人需求配置图片大小及位置。
                 * @return
                 */
                logoFilter.config(bitmap,
                        landscapeLogoHeight * 9 / 16 * logoWidth / logoHeight,
                        landscapeLogoHeight,
                        1 - logoWidth / screenHeight - landscapeMarginRight,
                        landscapeMarginTop * 2.2f);
            } else {
                logoFilter.config(bitmap,
                        landscapeLogoHeight * logoWidth / logoHeight,
                        landscapeLogoHeight * 9 / 16,
                        1 - logoWidth / screenWidth - portMarginRight,
                        portMarginTop);
            }

            mYfEncoderKit.addFilter(logoFilter);
            mSetLogo = true;
        }
    }

    private void changeFaceu(String itemName) {
        if (mFaceUnityFilter != null) {
            if (TextUtils.isEmpty(itemName)) {
                //关闭faceu
                mYfEncoderKit.removeFilter(FACE_INDEX);
                mSetFaceu = false;
            } else {
                if (!mSetFaceu) {
                    if (mSetBeauty) {//开启face u 则默认关闭原本的美颜
                        mYfEncoderKit.removeFilter(BEAUTY_INDEX);
                        mSetBeauty = !mSetBeauty;
                    }
                    mYfEncoderKit.addFilter(mFaceUnityFilter);
                    mSetFaceu = !mSetFaceu;
                }
                mFaceUnityFilter.setEffect(itemName, false);
            }
        }
    }


    private void changeBeauty(int level) {
        if (level != 0) {
            if (mBeautyFilter == null) {
                mBeautyFilter = new YfBlurBeautyFilter(this);
                mBeautyFilter.setIndex(BEAUTY_INDEX);
            }
            if (!mSetBeauty) mYfEncoderKit.addFilter(mBeautyFilter);

            mBeautyFilter.setBeautyLevel(level);
        } else {
            mYfEncoderKit.removeFilter(BEAUTY_INDEX);
        }
        mSetBeauty = level != 0;
    }

    private int mCountTime = 0;
    private Runnable mUpdateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            String formatStr = mRecordType == Const.TYPE_LIVE ? "直播中 %s" : "录制中 %s";
            mTvTime.setText(String.format(formatStr, Util.formatCountTime(++mCountTime)));
            mHandler.postDelayed(mUpdateTimeRunnable, 1000);
        }
    };
    private double mCurrentBitrate = -1;
    private double mCurrentBufferMs;
    private int mCurrentFPS;
    private double mCurrentSpeed;
    private double mUDPSendSpeed;
    private double mUDPRecSpeed;
    private String mUDPMsg;
    private RecordMonitor mRecordMonitor = new RecordMonitor() {

        private String currentConnectedIP;

        @Override
        public void onServerConnected() {
            if (mYfEncoderKit != null) {
                onServerConnected = true;
                Snackbar.make(mContainer, "成功连接服务器，编码方式:" +
                        (mYfEncoderKit.getEncodeMode() ? "硬编" : "软编"), Snackbar.LENGTH_SHORT).show();
                mHandler.post(mUpdateTimeRunnable);
            }
        }

        @Override
        public void onError(int mode, int err, String msg) {
            addCommonInfo("推流异常");
            mHandler.removeCallbacks(mUpdateTimeRunnable);
            Toast.makeText(LiveRecorderActivity.this, "onError: " + msg, Toast.LENGTH_SHORT).show();
            onServerConnected = false;
            if (!mForceStop)
                startRecorder(false);

            Log.i(TAG, "####### error: " + err + " " + msg);
        }

        @Override
        public void onStateChanged(int mode, int oldState, int newState) {
            if (onServerConnected && newState == YfEncoderKit.STATE_RECORDING) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(mContainer, "成功连接服务器，编码方式:" +
                                (mYfEncoderKit.getEncodeMode() ? "硬编" : "软编"), Snackbar.LENGTH_SHORT).show();
                        mHandler.post(mUpdateTimeRunnable);
                    }
                });
            } else {
//                if (startRecoderAuto && newState == YfEncoderKit.STATE_PREPARED) {
//                    startRecoderAuto = false;
//                }

            }
            Log.i(TAG,
                    "####### state changed: "
                            + YfEncoderKit.getRecordStateString(oldState) + " -> "
                            + YfEncoderKit.getRecordStateString(newState));
        }

        @Override
        public void onFragment(int mode, String fragPath, boolean success) {

        }


        @Override
        public void onInfo(int what, double arg1, double arg2, Object obj) {
            switch (what) {
                case YfEncoderKit.INFO_IP:
                    currentConnectedIP = Util.intToIp((int) arg1);
                    addCommonInfo("推流成功");
                    Log.d(TAG, "实际推流的IP地址:" + currentConnectedIP);
//                    logRecoder.writeLog("IP:" + currentConnectedIP);
                    break;
                case YfEncoderKit.INFO_DROP_FRAMES:
                    Log.d(TAG, "frames had been dropped");
//                    logRecoder.writeLog("drop frames");
                    break;
                case YfEncoderKit.INFO_PUSH_SPEED:
                    Log.d(TAG, "mCurrentSpeed: " + mCurrentSpeed);
                    mCurrentSpeed = arg1;
                    addCommonInfo(null);
                    break;
                case YfEncoderKit.INFO_BITRATE_CHANGED:
                    Log.d(TAG, "onInfo: INFO_BITRATE_CHANGED " + arg1);
                    mCurrentBitrate = arg1;
                    break;
                case YfEncoderKit.INFO_CURRENT_BUFFER:
                    mCurrentBufferMs = arg1;
                    break;
                case YfEncoderKit.INFO_FRAME:
                    mCurrentFPS = (int) arg1;
                    break;
                case YfEncoderKit.INFO_UDP_SPEED:
                    mUDPSendSpeed = arg1;
                    mUDPRecSpeed = arg2;
                    break;
                case YfEncoderKit.INFO_UDP_MSG:
                    mUDPMsg = (String) obj;
                    break;
                case YfEncoderKit.INFO_ADAPTIVE_BITRATE_CALLBACK:
                    switch ((int) obj) {
                        case YfEncoderKit.EVENT_BUFFER_INCREASING:
                            addCommonInfo("缓存持续过高");
                            break;
                        case YfEncoderKit.EVENT_DECREASE_BITRATE:
                            addCommonInfo("降低码率至：" + mCurrentBitrate);
                            break;
                        case YfEncoderKit.EVENT_DROP_FRAME:
                            addCommonInfo("丢帧");
                            break;
                        case YfEncoderKit.EVENT_RETURN_LAST_SMOOTHING_BITRATE:
                            addCommonInfo("回归码率");
                            break;
                        case YfEncoderKit.EVENT_INCREASE_BITRATE:
                            addCommonInfo("提高码率至：" + mCurrentBitrate);
                            break;
                    }
                    break;
            }
        }

    };

//    List<StreamInfo> mInfoList = new ArrayList<>();

    private void addCommonInfo(String describe) {
//        StreamInfo lastInfo;
//        if (mInfoList.size() > 0) {
//            lastInfo = mInfoList.get(mInfoList.size() - 1);
//        } else {
//            lastInfo = new StreamInfo();
//            mInfoList.add(lastInfo);
//        }
//        if (describe == null) {
//            StreamInfo info = new StreamInfo();
//            info.setbBitrate((int) mCurrentBitrate);
//            info.setcPredictedBitrate((int) (mCurrentBitrate / VIDEO_FRAME_RATE * mCurrentFPS));
//            info.seteBufferMs((int) mCurrentBufferMs);
//            info.seteChangeBuffer((int) (Math.max(mCurrentBufferMs - lastInfo.geteBufferMs(), 0)));
//            info.setfFps(mCurrentFPS);
//            info.setdSpeed((int) mCurrentSpeed);
//            info.setgDescribe(describe);
//            if (mYfEncoderKit != null) {
//                K2Pagent.RunInfo runInfo = mYfEncoderKit.getRunInfo();
//                if (runInfo != null) {
//                    info.sethUDPSpeed((int) mUDPSendSpeed);
//                    info.setiSndRtoDuration(runInfo.sndRtoDuration);
//                    info.setjSndRto(runInfo.sndRto);
//                    info.setlRcvWnd(runInfo.rcvWnd);
//                    info.setkSndWnd(runInfo.sndWnd);
//                }
//            }
//            final Calendar mCalendar = Calendar.getInstance();
//            mCalendar.setTimeInMillis(System.currentTimeMillis());
//            info.setaTimeStamp(mCalendar.get(Calendar.HOUR) + ":" + mCalendar.get(Calendar.MINUTE) + ":" + mCalendar.get(Calendar.SECOND) + ":" + mCalendar.get(Calendar.MILLISECOND));
//            mInfoList.add(info);
//        } else {
//            lastInfo.setgDescribe(lastInfo.getgDescribe() == null ? describe : lastInfo.getgDescribe() + "," + describe);
//            if (lastInfo.getgDescribe() != null && lastInfo.getgDescribe().contains("丢帧")) {
//                lastInfo.seteBufferMs((int) mCurrentBufferMs);
//            }
//        }
//        if (mInfoList.size() % 600 == 0) {
//            exportInfo(mInfoList.size() + "");
//        }
    }

    private void exportInfo(final String exName) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String filePath = CACHE_DIRS;
//                    File dir = new File(filePath);
//                    if (!dir.exists()) {
//                        dir.mkdirs();
//                    }
//                    String usersFilePath = filePath + "/" + mLiveUrl.substring(mLiveUrl.lastIndexOf("/") + 1) + System.currentTimeMillis() + exName + ".xls";
//                    ExcelManager excelManager = new ExcelManager();
//                    OutputStream excelStream = new FileOutputStream(usersFilePath);
//
//                    boolean success = excelManager.toExcel(excelStream, mInfoList);
//                    Log.d(TAG, "导出日志文件");
//                    if (success) {
//                        Log.d(TAG, "导出日志文件成功");
//                    } else {
//                        Log.d(TAG, "导出日志文件失败");
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    private void maybeRegisterReceiver() {
        Log.d(TAG, "maybeRegisterReceiver" + receiver);
        if (receiver != null) {
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        receiver = new NetworkConnectChangedReceiver();
        registerReceiver(receiver, filter);
    }

    public class NetworkConnectChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    NetworkInfo.State state = networkInfo.getState();
                    netIsConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
                    Log.e(TAG, "isConnected:" + netIsConnected);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(mContainer, netIsConnected ? "WIFI已连接" : "WIFI已断开",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    if (netIsConnected) {
                        if (!mYfEncoderKit.isRecording()) {
                            startRecorder(false);
                        }
                    } else {
                        stopRecorder();
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRlVodSave.getVisibility() == View.GONE) showStopRecordDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
