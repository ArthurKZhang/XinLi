package com.xinli.xinli;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.xinli.xinli.activity.MainActivity;
import com.xinli.xinli.activity.MessageList;
import com.xinli.xinli.activity.MineInfo;
import com.xinli.xinli.bean.test.Recommend;
import com.xinli.xinli.testdao.RecommendDao;
import com.xinli.xinli.testdao.TestIDao;
import com.xinli.xinli.testdao.VFDao;
import com.xinli.xinli.util.MyService;

public class HomeActivity extends TabActivity {

    TabHost tabHost;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        initDataBase();
        tabHost = getTabHost();
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("test").setIndicator("Test").setContent(new Intent(this, MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("message").setIndicator("Message").setContent(new Intent(this, MessageList.class)));
        tabHost.addTab(tabHost.newTabSpec("mine").setIndicator("Mine").setContent(new Intent(this, MineInfo.class)));

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

    }

    private final RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {

                case R.id.radio_test:
//                    AppContext.SHOW_LOGIN_FLAG = true;
                    tabHost.setCurrentTabByTag("test");
                    break;
                case R.id.radio_message:
                    tabHost.setCurrentTabByTag("message");
                    break;
                case R.id.radio_mine:
                    tabHost.setCurrentTabByTag("mine");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            HomeActivity.this.finish();
        }
        return false;

    }
}
