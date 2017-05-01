package com.xinli.xinli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyu on 22/03/2017.
 */
public class RecyclerChosAdapter extends RecyclerView.Adapter<RecyclerChosAdapter.MyViewHolder2> {

    private List<String> mDatas;
    //    private int count;
    private LayoutInflater mInflater;
    Context contex;

    public void setmDatas(List<String> mDatas) {
        this.mDatas = mDatas;
    }

    public void setContex(Context contex) {
        this.contex = contex;
    }

    public RecyclerChosAdapter(Context context, List<String> datas) {
        contex = context;
        mInflater = LayoutInflater.from(context);
        if (datas == null) datas = new ArrayList<String>();
        mDatas = datas;
//        count = 0;
    }

    //用于点击事件回调
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        Toast.makeText(contex, " onCreateViewHolder",
                Toast.LENGTH_SHORT).show();
        MyViewHolder2 holder = new MyViewHolder2(mInflater.inflate(R.layout.edit_exam_chose, parent, false));
        final int pos = holder.getLayoutPosition();
        Toast.makeText(contex, "onCreate" + pos, Toast.LENGTH_SHORT).show();
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder2 holder, int position) {
        Toast.makeText(contex, " onBindViewHolder",
                Toast.LENGTH_SHORT).show();
        final int pos = holder.getLayoutPosition();
        Toast.makeText(contex, "OnBind" + pos, Toast.LENGTH_SHORT).show();

        TextView num = (TextView) holder.mychoses.findViewById(R.id.edit_quz_chose_num);
        num.setText("" + pos);

        Button deleteButton = (Button) holder.mychoses.findViewById(R.id.edit_quz_chose_dele_bt);
        //设置添加选项按钮的监听事件
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(contex, pos + " click",
                        Toast.LENGTH_SHORT).show();
                removeData(pos);
            }
        });
//        holder.tv.setText(mDatas.get(position));
//        holder.quzView.setTag(position);


//        // 如果设置了回调，则设置点击事件
//        if (mOnItemClickLitener != null) {
//            //holder.itemView.
//            holder.quzView.findViewById(R.id.deletequz).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemClick(holder.quzView.findViewById(R.id.deletequz), pos);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    removeData(pos);
//                    return false;
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
//        return count;
    }


    public void addData(int position) {
        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
//        count += 1;
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
//        count -= 1;
    }


    class MyViewHolder2 extends RecyclerView.ViewHolder {

        public MyViewHolder2(View view) {
            super(view);
            mychoses = (LinearLayout) view.findViewById(R.id.mychoses);
//            num = (TextView) view.findViewById(R.id.edit_exam_chose_num);
//            editText = (EditText) view.findViewById(R.id.edit_exam_chose_edittext);
//            deleteButton = (Button) view.findViewById(R.id.edit_exam_chose_dele_bt);
        }

        LinearLayout mychoses;
//        TextView num;
//        EditText editText;
//        Button deleteButton;
    }
}
