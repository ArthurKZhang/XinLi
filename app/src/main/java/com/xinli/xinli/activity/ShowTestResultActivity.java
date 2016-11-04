package com.xinli.xinli.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.xinli.xinli.R;
import com.xinli.xinli.util.AppManager;


public class ShowTestResultActivity extends MyBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test_result);

        String resultUri = (String) getIntent().getExtras().get("resultUri");

        SharedPreferences sharedPreferences = getSharedPreferences("submitTestHistory", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(resultUri, null);

        TextView tv_result = (TextView) findViewById(R.id.tv_testResult);
        tv_result.setText(result);
    }

    @Override
    public void refresh(Object... param) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AppManager.getAppManager().finishActivity(ShowTestResultActivity.this);
//
        }

        return false;

    }
}
