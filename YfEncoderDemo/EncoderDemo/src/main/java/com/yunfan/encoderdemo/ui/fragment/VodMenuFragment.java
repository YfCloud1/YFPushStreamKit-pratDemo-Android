package com.yunfan.encoderdemo.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import com.yunfan.encoderdemo.R;
import com.yunfan.encoderdemo.consts.Const;
import com.yunfan.encoderdemo.ui.widget.AdaptiveBitratePopupWindow;
import com.yunfan.encoderdemo.ui.widget.BeautyPopupWindow;
import com.yunfan.encoderdemo.ui.widget.FaceuPopupWindow;
import com.yunfan.encoderdemo.ui.widget.LogoPopupWindow;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VodMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VodMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VodMenuFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View mBtnBeauty;
    private BeautyPopupWindow mBeautyMenuItemPopupWindow;
    private FaceuPopupWindow mFaceuMenuItemPopupWindow;
    private String[] mFaceuItems;
    private int[] mLogoItems = {R.mipmap.logo, R.mipmap.ic_wm_1, R.mipmap.ic_wm_2,
            R.mipmap.ic_wm_3, R.mipmap.ic_wm_4};
    private ToggleButton mTbAudioPlay;
    private ToggleButton mTbFlip;
    private ToggleButton mTbDenoise;
    private AdaptiveBitratePopupWindow mAdaptiveBitratePopupWindow;
    private LogoPopupWindow mLogoPopupWindow;
    private int mRecordType;
    private boolean mLandscape;
    private ToggleButton mTbMute;
    private ToggleButton mTbLog;

    public VodMenuFragment() {
    }

    public static VodMenuFragment newInstance(int type, boolean landscape) {
        VodMenuFragment fragment = new VodMenuFragment();
        Bundle args = new Bundle();
        args.putInt(Const.KEY_RECORD_TYPE, type);
        args.putBoolean(Const.KEY_SCREEN_ORIENTATION, landscape);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecordType = getArguments().getInt(Const.KEY_RECORD_TYPE);
            mLandscape = getArguments().getBoolean(Const.KEY_SCREEN_ORIENTATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnBeauty = view.findViewById(R.id.btn_beauty);
        View btnFaceu = view.findViewById(R.id.btn_face_u);
        View btnLogo = view.findViewById(R.id.btn_logo);
        mTbAudioPlay = (ToggleButton) view.findViewById(R.id.tb_audio_play);
        mTbDenoise = (ToggleButton) view.findViewById(R.id.tb_denoise);
        mTbFlip = (ToggleButton) view.findViewById(R.id.tb_flip_camera);
//        View btnAdaptiveBitrate = view.findViewById(R.id.btn_adaptive_bitrate);

        mTbMute = (ToggleButton) view.findViewById(R.id.tb_mute);
        mTbLog = (ToggleButton) view.findViewById(R.id.tb_log);
        mTbMute.setOnCheckedChangeListener(this);
        mTbLog.setOnCheckedChangeListener(this);

        mBtnBeauty.setOnClickListener(this);
        btnFaceu.setOnClickListener(this);
        btnLogo.setOnClickListener(this);

        mTbAudioPlay.setOnCheckedChangeListener(this);
        mTbDenoise.setOnCheckedChangeListener(this);
        mTbFlip.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_beauty:
                mOnFragmentInteractionListener.onHideMenu();
                showBeautyPopupWindow();
                break;
            case R.id.btn_face_u:
                mOnFragmentInteractionListener.onHideMenu();
                showFaceuPopupWindow();
                break;
            case R.id.btn_logo:
                mOnFragmentInteractionListener.onHideMenu();
                showLogoPopupWindow();
                break;
            case R.id.btn_adaptive_bitrate:
                if (mRecordType == Const.TYPE_LIVE) {
                    mOnFragmentInteractionListener.onHideMenu();
                    showAdaptiveBitratePopupWindow();
                }
                break;
            default:
                break;
        }
    }

    private void showLogoPopupWindow() {
        if (mLogoPopupWindow == null) {
            mLogoPopupWindow = new LogoPopupWindow(getActivity(), mLogoItems, mLandscape);
            mLogoPopupWindow.setOnLogoChangeListener(mOnLogoChangeListener);
            mLogoPopupWindow.setOnDismissListener(mOnDismissListener);
        }
        mLogoPopupWindow.showAtLocation(mBtnBeauty, Gravity.BOTTOM, 0, 0);
    }

    private void showAdaptiveBitratePopupWindow() {
        if (mAdaptiveBitratePopupWindow == null) {
            mAdaptiveBitratePopupWindow = new AdaptiveBitratePopupWindow(getActivity(), mLandscape);
            mAdaptiveBitratePopupWindow.setOnChooseBitrateListener(mOnChooseBitrateListener);
            mAdaptiveBitratePopupWindow.setOnDismissListener(mOnDismissListener);
        }
        mAdaptiveBitratePopupWindow.showAtLocation(mBtnBeauty, Gravity.BOTTOM, 0, 0);
    }

    private void showFaceuPopupWindow() {
        mFaceuItems = getContext().getResources().getStringArray(R.array.faceu);
        if (mFaceuMenuItemPopupWindow == null) {
            mFaceuMenuItemPopupWindow = new FaceuPopupWindow(getActivity(), mFaceuItems, mLandscape);
            mFaceuMenuItemPopupWindow.setOnFaceuChangeListener(mOnFaceuChangeListener);
            mFaceuMenuItemPopupWindow.setOnDismissListener(mOnDismissListener);
        }
        mFaceuMenuItemPopupWindow.showAtLocation(mBtnBeauty, Gravity.BOTTOM, 0, 0);
    }

    private void showBeautyPopupWindow() {
        if (mBeautyMenuItemPopupWindow == null) {
            mBeautyMenuItemPopupWindow = new BeautyPopupWindow(getActivity(), mLandscape);
            mBeautyMenuItemPopupWindow.setOnBeautyChangeListener(mOnBeautyChangeListener);
            mBeautyMenuItemPopupWindow.setOnDismissListener(mOnDismissListener);
        }
        mBeautyMenuItemPopupWindow.showAtLocation(mBtnBeauty, Gravity.BOTTOM, 0, 0);
    }

    private PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            mOnFragmentInteractionListener.onShowMenu();
        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tb_audio_play:
//                Drawable drawable = getContext().getResources().getDrawable(
//                        isChecked ? R.drawable.gn_erfan_2 : R.drawable.gn_erfan_1);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                mTbAudioPlay.setCompoundDrawables(null, drawable, null, null);
                mOnFragmentInteractionListener.onAudioPlay(isChecked);
                break;
            case R.id.tb_denoise:
//                Drawable drawable1 = getContext().getResources().getDrawable(
//                        isChecked ? R.drawable.gn_jingyin_2 : R.drawable.gn_jingyin_1);
//                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//                mTbDenoise.setCompoundDrawables(null, drawable1, null, null);
                break;
            case R.id.tb_flip_camera:
//                Drawable drawable2 = getContext().getResources().getDrawable(
//                        isChecked ? R.drawable.gn_jingxiang_2 : R.drawable.gn_jingxiang_1);
//                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
//                mTbFlip.setCompoundDrawables(null, drawable2, null, null);
                mOnFragmentInteractionListener.onFlipCamera(isChecked);
                break;
            case R.id.tb_mute:
//                            Drawable drawable = getContext().getResources().getDrawable(
//                                    isChecked ? R.drawable.gn_jingyin_2 : R.drawable.gn_jingyin_1);
//                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                            mTbMute.setCompoundDrawables(null, drawable, null, null);
                mOnFragmentInteractionListener.onMute(isChecked);
                break;
            case R.id.tb_log:
//                            Drawable drawable1 = getContext().getResources().getDrawable(
//                                    isChecked ? R.drawable.gn_rizhi_2 : R.drawable.gn_rizhi_1);
//                            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
//                            mTbLog.setCompoundDrawables(null, drawable1, null, null);
                mOnFragmentInteractionListener.onEnableLog(isChecked);
                break;
            default:
                break;
        }
    }

    private FaceuPopupWindow.OnFaceuChangeListener mOnFaceuChangeListener =
            new FaceuPopupWindow.OnFaceuChangeListener() {
                @Override
                public void onFaceuChanged(String itemName) {
                    mOnFragmentInteractionListener.onFaceuChanged(itemName);
                }
            };
    private BeautyPopupWindow.OnBeautyChangeListener mOnBeautyChangeListener =
            new BeautyPopupWindow.OnBeautyChangeListener() {
                @Override
                public void onBeautyChanged(int level) {
                    mOnFragmentInteractionListener.onBeautyChanged(level);
                }
            };

    private AdaptiveBitratePopupWindow.OnChooseBitrateListener mOnChooseBitrateListener =
            new AdaptiveBitratePopupWindow.OnChooseBitrateListener() {
                @Override
                public void onChooseBitrate(int bitrate) {
                    mOnFragmentInteractionListener.onChangeBitrate(bitrate);
                    mAdaptiveBitratePopupWindow.dismiss();
                }
            };
    private LogoPopupWindow.OnLogoChangeListener mOnLogoChangeListener =
            new LogoPopupWindow.OnLogoChangeListener() {
                @Override
                public void onLogoChanged(int resourceId) {
                    mOnFragmentInteractionListener.onChangeLogo(resourceId);
                }
            };

}
