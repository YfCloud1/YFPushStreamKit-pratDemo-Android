package com.yunfan.encoderdemo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by yunfan on 2017/3/29.
 */

public class BaseFragment extends Fragment {

    protected OnFragmentInteractionListener mOnFragmentInteractionListener;

    public BaseFragment() {
    }

    public interface OnFragmentInteractionListener {
        void onBeautyChanged(int level);

        void onFaceuChanged(String itemName);

        void onAudioPlay(boolean enable);

        void onFlipCamera(boolean isChecked);

        void onMute(boolean isMute);

        void onEnableLog(boolean isChecked);

        void onChangeBitrate(int bitrate);

        void onChangeLogo(int resourceId);

        void onHideMenu();

        void onShowMenu();
    }
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener
                                                         onFragmentInteractionListener){
        mOnFragmentInteractionListener = onFragmentInteractionListener;
    }
}
