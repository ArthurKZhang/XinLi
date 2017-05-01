package com.xinli.xinli.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xinli.xinli.R;
import com.xinli.xinli.activity.upload_test_tool.TeacherTestListFragment;
import com.xinli.xinli.bean.bean.UploadRecord;
import com.xinli.xinli.bean.protocol.CDownTeacherTestList;
import com.xinli.xinli.bean.protocol.SDownTeacherTestList;
import com.xinli.xinli.dao.UploadListDao;
import com.xinli.xinli.service.TeaDownTestListWork;
import com.xinli.xinli.util.AppManager;

import java.lang.reflect.Type;
import java.util.List;

public class TeacherTestListActivity extends MyBaseActivity {
    final String TAG = "TeacherTestListActivity";
    private TeacherTestListFragment fragment;

    private static final int ADDNEWTEST = 1;
    private static final int EDITTEST = 2;

    Gson gson = new Gson();
    Type typeListRecords = new com.google.gson.reflect.TypeToken<List<UploadRecord>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menu item click 的监听事件要设定在 setSupportActionBar 之后才有作用
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        //获取列表
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                String name = AppManager.getAppManager().userName;
                //数据库中有，读出来
                UploadListDao uploadListDao = new UploadListDao(TeacherTestListActivity.this);
                List<UploadRecord> records = uploadListDao.query(name);
                Log.e(TAG, "get records from database:" + records.toString() + "<end>");
                //数据库中没有，访问服务器，存服务器
                if (records == null || records.isEmpty()) {
                    Log.e(TAG, "no records got from database");
                    Log.e(TAG, "get records from server");
                    //访问服务器
                    CDownTeacherTestList c = new CDownTeacherTestList(name);
                    SDownTeacherTestList d = TeaDownTestListWork.down(c);
                    if (d == null) {
                        Log.e(TAG, "no records got from server");
                        //服务器也没有，返回空
                        return null;
                    }
                    records = d.getRecords();
                    //在存储进数据库
                    for (UploadRecord one : records) {
                        uploadListDao.insert(one);
                    }
                }

                String itemsInString = gson.toJson(records, typeListRecords);
                return itemsInString;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                fragment = new TeacherTestListFragment();
                fragment.setContext(TeacherTestListActivity.this);
                Bundle bundle = new Bundle();
                bundle.putString("content", s);
                fragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content, fragment)
                        .commit();
            }
        }.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADDNEWTEST:
                Bundle b = data.getExtras();
                String fileid = b.getString("fileid", null);
                String filepath = b.getString("filepath", null);
                String testname = b.getString("testname", null);
                if (testname != null) {
                    UploadRecord uploadRecord = new UploadRecord(AppManager.getAppManager().userName, testname, fileid, filepath);
                    Log.e(TAG, "new record:" + uploadRecord.toString());
                    fragment.updateUIadd(uploadRecord);
                }
                break;
            case EDITTEST:
                String flag = data.getStringExtra("flag");
                int position = data.getIntExtra("position",-1);
                String userName = data.getStringExtra("userName");
                String testName = data.getStringExtra("testName");
                String testId = data.getStringExtra("testId");
                String cachePath = data.getStringExtra("cachePath");
                switch (flag) {
                    case "good":
                        Toast.makeText(TeacherTestListActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                        fragment.updateUIremove(position);
                        fragment.updateUIadd(new UploadRecord(userName,testName,testId,cachePath));
                        break;
                    case "bad":
                        Toast.makeText(TeacherTestListActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
                        break;
                    case "back":
                        break;
                }
                break;
        }
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_settings:
                    msg += "Click 创建新的试题";
                    break;
            }

            Toast.makeText(TeacherTestListActivity.this, msg, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(TeacherTestListActivity.this, EditTestActivity.class);
            intent.putExtra("isAddNew", true);
            TeacherTestListActivity.this.startActivityForResult(intent, ADDNEWTEST);
            return true;
        }
    };

    @Override
    public void refresh(Object... param) {

    }
}
