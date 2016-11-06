package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.ActionBar.LayoutParams;

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
    Button bt_logout, bt_upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setCustomActionBar();
        setContentView(R.layout.activity_mine_info);

        checkLogin();

        initViews();

    }

    private void setCustomActionBar() {
        LayoutParams lp =new LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        TextView textView = (TextView) mActionBarView.findViewById(R.id.tv_actionbar);
        textView.setText("Personal Yard");  textView.setTextColor(Color.WHITE); textView.setTextSize(AppManager.dip2px(this,20));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
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

        bt_logout  = (Button) findViewById(R.id.bt_logout);
        bt_upload = (Button) findViewById(R.id.bt_upload);

        MyOnClickListener listener = new MyOnClickListener();

        IB_UserPhoto.setOnClickListener(listener);
        bt_testHistory.setOnClickListener(listener);
        bt_notificationSetting.setOnClickListener(listener);
        bt_exitApp.setOnClickListener(listener);
        bt_logout.setOnClickListener(listener);
        bt_upload.setOnClickListener(listener);

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
                addUploadTestButtons();
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
                    if (!isLogin) {
                        return;
                    }

                    if (AppManager.getAppManager().userType == null || AppManager.getAppManager().userType.equals("")) {
                        Log.e("test", "Click R.id.bt_testHistory-->\nAppManager.getAppManager().userType==null||AppManager.getAppManager().userType.equals(\"\")");
                        return;
                    }
                    Bundle bundle = new Bundle();//usertype

                    bundle.putString("usertype", AppManager.getAppManager().userType);
                    intent = new Intent(MineActivity.this, HistoryTestActivity.class);
                    intent.putExtras(bundle);
                    MineActivity.this.startActivity(intent);

                    break;
                case R.id.bt_notificationSetting:
                    //
                    break;
                case R.id.bt_exitApp:
                    AppManager.getAppManager().AppExit(MineActivity.this);
                    break;
                case R.id.bt_logout:
                    Log.d("test","logout Clicked");
                    logout();
                    break;
                case R.id.bt_upload:
                    Intent intent1 = new Intent(MineActivity.this, FileBrowserActivity.class);
                    Log.d("test", "MineActivity-->btupload.setOnClickListener" + intent1.toString());
                    MineActivity.this.startActivityForResult(intent1, REQUEST_UPLOAD_FILE);
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
                        addUploadTestButtons();
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
//        btlogout = new Button(MineActivity.this);
//
////        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams.getLayoutParams();
////
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(this,null);
//        lp.setMargins(0,
//                AppManager.dip2px(MineActivity.this,10),
//                0,
//                0);
//        btlogout.setLayoutParams(lp);
//        btlogout.setText("Logout");
//        btlogout.setBackgroundResource(R.drawable.border_normal);
        bt_logout.setVisibility(View.VISIBLE);
//        btlogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                logout(v);
//            }
//        });
//        LL_UserPart.addView(btlogout);
    }

    private void addUploadTestButtons() {
//        btupload = new Button(MineActivity.this);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btlogout.getLayoutParams();
//        lp.setMargins(0,
//                AppManager.dip2px(MineActivity.this,10),
//                0,
//                0);
//
//        btupload.setLayoutParams(lp);
//
//        btupload.setText("Upload Test");
//        btupload.setBackgroundResource(R.drawable.border_normal);
        bt_upload.setVisibility(View.VISIBLE);
//        btupload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("test", "MineActivity-->addUploadTestAndDownloadResultButtons-->btupload.setOnClickListener");
//                Intent intent = new Intent(MineActivity.this, FileBrowserActivity.class);
//                Log.d("test", "MineActivity-->btupload.setOnClickListener" + intent.toString());
//                MineActivity.this.startActivityForResult(intent, REQUEST_UPLOAD_FILE);
//            }
//        });
//        LL_UserPart.addView(btupload);


    }

    private void logout() {
        AppManager.getAppManager().isLoggedIn = false;
        AppManager.getAppManager().userName = null;
        AppManager.getAppManager().userType = null;
        SharedPreferences sp = MineActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
        this.isLogin = false;
        tv_UserName.setText("Login Please");
        IB_UserPhoto.setImageResource(R.mipmap.ic_launcher);
        bt_logout.setVisibility(View.GONE);
        if(bt_upload.getVisibility()==View.VISIBLE){
            bt_upload.setVisibility(View.GONE);
        }
//        LL_UserPart.removeView(v);
//        LL_UserPart.removeView(btupload);

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

        }
        return false;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        item=menu.findItem(R.id.action_refresh);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
