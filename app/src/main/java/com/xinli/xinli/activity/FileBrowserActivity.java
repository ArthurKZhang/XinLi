package com.xinli.xinli.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.adapter.FileListAdapter;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.NotifyService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileBrowserActivity extends MyBaseActivity {

    final int RESULT_CODE = 233;

    private List<String> items = null;
    private List<String> paths = null;
    private String rootPath = "/sdcard";
    private TextView tv_currentPath;
    private ListView listView;
    private FileListAdapter m_FileListAdapter;

    /**
     * 被选择文件的Uri
     */
    String selectedDirUri = null;

    Button bt_ok, bt_cancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.filelist);

        tv_currentPath = (TextView) this.findViewById(R.id.mPath);
        listView = (ListView) this.findViewById(R.id.filelist);

        getFileDir(rootPath);

        tv_currentPath.setTextColor(this.getResources().getColor(R.color.colorAccent));

        bt_ok = (Button) this.findViewById(R.id.fileok);
        bt_ok.setPadding(0, 5, 0, 5);
        bt_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                showProgress();
                returnResult(selectedDirUri);
                return;
            }
        });

        bt_cancle = (Button) this.findViewById(R.id.filecancel);
        bt_cancle.setPadding(0, 5, 0, 5);
        bt_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult(null);
            }
        });
    }

    private void getFileDir(String filePath) {

        selectedDirUri = filePath;
        File f = new File(filePath);
        Toast.makeText(this, f.exists() + "" + f.canWrite(), Toast.LENGTH_LONG).show();
        if (f.exists() && f.canWrite()) {
            tv_currentPath.setText(filePath);
            items = new ArrayList<String>();
            paths = new ArrayList<String>();

            File[] files = f.listFiles();

            if (!filePath.equals(rootPath)) {
                items.add("goroot");
                paths.add(rootPath);
                items.add("goparent");
                paths.add(f.getParent());
            }
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                items.add(file.getName());
                paths.add(file.getPath());
            }
            m_FileListAdapter = new FileListAdapter(this, items, paths);
            listView.setAdapter(m_FileListAdapter);
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String fileUri = paths.get(position);

                    if (items.get(position).toString().equals("goparent")) {
                        getFileDir(fileUri);
                    } else if (items.get(position).toString().equals("goroot")) {
                        getFileDir(fileUri);
                        return;
                    } else {
                        File file = new File(fileUri);
                        if (file.isDirectory()) {
                            getFileDir(fileUri);
                        } else {
                            //如果是选择的文件，那么不作任何处理，就是显示颜色变化
                            view.setBackgroundColor(Color.GRAY);
                        }
                    }
                }
            });
        } else {
            LinearLayout lay = new LinearLayout(FileBrowserActivity.this);
            lay.setOrientation(LinearLayout.HORIZONTAL);
            ImageView image = new ImageView(FileBrowserActivity.this);
            TextView text = new TextView(FileBrowserActivity.this);
            text.setTextColor(Color.RED);
            text.setTextSize(20);
            text.setText("您没有访问的权限");
            Toast toast = Toast.makeText(FileBrowserActivity.this, text.getText().toString(), Toast.LENGTH_LONG);
            image.setImageResource(android.R.drawable.stat_sys_warning);
            lay.addView(image);
            lay.addView(text);
            toast.setView(lay);
            toast.show();
        }
    }

    private void returnResult(String dirUri) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("DirUri", dirUri);
        Toast.makeText(FileBrowserActivity.this, dirUri, Toast.LENGTH_SHORT).show();
        intent.putExtras(bundle);
        FileBrowserActivity.this.setResult(RESULT_CODE, intent);

        AppManager.getAppManager().finishActivity(FileBrowserActivity.this);
    }

    @Override
    public void refresh(Object... param) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnResult(null);
        }
        return false;

    }
    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////

//    final static int MAX_PROGRESS = 100;
//    private int[] data = new int[50];
//    int progressStatus = 0;
//    int hasData = 0;
//    ProgressDialog progressDialog;
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0x123)
//                progressDialog.setProgress(progressStatus);
//        }
//    };
//
//    private void showProgress() {
//        progressStatus = 0;
//        hasData = 0;
//        progressDialog = new ProgressDialog(FileBrowserActivity.this);
//        progressDialog.setMax(MAX_PROGRESS);
//        progressDialog.setTitle("Uploading...");
//        progressDialog.setMessage("Operation Progressing");
//        progressDialog.setCancelable(false);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setIndeterminate(false);
//        progressDialog.show();
//        new Thread() {
//            @Override
//            public void run() {
//                while (progressStatus < MAX_PROGRESS) {
//                    progressStatus = MAX_PROGRESS * doWork() / data.length;
//                    handler.sendEmptyMessage(0x123);
//                }
//                if (progressStatus >= MAX_PROGRESS) {
//                    progressDialog.dismiss();
//                    returnResult(selectedDirUri);
//                }
//            }
//        }.start();
//    }
//
//    private int doWork() {
//        data[hasData++] = (int) (Math.random() * 100);
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return hasData;
//    }

}