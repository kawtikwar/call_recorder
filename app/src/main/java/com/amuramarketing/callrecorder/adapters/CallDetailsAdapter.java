package com.amuramarketing.callrecorder.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amuramarketing.callrecorder.R;
import com.amuramarketing.callrecorder.Utils.DateUtil;
import com.amuramarketing.callrecorder.models.CallDetailsModel;
import com.amuramarketing.callrecorder.preferences.PreferenceHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pooja on 4/8/2017.
 */

public class CallDetailsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<CallDetailsModel> list;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    // private OnLoadMoreListener onLoadMoreListener;


    public CallDetailsAdapter(Context context, ArrayList<CallDetailsModel> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_call_details, viewGroup, false);

        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (list.get(position).getCallType().equals(PreferenceHelper.CALL_TYPE_INCOMING)) {
            if (list.get(position).getCallStatus() != null && list.get(position).getCallStatus().equals(PreferenceHelper.CALL_STATUS_SPOKE)) {
                ((CustomViewHolder) holder).mIvCallType.setImageResource(R.mipmap.received_call);
                ((CustomViewHolder) holder).play.setVisibility(View.VISIBLE);
            } else {
                ((CustomViewHolder) holder).mIvCallType.setImageResource(R.mipmap.missed_call);
                ((CustomViewHolder) holder).play.setVisibility(View.GONE);
            }
        } else {
            if (list.get(position).getCallStatus() != null && list.get(position).getCallStatus().equals(PreferenceHelper.CALL_STATUS_SPOKE)) {
                ((CustomViewHolder) holder).mIvCallType.setImageResource(R.mipmap.dialed_call);
                ((CustomViewHolder) holder).play.setVisibility(View.VISIBLE);
            } else {
                ((CustomViewHolder) holder).mIvCallType.setImageResource(R.mipmap.not_available);
                ((CustomViewHolder) holder).play.setVisibility(View.GONE);
            }
        }

        ((CustomViewHolder) holder).tvNumber.setText(list.get(position).getName());

//        ((CustomViewHolder) holder).tvNumber.setText(!TextUtils.isEmpty()) ? getContactName(list.get(position).getMobileNo(), mContext) : list.get(position).getMobileNo());
        //((CustomViewHolder) holder).tvNumber.setText(list.get(position).getMobileNo());

        ((CustomViewHolder) holder).mTvDateTime.setText(DateUtil.getDayDateString(list.get(position).getStartTime()));

        ((CustomViewHolder) holder).mTvDuration.setText(DateUtil.getTimeString(list.get(position).getStartTime())
                + ", " + DateUtil.getDurationString(list.get(position).getDuration()));


        ((CustomViewHolder) holder).play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File file = new File(list.get(position).getFilePath());
                intent.setDataAndType(Uri.fromFile(file), "audio/*");

                PackageManager packageManager = mContext.getPackageManager();
                if (intent.resolveActivity(packageManager) != null) {
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "No app available to handle action", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView mTvDuration, mTvDateTime, tvNumber;
        ImageView play, mIvCallType;

        public CustomViewHolder(View itemView) {
            super(itemView);

            // mTvType = (TextView) itemView.findViewById(R.id.tvType);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            mTvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            mTvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            play = (ImageView) itemView.findViewById(R.id.play);
            mIvCallType = (ImageView) itemView.findViewById(R.id.ivCallType);
        }

    }


}
