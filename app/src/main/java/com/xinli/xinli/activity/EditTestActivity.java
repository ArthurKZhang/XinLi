package com.xinli.xinli.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xinli.xinli.R;
import com.xinli.xinli.adapter.RecyclerAdapter;
import com.xinli.xinli.bean.bean.Quz;
import com.xinli.xinli.bean.bean.Test;
import com.xinli.xinli.bean.bean.UploadRecord;
import com.xinli.xinli.bean.protocol.CTeacherPostTest;
import com.xinli.xinli.bean.protocol.STeacherPostTest;
import com.xinli.xinli.dao.UploadListDao;
import com.xinli.xinli.service.TeaPostTestWork;
import com.xinli.xinli.ui.DividerGridItemDecoration;
import com.xinli.xinli.util.AppManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyu on 21/03/2017.
 */
public class EditTestActivity extends MyBaseActivity {
    private static final int ADDNEWTEST = 1;
    private static final int EDITTEST = 2;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    /**
     * 这个mDatas放的是一个试题Test，里面包括很多题Quz和一个题目名称
     */
    private Test alldata;

    EditText et_testName;
    private Button addButton, deleteButton, bt_saveAsNew, bt_saveEdit;
    private TextView justfortest;

    private boolean isAddNew;
    private String fileID;
    private String filePath;
    private String testName;

    private int recordPosition;

    private String newTestName;

    private String appPath;
    private String content;

    Gson gson = new Gson();
    Type testtype = new com.google.gson.reflect.TypeToken<Test>() {
    }.getType();

    /**
     * 根据传到这个activity的参数进行负值
     */
    private void initData() {
        appPath = getApplicationContext().getFilesDir().getPath() + "/teacherTestCache/";
        isAddNew = getIntent().getBooleanExtra("isAddNew", true);
        recordPosition = getIntent().getIntExtra("position", -1);
        if (isAddNew == true) {
            Log.e("EditTest", "isAddNew?:" + isAddNew);
            fileID = null;
            filePath = null;
            content = null;
            testName = null;
            alldata = new Test(testName, new ArrayList<Quz>());
            return;
        }
        Log.e("EditTest", "isAddNew?:" + isAddNew);
        fileID = getIntent().getStringExtra("testid");
        content = getIntent().getStringExtra("content");
        alldata = gson.fromJson(content, testtype);
        if (alldata == null) {
            testName = "";
        } else {
            testName = alldata.getTestName();
        }
    }

    private void initView() {
        et_testName = (EditText) this.findViewById(R.id.et_testName);

        addButton = (Button) this.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 这样如果是添加新的试题的话，size = 0；要么这里的quzs和adapter里的实时同步，要么就交给adapter
                //TODO 个人倾向前者，内存中是同一个对象  完成！
//                adapter.addData(quzs.size());
                adapter.addData(alldata.getQuzs().size());
                Log.e("@@@@", "" + alldata.getQuzs().size());
            }
        });

        deleteButton = (Button) this.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adapter.removeData(quzs.size() - 1);
                adapter.removeData(alldata.getQuzs().size() - 1);
            }
        });

        bt_saveEdit = (Button) findViewById(R.id.bt_saveEdit);
        //TODO 保存编辑
        bt_saveEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO 获取新的fileName
                testName = et_testName.getText().toString().trim();
                if (testName == null || testName.isEmpty()) {
                    Toast.makeText(EditTestActivity.this, "必须要有试题名字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fileID == null || fileID.isEmpty()) {
                    return;
                }
                //TODO 保存编辑
                saveEditTest.execute();

            }
        });

        bt_saveAsNew = (Button) this.findViewById(R.id.bt_saveAsNew);
        //TODO 保存为新试题
        bt_saveAsNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTestName = et_testName.getText().toString().trim();
                //TODO 保存新试题
                if (newTestName == null || newTestName.isEmpty()) {
                    Toast.makeText(EditTestActivity.this, "必须要有试题名字", Toast.LENGTH_SHORT).show();
                    return;
                }
                new MySaveNewTestAsyn(EditTestActivity.this).execute();

            }
        });
//        justfortest = (TextView) this.findViewById(R.id.justfortest);

        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);

        adapter = new RecyclerAdapter(this, alldata);
        initRecollEvent();
        if (isAddNew == true) {
            bt_saveEdit.setVisibility(View.GONE);
            adapter.setFlag(false);
        } else {
            et_testName.setText(testName);
            adapter.setFlag(true);
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(EditTestActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        // 设置item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_exam);

        initData();
        initView();
    }

    /**
     * * 更新客户端的缓存
     *
     * @param fileName 不能为空
     * @param filePath 为空表示是新添加的试题，否则是再次编辑试题
     * @param content  测试题的内容
     * @return String 新文件缓存的path
     */
    private String updateCache(String fileName, String filePath, String content) {
        if (filePath == null || filePath.isEmpty()) {
            // 这是要 添加新的试题
            filePath = appPath + fileName;
            fileID = "";
            File file = save2Disk(filePath, content);
            //数据库插入
            UploadListDao u = new UploadListDao(EditTestActivity.this);
            Log.e("EditTest", "updateCache-try insert:userName:" + AppManager.getAppManager().userName
                    + ";filename:" + fileName + ";filePath" + filePath);
            boolean i = u.insert(new UploadRecord(AppManager.getAppManager().userName,
                    fileName, fileID, filePath));
            if (i) {
                Log.e("DB", "insert testRecord " + fileName + "success");
            } else {
                Log.e("DB", "insert testRecord " + fileName + "failure");
            }
        } else {
            File file = save2Disk(filePath, content);
        }
        return filePath;
    }

    /**
     * 使用简单的post方式
     *
     * @param content
     * @return
     */
    public String upLoadTestByPost(String userName, String testName, String content) {
        CTeacherPostTest c = new CTeacherPostTest(userName, testName, content);
        STeacherPostTest s = TeaPostTestWork.postTest(c);
        if (s == null) return null;
        else return s.getTestID();
    }

    private File save2Disk(String filepath, String content) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return null;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File(appPath);
        file.mkdirs();// 创建文件夹
        try {
            fileOutputStream = new FileOutputStream(filepath);
            fileOutputStream.write(content.getBytes());

            Log.i("test", "save test ready ,path:" + filepath);

            return new File(filepath);
        } catch (IOException e) {
            Log.e("test", "save test IOException");
            e.printStackTrace();
            return null;
        } finally {
            try {
                // 关闭流
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Test getTest(Context context) {
        testName = et_testName.getText().toString().trim();
        List<Quz> quzs = new ArrayList<Quz>();

        int total = recyclerView.getChildCount();
        for (int i = 0; i < total; i++) {
            View item = recyclerView.getChildAt(i);
            EditText quzName = (EditText) item.findViewById(R.id.comp_quz_quzcontent);

            RadioGroup rg = (RadioGroup) item.findViewById(R.id.comp_quz_radiogrp);
            int checkBoxID = rg.getCheckedRadioButtonId();
            int type = -1;
            switch (checkBoxID) {
                case R.id.comp_quz_rad1:    //单选
                    type = 1;
                    break;
                case R.id.comp_quz_rad2:    //多选
                    type = 2;
                    break;
                case R.id.comp_quz_rad3:    //判断
                    type = 3;
                    break;
                case R.id.comp_quz_rad4:    //填空
                    type = 4;
                    break;
                case -1:
                    Toast.makeText(context, "第" + i + 1 + "没选题目类型", Toast.LENGTH_SHORT).show();
                    break;
            }

            RecyclerView choses = (RecyclerView) item.findViewById(R.id.comp_quz_chofield);
            int choseNum = choses.getChildCount();

            List<String> choseList = new ArrayList<String>();
            for (int j = 0; j < choseNum; j++) {
                EditText choseEditText = (EditText) choses.getChildAt(j).findViewById(R.id.edit_quz_chose_edittext);
                choseList.add(choseEditText.getText().toString().trim());
            }

            Quz quz = new Quz();
            quz.setQuzContent(quzName.getText().toString().trim());
            quz.setType(type);
            if (type == 3 && choseNum != 2) {
                Toast.makeText(context, "第" + i + 1 + "题不是判断题，判断题只有两个选项", Toast.LENGTH_SHORT).show();
                return null;//"第"+i+1+"题不是判断题，判断题只有两个选项";
                //TODO 判断只有对错，如果是 "不确定" 之类的，那是选择题
            }
            if (type != 4) {
                quz.setChooseNum(choseNum);
                quz.setChooseItems(choseList);
            }
            quzs.add(quz);
        }
        return new Test(testName, quzs);
    }

    private String getTestAsJson(Context context) {
        Test test = getTest(context);
        String json = gson.toJson(test, testtype);
        return json;
    }

    /**
     * 设置RecyclerAdapter 里面的回调函数
     */
    private void initRecollEvent() {
        adapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(EditTestActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
                adapter.removeData(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(EditTestActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void refresh(Object... param) {

    }

    class MySaveNewTestAsyn extends AsyncTask<Void, Void, Map<String, String>> {

        Context context;

        public MySaveNewTestAsyn(Context context) {
            this.context = context;
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            Map<String, String> map = new HashMap<String, String>();

            if (newTestName.equals(testName)) {
                Toast.makeText(context, "测试题的名字不可以重复", Toast.LENGTH_SHORT).show();
                return null;
            }
            content = getTestAsJson(context);

            updateCache(newTestName, null, content);
//                        String fid = uploadFile(file);TODO 备用方案

            String fid = upLoadTestByPost(AppManager.getAppManager().userName, testName, content);
            if (fid == null) {
                return null;
            }
            //TODO 更新数据库，插入测试题的id
            boolean updataOK = new UploadListDao(EditTestActivity.this).update(newTestName, "testId", fid);
            if (!updataOK) {
                Log.e("test", "update testid error");
            }
            map.put("fileid", fid);
            map.put("filepath", filePath);
            map.put("testname", testName);
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, String> s) {
            if (s == null) {
//                            Log.e("test", "发生了错误");
//                Toast.makeText(EditTestActivity.this,"保存发生了错误"，Toast.)
                return;
            }
            Log.i("test", "保存--新新新--操作所有操作完成");

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("fileid", s.get("fileid"));
            bundle.putString("filepath", s.get("filepath"));
            bundle.putString("testname", s.get("testname"));

            intent.putExtras(bundle);
            EditTestActivity.this.setResult(ADDNEWTEST, intent);
            AppManager.getAppManager().finishActivity(EditTestActivity.this);

        }
    }

    ;

    AsyncTask<Void, Void, Map<String, Object>> saveEditTest = new AsyncTask<Void, Void, Map<String, Object>>() {

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            updateCache(testName, filePath, content);
//            String reId = uploadFile(file);TODO 备用方案
            Test t = getTest(EditTestActivity.this);
            String testId = upLoadTestByPost(AppManager.getAppManager().userName, testName, gson.toJson(t, testtype));
            Map<String, Object> info = new HashMap<>();

            if (testId == null) {
                info.put("flag", "nothing return from server");
                Log.e("test", "没有东西返回");
                Toast.makeText(EditTestActivity.this, "network error", Toast.LENGTH_LONG).show();
                info.put("flag", "bad");
            } else {
                if (!testId.equals(fileID)) {
                    Log.e("test", "上传给服务器更新后测试题id变化了:返回：" + testId + "||old:" + fileID);
                }
                boolean updataOK = new UploadListDao(EditTestActivity.this).update(newTestName, "testId", testId);
                if (!updataOK) {
                    Log.e("test", "saveEditTest->update testid error");
                }
                info.put("flag", "good");
                info.put("position", recordPosition);
            }
            return info;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Map<String, Object> s) {
            if (s.isEmpty()) return;
            Log.i("test", "保存操作所有操作完成");
            Intent intent = new Intent();
            if (s.get("flag").equals("good")) {
                intent.putExtra("flag", "good");
                intent.putExtra("position", (Integer) s.get("position"));

                intent.putExtra("userName", AppManager.getAppManager().userName);
                intent.putExtra("testName", testName);
                intent.putExtra("testId", fileID);
                intent.putExtra("cachePath", filePath);

                EditTestActivity.this.setResult(EDITTEST, intent);
                AppManager.getAppManager().finishActivity(EditTestActivity.this);
            }
            if (s.get("flag").equals("bad")) {

            }
        }
    };

    /**
     * 按下返回键，这种状态下是没有登录成功的，否则如果登录成功会自行跳转回去。
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            if (isAddNew == false) {
                intent.putExtra("flag", "back");
                EditTestActivity.this.setResult(EDITTEST, intent);
                AppManager.getAppManager().finishActivity(EditTestActivity.this);
            } else {
                intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("fileid", null);
                bundle.putString("filepath", null);
                bundle.putString("testname", null);
                intent.putExtras(bundle);
                EditTestActivity.this.setResult(ADDNEWTEST, intent);
                AppManager.getAppManager().finishActivity(EditTestActivity.this);
            }

        }
        return false;

    }

    //    /**
//     * 上传文件到服务器使用socket
//     *
//     * @param file 返回文件的id
//     */
//    public String uploadFile(File file) {
//        String result;
//
//        Log.i("updateServer", "upload start");
//        try {
//            String requestUrl = "http://" + AppManager.serverIP + ":8080/Test/" + Resource.ACTION_UPLOAD_TEACHER_TEST_LIST;
//            //请求普通信息
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("username", AppManager.getAppManager().userName);
//            params.put("filename", testName);
//
//            //上传文件,第三个参数是struts2接收的参数
//            FormFile formfile = new FormFile(testName, file, "Json", "application/octet-stream");
//            result = FileUtil.post(requestUrl, params, formfile);
//            Log.i("updateServer", "upload success,result:" + result);
////            textView.setText("上传成功");
//            String id = "1111111";
//            return id;
//        } catch (Exception e) {
//            Log.i("updateServer", "upload error");
//            e.printStackTrace();
//            return null;
//        }
//    }
}
