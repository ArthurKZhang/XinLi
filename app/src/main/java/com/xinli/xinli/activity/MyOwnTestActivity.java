package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.app.ActionBar.LayoutParams;

import com.xinli.xinli.R;
import com.xinli.xinli.activity.upload_test_tool.SimpleItemTouchHelperCallback;
import com.xinli.xinli.adapter.MyOwntestAdapter;
import com.xinli.xinli.bean.bean.PublishRecord;
import com.xinli.xinli.util.AppManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyOwnTestActivity extends MyBaseActivity {
    private ItemTouchHelper mItemTouchHelper;
    private MyOwntestAdapter adapter;
    private List<PublishRecord> records;

    private void initData() {
        records = new ArrayList<PublishRecord>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setCustomActionBar();
        setContentView(R.layout.activity_my_own_test_list);

//        adapter = new MyOwntestAdapter(records);
//        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.myownTestRecyclerV);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void refresh(Object... param) {
        Map<String,Object> map = (Map<String, Object>) param[0];

        if (((String)map.get("hasNew")).equals("hasNew")){
            PublishRecord newRecord = (PublishRecord) map.get("record");
            updateUIadd(newRecord);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, Object> tag = (Map<String, Object>) v.getTag();
            PublishRecord record = (PublishRecord) tag.get("record");
            int position = (int) tag.get("position");

            Intent intent = new Intent(MyOwnTestActivity.this,DoTestAtivity.class);
            intent.putExtra("position",position);
            intent.putExtra("record",record.getTestId());
            startActivityForResult(intent,1);
        }
    };

    public void updateUIadd(PublishRecord u) {
        adapter.onItemAdd(u);
    }

    public void updateUIremove(int index) {
        adapter.onItemDismiss(index);
    }

    private void setCustomActionBar() {
        LayoutParams lp = new LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        TextView textView = (TextView) mActionBarView.findViewById(R.id.tv_actionbar);
        textView.setText("Message Yard");
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(AppManager.dip2px(this, 10));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(MyOwnTestActivity.this).setTitle("Exit App?")
                    .setMessage("Click \"Exit\" button to exit App")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppManager.getAppManager().AppExit(MyOwnTestActivity.this);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        item = menu.findItem(R.id.action_refresh);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
