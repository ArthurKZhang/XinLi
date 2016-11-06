package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MineActivity extends MyBaseActivity {

    private boolean isLogin;

    private String name;
    private int photo;

    private final int REQUEST_LOGIN = 1;
    private final int REQUEST_UPLOAD_FILE = 2;
    private final int REQUEST_PHOTO_ALBUM = 3;
    private final int REQUEST_PHOTO_CAMERA = 4;
    private final int REQUEST_PHOTO_CORP = 5;

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
    /**
     * // sd路径
     */
    private static String path = "/sdcard/myHead/";
    /**
     * // 头像Bitmap
     */
    private Bitmap head;

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
        LayoutParams lp = new LayoutParams(
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                android.support.v7.app.ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        TextView textView = (TextView) mActionBarView.findViewById(R.id.tv_actionbar);
        textView.setText("Personal Yard");
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(AppManager.dip2px(this, 20));
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

        bt_logout = (Button) findViewById(R.id.bt_logout);
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
//            photo = sp.getInt("photo", -1);
//            if (photo != -1)
//                IB_UserPhoto.setImageResource(photo);

            Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                IB_UserPhoto.setImageDrawable(drawable);
            } else {
                IB_UserPhoto.setImageResource(R.mipmap.ic_launcher);
                /**
                 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
                 *
                 */
            }

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
                        showTypeDialog();
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
                    Log.d("test", "logout Clicked");
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
                    Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
                    if (bt != null) {
                        @SuppressWarnings("deprecation")
                        Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                        IB_UserPhoto.setImageDrawable(drawable);
                    } else {
                        IB_UserPhoto.setImageResource(R.mipmap.ic_launcher);
                        /**
                         * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
                         *
                         */
                    }
//                    IB_UserPhoto.setImageResource(photo);
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
            case REQUEST_PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case REQUEST_PHOTO_CAMERA:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }
                break;
            case REQUEST_PHOTO_CORP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        IB_UserPhoto.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
        }
    }

    private void addLogoutButton() {
        bt_logout.setVisibility(View.VISIBLE);
    }

    private void addUploadTestButtons() {
        bt_upload.setVisibility(View.VISIBLE);
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
        if (bt_upload.getVisibility() == View.VISIBLE) {
            bt_upload.setVisibility(View.GONE);
        }

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
        item = menu.findItem(R.id.action_refresh);
        item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {
            // 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, REQUEST_PHOTO_ALBUM);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {
            // 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, REQUEST_PHOTO_CAMERA);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_PHOTO_CORP);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
