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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.ui.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 21/03/2017.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context contex;

    private List<String> mDatas;
    private LayoutInflater mInflater;

//    RecyclerChosAdapter adapter;// = new RecyclerChosAdapter(contex, new ArrayList<String>());

    public RecyclerAdapter(Context context, List<String> datas) {
        contex = context;

//        adapter = new RecyclerChosAdapter(contex, new ArrayList<String>());不能在这里创建
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
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

        RecyclerView rv = (RecyclerView) holder.quzView.findViewById(R.id.comp_quz_chofield);
        final RecyclerChosAdapter adapter = new RecyclerChosAdapter(contex, new ArrayList<String>());
        rv.setAdapter(adapter);//TODO
        rv.addItemDecoration(new DividerGridItemDecoration(contex));
        // 设置item动画
        rv.setItemAnimator(new DefaultItemAnimator());

        //设置RadioButton的监听，如果是问答题的话，隐藏选项框和添加选项按钮
        RadioButton rb = (RadioButton)holder.quzView.findViewById(R.id.comp_quz_rad4);
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.quzView.findViewById(R.id.comp_quz_chofield).setVisibility(View.INVISIBLE);
                    holder.quzView.findViewById(R.id.comp_quz_addchos).setVisibility(View.INVISIBLE);
                }else {
                    holder.quzView.findViewById(R.id.comp_quz_chofield).setVisibility(View.VISIBLE);
                    holder.quzView.findViewById(R.id.comp_quz_addchos).setVisibility(View.VISIBLE);
                }
            }
        });

        //设置添加选项按钮的监听事件
        holder.quzView.findViewById(R.id.comp_quz_addchos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = adapter.getItemCount();
                Toast.makeText(contex, "" + count, Toast.LENGTH_SHORT).show();
                adapter.addData(count);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.tv.setText(mDatas.get(position));
//        holder.quzView.setTag(position);
//        //设置添加选项按钮的监听事件
//        holder.quzView.findViewById(R.id.comp_quz_addchos).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int count = adapter.getItemCount();
//                Toast.makeText(contex,""+count,Toast.LENGTH_SHORT).show();
//                adapter.addData(count);
//
//            }
//        });


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
        return mDatas.size();
    }

    public void addData(int position) {
        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
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
