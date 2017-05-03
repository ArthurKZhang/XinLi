package com.xinli.xinli.activity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xinli.xinli.R;
import com.xinli.xinli.bean.protocol.CPublishTest;
import com.xinli.xinli.bean.protocol.SPublishTest;
import com.xinli.xinli.service.TeaPublishTestWork;
import com.xinli.xinli.util.AppManager;

import java.util.Calendar;
import java.util.Date;

public class PublishTestActivity extends MyBaseActivity {

    private final String tag = "PublishTestActivity";

    private EditText et_institution;
    private EditText et_enrollYear;
    private TextView tv_StartDate;
    private TextView tv_EndDate;
    private Button bt_publish;

    private String testid;
    private String teacherName;
    private String testName;
    private int sy, sm, sd, ey, em, ed;
    int myear;
    int mmonth;
    int mday;

    private void initView() {
        et_institution = (EditText) findViewById(R.id.et_institution);
        et_enrollYear = (EditText) findViewById(R.id.et_enrollYear);

        tv_StartDate = (TextView) findViewById(R.id.tv_StartDate);
        tv_StartDate.setOnClickListener(startDateListener);

        tv_EndDate = (TextView) findViewById(R.id.tv_EndDate);
        tv_EndDate.setOnClickListener(endDateListener);

        bt_publish = (Button) findViewById(R.id.bt_publish);
        bt_publish.setOnClickListener(bt_publishListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_test);

        Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
        initView();
        testid = getIntent().getStringExtra("testid");
        teacherName = getIntent().getStringExtra("teacherName");
        testName = getIntent().getStringExtra("testName");
    }

    private View.OnClickListener bt_publishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String institution = et_institution.getText().toString().trim();
            final String enrollYear = et_enrollYear.getText().toString().trim();

            Calendar tmp = Calendar.getInstance();
            tmp.set(sy, sm, sd);
            final Date sDate = tmp.getTime();
            tmp.set(ey, em, ed);
            final Date eDate = tmp.getTime();

            new AsyncTask<Void, Void, SPublishTest>() {

                @Override
                protected SPublishTest doInBackground(Void... params) {
                    CPublishTest cPublishTest = new CPublishTest(testid, testName,teacherName, institution, enrollYear, sDate, eDate);
                    SPublishTest sPublishTest = TeaPublishTestWork.publish(cPublishTest);
                    return sPublishTest;
                }

                @Override
                protected void onPostExecute(SPublishTest s) {
                    if (s == null) {
                        Toast.makeText(PublishTestActivity.this, "网络状况不好", Toast.LENGTH_SHORT).show();
                        Log.e(tag, "spublish is null");
                        return;
                    }
                    if (s.getResult().equals("failure")) {
                        Toast.makeText(PublishTestActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    }
                    if (s.getResult().equals("success")) {
                        Toast.makeText(PublishTestActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        AppManager.getAppManager().finishActivity(PublishTestActivity.this);
                    }
                }
            }.execute();
        }
    };

    private View.OnClickListener startDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(PublishTestActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            sy = year;
                            sm = month;
                            sd = dayOfMonth;
                            tv_StartDate.setText(sy + "-" + sm+1 + "-" + sd);
                        }
                    }, myear, mmonth, mday);
            datePickerDialog.show();
        }
    };
    private View.OnClickListener endDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(PublishTestActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            ey = year;
                            em = month;
                            ed = dayOfMonth;
                            tv_EndDate.setText(ey + "-" + em+1 + "-" + ed);
                        }
                    }, myear, mmonth, mday);
            datePickerDialog.show();
        }
    };

    @Override
    public void refresh(Object... param) {

    }
}
