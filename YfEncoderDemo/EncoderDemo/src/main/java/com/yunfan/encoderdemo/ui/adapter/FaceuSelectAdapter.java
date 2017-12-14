package com.yunfan.encoderdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunfan.encoderdemo.R;

/**
 * Created by yunfan on 2017/3/15.
 */

public class FaceuSelectAdapter extends RecyclerView.Adapter<FaceuSelectAdapter.SelectViewHolder> {
    private final String TAG = "FaceuSelectAdapter";
    Context mContext;
    private String[] mFaceuItems;

    public FaceuSelectAdapter(Context context, String[] faceuItems) {
        mContext = context;
        mFaceuItems = faceuItems;
    }


    @Override
    public FaceuSelectAdapter.SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_select, null);
        SelectViewHolder viewHolder = new SelectViewHolder(inflate);
        viewHolder.mButton = (Button) inflate.findViewById(R.id.btn_faceu_item);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final FaceuSelectAdapter.SelectViewHolder holder, final int position) {
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
        return mFaceuItems.length + 1;//position为0时，不开启faceu
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
