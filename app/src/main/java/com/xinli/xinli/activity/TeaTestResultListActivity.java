package com.xinli.xinli.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xinli.xinli.R;
import com.xinli.xinli.adapter.TeaTestResultListAdapter;
import com.xinli.xinli.bean.bean.TestResultItemBean;
import com.xinli.xinli.bean.protocol.CTeaDownTestResult;
import com.xinli.xinli.bean.protocol.CTeaTestResultItem;
import com.xinli.xinli.bean.protocol.STeaDownTestResult;
import com.xinli.xinli.bean.protocol.STeaTestResultItem;
import com.xinli.xinli.service.TeaDownTestResultWork;
import com.xinli.xinli.service.TeaTestResultListWork;
import com.xinli.xinli.util.AppManager;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class TeaTestResultListActivity extends MyBaseActivity {

    private RecyclerView recyclerView;
    private TeaTestResultListAdapter adapter;

    List<TestResultItemBean> datas;

    String DirUri;
    String testId;
    String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_test_result_list);

        new MyAsync().execute();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            testId = ((TestResultItemBean) v.getTag()).getTestId();
            testName = ((TestResultItemBean) v.getTag()).getTestName();
            Intent intent = new Intent(TeaTestResultListActivity.this, FileBrowserActivity.class);
            TeaTestResultListActivity.this.startActivityForResult(intent, 233);
        }
    };
//    DirUri


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 233) {
            Bundle b = data.getExtras();
            DirUri = b.getString("DirUri");
            //TODO 开始存储文件
            new MyAsync2(this).execute();

        }

    }

    class MyAsync2 extends AsyncTask<Void, Void, String> {
        Context context;

        public MyAsync2(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            CTeaDownTestResult c = new CTeaDownTestResult(testId);
            STeaDownTestResult s = TeaDownTestResultWork.getResult(c);
            //TODO k开始保存文件
            String filePath = DirUri + "/" + testName + ".xlsx";
            File f = new File(filePath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                fos.write(s.getDoc().getBytes("UTF-8"));
                return "success";
            } catch (FileNotFoundException e) {
                Log.e("写Excel文件", "目标写入文件找不到");
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                Log.e("写Excel文件", "写失败");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(context, "写入文件失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (s.equals("success")) {
                Toast.makeText(context, "写入文件成功", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public void refresh(Object... param) {
        recyclerView = (RecyclerView) this.findViewById(R.id.recy_testResultList);
        adapter = new TeaTestResultListAdapter(datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 更新列表的
     */
    class MyAsync extends AsyncTask<Void, Void, List<TestResultItemBean>> {

        @Override
        protected List<TestResultItemBean> doInBackground(Void... params) {
            CTeaTestResultItem c = new CTeaTestResultItem(AppManager._id);
            STeaTestResultItem s = TeaTestResultListWork.getTestResultItem(c);
            String itemListAsJson = s.getItems();
            Gson gson = new Gson();
            Type type = new com.google.gson.reflect.TypeToken<List<TestResultItemBean>>() {
            }.getType();
            List<TestResultItemBean> items = gson.fromJson(itemListAsJson, type);
            return items;
        }

        @Override
        protected void onPostExecute(List<TestResultItemBean> testResultItemBeen) {
            datas = testResultItemBeen;
            refresh();
        }
    }
}
