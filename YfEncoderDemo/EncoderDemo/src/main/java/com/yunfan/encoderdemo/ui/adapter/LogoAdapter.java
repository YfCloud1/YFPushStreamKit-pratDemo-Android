package com.yunfan.encoderdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yunfan.encoderdemo.R;

/**
 * Created by yunfan on 2017/3/15.
 */

public class LogoAdapter extends RecyclerView.Adapter<LogoAdapter.SelectViewHolder> {
    private final String TAG = "LogoAdapter";
    Context mContext;
    private int[] mLogoItems;

    public LogoAdapter(Context context, int[] logoItems) {
        mContext = context;
        mLogoItems = logoItems;
    }


    @Override
    public LogoAdapter.SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_select, null);
        SelectViewHolder viewHolder = new SelectViewHolder(inflate);
        viewHolder.mButton = (Button) inflate.findViewById(R.id.btn_faceu_item);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final LogoAdapter.SelectViewHolder holder, final int position) {
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mButton.setBackground(mContext.getResources().getDrawable(R.drawable.select));
                int lastSelected = mCurrentSelectPosition;
                notifyItemChanged(lastSelected);
                mCurrentSelectPosition = position;
                Log.d(TAG, "position: " + mCurrentSelectPosition);
                notifyItemChanged(position);
                if (mOnSelectListener != null) {
                    mOnSelectListener.onSelect(position);
                }
            }
        });
        holder.mButton.setText(position == 0 ? "无" : String.valueOf(position));
        if (position == mCurrentSelectPosition) {
            holder.mButton.setBackground(mContext.getResources().getDrawable(R.drawable.select));

        } else {
            holder.mButton.setBackground(mContext.getResources().getDrawable(R.drawable.ic_unselect));
        }
    }

    @Override
    public int getItemCount() {
        return mLogoItems.length + 1;
    }

    private int mCurrentSelectPosition = 0;

    class SelectViewHolder extends RecyclerView.ViewHolder {
        Button mButton;
//        TextView mTextView;

        SelectViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setSelectListener(OnSelectListener selectListener) {
        mOnSelectListener = selectListener;
    }

    OnSelectListener mOnSelectListener;

    public interface OnSelectListener {
        void onSelect(int position);
    }
}
