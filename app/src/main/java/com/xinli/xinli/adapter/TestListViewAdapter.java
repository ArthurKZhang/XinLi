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
import com.xinli.xinli.bean.TestListItem;
import com.xinli.xinli.bean.test.TestLI;

import java.util.List;

/**
 * Created by zhangyu on 10/11/16.
 */
public class TestListViewAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private List<TestLI> testLIs;

    public TestListViewAdapter(Context context, List<TestLI> testLIs) {
        this.context = context;
        this.testLIs = testLIs;
    }

    @Override
    public int getCount() {
        return testLIs.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<0 || position>=getCount()){
            return null;
        }
        return testLIs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.test_list_item,parent,false);
            holder = new MyViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.im_testItemImage);
            holder.shortdes = (TextView) convertView.findViewById(R.id.tv_shortDes);
            holder.viewed = (TextView) convertView.findViewById(R.id.tv_viewedNum);
            holder.tested = (TextView) convertView.findViewById(R.id.tv_testedNum);
//            holder.testURI =
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        TestLI data = (TestLI) getItem(position);
        /*
        AsyncTaskImageLoad extends AsyncTask<S
        异步加载图片:http://www.cnblogs.com/snake-hand/p/3206655.html
         */
        holder.iv.setImageResource(data.img);
        holder.shortdes.setText(""+data.shortdes);
        holder.viewed.setText(""+data.readn);
        holder.tested.setText(""+data.testn);
//        holder.testURI = data.getTestURI();
        return convertView;

    }

    public static class MyViewHolder {
        ImageView iv = null;
        TextView shortdes = null;
        TextView viewed = null;
        TextView tested = null;
//        String testURI = null;
    }
}