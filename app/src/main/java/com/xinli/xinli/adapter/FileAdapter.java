package com.xinli.xinli.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinli.xinli.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Bitmap icon_backroot;
    private Bitmap icon_backparent;
    private Bitmap icon_folder;
    private Bitmap icon_doc;
    private List<String> items;
    private List<String> paths;


    private List<ViewHolder> viewHolders = new ArrayList<ViewHolder>();
    private Context context;

    public FileAdapter(Context context, List<String> its, List<String> pas) {

        this.context = context;
        mInflater = LayoutInflater.from(context);

        items = its;
        paths = pas;
        icon_backroot = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.backroot);
        icon_backparent = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.backparent);
        icon_folder = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder);
        icon_doc = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.doc);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_row, null);
            holder = new ViewHolder();
            viewHolders.add(holder);
            holder.text = (TextView) convertView.findViewById(R.id.filetext);
            holder.icon = (ImageView) convertView.findViewById(R.id.fileicon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
        if (items.get(position).toString().equals("goroot")) {
            holder.text.setText("返回根目录");
            holder.icon.setImageBitmap(icon_backroot);
        } else if (items.get(position).toString().equals("goparent")) {
            holder.text.setText("返回上一级");
            holder.icon.setImageBitmap(icon_backparent);
        } else {
            holder.text.setText(f.getName());
            if (f.isDirectory()) {
                holder.icon.setImageBitmap(icon_folder);
            } else {
                holder.icon.setImageBitmap(icon_doc);
            }
        }
        return convertView;
    }

    public List<ViewHolder> getviewHolders() {
        return viewHolders;
    }

    public final class ViewHolder {
        public TextView text;
        public ImageView icon;
    }
}