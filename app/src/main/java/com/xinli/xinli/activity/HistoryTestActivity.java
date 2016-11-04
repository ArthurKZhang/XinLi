package com.xinli.xinli.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.adapter.TestHistoryListAdapter;
import com.xinli.xinli.adapter.TestListViewAdapter;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.testdao.LoginUtil;
import com.xinli.xinli.testdao.SharedFileHelper;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HistoryTestActivity extends MyBaseActivity {

    private String userType;
    private ListView listView;

    /**
     * 存储的是每一个List Item 的 testName 和 tag 内容-(teacher||student)
     */
    private List<Map<String, String>> itemMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局代码一模一样,就是一个ListView,所以复用
        setContentView(R.layout.activity_test_list);

        userType = (String) getIntent().getExtras().get("usertype");

//        initViews();
// NullPointerException: Attempt to invoke interface method 'int java.util.List.size()' on a null object reference

        loadData(userType);

    }

    private void initViews() {
        listView = (ListView) findViewById(R.id.testList);
        if(itemMaps==null||itemMaps.isEmpty()){
            return;
        }
        listView.setAdapter(new TestHistoryListAdapter(this, itemMaps));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tvtemp = null;
//                if (view instanceof TextView) {
//                    Log.e("test","*********************");
//                    tvtemp = (TextView) view;
//                }
                String tag = (String) ((TestHistoryListAdapter.MyViewHolder)view.getTag()).tv_testName.getTag();

//                String tag = tvtemp.getTag().toString();
                if (tag.equals("student")) {
                    Intent it = new Intent(HistoryTestActivity.this, ShowTestResultActivity.class);
                    Bundle bd = new Bundle();
                    bd.putString("resultUri", itemMaps.get(position).get("testUri"));
                    it.putExtras(bd);
                    startActivity(it);
                }
                if (tag.equals("teacher")) {
                    Log.e("test", "teacher Item clicked");
                    //选择下载地点, 复用文件浏览Activity
                    Intent it = new Intent(HistoryTestActivity.this, FileBrowserActivity.class);
                    Bundle bd = new Bundle();
//                    bd.putString("resultUri", itemMaps.get(position).get("testUri"));
                    it.putExtras(bd);
                    startActivity(it);
                }
            }
        });
        setContentView(listView);
    }

    private void loadData(String type) {

        if (userType.equals(LoginUtil.STUDENT)) {
            loadDidTest();
        }
        if (userType.equals(LoginUtil.TEACHER)) {
            loadUploadedTest();
        }
    }

    private void loadDidTest() {

        SharedPreferences sharedPreferences = getSharedPreferences("submitTestHistory", Context.MODE_PRIVATE);
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("sharedPreferences", sharedPreferences);
        Task ts = new Task(Task.TEST_HISTORY_GET_DATA, hm);
        Log.d("test", "HistoryTestActivity-->loadDidTest-" + "");
        MyService.newTask(ts);
    }

    private void loadUploadedTest() {
        //读取纪录的上传文件的信息
        SharedPreferences sharedPreferences = getSharedPreferences("UploadedTest", Context.MODE_PRIVATE);

        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("sharedPreferences", sharedPreferences);
//        hm.put("category", category);
        Task ts = new Task(Task.UPLOADED_HISTORY_GET_DATA, hm);
        Log.d("test", "HistoryTestActivity-->loadUploadedTest-" + "");
        MyService.newTask(ts);

    }

    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];
        itemMaps = (List<Map<String, String>>) map.get("list");
        initViews();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(HistoryTestActivity.this);
        }
        return false;
    }

}
