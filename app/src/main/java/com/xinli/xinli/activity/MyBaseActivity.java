package com.xinli.xinli.activity;

import android.app.Activity;
import android.os.Bundle;

import com.xinli.xinli.util.AppManager;

/**
 * Created by zhangyu on 10/29/16.
 */
public abstract class MyBaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    public abstract void refresh(Object... param);
}
