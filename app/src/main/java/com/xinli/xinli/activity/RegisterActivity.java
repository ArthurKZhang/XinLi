package com.xinli.xinli.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.Task;
import com.xinli.xinli.util.AppManager;
import com.xinli.xinli.util.MyService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class RegisterActivity extends MyBaseActivity {
    /**
     * 注册成功的标志
     */
    final int RESULT_CODE_OK = 1;
    /**
     * 注册失败的标志
     */
    final int RESULT_CODE_FAIL = 0;

    EditText et_register_name, et_register_password, et_register_password2, et_register_institution;
    RadioGroup rg_register_type;
    TextView tv_register_date;
    Button bt_register;

    int myear, mmonth, mday;

    String name = null;
    String password = null;//暂时没有传给MineActivity
    String institution = null;
    String type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initViews();

    }

    private void initViews() {
        et_register_name = (EditText) findViewById(R.id.et_register_name);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_password2 = (EditText) findViewById(R.id.et_register_password2);//TODO 现在还没有处理
        et_register_institution = (EditText) findViewById(R.id.et_register_institution);
        rg_register_type = (RadioGroup) findViewById(R.id.rg_register_type);
        tv_register_date = (TextView) findViewById(R.id.tv_register_date);
        bt_register = (Button) findViewById(R.id.bt_register);

        // 获取当前的年、月、日、小时、分钟
        Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        tv_register_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myear = year;
                        mmonth = month + 1;
                        mday = dayOfMonth;
                        showDate();
                    }
                }, myear, mmonth, mday);
                datePickerDialog.show();
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_register_name.getText().toString().trim();
                password = et_register_password.getText().toString().trim();
                institution = et_register_institution.getText().toString().trim();
                type = null;
                int id = rg_register_type.getCheckedRadioButtonId();
                if (id == R.id.rb_register_student) {
                    type = "student";
                } else if (id == R.id.rb_register_teacher) {
                    type = "teacher";
                } else {
//                    type = "error";
                    Toast.makeText(RegisterActivity.this,"请选择身份类型",Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar tmp = Calendar.getInstance();
                tmp.set(myear, mmonth, mday);
                Date enrollmentDate = tmp.getTime();
                HashMap<String, Object> infos = new HashMap<String, Object>();
                infos.put("name", name);
                infos.put("password", password);
                infos.put("institution", institution);
                infos.put("type", type);
                infos.put("enrollmentDate", enrollmentDate);
                Task ts = new Task(Task.USER_REGISTER, infos);
                Log.d("test", "RegisterActivity-->RegisterButton-->Onclick" + infos.toString());
                MyService.newTask(ts);

            }
        });
    }

    private void showDate() {
        tv_register_date.setText(myear + "-" + mmonth + "-" + mday);
    }

    @Override
    public void refresh(Object... param) {
        Map<String, Object> map = (Map<String, Object>) param[0];
        if (map==null){
            Log.e("net error","RegisterActivity->refresh->map is null'");
            Toast.makeText(RegisterActivity.this,"refresh-map is null",Toast.LENGTH_SHORT).show();
            return;
        }
        String _id = (String) map.get("_id");
        String result = (String) map.get("result");
        if (_id == null || result == null) {
            Toast.makeText(RegisterActivity.this, "_id or result is NULL", Toast.LENGTH_LONG).show();
            return;
        }
        if (result.equals("fail")) {
            Toast.makeText(RegisterActivity.this, "result is fail, Check again", Toast.LENGTH_LONG).show();
            return;
        }
        if (result.equals("success")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("type", type);
            bundle.putBoolean("isLogIn", true);
            bundle.putString("institution", institution);
            //TODO 时间还没有存 Date
            intent.putExtras(bundle);
            RegisterActivity.this.setResult(RESULT_CODE_OK, intent);
            AppManager.getAppManager().finishActivity(RegisterActivity.this);
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
            bundle.putBoolean("isLogIn", false);

            intent.putExtras(bundle);
            RegisterActivity.this.setResult(RESULT_CODE_FAIL, intent);
            AppManager.getAppManager().finishActivity(RegisterActivity.this);

        }
        return false;

    }
}
