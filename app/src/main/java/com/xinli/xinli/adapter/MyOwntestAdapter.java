package com.xinli.xinli.adapter;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.activity.upload_test_tool.ItemTouchHelperAdapter;
import com.xinli.xinli.activity.upload_test_tool.ItemTouchHelperViewHolder;
import com.xinli.xinli.activity.upload_test_tool.OnStartDragListener;
import com.xinli.xinli.bean.bean.PublishRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 02/05/2017.
 */
public class MyOwntestAdapter extends RecyclerView.Adapter<MyOwntestAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<PublishRecord> records;
    private View.OnClickListener onClickListener;

    public MyOwntestAdapter(List<PublishRecord> items) {
        records.addAll(items);
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_own_text_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.testName.setText(records.get(position).getTestName());
        holder.endDate.setText(records.get(position).getEndDate().toString());
        holder.testName.setText(records.get(position).getTeacherName());

        Map<String, Object> tag = new HashMap<>();
        tag.put("record", records.get(position));
        tag.put("position", position);
        holder.item.setTag(tag);

        if (this.onClickListener != null) {
            holder.item.setOnClickListener(onClickListener);
        }
    }

    public void onItemAdd(PublishRecord record) {
        records.add(record);
        notifyItemInserted(0);
    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(records, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //TODO 对数据进行处理，数据库，服务器
        records.remove(position);
        notifyItemRemoved(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        public LinearLayout item;
        public TextView testName;
        public TextView endDate;
        public TextView teaName;
        public ImageView icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item = (LinearLayout) itemView.findViewById(R.id.myowntestitem);
            testName = (TextView) itemView.findViewById(R.id.myowntestitem_testname);
            endDate = (TextView) itemView.findViewById(R.id.myowntestitem_endDate);
            teaName = (TextView) itemView.findViewById(R.id.myowntestitem_teaName);
            icon = (ImageView) itemView.findViewById(R.id.myowntestitem_img);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
