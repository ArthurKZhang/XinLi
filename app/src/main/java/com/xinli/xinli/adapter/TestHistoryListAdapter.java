package com.xinli.xinli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.test.TestLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 10/11/16.
 */
public class TestHistoryListAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<Map<String,String>> itemMaps;

    /**
     * Map 里应该包含两个键值对,testUri 和 tag.
     * @param context
     * @param maps
     */
    public TestHistoryListAdapter(Context context, List<Map<String,String>> maps) {
        this.context = context;
        if (maps==null||maps.isEmpty()){
            this.itemMaps = new ArrayList<>();
        }else {
            this.itemMaps = maps;
        }
    }

    @Override
    public int getCount() {
        return itemMaps.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<0 || position>=getCount()){
            return null;
        }
        return itemMaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_list_item,parent,false);
            holder = new MyViewHolder();
            holder.tv_testName = (TextView) convertView.findViewById(R.id.tv_testName);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        Map<String,String> data = (Map<String,String>) getItem(position);

        holder.tv_testName.setText(data.get("testUri").toString());
        holder.tv_testName.setTag(data.get("tag").toString());
        return convertView;

    }

    public static class MyViewHolder {
        public TextView tv_testName = null;
    }

}