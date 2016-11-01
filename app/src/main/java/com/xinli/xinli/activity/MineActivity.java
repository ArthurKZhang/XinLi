package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.testdao.LoginUtil;
import com.xinli.xinli.util.AppManager;


public class MineActivity extends MyBaseActivity {

    private boolean isLogin;

    private String name;
    private int photo;

    private final int REQUEST_LOGIN = 1;

    private final int REQUEST_UPLOAD_FILE = 2;

    LinearLayout LL_UserPart;
    /**
     * 在LL_UserPart中
     */
    ImageButton IB_UserPhoto;
    /**
     * 在LL_UserPart中,用来显示名字和类型,学生还是心理医生还是没登录
     */
    TextView tv_UserName;
    Button bt_testHistory, bt_notificationSetting, bt_exitApp;
    Button btlogout, btupload, btdownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);

        checkLogin();

        initViews();

    }

    private void checkLogin() {
        isLogin = AppManager.getAppManager().isLoggedIn;
    }

    private void initViews() {
        LL_UserPart = (LinearLayout) findViewById(R.id.LL_UserPart);
        IB_UserPhoto = (ImageButton) findViewById(R.id.IB_UserPhoto);
        tv_UserName = (TextView) findViewById(R.id.tv_UserName);
        bt_testHistory = (Button) findViewById(R.id.bt_testHistory);
        bt_notificationSetting = (Button) findViewById(R.id.bt_notificationSetting);
        bt_exitApp = (Button) findViewById(R.id.bt_exitApp);

        MyOnClickListener listener = new MyOnClickListener();

        IB_UserPhoto.setOnClickListener(listener);
        bt_testHistory.setOnClickListener(listener);
        bt_notificationSetting.setOnClickListener(listener);
        bt_exitApp.setOnClickListener(listener);

        if (isLogin) {
            SharedPreferences sp = MineActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
            String uname = sp.getString("userName", "Login Please");
            String utype = sp.getString("userType", "");
            name = utype + ": " + uname;
            photo = sp.getInt("photo", -1);
            if (photo != -1)
                IB_UserPhoto.setImageResource(photo);
            tv_UserName.setText(name);

            addLogoutButton();

            if (utype.equals(LoginUtil.TEACHER)) {
                addUploadTestAndDownloadResultButtons();
            }
        }

    }


    @Override
    public void refresh(Object... param) {

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.IB_UserPhoto:
                    if (!isLogin) {
                        intent = new Intent(MineActivity.this, LoginActivity.class);
                        MineActivity.this.startActivityForResult(intent, REQUEST_LOGIN);
                    } else {
                        //设置新的点击事件,登陆后点击就要换头像了
                    }

                    break;
                case R.id.bt_testHistory:
                    break;
                case R.id.bt_notificationSetting:
                    break;
                case R.id.bt_exitApp:
                    break;


            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                Bundle bundle = data.getExtras();
                String name = bundle.getString("userName");
                String userType = bundle.getString("userType");
                Boolean isLogIn = bundle.getBoolean("isLogIn");
                int photo = bundle.getInt("photo");

                //更新界面
                if (isLogIn == true) {
                    tv_UserName.setText(userType + ": " + name);
                    IB_UserPhoto.setImageResource(photo);
                    MineActivity.this.isLogin = true;
                    //加载新的Logout选项:
                    addLogoutButton();
                    if (userType.equals(LoginUtil.TEACHER)) {
                        addUploadTestAndDownloadResultButtons();
                    }
                }
                break;
            case REQUEST_UPLOAD_FILE:
                //fileUri
                Bundle bundle1 = data.getExtras();
                Toast.makeText(MineActivity.this, "chose file Uri: " + bundle1.getString("fileUri"),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void addLogoutButton() {
        btlogout = new Button(MineActivity.this);

        btlogout.setText("Logout");
        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });
        LL_UserPart.addView(btlogout);
    }

    private void addUploadTestAndDownloadResultButtons() {
        btupload = new Button(MineActivity.this);
        btupload.setText("Upload Test");
        btupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","MineActivity-->addUploadTestAndDownloadResultButtons-->btupload.setOnClickListener");
                Intent intent = new Intent(MineActivity.this, FileBrowserActivity.class);
                Log.d("test","MineActivity-->btupload.setOnClickListener"+intent.toString());
                MineActivity.this.startActivityForResult(intent, REQUEST_UPLOAD_FILE);
            }
        });
        LL_UserPart.addView(btupload);

        btdownload = new Button(MineActivity.this);
        btdownload.setText("Download Test Result");
        btdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.........
            }
        });
        LL_UserPart.addView(btdownload);
    }

    private void logout(View v) {
        AppManager.getAppManager().isLoggedIn = false;
        SharedPreferences sp = MineActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
        this.isLogin = false;
        tv_UserName.setText("Login Please");
        IB_UserPhoto.setImageResource(R.mipmap.ic_launcher);
        LL_UserPart.removeView(v);
        LL_UserPart.removeView(btupload);
        LL_UserPart.removeView(btdownload);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(MineActivity.this).setTitle("Exit App?")
                    .setMessage("Click \"Exit\" button to exit App")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppManager.getAppManager().AppExit(MineActivity.this);
                        }
                    })
                    .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).show();

//            HomeActivity.this.finish();

        }
        return false;

    }
}
