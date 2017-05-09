package com.xinli.xinli.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.activity.FileBrowserActivity;
import com.xinli.xinli.bean.bean.TestResultItemBean;

import java.util.List;

/**
 * Created by zhangyu on 03/05/2017.
 */
public class TeaTestResultListAdapter extends RecyclerView.Adapter<TeaTestResultListAdapter.MyViewHolder> {
    List<TestResultItemBean> datas;


    public TeaTestResultListAdapter(List<TestResultItemBean> das) {
        this.datas = das;
    }

    View.OnClickListener onClickListener = null;

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }

    @Override
    public TeaTestResultListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.im_resultItem_Icon.setTag(datas.get(position));
        holder.tv_resultItem_testName.setText(datas.get(position).getTestName());
        holder.tv_resultItem_stuNum.setText(datas.get(position).getAnswerNum());
        if (onClickListener != null)
            holder.im_resultItem_Icon.setOnClickListener(onClickListener);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_resultItem_testName;
        TextView tv_resultItem_stuNum;
        ImageView im_resultItem_Icon;

        public MyViewHolder(View view) {
            super(view);
            tv_resultItem_testName = (TextView) view.findViewById(R.id.tv_resultItem_testName);
            tv_resultItem_stuNum = (TextView) view.findViewById(R.id.tv_resultItem_stuNum);
            im_resultItem_Icon = (ImageView) view.findViewById(R.id.im_resultItem_Icon);
        }
    }

}
