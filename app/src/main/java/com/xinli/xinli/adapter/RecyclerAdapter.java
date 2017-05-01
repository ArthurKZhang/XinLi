package com.xinli.xinli.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.bean.Quz;
import com.xinli.xinli.bean.bean.Test;
import com.xinli.xinli.ui.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 21/03/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context contex;

    private List<Quz> quzs;
    private LayoutInflater mInflater;
    private String testName;
    private boolean flag;

    /**
     * 标记，表示是否是 重新编辑试题，如果是重新编辑，那么设为true
     */
    public void setFlag(Boolean flag) {
        this.flag = flag;
    }


    public RecyclerAdapter(Context context, Test test) {
        contex = context;
        this.testName = test.getTestName();
        mInflater = LayoutInflater.from(context);
        quzs = test.getQuzs();
        if (quzs == null) {
            quzs = new ArrayList<Quz>();
            testName = "";
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final MyViewHolder holder = new MyViewHolder(mInflater.inflate(R.layout.item_home, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        RecyclerView rv = (RecyclerView) holder.quzView.findViewById(R.id.comp_quz_chofield);

        //TODO 进入再次编辑功能。需要将传进来的数据 填充到一个个的holder里面
        Quz quz = null;
        List<String> chooseItems = null;
        if (quzs!=null){
            quz = quzs.get(position);
            chooseItems = quz.getChooseItems();
        }
        if (flag) {
            EditText quzContent = (EditText) holder.quzView.findViewById(R.id.comp_quz_quzcontent);
            RecyclerView chosesView = (RecyclerView) holder.quzView.findViewById(R.id.comp_quz_chofield);
            String content = quz.getQuzContent();
            quzContent.setText(content);
            RadioGroup radioGroup = (RadioGroup) holder.quzView.findViewById(R.id.comp_quz_radiogrp);
            int choType = quz.getType();
            switch (choType) {
                case 1:
                    radioGroup.check(R.id.comp_quz_rad1);
                    break;
                case 2:
                    radioGroup.check(R.id.comp_quz_rad2);
                    break;
                case 3:
                    radioGroup.check(R.id.comp_quz_rad3);
                    break;
                case 4:
                    radioGroup.check(R.id.comp_quz_rad4);
                    break;
            }
            if (choType == 4) {
                chosesView.setVisibility(View.GONE);
            }

        }
        final RecyclerChosAdapter adapter = new RecyclerChosAdapter(contex, chooseItems);
        final RecyclerChosAdapter adapter2 = new RecyclerChosAdapter(contex, new ArrayList<String>());
        if (!flag){
            rv.setAdapter(adapter2);//TODO
        }else {
            rv.setAdapter(adapter);//TODO
        }
        rv.addItemDecoration(new DividerGridItemDecoration(contex));
        // 设置item动画
        rv.setItemAnimator(new DefaultItemAnimator());

        //设置RadioButton的监听，如果是问答题的话，隐藏选项框和添加选项按钮
        RadioButton rb = (RadioButton) holder.quzView.findViewById(R.id.comp_quz_rad4);
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.quzView.findViewById(R.id.comp_quz_chofield).setVisibility(View.GONE);
                    holder.quzView.findViewById(R.id.comp_quz_addchos).setVisibility(View.GONE);
                } else {
                    holder.quzView.findViewById(R.id.comp_quz_chofield).setVisibility(View.VISIBLE);
                    holder.quzView.findViewById(R.id.comp_quz_addchos).setVisibility(View.VISIBLE);
                }
            }
        });

        //设置添加选项按钮的监听事件
        holder.quzView.findViewById(R.id.comp_quz_addchos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag){
                    int count = adapter2.getItemCount();
                    adapter2.addData(count);
                }else {
                    int count = adapter.getItemCount();
                    adapter.addData(count);
                }
            }
        });
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            //holder.itemView.
            holder.quzView.findViewById(R.id.deletequz).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.quzView.findViewById(R.id.deletequz), pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    removeData(pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return quzs.size();
    }

    public void addData(int position) {
        quzs.add(position, new Quz());
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        quzs.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);
            quzView = (LinearLayout) view.findViewById(R.id.myview);
        }

        LinearLayout quzView;
    }
}
