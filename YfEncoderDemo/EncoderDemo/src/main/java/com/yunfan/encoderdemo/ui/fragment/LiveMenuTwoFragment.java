package com.yunfan.encoderdemo.ui.fragment;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.yunfan.encoderdemo.R;
import com.yunfan.encoderdemo.consts.Const;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LiveMenuTwoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LiveMenuTwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveMenuTwoFragment extends BaseFragment {

    private ToggleButton mTbMute;
    private ToggleButton mTbLog;
    private int mRecordType;

    public LiveMenuTwoFragment() {
        // Required empty public constructor
    }

    public static LiveMenuTwoFragment newInstance(int type) {
        LiveMenuTwoFragment fragment = new LiveMenuTwoFragment();
        Bundle args = new Bundle();
        args.putInt(Const.KEY_RECORD_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecordType = getArguments().getInt(Const.KEY_RECORD_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTbMute = (ToggleButton) view.findViewById(R.id.tb_mute);
        mTbLog = (ToggleButton) view.findViewById(R.id.tb_log);
        mTbMute.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mTbLog.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
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
            };
}
