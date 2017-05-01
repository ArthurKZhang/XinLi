package com.xinli.xinli.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.xinli.xinli.R;
import com.xinli.xinli.util.AppManager;

/**
 * Created by zhangyu on 10/29/16.
 */
public abstract class MyBaseActivity extends AppCompatActivity {
    String LogTag = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    public abstract void refresh(Object... param);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
}
