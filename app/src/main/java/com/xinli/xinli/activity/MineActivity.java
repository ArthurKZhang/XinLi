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
import android.os.AsyncTask;
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
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.bean.protocol.CUploadPhoto;
import com.xinli.xinli.bean.protocol.SUploadPhoto;
import com.xinli.xinli.service.UploadPhotoWork;
import com.xinli.xinli.testdao.LoginUtil;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.ImgHelper;
import com.xinli.xinli.util.MyService;
import com.xinli.xinli.util.NotifyService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class MineActivity extends MyBaseActivity {

    private boolean isLogin;

    private String nameAndType;
    private String photoid;

    private final int REQUEST_LOGIN = 1;
    private final int REQUEST_UPLOAD_FILE = 2;
    private final int REQUEST_PHOTO_ALBUM = 3;
    private final int REQUEST_PHOTO_CAMERA = 4;
    private final int REQUEST_PHOTO_CORP = 5;
    private final int REQUEST_REGISTER = 6;

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
    Button bt_logout, bt_upload, bt_register;
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
        path = getApplicationContext().getFilesDir().getPath() + "/myHead/";
        Log.e("photo", path);
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
        textView.setTextSize(AppManager.dip2px(this, 10));
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

        bt_register = (Button) findViewById(R.id.bt_register);

        MyOnClickListener listener = new MyOnClickListener();

        IB_UserPhoto.setOnClickListener(listener);
        bt_testHistory.setOnClickListener(listener);
        bt_notificationSetting.setOnClickListener(listener);
        bt_exitApp.setOnClickListener(listener);
        bt_logout.setOnClickListener(listener);
        bt_upload.setOnClickListener(listener);
        bt_register.setOnClickListener(listener);

        if (isLogin) {
            SharedPreferences sp = MineActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
            String uname = sp.getString("userName", "Login Please");
            String utype = sp.getString("userType", "");
            nameAndType = utype + ": " + uname;

            refreshPhoto(path);

            tv_UserName.setText(nameAndType);

            addLogoutButton();
            //如果是已登录状态，就隐藏注册button
            hideRegisterButton();

            if (utype.equals(LoginUtil.TEACHER)) {
                addUploadTestButtons();
            }
        }

    }

    /**
     * change Photo zone
     */
    public void refreshPhoto(String photoPath) {
        photoid = MineActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE).getString("photo", "no");
        Log.e("photo", "refreshPhoto photoid:" + photoid);
        if (photoid == null) return;
        //if photo record exits in shared preference file
        if (!photoid.equals("no")) {
            Bitmap bt = BitmapFactory.decodeFile(photoPath + "head.png");
            //如果找到头像，获得头像
            if (bt != null) {
                Log.e("photo", "bitmap is not null,path:" + photoPath);
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);
                IB_UserPhoto.setImageDrawable(drawable);
                return;
            } else {
                Log.e("photo", "bitmap is null, find photo through server-->requestPhoto(" + photoid + ")");
                //如果没有找到头像，网络获得头像
                requestPhoto(photoid);
                //TODO 回调saveimage方法
                return;
            }
        }
        //if photo record doesn't exist in shared preference file
        if (photoid.equals("no")) {
            Log.e("photo", "photoid is null, set default image");
            IB_UserPhoto.setImageResource(R.mipmap.ic_launcher);
            return;
        }
    }

    private void requestPhoto(String photoId) {
        HashMap<String, Object> hm = new HashMap<String, Object>();
        hm.put("photoid", photoId);
        Task ts = new Task(Task.REQUEST_PHOTO, hm);
        Log.e("photo", "MineActivity-->after task,request photoid:" + photoId);
        MyService.newTask(ts);
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
                    Intent in = new Intent(MineActivity.this, NotifyService.class);
                    // 为Intent设置Action属性
                    in.setAction("com.xinli.xinli.NotifyService");
                    stopService(in);
                    break;
                case R.id.bt_exitApp:
                    AppManager.getAppManager().AppExit(MineActivity.this);
                    break;
                case R.id.bt_logout:
                    Log.d("test", "logout Clicked");
                    logout();
                    break;
                case R.id.bt_upload:
                    Intent intent1 = new Intent(MineActivity.this, TeacherTestListActivity.class);
                    Log.d("test", "MineActivity-->btupload.setOnClickListener" + intent1.toString());
                    MineActivity.this.startActivity(intent1);
//                    Intent intent1 = new Intent(MineActivity.this, EditTestActivity.class);
//                    Log.d("test", "MineActivity-->btupload.setOnClickListener" + intent1.toString());
//                    MineActivity.this.startActivity(intent1);
                    break;
                case R.id.bt_register:
                    intent = new Intent(MineActivity.this, RegisterActivity.class);
                    MineActivity.this.startActivityForResult(intent, REQUEST_REGISTER);
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
                String photoid = bundle.getString("photo");

                //更新界面
                if (isLogIn == true) {
                    tv_UserName.setText(userType + ": " + name);

                    refreshPhoto(path);

                    MineActivity.this.isLogin = true;
                    //加载新的Logout选项:
                    addLogoutButton();
                    //登录成功，隐藏register button。当登录成功或者注册成功时
                    hideRegisterButton();
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
                        new AsyncTask<Void, Void, String>() {

                            @Override
                            protected String doInBackground(Void... params) {
                                String headString = ImgHelper.bitMapToString2(head);
                                byte[] headbytes = new ImgHelper().Bitmap2Bytes(head);
                                CUploadPhoto cUploadPhoto = null;
//                                try {
//                                    cUploadPhoto = new CUploadPhoto(AppManager.getAppManager().userName
//                                            , new String(headbytes, "UTF-8"));
                                    cUploadPhoto = new CUploadPhoto(AppManager.getAppManager().userName
                                            , headString);
                                    Log.e("UploadPhoto", "userName:" + AppManager.getAppManager().userName);
                                    Log.e("UploadPhoto", "photo:" + cUploadPhoto.getPhoto());
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
                                SUploadPhoto sUploadPhoto = UploadPhotoWork.upload(cUploadPhoto);
                                if (sUploadPhoto == null) {
                                    Log.e("photo", "MineActivity->AsyncTask->SUploadPhoto: null");
                                    return "error";
                                }
                                Log.e("photo", "MineActivity->AsyncTask->SUploadPhoto:" + sUploadPhoto.toString());
                                return sUploadPhoto.getPhotoid();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                if (s.equals("erroe")) {
                                    Log.e("photo", "MineActivity->AsyncTask->sUploadPhoto is null");
                                    return;
                                }
                                if (s == null) {
                                    Log.e("photo", "MineActivity->AsyncTask->photoid is null");
                                    return;
                                }
                                SharedPreferences sp = getApplicationContext()
                                        .getSharedPreferences("LoginInfo", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();

                                editor.putString("photo", s);
                                editor.commit();
                                savePicToSD(head);// 保存在SD卡中并显示
                            }
                        }.execute();
                    }
                }
                break;
            case REQUEST_REGISTER:
                /*
                bundle.putString("name", name);
            bundle.putString("identity", identity);
            bundle.putBoolean("isLogIn", true);
            bundle.putString("institution",institution);
                 */
                Bundle bundleRe = data.getExtras();
                Boolean isLogInRe = bundleRe.getBoolean("isLogIn");

                //登录成功更新界面
                if (isLogInRe == true) {
                    String nameRe = bundleRe.getString("name");
                    String userTypeRe = bundleRe.getString("type");
                    String institution = bundleRe.getString("institution");
                    tv_UserName.setText(userTypeRe + ": " + nameRe);

                    AppManager.getAppManager().userName = nameRe;
                    AppManager.getAppManager().userType = userTypeRe;
                    AppManager.getAppManager().isLoggedIn = isLogInRe;
                    AppManager.getAppManager().userInstitude = institution;


                    MineActivity.this.isLogin = true;
                    //加载新的Logout选项:
                    addLogoutButton();
                    //登录成功，隐藏register button。当登录成功或者注册成功时
                    hideRegisterButton();
                    if (userTypeRe.equals(LoginUtil.TEACHER)) {
                        addUploadTestButtons();
                    }
                }
                break;
        }
    }

    private void hideRegisterButton() {
        bt_register.setVisibility(View.GONE);
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

    /**
     * Save photo and refresh photo image
     *
     * @param mBitmap
     */
    public void savePicToSD(Bitmap mBitmap) {
        if (mBitmap==null) return;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream fileOutputStream = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.png";// 图片名字
        try {
            fileOutputStream = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);// 把数据写入文件
            for (int i = 1; i < 1000; i++) {
                //空循环保证图片写操作和系统级资源更新完成
            }
            Log.i("photo", "savePicToSD ready to call refreshPhoto(),path:" + path);
            refreshPhoto(path);
            Log.i("photo", "savePicToSD after call refreshPhoto(),path:" + path);
        } catch (FileNotFoundException e) {
            Log.e("photo", "savePicToSD exception happened");
            e.printStackTrace();
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

}
