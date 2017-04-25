package com.xinli.xinli.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends MyBaseActivity {
    EditText et_userName, et_userpasswd;
//    RadioGroup rg_characterChoose;
    Button bt_login;
    String name, passwd,userType;
    final int RESULT_CODE = 1;
//    final int STD_ID = R.id.rb_student;
//    final int TEA_ID = R.id.rb_teacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initViews();

    }

    private void initViews() {
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_userpasswd = (EditText) findViewById(R.id.et_userpasswd);
//        rg_characterChoose = (RadioGroup) findViewById(R.id.rg_characterChoose);
        bt_login = (Button) findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_userName.getText().toString().trim();
                if (name.isEmpty() || name == null) {
                    Toast.makeText(LoginActivity.this, "Must Type in User Name!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                passwd = et_userpasswd.getText().toString().trim();
                if (passwd.isEmpty() || passwd == null) {
                    Toast.makeText(LoginActivity.this, "Must Type in Password!!", Toast.LENGTH_SHORT).show();
                    return;
                }
//                int id = rg_characterChoose.getCheckedRadioButtonId();
//                if (id == TEA_ID) {
//                    userType = "teacher";
//                } else if (id == STD_ID) {
//                    userType = "student";
//                } else {//id = -1
//                    Toast.makeText(LoginActivity.this, "Must Choose User Type!!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                HashMap<String, Object> hm = new HashMap<String, Object>();
                hm.put("name", name);
                hm.put("passwd", passwd);
//                hm.put("userType", userType);
                Task ts = new Task(Task.USER_GET_DATA, hm);
                Log.d("test", "LoginActivity-->submitButton-->Onclick" + name + "@" + passwd );//+ "@" + userType);
                MyService.newTask(ts);
            }
        });
    }

    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];
        if(map==null || map.isEmpty()){
            return;
        }
        Log.d("test", "LoginActivity-->refresh():map:"+map.toString());
        Boolean isLoginSuccess = (Boolean) map.get("isLoginSuccess");
        String photo = (String) map.get("photoid");
//        map.put("photo", R.drawable.profile_photo);
        userType = (String) map.get("type");
        if (isLoginSuccess) {

            //写文件登录信息文件,SharedPreferences:"LoginInfo"
            SharedPreferences sp = LoginActivity.this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("userName", name).putString("userType", userType).putBoolean("isLogIn", isLoginSuccess)
                    .putString("photo", photo);
            editor.commit();
            AppManager.getAppManager().isLoggedIn = isLoginSuccess;
            AppManager.getAppManager().userName = name;
            AppManager.getAppManager().userType = userType;
            AppManager._id = (String) map.get("_id");

            Log.e("test", "isLog:" + AppManager.getAppManager().isLoggedIn +
                    "\nuname:" + AppManager.getAppManager().userName +
                    "\nutype:" + AppManager.getAppManager().userType);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("userName", name);
            bundle.putString("userType", userType);
            bundle.putBoolean("isLogIn", isLoginSuccess);
            bundle.putString("photo", photo);

            intent.putExtras(bundle);
            LoginActivity.this.setResult(RESULT_CODE, intent);
            AppManager.getAppManager().finishActivity(LoginActivity.this);
        } else {
            Toast.makeText(LoginActivity.this, "Login Failed!!", Toast.LENGTH_SHORT).show();
        }

    }


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
            Bundle bundle = new Bundle();
            bundle.putString("userName", name);
            bundle.putString("userType", userType);
            bundle.putBoolean("isLogIn", false);
            bundle.putInt("photo", -1);

            intent.putExtras(bundle);
            LoginActivity.this.setResult(RESULT_CODE, intent);
            AppManager.getAppManager().finishActivity(LoginActivity.this);

        }


        return false;

    }


}
